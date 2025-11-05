package DomainLayer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

// This class represents a shift in the system, holding details such as shift type, date, branch, etc.
public class Shift {

    private int shiftID; // (0-13)
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private Branch branch;
    private boolean isVacation;
    private shiftType shiftType;
    private HashMap<String, Integer> requirements; // requirements for the shift
    private ArrayList<AssignedEmployee> employees;
    private ArrayList<Employee> availableEmployees;
    private LocalDate weekStart;
    private boolean isPublished = false;

    // This class is designed to assign an employee to each shift.
    public static class AssignedEmployee {
        private String name;
        private String role;
        private LocalTime start;
        private LocalTime end;

        public AssignedEmployee(String name, String role, LocalTime start, LocalTime end) {
            this.name = name;
            this.role = role;
            this.start = start;
            this.end = end;
        }

        public String getName() {return name;}

        public String getRole() {return role;}

        public LocalTime getStart() {return start;}

        public LocalTime getEnd() {return end;}

        public void setStart(LocalTime start) {this.start = start;}

        public void setEnd(LocalTime end) {this.end = end;}

        @Override
        public String toString() {
            return name + " (" + role + ") " + start + "–" + end;
        }

    }


    public enum shiftType {
        morning, evening;
    }

    private boolean isCurrentWeek;
    private boolean isNextWeek;

    // Constructor
    public Shift(LocalDate date, shiftType shiftType, Branch branch, boolean isVacation, int shiftID, String start, String end, LocalDate weekStart ) {
        this.date = date;
        this.shiftType = shiftType;
        this.branch = branch;
        this.start = convertStringToTime(start);
        this.end = convertStringToTime(end);
        this.isVacation = isVacation;
        this.shiftID = shiftID;
        this.requirements = new HashMap<>();
        this.employees = new ArrayList<>();
        this.availableEmployees = new ArrayList<>();
        setShiftNum();
        initDefaultRequirements();
        this.isNextWeek = shiftID < 0;
        this.weekStart = weekStart;
        this.isPublished = false;
    }

    // Currently, the system is configured by default to require one staff member for each role.
    private void initDefaultRequirements() {
        requirements.clear(); // ננקה קודם
        for (String role : Role.getInstance().getRoles()) {
            // הגדר 0 למחסנאי וכל סוגי הנהגים
            if (role.equals("StoreKeeper") || 
                role.equals("Driver_A") || 
                role.equals("Driver_B") || 
                role.equals("Driver_C") || 
                role.equals("Driver_D")) {
                requirements.put(role, 0);
            } else {
                requirements.put(role, 1);
            }
        }
    }

    private void setShiftNum() {
        int dayIndex = (date.getDayOfWeek().getValue() % 7); // הופך יום ראשון ל-0
        int shiftIndex = (this.shiftType == shiftType.morning) ? 0 : 1;
        int shiftID = dayIndex * 2 + shiftIndex;
        this.shiftID = shiftID;
    }

    public ArrayList<AssignedEmployee> getEmployyes() {
        return employees;
    }

    public int getShiftID() {
        return shiftID;
    }

    public void setShiftID(int shiftID) {
        this.shiftID = shiftID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public shiftType getShiftType() {
        return shiftType;
    }

   

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public boolean isVacation() {
        return isVacation;
    }

    public void setVacation(boolean vacation) {
        this.isVacation = vacation;
    }

    public boolean isCurrentWeek() {
        return isCurrentWeek;
    }

    public void setCurrentWeek(boolean currentWeek) {
        isCurrentWeek = currentWeek;
    }

    public boolean isNextWeek() {
        return isNextWeek;
    }

    public void setNextWeek(boolean nextWeek) {
        isNextWeek = nextWeek;
    }



    //Returns the requirements map: how many employees are needed for each position
    public Map<String, Integer> getRequirements() {
        return requirements;
    }

    // //Returns the list of employees who are available for assignment to a shift (not actually assigned yet)
    public ArrayList<Employee> getAvailableEmployees() {
        return availableEmployees;
    }

    public boolean isAvailable(Employee e){
        return availableEmployees.contains(e);
    }

    public void addAvailableEmployee(Employee e){
        if (!availableEmployees.contains(e)){
            availableEmployees.add(e);
        }
    }

    public void removeAvailableEmployee(Employee e){
        availableEmployees.remove(e);
    }



    public AssignedEmployee getEmployeeArray(String userName){
        for(AssignedEmployee employee : employees){
            if ((employee.getName()).equals(userName)){
                return employee;
            }
        }
        return null;
    }

    public ArrayList<Employee> getAvailableByRole(String role){
        ArrayList<Employee> byRole = new ArrayList<>();
        for (Employee e : availableEmployees){
            if (e.getRoles().contains(role)){
                byRole.add(e);
            }
        }
        return byRole;
    }

    public void changeReq(String role, int num){ // מומש
        requirements.put(role, num);
    }

    public boolean isWorked(String userName){
        for(AssignedEmployee e : employees){
            if (e.getName().equals(userName)){
                return true;
            }
        }
        return false;
    }

    //סופר כמה עובדים שובצו בפועל לתפקיד מסוים במשמרת
    public int chosenForRole(String role){
        int count = 0;
        for (AssignedEmployee e  : employees){
            if(e.getRole().equals(role)){
                count++;
            }
        }
        return count;
    }


    // //Checks if the shift is correct (and the exact number of employees required for each position, if the shift is not a vacation)
    public boolean isValid(){
        for (String role : requirements.keySet()){
            if (chosenForRole(role) != requirements.get(role) && !isVacation){
                return false;
            }
        }
        return true;
    }

    public void addAssignedEmployee(String name, String role, String start, String end) {
        // Check if employee already exists in this role
        for (AssignedEmployee emp : employees) {
            if (emp.getName().equals(name) && emp.getRole().equals(role)) {
                return; // Employee already assigned to this role
            }
        }
        
        // Check if we have enough slots for this role
        int currentCount = chosenForRole(role);
        int requiredCount = requirements.getOrDefault(role, 1); // Default to 1 if not specified
        
        // Check if we have available slots
        if (currentCount >= requiredCount && !isVacation) {
            throw new RuntimeException("No more slots available for this role in this shift");
        }
        
        AssignedEmployee e = new AssignedEmployee(name, role, convertStringToTime(start), convertStringToTime(end));
        employees.add(e);
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    @Override
    public String toString() {
        // טען דרישות מה-DB לפני הצגה
        try {
            DataLayer.ShiftDAO shiftDAO = new DataLayer.ShiftDAO();
            Map<String, Integer> reqs = shiftDAO.getRequirementsForShift(
                branch.getBranchID(), weekStart, shiftID
            );
            if (reqs != null && !reqs.isEmpty()) {
                requirements.clear();
                requirements.putAll(reqs);
            }
        } catch (Exception e) {
            // אפשר להציג אזהרה או להתעלם
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Branch: ").append(branch).append("\n");
        sb.append("shift ID: ").append(shiftID).append("\n");
        sb.append("week_start: ").append(weekStart).append("\n");
        sb.append("day: ").append(date.getDayOfWeek()).append(" ").append(shiftType).append("\n");
        sb.append("start: ").append(start).append("\n");
        sb.append("end: ").append(end).append("\n");
        if (isVacation) {
            sb.append("** VACATION / CLOSED **\n");
            return sb.toString();
        }
        sb.append("Requirements:\n");
        for (Map.Entry<String, Integer> entry : requirements.entrySet()) {
            if (entry.getValue() == 0) continue; // אל תדפיס דרישות 0
            sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        sb.append("Assigned Employees:\n");
        if (employees.isEmpty()) {
            sb.append("  None assigned yet\n");
        } else {
            for (AssignedEmployee emp : employees) {
                sb.append("  ").append(emp).append("\n");
            }
        }
        return sb.toString();
    }


    private LocalTime convertStringToTime(String time){
        try {
            return LocalTime.parse(time);
        } catch (Exception e) {
            throw new RuntimeException("invalid time format, must be 'HH:mm'");
        }
    } // מומש

    private String convertTimeToString(LocalTime time) {
        try {
            return time.toString();
        } catch (Exception e) {
            throw new RuntimeException("failed to convert time to string");
        }
    }

    public ArrayList<AssignedEmployee> getDrivers() {
        ArrayList<AssignedEmployee> drivers = new ArrayList<>();
        for (AssignedEmployee employee : employees) {
            String role = employee.getRole();
            if (role.equals("Driver_A") || role.equals("Driver_B") ||
                    role.equals("Driver_C") || role.equals("Driver_D")) {
                drivers.add(employee);
            }
        }
        return drivers;
    }
    public LocalDate getWeekStart() {
        return weekStart;
    }

    public void setWeekStart(LocalDate weekStart) {
        this.weekStart = weekStart;
    }

    public void setAvailableEmployees(ArrayList<String> userNames) {
        this.availableEmployees.clear();
        for (String userName : userNames) {
            // יש להניח שיש פונקציה למציאת Employee לפי שם בסניף
            if (branch != null) {
                for (Employee e : branch.getEmployees()) {
                    if (e.getUserName().equals(userName)) {
                        this.availableEmployees.add(e);
                        break;
                    }
                }
            }
        }
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        this.isPublished = published;
    }

    /**
     * Sets the list of assigned employees for this shift.
     * @param employees List of assigned employees
     */
    public void setEmployyes(List<AssignedEmployee> employees) {
        this.employees = new ArrayList<>(employees);
    }

}