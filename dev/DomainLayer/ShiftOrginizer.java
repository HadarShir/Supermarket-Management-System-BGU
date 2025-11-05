package DomainLayer;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Map;
import DataLayer.EmployeeAvailabilityDAO;
import java.sql.SQLException;
import DataLayer.ShiftDAO;


public class ShiftOrginizer {
    private Branch branch;
    private boolean availabilityChangesAllowed;
    private Map<String, ArrayList<String>>[] AvailableEmployeesPerShiftByRole;
    private Map<String, Integer>[] requirementsPerShift;
    private Shift[] newShifts;
    private Shift[] currentWeek;
    LocalDate sunday;
    private boolean isPublished;



    public ShiftOrginizer(Branch branch, Shift[] currentWeek, Shift[] nextWeek)
    {
        this.branch = branch;
        initWeeks(currentWeek, nextWeek);
        this.availabilityChangesAllowed = true;
        this.sunday  = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
    }

    public void pickEmployee(int shiftNum, Employee e, String role){
//        System.out.println("[DEBUG] Attempting to pick employee: " + e.getUserName() + " for role: " + role + " in shift: " + shiftNum);
        Shift shift = newShifts[shiftNum];
        if (shift.isWorked(e.getUserName())) {
//            System.out.println("[DEBUG] Employee already assigned to this shift");
            throw new RuntimeException("This employee is already assigned to a different role in this shift.");
        } else if (shift.chosenForRole(role) == shift.getRequirements().get(role)){
//            System.out.println("[DEBUG] Role requirements already met. Current count: " + shift.chosenForRole(role) +
//                             ", Required: " + shift.getRequirements().get(role));
            throw new RuntimeException("there is enough employees to this role");
        }
//        System.out.println("[DEBUG] Adding employee to shift");
        shift.addAssignedEmployee(e.getUserName(), role, convertTimeToString(shift.getStart()), convertTimeToString(shift.getEnd()));
//        System.out.println("[DEBUG] Successfully added employee to shift");
    }

    public ArrayList<Employee> getAvailableEmployeesToShiftByRole (int shiftNum, String role){
        Shift shift = newShifts[shiftNum];
        return shift.getAvailableByRole(role);
    }

    public String showCurrentWeek() {
        if (currentWeek == null) {
            return "No shifts this week";
        }

        StringBuilder shiftToView = new StringBuilder();
        boolean hasShifts = false;

        for (int i = 0; i < currentWeek.length; i++) {
            Shift shift = currentWeek[i];
            if (shift != null) {
                shiftToView.append(shift.toString()).append("\n");
                hasShifts = true;
            }
        }

        if (!hasShifts) {
            return "No shifts this week";
        }

        return shiftToView.toString();
    }

    public String showNextWeek(){
        StringBuilder shiftToView = new StringBuilder();
        for (Shift shift : newShifts){
            shiftToView.append(shift.toString()).append("\n");
        }
        return shiftToView.toString();
    }

    public void publishNextWeek(){
        for (Shift shift : newShifts){
            if (!shift.isValid()){
                throw new RuntimeException("not match to HR requirements");
            }
        }
        isPublished = true;
    }

    public ArrayList<Shift.AssignedEmployee> getDrivers(int shiftNum) {
        Shift shift = newShifts[shiftNum];
        if (shift == null) {
            throw new RuntimeException("Shift not found for shift number: " + shiftNum);
        }
        return shift.getDrivers();
    }

    public void SetAsCurrentWeek(){
        if (!isPublished){
            throw new RuntimeException("you need to publish first");
        }

        for (int i = 0; i<14; i++){
            currentWeek[i].setCurrentWeek(false);
            currentWeek[i] = newShifts[i];
            currentWeek[i].setCurrentWeek(true);
            // Add shifts to branch history when setting as current week
            branch.addToShiftHistory(currentWeek[i]);
        }

        initNewWeekByCurrent(currentWeek[13]);

        isPublished = false;
        availabilityChangesAllowed = true;
    }

    public void changeReq(int shiftNum, String role, int amountOfEmployees){
        newShifts[shiftNum].changeReq(role, amountOfEmployees);
    }

    public void setVacation(int shiftNum){
        newShifts[shiftNum].setVacation(true);
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public boolean isAvailabilityChangesAllowed() {
        return availabilityChangesAllowed;
    }

    public Shift[] getNewShifts() {
        return newShifts;
    }

    public Shift[] getCurrentWeek() {
        return currentWeek;
    }

    public void setCurrentWeek(Shift[] currentWeek) {
        if (currentWeek == null) {
            return;
        }

        this.currentWeek = currentWeek;
    }

    public void setAvailableEmployeesPerShiftByRole(Map<String, ArrayList<String>> [] availableEmployeesPerShiftByRole) {
        AvailableEmployeesPerShiftByRole = availableEmployeesPerShiftByRole;
    }

    public void setNewShifts(Shift[] newShifts) {
        this.newShifts = newShifts;
    }

    public void setRequirementsPerShiftByRole(Map<String,Integer>[] requirementsPerShiftByRole) {
        this.requirementsPerShift = requirementsPerShiftByRole;

    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    private void initWeeks(Shift[] current, Shift[] next) {
        this.currentWeek = new Shift[14];
        this.newShifts = new Shift[14];

        if (current == null) {
            return;
        }

        for (int i = 0; i < 14; i++) {
            if (i < current.length && current[i] != null) {
                this.currentWeek[i] = current[i];
                branch.addToShiftHistory(current[i]);
            }

            if (next != null && i < next.length && next[i] != null) {
                this.newShifts[i] = next[i];
            }
        }

        if (next == null) {
            initNewWeekByCurrent(current[13]);
        }
    }

    public boolean isAvailableEmployee(Employee e, int shiftNum){
        Shift shift = newShifts[shiftNum];
        return shift.isAvailable(e);
    }

    public void removeAvailableEmployee(Employee e, int shiftNum){
        Shift shift = newShifts[shiftNum];
        if (shift.isVacation()){
            throw new RuntimeException("it's Vacation");
        }
        shift.removeAvailableEmployee(e);
        // Remove from DB for all roles
        try {
            EmployeeAvailabilityDAO dao = new EmployeeAvailabilityDAO();
            for (String role : e.getRoles()) {
                dao.deleteAvailability(e.getUserName(), branch.getBranchID(), shift.getWeekStart(), shiftNum, role);
            }
        } catch (SQLException ex) {
            System.err.println("[ERROR] Failed to remove availability from DB: " + ex.getMessage());
        }
    }

    public void addAvailableEmployee(Employee e, int shiftNum){
        Shift shift = newShifts[shiftNum];
        if (shift.isVacation()){
            throw new RuntimeException("it's Vacation");
        }
        shift.addAvailableEmployee(e);
        // Save to DB for all roles
        try {
            EmployeeAvailabilityDAO dao = new EmployeeAvailabilityDAO();
            for (String role : e.getRoles()) {
                dao.upsertAvailability(e.getUserName(), branch.getBranchID(), shift.getWeekStart(), shiftNum, role);
            }
        } catch (SQLException ex) {
            System.err.println("[ERROR] Failed to save availability to DB: " + ex.getMessage());
        }
    }

    public String getEmployeeAvailability(Employee e){
        String st = "";
        for (Shift shift : newShifts){
            if (shift.isAvailable(e)){
                st = st + "day: " + shift.getDate().getDayOfWeek() + "  type: " + shift.getShiftType() +
                        " shift ID: " + shift.getShiftID();
            }
        }
        return st;
    }

    public void setAvailabilityChangesAllowed(boolean availabilityChangesAllowed) {
        this.availabilityChangesAllowed = availabilityChangesAllowed;
    }

    private void initNewWeekByCurrent(Shift lastShift){
        LocalDate nextSunday = lastShift.getWeekStart().plusWeeks(1);
        boolean isVacation = false;

        for (int i = 0; i<14; i++) {
            LocalDate date = nextSunday.plusDays(i/2);
            Shift.shiftType shiftType = (i % 2 == 0) ? Shift.shiftType.morning : Shift.shiftType.evening;
            String start = (shiftType == Shift.shiftType.morning) ? "08:00" : "15:00";
            String end = (shiftType == Shift.shiftType.morning) ? "15:00" : "20:00";

            Shift shift = new Shift(date, shiftType, branch, false, i, start, end, nextSunday);
            shift.setNextWeek(true);
            newShifts[i] = shift;
            // Add shift to branch's next week shifts
            branch.getNextWeekShifts()[i] = shift;
        }
    }


    private String convertTimeToString(LocalTime time) {
        try {
            return time.toString();
        } catch (Exception e) {
            throw new RuntimeException("failed to convert time to string");
        }
    }

    private LocalDate getNearestSunday(LocalDate date) {
        DayOfWeek currentDay = date.getDayOfWeek();
        int daysUntilSunday = DayOfWeek.SUNDAY.getValue() - currentDay.getValue();

        if (daysUntilSunday < 0) {
            daysUntilSunday += 7; // Adjust to next Sunday if the value is negative
        }
        return date.plusDays(daysUntilSunday);
    }
//    public Shift getShift(int shiftNum) {
//        return newShifts[shiftNum];
//    }
    public Shift getShift(int shiftNum) {
        LocalDate today = LocalDate.now();
        for (Shift shift : currentWeek) {
            if (shift != null && shift.getShiftID() == shiftNum && shift.getDate().isEqual(today)) {
                return shift;
            }
        }
        throw new RuntimeException("Shift not found in current week for today");
    }

}