package ServiceLayer;

import DomainLayer.Branch;
import DomainLayer.Employee;

import java.sql.SQLException;
import java.util.List;

// This class is designed to link the domain layer to the presentation layer.
public class Service {
    private ShiftService shiftService;
    private UserService userService;
    private BranchService branchService;
    private DeliveryService deliveryService;
    private TruckService truckService;

    public Service(){
        shiftService = new ShiftService();
        userService = new UserService();
        branchService = new BranchService();
        deliveryService = new DeliveryService();
        truckService = new TruckService();
        deliveryService = new DeliveryService();
        truckService = new TruckService();
    }


    // regular user
    public
    Response login(String userName, String password){
        return userService.login(userName,password);
    }

    public Response logout(String userName){
        return userService.logout(userName);
    }

    public Response changePassword(String userName, String newPassword){
        return userService.changePassword(userName,newPassword);
    }

    public Response daysLeftForContract(String userName){
        return userService.daysLeftForContract(userName);
    }



    public Response pickAvailability(String userName, String requiredShifts){
        Response r = userService.getInOrKickOut(userName);
        if (!r.isError){
            r = shiftService.pickAvailability(userName,requiredShifts);
        }
        return r;
    }

    public Response showMyAvailability(String userName){
        Response r = userService.getInOrKickOut(userName);
        if (!r.isError){
            r = shiftService.showMyAvailability(userName);
        }
        return r;
    }

    public Response changeAvailability(String userName, String dayAndType){
        Response r = userService.getInOrKickOut(userName);
        if (!r.isError){
            r = shiftService.changeAvailability(userName,dayAndType);
        }
        return r;
    }


    public Response showCurrentWeek(String userName){
        Response r = userService.getInOrKickOut(userName);
        if (!r.isError){
            r = shiftService.showCurrentWeek(userName);
        }
        return r;
    }

    public Response showNextWeek(String userName){
        Response r = userService.getInOrKickOut(userName);
        if (!r.isError){
            r = shiftService.showNextWeek(userName);
        }
        return r;
    }

    public Response showMyHistory(String userName){
        Response r = userService.getInOrKickOut(userName);
        if (!r.isError){
            r = shiftService.showMyHistory(userName);
        }
        return r;
    }

    public Response reportHours(String userName, String SID, String startTime, String endTime){
        Response r = userService.getInOrKickOut(userName);
        if (!r.isError){
            r = shiftService.reportHours(userName,SID, startTime, endTime);
        }
        return r;
    }




    // HR

    public Response HR_showCurrentWeek(String userName, String BID){
        Response r = userService.getInOrKickOut(userName);
        if (!r.isError){
            r = shiftService.HR_showCurrentWeek(BID);
        }
        return r;
    }

    public Response HR_showNextWeek(String userName, String BID){
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError){
            r = shiftService.HR_showNextWeek(BID);
        }
        return r;
    }

    public Response showAllHistory(String userName){
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError){
            r = shiftService.showAllHistory();
        }
        return r;
    }

    public Response showAllHistoryByBranch(String userName, String BID){
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError) {
            try {
                int branchID = Integer.parseInt(BID);
                r = branchService.getBranchShiftHistory(branchID);
            } catch (NumberFormatException e) {
                r = new Response(true, "Invalid branch ID format", ResponseType.InvalidInput);
            }
        }
        return r;
    }

    public Response showBranchesID(String userName){
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError) {
            r = branchService.getAllBranches();
        }
        return r;
    }


    public Response addBranch(String userName, String city, int shippingArea, String phoneNumber, String name){
        Response r = branchService.addBranch(city, shippingArea, phoneNumber, name);
        return r;
    }

    public Response showRoles(String userName){
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError) {
            r = userService.showRoles();
        }
        return r;
    }

    public Response createNewRole(String userName, String role){
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError) {
            r = userService.createNewRole(role);
        }
        return r;
    }

    public Response publishNextWeek(String userName, String BID){
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError) {
            r = shiftService.publishNextWeek(BID);
        }
        return r;
    }

    public Response setAsCurrentWeek(String userName, String BID){
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError) {
            r = shiftService.setAsCurrentWeek(BID);
        }
        return r;
    }

    public Response changeRequirement(String userName, String BID, String dayAndType, String role, String amountOfEmployees){
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError) {
            r = shiftService.changeRequirement(BID, dayAndType, role, amountOfEmployees);
        }
        return r;
    }

    public Response changeTime(String userName, String BID, String dayAndType, String start, String end){
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError) {
            r = shiftService.changeTime(BID, dayAndType, start, end);
        }
        return r;
    }

    public Response showAvailableEmployees(String userName, String BID, String dayAndType, String role){
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError) {
            r = shiftService.showAvailableEmployees(BID, dayAndType, role);
        }
        return r;
    }

    public Response pickEmployee(String userName, String BID, String dayAndType, String role, String employeeName){
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError) {
            r = shiftService.pickEmployee(BID, dayAndType, role, employeeName);
        }
        return r;
    }

    public Response setVacation(String userName, String BID, String dayAndType){
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError) {
            r = shiftService.setVacation(BID, dayAndType);
        }
        return r;
    }

    public Response allowAvailabilityChanges(String userName, String BID){
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError) {
            r = shiftService.allowAvailabilityChanges(BID);
        }
        return r;
    }

    public Response disableAvailabilityChanges(String userName, String BID){
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError) {
            r = shiftService.disableAvailabilityChanges(BID);
        }
        return r;
    }

    public Response addRoleToEmployee(String userName, String employeeName, String BID, String role) {
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError) {
            try {
                int branchID = Integer.parseInt(BID);
                Branch branch = branchService.getBranchController().findBranchById(branchID);
                if (branch == null) {
                    return new Response(true, "Branch not found", ResponseType.NotFound);
                }
                Employee employee = userService.getEmployeeController().getEmployee(employeeName);
                if (employee == null) {
                    return new Response(true, "Employee not found", ResponseType.NotFound);
                }
                if (employee.getBranch() == null || employee.getBranch().getBranchID() != branchID) {
                    return new Response(true, "Employee does not belong to this branch", ResponseType.InvalidInput);
                }
                r = userService.addRoleToEmployee(employeeName, role);
            } catch (NumberFormatException e) {
                r = new Response(true, "Invalid branch ID format", ResponseType.InvalidInput);
            } catch (RuntimeException e) {
                r = new Response(true, e.getMessage(), ResponseType.InvalidInput);
            }
        }
        return r;
    }

    public Response hireEmployee(String userName, String BID, String employeeName, String bankAccount, String role,String enterPassword,Integer hourlySalary){
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError) {
            r = userService.hireEmployee(BID,employeeName,bankAccount,role,enterPassword,hourlySalary);
        }
        return r;
    }

    public Response fireEmployee(String userName, String employeeName){
        Response r = userService.HR_Or_KickOut(userName);
        if(!r.isError){
            r = userService.fireEmployee(employeeName);
        }
        return r;
    }


    public Response changeEmployeeHourly(String userName, String employeeName, String newHourlySalary){
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError) {
            r = userService.changeEmployeeHourly(employeeName, newHourlySalary);
        }
        return r;
    }

    public Response extendContract(String userName, String employeeName, String endContractDate){
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError) {
            r = userService.extendContract(employeeName, endContractDate);
        }
        return r;
    }


    public Response showAllActiveEmployees(String userName){
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError){
            r = userService.showAllActiveEmployees();
        }
        return r;
    }

    public Response showAllFiredEmployees(String userName){
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError){
            r = userService.showAllFiredEmployees();
        }
        return r;
    }

    public Response showCurrentWeekByBranch(String userName, String BID) {
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError) {
            try {
                int branchID = Integer.parseInt(BID);
                r = branchService.getCurrentWeekShifts(branchID);
            } catch (NumberFormatException e) {
                r = new Response(true, "Invalid branch ID format", ResponseType.InvalidInput);
            }
        }
        return r;
    }

    public Response showNextWeekByBranch(String userName, String BID) {
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError) {
            try {
                int branchID = Integer.parseInt(BID);
                r = branchService.getNextWeekShifts(branchID);
            } catch (NumberFormatException e) {
                r = new Response(true, "Invalid branch ID format", ResponseType.InvalidInput);
            }
        }
        return r;
    }

    public Response validateBranchShifts(String userName, String BID, String shiftNum) {
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError) {
            try {
                int branchID = Integer.parseInt(BID);
                int shift = Integer.parseInt(shiftNum);
                r = branchService.validateShiftRequirements(branchID, shift);
            } catch (NumberFormatException e) {
                r = new Response(true, "Invalid branch ID or shift number format", ResponseType.InvalidInput);
            }
        }
        return r;
    }

    public Response showEmployeeDetails(String userName, String employeeName) {
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError) {
            r = userService.showEmployeeDetails(employeeName);
        }
        return r;
    }

    public Response HR_Or_KickOut(String userName) {
        return userService.HR_Or_KickOut(userName);
    }

    public Response isDriverManager(String userName) {
        boolean error = false;
        String msg;
        try {
            Employee employee = userService.getEmployeeController().getEmployee(userName);
            if (employee == null) {
                error = true;
                msg = "User not found";
            } else if (!employee.getRoles().contains("DriverManager")) {
                error = true;
                msg = "User is not a DriverManager";
            } else {
                msg = "DriverManager access granted";
            }
        } catch (RuntimeException e) {
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public List<String> getActiveEmployeeUsernamesByBranch(String BID) {
        int branchId = Integer.parseInt(BID);
        return branchService.getActiveEmployeeUsernamesByBranch(branchId);
    }

    public Response showFiredEmployeesByBranch(String userName, String BID) {
        Response r = userService.HR_Or_KickOut(userName);
        if (!r.isError) {
            try {
                int branchID = Integer.parseInt(BID);
                List<Employee> fired = branchService.getBranchController().getFiredEmployeesByBranch(branchID);
                StringBuilder sb = new StringBuilder();
                for (Employee e : fired) {
                    sb.append(e.getUserName()).append("\n");
                }
                return new Response(false, sb.toString(), ResponseType.Success);
            } catch (NumberFormatException e) {
                return new Response(true, "Invalid branch ID format", ResponseType.InvalidInput);
            }
        }
        return r;
    }

//    public void resetDatabase() throws Exception {
//        deliveryService.restartSystem();
//        truckService.restartSystem();
//    }

}