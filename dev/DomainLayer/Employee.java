package DomainLayer;
import java.util.ArrayList;
import java.time.LocalDate;

// This department will be responsible for loading information to this work computer.
public class Employee {
    private String userName;
    private String password;
    private Branch branch;
    private boolean isLoggedIn;
    private String bankAccount;
    private boolean isShiftManager;
    private boolean isHrManager;
    private ArrayList<String> roles;
    private int hourlySalary;
    private LocalDate startContract;
    private LocalDate endContract;
    private LocalDate firedDate;

    public Employee(Branch branch, String userName, String bankAccount, int hourlySalary, String enterPassword){
        this.branch = branch;
        this.userName = userName;
        this.bankAccount = bankAccount;
        this.roles = new ArrayList<>();  // רשימה ריקה , מתחיל ללא תפקיד
        this.hourlySalary = hourlySalary;
        this.startContract = LocalDate.now();
        this.endContract = startContract.plusYears(1);
        this.password = enterPassword;
        this.isLoggedIn = false;
        this.isHrManager = false;
        this.isShiftManager = false;
    }

    public Employee(){
        this.userName = "MY_HR"; // create HR
        this.password = "MY_HR";
        this.branch = null; // יש להכניס Branch מתאים באתחול המערכת
        this.bankAccount = "Bank";
        this.roles = new ArrayList<>();
        this.hourlySalary = 34;
        this.startContract = LocalDate.now();
        this.endContract = startContract.plusYears(1);
        this.isLoggedIn = false;
        this.isHrManager = true;
        this.isShiftManager = true;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public boolean isHrManager() {
        return isHrManager;
    }

    public void setHrManager(boolean isHrManager) {
        this.isHrManager = isHrManager;
    }

    public boolean isShiftManager() {
        return isShiftManager;
    }

    public void setShiftManager(boolean isShiftManager) {
        this.isShiftManager = isShiftManager;
    }

    public ArrayList<String> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }

    public int getHourlySalary() {
        return hourlySalary;
    }

    public void setHourlySalary(int hourlySalary) {
        this.hourlySalary = hourlySalary;
    }

    public LocalDate getStartContract() {
        return startContract;
    }

    public void setStartContract(LocalDate startContract) {
        this.startContract = startContract;
    }

    public LocalDate getEndContract() {
        return endContract;
    }

    public void setEndContract(LocalDate endContract) {
        this.endContract = endContract;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public boolean checkPassword(String input) {
        return this.password.equals(input);
    }

    public void addRole(String role) {
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }

    public LocalDate getFiredDate() {
        return firedDate;
    }

    public void setFiredDate(LocalDate firedDate) {
        this.firedDate = firedDate;
    }

    @Override
    public String toString() {
        String branchInfo = (branch != null) ? ("Branch " + branch.getBranchID() + ": " + branch.getCity() + ", " + branch.getShippingArea()) : "N/A";
        StringBuilder sb = new StringBuilder();
        sb.append("Employee: ").append(userName)
          .append(", Branch: ").append(branchInfo)
          .append(", Roles: ").append(roles.toString())
          .append(" Salary: ").append(hourlySalary)
          .append(" End Contract: ").append(endContract);
        if (firedDate != null) {
            sb.append(", Fired Date: ").append(firedDate);
        }
        return sb.toString();
    }


}
