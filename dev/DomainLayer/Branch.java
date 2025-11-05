package DomainLayer;

import java.util.ArrayList;
import java.util.List;

public class Branch {
    private int branchID;
    private String city;
    private int shippingArea;
    private String phone;
    private String contactName;
    private List<Employee> employees;
    private List<Employee> firedEmployees;
    private Shift[] currentWeekShifts;
    private Shift[] nextWeekShifts;
    private List<Shift> shiftHistory = new ArrayList<>();
    private ShiftOrginizer shiftOrganizer;

    public Branch(int branchID, String city, int shippingArea, String phone, String contactName) {
        this.branchID = branchID;
        this.city = city;
        this.shippingArea = shippingArea;
        this.phone = phone;
        this.contactName = contactName;
        this.employees = new ArrayList<>();
        this.firedEmployees = new ArrayList<>();
        this.currentWeekShifts = new Shift[14];
        this.nextWeekShifts = new Shift[14];
        this.shiftOrganizer = new ShiftOrginizer(this, currentWeekShifts, nextWeekShifts);
    }

    public int getBranchID() {
        return branchID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getShippingArea() {
        return shippingArea;
    }

    public void setShippingArea(int shippingArea) {
        this.shippingArea = shippingArea;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<Employee> getFiredEmployees() {
        return firedEmployees;
    }

    public Shift[] getCurrentWeekShifts() {
        return currentWeekShifts;
    }

    public void setCurrentWeekShifts(Shift[] currentWeekShifts) {
        this.currentWeekShifts = currentWeekShifts;
        for (int i = 0; i < currentWeekShifts.length; i++) {
            if (currentWeekShifts[i] != null && currentWeekShifts[i].getBranch() == null) {
                currentWeekShifts[i].setBranch(this);
            }
        }
        if (shiftOrganizer != null) {
            shiftOrganizer.setCurrentWeek(currentWeekShifts);
            // Initialize newShifts based on current week
            Shift[] newShifts = new Shift[14];
            for (int i = 0; i < 14; i++) {
                if (currentWeekShifts[i] != null) {
                    newShifts[i] = new Shift(
                        currentWeekShifts[i].getDate().plusDays(7),
                        currentWeekShifts[i].getShiftType(),
                        this,
                        currentWeekShifts[i].isVacation(),
                        currentWeekShifts[i].getShiftID(),
                        currentWeekShifts[i].getStart().toString(),
                        currentWeekShifts[i].getEnd().toString(),
                        currentWeekShifts[i].getWeekStart().plusDays(7)
                    );
                }
            }
            shiftOrganizer.setNewShifts(newShifts);
        }
    }

    public Shift[] getNextWeekShifts() {
        return nextWeekShifts;
    }

    public void setNextWeekShifts(Shift[] nextWeekShifts) {
        this.nextWeekShifts = nextWeekShifts;
        for (int i = 0; i < nextWeekShifts.length; i++) {
            if (nextWeekShifts[i] != null && nextWeekShifts[i].getBranch() == null) {
                nextWeekShifts[i].setBranch(this);
            }
        }
        if (shiftOrganizer != null) {
            shiftOrganizer.setNewShifts(nextWeekShifts);
        }
    }

    public void addEmployee(Employee employee) {
        if (!employees.contains(employee)) {
            employees.add(employee);
        }
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
    }

    public void addFiredEmployee(Employee employee) {
        if (!firedEmployees.contains(employee)) {
            firedEmployees.add(employee);
        }
    }

    public void removeFiredEmployee(Employee employee) {
        firedEmployees.remove(employee);
    }

    public List<Shift> getShiftHistory() {
        return shiftHistory;
    }

    public void addToShiftHistory(Shift shift) {
        shiftHistory.add(shift);
    }

    public ShiftOrginizer getShiftOrganizer() {
        return shiftOrganizer;
    }

    public void setShiftOrganizer(ShiftOrginizer shiftOrganizer) {
        this.shiftOrganizer = shiftOrganizer;
    }

    @Override
    public String toString() {
        return "Branch " + branchID + ": " + city + ", " + shippingArea + ", " + phone + ", " + contactName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return branchID == branch.branchID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(branchID);
    }

} 