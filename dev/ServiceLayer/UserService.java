package ServiceLayer;

import DomainLayer.*;

public class UserService {
    private EmployeeController employeeController;
    private BranchController branchController;
    private BranchRepository branchRepository;

    public UserService(){
        branchRepository = new BranchRepository();
        branchController = new BranchController(branchRepository);
        employeeController = EmployeeController.getInstance(branchRepository);
    }

    public Response login(String userName, String password){
        boolean error = false;
        String msg;
        try {
            employeeController.login(userName, password);
            msg = "Login successful!";
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in UserService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public Response logout(String userName){
        boolean error = false;
        String msg;
        try {
            employeeController.logout(userName);
            msg = "Logout successful!";
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in UserService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public Response changePassword(String userName, String newPassword){
        boolean error = false;
        String msg;
        try {
            employeeController.changePassword(userName, newPassword);
            msg = "Password updated successfully";
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in UserService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Updated;
        return new Response(error, msg, type);
    }

    public Response hireEmployee(String BID, String employeeName, String bankAccount, String role, String enterPassword, Integer hourlySalary) {
        boolean error = false;
        String msg;
        try {
            employeeController.hireEmployee(BID, employeeName, bankAccount, role, enterPassword, hourlySalary);
            msg = "congrats! you hired: " + employeeName;
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in UserService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Created;
        return new Response(error, msg, type);
    }

    public Response fireEmployee(String employeeName) {
        boolean error = false;
        String msg;
        try {
            msg = employeeController.fireEmployee(employeeName);
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in UserService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.NotFound : ResponseType.Deleted;
        return new Response(error, msg, type);
    }

    public Response changeEmployeeHourly(String employeeName, String newHourlySalary) {
        boolean error = false;
        String msg;
        try {
            employeeController.changeEmployeeHourly(employeeName, newHourlySalary);
            msg = "Hourly salary updated";
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in UserService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Updated;
        return new Response(error, msg, type);
    }

    public Response extendContract(String employeeName, String endContractDate) {
        boolean error = false;
        String msg;
        try {
            employeeController.extendContract(employeeName, endContractDate);
            msg = "Contract extended successfully";
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in UserService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Updated;
        return new Response(error, msg, type);
    }

    public Response showAllActiveEmployees() {
        boolean error = false;
        String msg;
        try {
            StringBuilder sb = new StringBuilder();
            for (Employee e : employeeController.getEmployees()) {
                if (e.getBranch() != null && e.getFiredDate() == null && 
                    !e.getBranch().getFiredEmployees().contains(e)) {
                    sb.append("Branch ").append(e.getBranch().getBranchID()).append(": ")
                      .append(e.getUserName()).append("\n");
                }
            }
            msg = sb.toString();
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in UserService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public Response showAllFiredEmployees() {
        boolean error = false;
        String msg;
        try {
            StringBuilder sb = new StringBuilder();
            for (Employee e : employeeController.getFiredEmployees()) {
                if (e.getBranch() != null) {
                    sb.append("Branch ").append(e.getBranch().getBranchID()).append(": ")
                      .append(e.getUserName()).append("\n");
                }
            }
            msg = sb.toString();
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in UserService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public Response showEmployeeDetails(String employeeName) {
        boolean error = false;
        String msg;
        try {
            Employee e = employeeController.getEmployee(employeeName);
            if (e == null) {
                error = true;
                msg = "Employee not found";
            } else {
                msg = e.toString();
            }
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in UserService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.NotFound : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public Response addRoleToEmployee(String employeeName, String role) {
        boolean error = false;
        String msg;
        try {
            employeeController.addRoleToEmployee(employeeName, role);
            msg = "Role added successfully";
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in UserService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Updated;
        return new Response(error, msg, type);
    }

    public Response HR_Or_KickOut(String userName) {
        boolean error = false;
        String msg;
        try {
            employeeController.HR_Or_KickOut(userName);
            msg = "HR access granted";
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in UserService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public EmployeeController getEmployeeController() {
        return employeeController;
    }

    public Response showRoles(){
        boolean error = false;
        String msg;
        try {
            msg = Role.getInstance().toString();
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in UserService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.NotFound : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public Response createNewRole(String role){
        boolean error = false;
        String msg;
        try {
            Role.getInstance().getRoles().add(role);
            msg = "new role added: " + role;
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in UserService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Updated;
        return new Response(error, msg, type);
    }

    public Response daysLeftForContract(String userName){
        boolean error = false;
        String msg;
        try {
            msg = String.valueOf(employeeController.daysLeftForContract(userName));
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in UserService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.NotFound : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public Response getInOrKickOut(String userName){
        try {
            employeeController.getInOrKickOut(userName);
        }catch (RuntimeException e){
            return new Response(true, e.getMessage(), ResponseType.InvalidInput);
        }
        return new Response(false, "You have successfully logged in", ResponseType.Success);
    }
}