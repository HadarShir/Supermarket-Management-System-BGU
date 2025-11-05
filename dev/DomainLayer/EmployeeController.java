package DomainLayer;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import DomainLayer.EmployeeRepository;
import DomainLayer.BranchRepository;
import DataLayer.RoleDAO;
import DataLayer.EmployeeAvailabilityDAO;
import java.sql.SQLException;

// This class is designed to centralize the business logic related to employees.
public class EmployeeController {
    private static EmployeeController instance;
    private EmployeeRepository employeeRepository;
    private BranchRepository branchRepository;
    private RoleDAO roleDAO = new RoleDAO();

    public EmployeeController(BranchRepository branchRepository){
        this.branchRepository = branchRepository;
        this.employeeRepository = new EmployeeRepository();
    }

    public static EmployeeController getInstance(BranchRepository branchRepository) {
        if (instance == null) {
            instance = new EmployeeController(branchRepository);
        }
        return instance;
    }

    public void login(String userName, String password){
        Employee e = getEmployee(userName);
        if (e == null){
            throw new RuntimeException("User does not exist");
        } else {
            if (!e.checkPassword(password)){
                throw new RuntimeException("Incorrect password");
            }
            e.setIsLoggedIn(true);
            employeeRepository.updateEmployee(e);
        }
    }

    public void logout(String userName){
        Employee e = getEmployee(userName);
        if (e == null){
            throw new RuntimeException("User does not exist");
        }
        if (!e.getIsLoggedIn()){
            throw new RuntimeException("You can't logout if you're not logged in.");
        }
        e.setIsLoggedIn(false);
        employeeRepository.updateEmployee(e);
    }

    public void changePassword(String userName, String newPassword){
        Employee e = getEmployee(userName);
        if (e == null){
            throw new RuntimeException("User does not exist");
        }
        if (!e.getIsLoggedIn()){
            throw new RuntimeException("You can't change your password if you're not logged in.");
        }
        e.setPassword(newPassword);
        employeeRepository.updateEmployee(e);
    }

    public int daysLeftForContract(String userName){
        Employee e = getEmployee(userName);
        if (e == null){
            throw new RuntimeException("User does not exist");
        }
        if (!e.getIsLoggedIn()){
            throw new RuntimeException("You must login");
        }
        LocalDate end = e.getEndContract();
        LocalDate start = LocalDate.now();
        return (int) (ChronoUnit.DAYS.between(start, end));
    }

    public void addRoleToEmployee(String employeeName, String role){
        Employee e = getEmployee(employeeName);
        if (e == null){
            throw new RuntimeException("Employee does not exist in the system.");
        }
        if (!checkIfValidRole(role)){
            throw new RuntimeException("Invalid role, This role does not exist in the system.");
        }
        try {
            if (role.equals("ShiftManager")){
                e.setShiftManager(true);
                employeeRepository.updateEmployee(e);
                for (String _role : Role.getInstance().getRoles()){
                    if (!_role.equals("HrManager") ){
                        roleDAO.assignRoleToEmployee(employeeName, _role);
                    }
                }
            }
            else{
                roleDAO.assignRoleToEmployee(employeeName, role);
            }
        } catch (java.sql.SQLException ex) {
            throw new RuntimeException("Failed to assign role to employee", ex);
        }
    }

    public void hireEmployee(String BID, String employeeName, String bankAccount, String role , String enterPassword,int salary){
        Employee e = getEmployee(employeeName);
        if (e != null){
            throw new RuntimeException("A user with this name already exists.");
        }
        if (!checkIfValidUserName(employeeName)){
            throw new RuntimeException("UserName cannot include white spaces");
        }
        if (!checkIfValidRole(role)){
            throw new RuntimeException("invalid role, This role does not exist in the system.");
        }
        if (salary < 34){
            throw new RuntimeException("Invalid salary, the entered salary is less than the legal minimum wage.");
        }
        if (enterPassword.isEmpty()){
            throw new RuntimeException("Please enter a password");
        }
        int bid = convertStringToInt(BID);
        Branch branch = branchRepository.findById(bid);
        if (branch == null) {
            throw new RuntimeException("Branch not found");
        }
        Employee employee = new Employee(branch, employeeName, bankAccount,salary,enterPassword);
        employeeRepository.addEmployee(employee);
        addRoleToEmployee(employeeName, role);
    }

    public String fireEmployee(String employeeName){
        Employee e = getEmployee(employeeName);
        if (e == null){
            throw new RuntimeException("userName isn't exist");
        }
        e.setEndContract(LocalDate.now());
        e.setFiredDate(LocalDate.now());
        if (e.getBranch() != null) {
            e.getBranch().removeEmployee(e);
            e.getBranch().addFiredEmployee(e);
        }
        employeeRepository.updateEmployee(e);
        return employeeName + " is fired";
    }

    public void changeEmployeeHourly(String employeeName, String newHourlySalary){
        Employee e = getEmployee(employeeName);
        if (e == null){
            throw new RuntimeException("userName isn't exist");
        }
        int newSalary = convertStringToInt(newHourlySalary);
        e.setHourlySalary(newSalary);
        employeeRepository.updateEmployee(e);
    }

    public void extendContract(String employeeName, String endContractDate){
        Employee e = getEmployee(employeeName);
        if (e == null){
            throw new RuntimeException("userName isn't exist");
        }
        LocalDate date = stringToDateOrException(endContractDate);
        e.setEndContract(date);
        employeeRepository.updateEmployee(e);
    }

    public Employee getEmployee(String userName){
        Employee emp = employeeRepository.findByUserName(userName);
        if (emp != null && emp.getBranch() != null) {
            // Load availability from DB
            try {
                EmployeeAvailabilityDAO dao = new EmployeeAvailabilityDAO();
                int branchId = emp.getBranch().getBranchID();
                LocalDate weekStart = java.time.LocalDate.now().with(java.time.DayOfWeek.SUNDAY);
                var records = dao.getAvailabilityForEmployee(userName, branchId, weekStart);
                ShiftOrginizer org = emp.getBranch().getShiftOrganizer();
                for (var rec : records) {
                    Shift[] shifts = org.getNewShifts();
                    if (rec.shiftId >= 0 && rec.shiftId < shifts.length && shifts[rec.shiftId] != null) {
                        // Add employee as available for this shift
                        shifts[rec.shiftId].addAvailableEmployee(emp);
                    }
                }
            } catch (SQLException e) {
                System.err.println("[ERROR] Failed to load employee availability from DB: " + e.getMessage());
            }
        }
        return emp;
    }

    public ArrayList<Employee> getEmployees() {
        return new ArrayList<>(employeeRepository.getAllEmployees());
    }

    public void getInOrKickOut(String userName){
        Employee e = getEmployee(userName);
        if (e == null){
            throw new RuntimeException("User doesn't exist");
        }
        if (!e.getIsLoggedIn()){
            throw new RuntimeException("not logged in");
        }
    }

    public void HR_Or_KickOut(String userName){
        Employee e = getEmployee(userName);
        if (e == null){
            throw new RuntimeException("User does not exist");
        }
        if (!e.isHrManager() && !e.getRoles().contains("HR")){
            throw new RuntimeException("you are not HR");
        }
        if (!e.getIsLoggedIn()){
            throw new RuntimeException("hello HR Manager, please login to the system");
        }
    }

    public Branch getBranchById(int bid) {
        return branchRepository.findById(bid);
    }

    public boolean checkIfValidRole(String role){
        return Role.getInstance().containsRole(role);
    }

    public void exceptionIfInvalidBID(String BID){
        int bid = convertStringToInt(BID);
        Branch branch = branchRepository.findById(bid);
        if (branch == null) {
            throw new RuntimeException("illegal BID");
        }
    }

    private int convertStringToInt(String shift){
        int s;
        try{
            s = Integer.parseInt(shift);
        }catch (RuntimeException e){
            throw new RuntimeException("invalid value, you must provide a number");
        }
        return s;
    }

    private boolean checkIfValidUserName(String name){
        return !name.matches(".*\\s.*");
    }

    private LocalDate stringToDateOrException(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(date, formatter);
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format. Please use yyyy-MM-dd format.");
        }
    }

    public ArrayList<Employee> getFiredEmployees() {
        ArrayList<Employee> all = new ArrayList<>(employeeRepository.getAllEmployees());
        ArrayList<Employee> fired = new ArrayList<>();
        for (Employee e : all) {
            if (e.getFiredDate() != null) {
                fired.add(e);
            }
        }
        return fired;
    }
}
