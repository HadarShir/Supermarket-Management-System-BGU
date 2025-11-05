package DomainLayer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import DataLayer.ShiftDAO;
import java.sql.SQLException;
import DataLayer.EmployeeAvailabilityDAO;
import java.util.Map;

/**
 * This class is designed to centralize the business logic related to shift management.
 * Major changes made:
 * 1. Removed nextWeeks list - now using Branch's ShiftOrganizer directly
 * 2. All shift operations are now performed through Branch
 * 3. Simplified shift history management
 * 4. Improved error handling with specific messages
 */
public class ShiftController {

    private static ShiftController instance;
    private EmployeeController employeeController;
    private ShiftRepository shiftRepository;
    private BranchController branchController;
    private static final DateTimeFormatter SQL_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private ShiftController(){
        BranchRepository branchRepository = new BranchRepository();
        this.branchController = BranchController.getInstance(branchRepository);
        this.employeeController = EmployeeController.getInstance(branchRepository);
        this.shiftRepository = new ShiftRepository();
        initNextWeeksArray();
    }

    /**
     * Initializes the next week shifts for all branches
     * Now uses Branch's ShiftOrganizer directly instead of maintaining separate list
     */
    private void initNextWeeksArray() {
        for (Branch branch : branchController.getAllBranches()) {
            Shift[] current = getCurrentWeek(branch.getBranchID());
            if (current == null || current[0] == null) {
                current = firstInit(branch.getBranchID());
            }
            Shift[] next = new Shift[14];
            LocalDate nextWeekStart = current[0].getDate().plusDays(7).with(DayOfWeek.SUNDAY);
            for (int j = 0; j < 14; j++) {
                if (current[j] != null) {
                    LocalDate date = current[j].getDate().plusDays(7);
                    Shift.shiftType shiftType = current[j].getShiftType();
                    String start = current[j].getStart().toString();
                    String end = current[j].getEnd().toString();
                    next[j] = new Shift(
                            date,
                            shiftType,
                            branch,
                            false,
                            current[j].getShiftID(),
                            start,
                            end,
                            nextWeekStart
                    );
                } else {
                    next[j] = null;
                }
            }
            branch.getShiftOrganizer().setCurrentWeek(current);
            branch.getShiftOrganizer().setNewShifts(next);
        }
    }

    /**
     * Creates initial shifts for a new branch
     * Now adds shifts directly to branch's history
     */
    private Shift[] firstInit(int branchID){
        Shift[] currentWeek = new Shift[14];
        LocalDate weekStart = LocalDate.now().with(DayOfWeek.SUNDAY);
        Branch branch = branchController.findBranchById(branchID);
        if (branch == null) {
            throw new RuntimeException("Branch not found");
        }
        for (int i = 0; i<14; i++){
            Shift.shiftType _shiftType = (i % 2 == 0) ? Shift.shiftType.morning : Shift.shiftType.evening;
            String start = (_shiftType == Shift.shiftType.morning) ? "08:00" : "15:00";
            String end = (_shiftType == Shift.shiftType.morning) ? "15:00" : "20:00";
            LocalDate date = weekStart.plusDays(i / 2);
            Shift shift = new Shift(date,_shiftType, branch, false, i, start, end, weekStart);
            shift.setCurrentWeek(true);
            currentWeek[i] = shift;
            branch.getCurrentWeekShifts()[i] = shift;
            branch.addToShiftHistory(shift);
        }
        return currentWeek;
    }

    public static ShiftController getInstance(){
        if (instance == null)
            instance = new ShiftController();
        return instance;
    }

    /**
     * Employee availability management methods
     * Now using Branch's ShiftOrganizer directly
     */
    public void pickAvailability(String userName, String requiredShifts){
        Employee e = employeeController.getEmployee(userName);
        if (e == null){
            throw new RuntimeException("user not found");
        }
        Branch branch = e.getBranch();
        if (!branch.getShiftOrganizer().isAvailabilityChangesAllowed()){
            throw new RuntimeException("changes in availability are not allowed");
        }
        String[] picks = requiredShifts.trim().split("\\s+");
        int[] _picks = new int[picks.length];
        for (int i = 0; i<picks.length; i++){
            int shift = convertStringToInt(picks[i]);
            if (shift < 0 || shift > 13){
                throw new RuntimeException("invalid shift. please choose 0-13");
            }
            _picks[i] = shift;
        }
        for (int i : _picks){
            branch.getShiftOrganizer().addAvailableEmployee(e, i);
        }
    }

    public String showMyAvailability(String userName){
        Employee e = employeeController.getEmployee(userName);
        if (e == null){
            throw new RuntimeException("user not found");
        }
        return e.getBranch().getShiftOrganizer().getEmployeeAvailability(e);
    }

    public void changeAvailability(String userName, String dayAndType){
        int shift = convertStringToInt(dayAndType);
        if (shift < 0 || shift > 13){
            throw new RuntimeException("invalid shift. please choose 0-13");
        }
        Employee e = employeeController.getEmployee(userName);
        if (e == null){
            throw new RuntimeException("user not found");
        }
        Branch branch = e.getBranch();
        if (!branch.getShiftOrganizer().isAvailabilityChangesAllowed()){
            throw new RuntimeException("changes in availability are not allowed");
        }
        if (branch.getShiftOrganizer().isAvailableEmployee(e, shift)){
            branch.getShiftOrganizer().removeAvailableEmployee(e, shift);
        }else{
            branch.getShiftOrganizer().addAvailableEmployee(e, shift);
        }
    }

    /**
     * Shift viewing methods
     * Now using Branch's ShiftOrganizer directly
     */
    public String showCurrentWeek(String name){
        Employee e = employeeController.getEmployee(name);
        if (e==null){
            throw new RuntimeException("illegal user name");
        }
        return e.getBranch().getShiftOrganizer().showCurrentWeek();
    }

    public String showMyHistory(String userName){
        Employee e = employeeController.getEmployee(userName);
        if (e == null) {
            throw new RuntimeException("User not found");
        }
        StringBuilder sb = new StringBuilder();
        for (Shift shift : e.getBranch().getShiftHistory()) {
            if (shift.isWorked(userName)){
                sb.append(shift.getDate().toString()).append(" ").append(shift.getShiftType()).append("\n");
            }
        }
        return sb.toString();
    }

    public void reportHours(String userName, String SID, String startTime, String endTime){
        LocalTime start = convertStringToTime(startTime);
        LocalTime end = convertStringToTime(endTime);
        Shift toReport = findShift(SID);
        if (toReport == null){
            throw new RuntimeException("there isn't shift with that shiftID : " + SID);
        }
        Shift.AssignedEmployee employee = toReport.getEmployeeArray(userName);
        if (employee == null){
            throw new RuntimeException("this isn't your shift, can't report hours");
        }
        employee.setStart(start);
        employee.setEnd(end);
    }

    public String showHistory(){
        StringBuilder sb = new StringBuilder();
        for (Branch branch : branchController.getAllBranches()) {
            for (Shift shift : branch.getShiftHistory()) {
                sb.append(shift.toString());
            }
        }
        return sb.toString();
    }

    /**
     * HR management methods
     * Now using Branch's ShiftOrganizer directly
     */
    public void allowAvailabilityChanges(String BID){
        int bid = convertStringToInt(BID);
        Branch branch = branchController.findBranchById(bid);
        if (branch == null) {
            throw new RuntimeException("Branch not found");
        }
        branch.getShiftOrganizer().setAvailabilityChangesAllowed(true);
    }

    public void disableAvailabilityChanges(String BID){
        int bid = convertStringToInt(BID);
        Branch branch = branchController.findBranchById(bid);
        if (branch == null) {
            throw new RuntimeException("Branch not found");
        }
        branch.getShiftOrganizer().setAvailabilityChangesAllowed(false);
    }

    public void setVacation(String BID, String dayAndType){
        int branchId = convertStringToInt(BID);
        int shiftNum = convertStringToInt(dayAndType);
        Branch branch = branchController.findBranchById(branchId);
        if (branch == null) {
            throw new RuntimeException("Branch not found");
        }
        Shift[] shifts = branch.getShiftOrganizer().getNewShifts();
        Shift shift = shifts[shiftNum];
        if (shift == null) {
            throw new RuntimeException("Shift not found");
        }
        // Update in DB using ShiftRepository
        Shift dbShift = shiftRepository.findById(branchId, shift.getWeekStart(), shift.getShiftID());
        if (dbShift == null) {
            throw new RuntimeException("Shift not found in DB");
        }
        dbShift.setVacation(true);
        shiftRepository.updateShift(dbShift);
    }

    public void changeRequirement(String BID, String dayAndType, String role, String amountOfEmployees){
        int bid = convertStringToInt(BID);
        int shiftNum = convertStringToInt(dayAndType);
        if (shiftNum < 0 || shiftNum > 13){
            throw new RuntimeException("illegal shift id. please pick 0-13");
        }
        int newAmount = convertStringToInt(amountOfEmployees);
        if (newAmount < 0){
            throw new RuntimeException("insert positive number");
        }
        if (!Role.getInstance().containsRole(role)){
            throw new RuntimeException("illegal role");
        }
        Branch branch = branchController.findBranchById(bid);
        if (branch == null) {
            throw new RuntimeException("Branch not found");
        }

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(java.time.DayOfWeek.SUNDAY);

        Shift shift = branch.getShiftOrganizer().getNewShifts()[shiftNum];
        if (shift == null) {
            throw new RuntimeException("Shift not found");
        }
        int currentAmount = shift.getRequirements().getOrDefault(role, 0);
        if (newAmount < currentAmount) {
            if (role.equals("StoreKeeper")) {
                System.out.println("There is a delivery that needs to be received");
            } else if (role.equals("Driver_A") || role.equals("Driver_B") || role.equals("Driver_C") || role.equals("Driver_D")) {
                System.out.println("There is a delivery that needs to be dispatched");
            }
        }
        // Update requirements in DB
        System.out.println("[DEBUG] updateRequirement: branchId=" + bid + ", weekStart=" + weekStart + ", shiftId=" + shiftNum + ", role=" + role + ", requiredAmount=" + newAmount);
        try {
            ShiftDAO shiftDAO = new ShiftDAO();
            shiftDAO.updateRequirement(bid, weekStart, shiftNum, role, newAmount);
            // טען מחדש את הדרישות מה-DB
            Map<String, Integer> reqs = shiftDAO.getRequirementsForShift(bid, weekStart, shiftNum);
            if (shift != null) {
                shift.getRequirements().clear();
                shift.getRequirements().putAll(reqs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update requirements in DB: " + e.getMessage(), e);
        }
    }

    public String showAvailableEmployees(String BID, String dayAndType, String role){
        int bid = convertStringToInt(BID);
        Branch branch = branchController.findBranchById(bid);
        if (branch == null) {
            throw new RuntimeException("Branch not found");
        }
        int shiftNum = convertStringToInt(dayAndType);
        if (shiftNum < 0 || shiftNum > 13){
            throw new RuntimeException("illegal shift id. please pick 0-13");
        }
        if (!Role.getInstance().containsRole(role)){
            throw new RuntimeException("illegal role");
        }

        // שליפת העובדים מה-DB
        try {
            EmployeeAvailabilityDAO dao = new EmployeeAvailabilityDAO();
            Shift shift = branch.getShiftOrganizer().getNewShifts()[shiftNum];
            java.util.List<String> usernames = dao.getAvailableUsernamesForShiftAndRole(
                    bid, shift.getWeekStart(), shiftNum, role
            );
            if (usernames.isEmpty()) {
                return "no employees available";
            }
            // החזר את רשימת השמות
            return String.join(" ", usernames);
        } catch (Exception ex) {
            return "error loading employees";
        }
    }

    // Helper to ensure time is in HH:mm:ss format
    private String toSqlTimeFormat(String time) {
        if (time != null && time.length() == 5) { // HH:mm
            return time + ":00";
        }
        return time;
    }

    public void pickEmployee(String BID, String dayAndType, String role, String employeeName){
        int branchId = convertStringToInt(BID);
        int shiftNum = convertStringToInt(dayAndType);
//        System.out.println("[DEBUG] ShiftController.pickEmployee: branchId=" + branchId + ", shiftNum=" + shiftNum + ", role=" + role + ", employeeName=" + employeeName);
        if (shiftNum < 0 || shiftNum > 13){
            throw new RuntimeException("illegal shift id. please pick 0-13");
        }
        if (!Role.getInstance().containsRole(role)){
            throw new RuntimeException("illegal role");
        }
        Employee e = employeeController.getEmployee(employeeName);
//        System.out.println("[DEBUG] employee=" + e);
        if (e == null) {
            throw new RuntimeException("Employee not found: " + employeeName);
        }
//        System.out.println("[DEBUG] employee.getRoles()=" + e.getRoles());
        if (e.getRoles() == null) {
            throw new RuntimeException("Employee roles is null for: " + employeeName);
        }
//        System.out.println("[DEBUG] employee.getBranch()=" + e.getBranch());
        if (e.getBranch() == null) {
            throw new RuntimeException("Employee branch is null for: " + employeeName);
        }
        if (!e.getRoles().contains(role)){
            throw new RuntimeException("the employee does not have this role");
        }
        Branch branch = branchController.findBranchById(branchId);
        if (branch == null) {
            throw new RuntimeException("Branch not found");
        }
        Shift shift = branch.getShiftOrganizer().getNewShifts()[shiftNum];
        if (shift == null) {
            throw new RuntimeException("Shift not found");
        }
        // Check if employee is already assigned to this shift
        if (shift.isWorked(employeeName)) {
            throw new RuntimeException("This employee is already assigned to this shift");
        }
        // Check if we have enough slots for this role
        if (shift.chosenForRole(role) >= shift.getRequirements().get(role)) {
            throw new RuntimeException("No more slots available for this role in this shift");
        }
        // עדכן בזיכרון
        shift.addAssignedEmployee(employeeName, role, shift.getStart().format(SQL_TIME_FORMAT), shift.getEnd().format(SQL_TIME_FORMAT));
//        System.out.println("[DEBUG] ShiftController.pickEmployee: Employee added in memory. Assigned employees: " + shift.getEmployyes());
//        for (Shift.AssignedEmployee emp : shift.getEmployyes()) {
//            System.out.println("[DEBUG] AssignedEmployee: name=" + emp.getName() + ", role=" + emp.getRole() + ", start=" + emp.getStart() + ", end=" + emp.getEnd());
//        }

        Shift dbShift = shiftRepository.findById(branchId, shift.getWeekStart(), shift.getShiftID());
        if (dbShift == null) {
            throw new RuntimeException("Shift not found in DB");
        }
        dbShift.addAssignedEmployee(employeeName, role, dbShift.getStart().format(SQL_TIME_FORMAT), dbShift.getEnd().format(SQL_TIME_FORMAT));
//        System.out.println("[DEBUG] ShiftController.pickEmployee: Employee added to DB shift. Assigned employees: " + dbShift.getEmployyes());
//        for (Shift.AssignedEmployee emp : dbShift.getEmployyes()) {
//            System.out.println("[DEBUG] DB AssignedEmployee: name=" + emp.getName() + ", role=" + emp.getRole() + ", start=" + emp.getStart() + ", end=" + emp.getEnd());
//        }
        shiftRepository.updateShift(dbShift);

    }

    public void publishNextWeek(String BID){
        int bid = convertStringToInt(BID);
        Branch branch = branchController.findBranchById(bid);
        if (branch == null) {
            throw new RuntimeException("Branch not found");
        }
        Shift[] nextWeek = branch.getNextWeekShifts();
        for (int i = 0; i < 14; i++) {
            Shift shift = nextWeek[i];
            if (shift != null && !shift.isVacation() && !shift.isValid()) {
                throw new RuntimeException("not match to HR requirements");
            }
            if (shift != null) {
                shift.setPublished(true);
                shiftRepository.updateShift(shift);
            }
        }
        branch.getShiftOrganizer().publishNextWeek();
    }

    public void setAsCurrentWeek(String BID) {
        int bid = convertStringToInt(BID);
        Branch branch = branchController.findBranchById(bid);
        if (branch == null) {
            throw new RuntimeException("Branch not found");
        }
        // שלוף את המשמרות של שבוע הבא
        Shift[] nextWeek = branch.getNextWeekShifts();
        Shift[] newNextWeek = new Shift[14];
        LocalDate nextSunday = nextWeek[0].getWeekStart();
        LocalDate nextNextSunday = nextSunday.plusWeeks(1);
        for (int i = 0; i < 14; i++) {
            // העבר את המשמרת של שבוע הבא לשבוע נוכחי
            Shift shift = nextWeek[i];
            if (shift != null) {
                shift.setCurrentWeek(true);
                shift.setNextWeek(false);
                shiftRepository.updateShift(shift);
                branch.getCurrentWeekShifts()[i] = shift;
            }

            LocalDate date = nextNextSunday.plusDays(i / 2);
            Shift.shiftType shiftType = (i % 2 == 0) ? Shift.shiftType.morning : Shift.shiftType.evening;
            String start = (shiftType == Shift.shiftType.morning) ? "08:00" : "15:00";
            String end = (shiftType == Shift.shiftType.morning) ? "15:00" : "20:00";
            Shift newShift = new Shift(date, shiftType, branch, false, i, start, end, nextNextSunday);
            newShift.setNextWeek(true);
            shiftRepository.addShift(newShift);
            newNextWeek[i] = newShift;
            branch.getNextWeekShifts()[i] = newShift;
            TruckManager.getInstance().clearTruckSchedulesForNewWeek();
        }

        branch.getShiftOrganizer().setCurrentWeek(branch.getCurrentWeekShifts());
        branch.getShiftOrganizer().setNewShifts(branch.getNextWeekShifts());
    }

    public String showNextWeek(String name){
        Employee e = employeeController.getEmployee(name);
        if (e==null){
            throw new RuntimeException("illegal user name");
        }
        return e.getBranch().getShiftOrganizer().showNextWeek();
    }

    public String HR_showCurrentWeek(String BID){
        int bid = convertStringToInt(BID);
        Branch branch = branchController.findBranchById(bid);
        if (branch == null) {
            throw new RuntimeException("Branch not found");
        }
        return branch.getShiftOrganizer().showCurrentWeek();
    }

    public String HR_showNextWeek(String BID){
        int bid = convertStringToInt(BID);
        Branch branch = branchController.findBranchById(bid);
        if (branch == null) {
            throw new RuntimeException("Branch not found");
        }
        return branch.getShiftOrganizer().showNextWeek();
    }

    public ArrayList<Shift.AssignedEmployee> getDriversInShift(int branchID, int shiftNum) {
        if (shiftNum < 0 || shiftNum > 13) {
            throw new RuntimeException("Illegal shift number. Please choose 0–13.");
        }

        Branch branch = branchController.findBranchById(branchID);
        if (branch == null) {
            throw new RuntimeException("Branch not found");
        }

        Shift shift = branch.getShiftOrganizer().getShift(shiftNum);
        if (shift == null) {
            throw new RuntimeException("Shift not found");
        }
        return shift.getDrivers();
    }

    public void changeTime(String BID, String dayAndType, String start, String end){
        int branchId = convertStringToInt(BID);
        int shiftNum = convertStringToInt(dayAndType);
        if (shiftNum < 0 || shiftNum > 13){
            throw new RuntimeException("illegal shift id. please pick 0-13");
        }
        LocalTime _start = convertStringToTime(start);
        LocalTime _end = convertStringToTime(end);
        Branch branch = branchController.findBranchById(branchId);
        if (branch == null) {
            throw new RuntimeException("Branch not found");
        }
        Shift shift = branch.getShiftOrganizer().getNewShifts()[shiftNum];
        if (shift == null) {
            throw new RuntimeException("Shift not found");
        }
        // עדכן בזיכרון
        shift.setStart(_start);
        shift.setEnd(_end);
        // עדכן ב-DB
        Shift dbShift = shiftRepository.findById(branchId, shift.getWeekStart(), shift.getShiftID());
        if (dbShift == null) {
            throw new RuntimeException("Shift not found in DB");
        }
        dbShift.setStart(_start);
        dbShift.setEnd(_end);
        shiftRepository.updateShift(dbShift);
    }

    /**
     * Helper methods for finding shifts
     * Now using Branch's arrays directly
     */
    private Shift[] getNextWeek(int BID){
        Branch branch = branchController.findBranchById(BID);
        if (branch == null) {
            throw new RuntimeException("Branch not found");
        }
        return branch.getNextWeekShifts();
    }

    private Shift findNextWeekShift(int bid, int shiftNum){
        Branch branch = branchController.findBranchById(bid);
        if (branch == null) {
            throw new RuntimeException("Branch not found");
        }
        return branch.getNextWeekShifts()[shiftNum];
    }

    private Shift[] getCurrentWeek(int BID){
        Branch branch = branchController.findBranchById(BID);
        if (branch == null) {
            throw new RuntimeException("Branch not found");
        }
        return branch.getCurrentWeekShifts();
    }

    private Shift findCurrentWeekShift(int bid, int shiftNum){
        Branch branch = branchController.findBranchById(bid);
        if (branch == null) {
            throw new RuntimeException("Branch not found");
        }
        return branch.getCurrentWeekShifts()[shiftNum];
    }

    /**
     * Utility methods
     */
    private int convertStringToInt(String st){
        int s;
        try{
            s = Integer.parseInt(st);
        }catch (RuntimeException e){
            throw new RuntimeException("invalid value, insert a number");
        }
        return s;
    }

    private LocalTime convertStringToTime(String time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            LocalTime t = LocalTime.parse(time, formatter);
            return t;
        } catch (Exception e) {
            throw new RuntimeException("invalid time format, must be 'HH:mm'");
        }
    }

    private Shift findShift(String SID){
        int sid = convertStringToInt(SID);
        for (Branch branch : branchController.getAllBranches()) {
            for (Shift shift : branch.getShiftHistory()) {
                if (shift.getShiftID() == sid){
                    return shift;
                }
            }
        }
        return null;
    }

    /**
     * Branch management methods
     */
    public void addBranch(String city, int shippingArea, String phone, String name){
        int newBranchId = branchController.getAllBranches().size();
        Branch newBranch = new Branch(newBranchId, city, shippingArea, phone, name);
        branchController.addBranch(newBranch);

        // צור משמרות ריקות לשבוע הנוכחי ולשבוע הבא והוסף ל-DB
        LocalDate sunday = LocalDate.now().with(DayOfWeek.SUNDAY);
        LocalDate nextSunday = sunday.plusDays(7);
        for (int i = 0; i < 14; i++) {
            // שבוע נוכחי
            LocalDate date = sunday.plusDays(i / 2);
            Shift.shiftType shiftType = (i % 2 == 0) ? Shift.shiftType.morning : Shift.shiftType.evening;
            String start = (shiftType == Shift.shiftType.morning) ? "08:00" : "15:00";
            String end = (shiftType == Shift.shiftType.morning) ? "15:00" : "20:00";
            Shift shift = new Shift(date, shiftType, newBranch, false, i, start, end, sunday);
            shift.setCurrentWeek(true);
            shiftRepository.addShift(shift);
            newBranch.getCurrentWeekShifts()[i] = shift;

            // שבוע הבא
            LocalDate nextDate = nextSunday.plusDays(i / 2);
            Shift nextShift = new Shift(nextDate, shiftType, newBranch, false, i, start, end, nextSunday);
            nextShift.setNextWeek(true);
            shiftRepository.addShift(nextShift);
            newBranch.getNextWeekShifts()[i] = nextShift;
        }
    }

    public Shift getShift(int branchID, int shiftNum) {
        Branch branch = branchController.findBranchById(branchID);
        if (branch == null) {
            throw new RuntimeException("Branch not found");
        }
        return branch.getShiftOrganizer().getShift(shiftNum);
    }

    public String showHistoryByBranch(int branchID) {
        Branch branch = branchController.findBranchById(branchID);
        if (branch == null) return "Branch not found";
        StringBuilder sb = new StringBuilder();
        for (Shift shift : branch.getShiftHistory()) {
            sb.append(shift.toString()).append("\n");
        }
        if (sb.length() == 0) return "No shifts in history for this branch";
        return sb.toString();
    }

    public String showAllBranchesckHistory() {
        StringBuilder sb = new StringBuilder();
        for (Branch branch : branchController.getAllBranches()) {
            sb.append("Branch ").append(branch.getBranchID()).append(": ")
                    .append(branch.getCity()).append(", ")
                    .append(branch.getShippingArea()).append("\n");
            sb.append(showHistoryByBranch(branch.getBranchID())).append("\n");
        }
        return sb.toString();
    }
}