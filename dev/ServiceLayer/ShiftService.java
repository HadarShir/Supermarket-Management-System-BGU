package ServiceLayer;

import DomainLayer.*;

public class ShiftService {
    private ShiftController shiftController;
    private EmployeeController employeeController;
    private BranchService branchService;

    public ShiftService() {
        shiftController = ShiftController.getInstance();
        branchService = new BranchService();
        employeeController = EmployeeController.getInstance(new BranchRepository());
    }

    public Response pickAvailability(String userName, String requiredShifts){
        boolean error = false;
        String msg ;
        try {
            shiftController.pickAvailability(userName, requiredShifts);
            msg = "Availability picked successfully!";
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in ShiftService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.SystemError : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public Response showMyAvailability(String userName){
        boolean error = false;
        String msg;
        try {
            msg = shiftController.showMyAvailability(userName);
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in ShiftService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.NotFound : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public Response changeAvailability(String userName, String dayAndType){
        boolean error = false;
        String msg;
        try {
            shiftController.changeAvailability(userName, dayAndType);
            msg = "Availability changed successfully!";
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in ShiftService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Updated;
        return new Response(error, msg, type);
    }

    public Response showCurrentWeek(String employeeName){
        boolean error = false;
        String msg;
        try {
            msg = shiftController.showCurrentWeek(employeeName);
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in ShiftService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public Response showNextWeek(String employeeName){
        boolean error = false;
        String msg;
        try {
            msg = shiftController.showNextWeek(employeeName);
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in ShiftService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public Response showMyHistory(String userName){
        boolean error = false;
        String msg;
        try {
            msg = shiftController.showMyHistory(userName);
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in ShiftService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public Response reportHours(String userName, String SID, String startTime, String endTime){
        boolean error = false;
        String msg ;
        try {
            shiftController.reportHours(userName, SID, startTime, endTime);
            msg = "Hours reported successfully!";
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in ShiftService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Updated;
        return new Response(error, msg, type);
    }

    // **HR

    public Response HR_showCurrentWeek(String BID){
        boolean error = false;
        String msg;
        try {
            msg = shiftController.HR_showCurrentWeek(BID);
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in ShiftService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public Response HR_showNextWeek(String BID){
        boolean error = false;
        String msg;
        try {
            msg = shiftController.HR_showNextWeek(BID);
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in ShiftService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public Response showAllHistory(){
        boolean error = false;
        String msg;
        try {
            StringBuilder sb = new StringBuilder();
            for (Branch branch : branchService.getBranchController().getAllBranches()) {
                Response branchHistoryResponse = branchService.getBranchShiftHistory(branch.getBranchID());
                if (branchHistoryResponse.isError) {
                    throw new RuntimeException(branchHistoryResponse.msg);
                }
                sb.append(branchHistoryResponse.msg).append("\n");
            }
            msg = sb.toString();
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in ShiftService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public Response allowAvailabilityChanges(String BID){
        boolean error = false;
        String msg;
        try {
            // Validate branch exists
            Response branchResponse = branchService.validateBranch(Integer.parseInt(BID));
            if (branchResponse.isError) {
                throw new RuntimeException(branchResponse.msg);
            }
            shiftController.allowAvailabilityChanges(BID);
            msg = "Availability changes allowed.";
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in ShiftService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public Response disableAvailabilityChanges(String BID){
        boolean error = false;
        String msg;
        try {
            // Validate branch exists
            Response branchResponse = branchService.validateBranch(Integer.parseInt(BID));
            if (branchResponse.isError) {
                throw new RuntimeException(branchResponse.msg);
            }
            shiftController.disableAvailabilityChanges(BID);
            msg = "Availability changes disabled.";
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in ShiftService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public Response setVacation(String BID, String dayAndType){
        boolean error = false;
        String msg;
        try {
            // Validate branch exists
            Response branchResponse = branchService.validateBranch(Integer.parseInt(BID));
            if (branchResponse.isError) {
                throw new RuntimeException(branchResponse.msg);
            }
            shiftController.setVacation(BID, dayAndType);
            msg = "Vacation set successfully.";
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in ShiftService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Updated;
        return new Response(error, msg, type);
    }

    public Response changeRequirement(String BID, String dayAndType, String role, String amountOfEmployees){
        boolean error = false;
        String msg;
        try {
            // Validate branch exists
            Response branchResponse = branchService.validateBranch(Integer.parseInt(BID));
            if (branchResponse.isError) {
                throw new RuntimeException(branchResponse.msg);
            }
            // Validate shift manager requirement
            if (role.equalsIgnoreCase("ShiftManager") && Integer.parseInt(amountOfEmployees) < 1) {
                error = true;
                msg = "A shift must have at least one shift manager.";
            } else {
                shiftController.changeRequirement(BID, dayAndType, role, amountOfEmployees);
                msg = "Requirement changed successfully.";
            }
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in ShiftService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Updated;
        return new Response(error, msg, type);
    }

    public Response changeTime(String BID, String dayAndType, String start, String end){
        boolean error = false;
        String msg;
        try {
            // Validate branch exists
            Response branchResponse = branchService.validateBranch(Integer.parseInt(BID));
            if (branchResponse.isError) {
                throw new RuntimeException(branchResponse.msg);
            }
            shiftController.changeTime(BID, dayAndType, start, end);
            msg = "Time changed successfully.";
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in ShiftService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Updated;
        return new Response(error, msg, type);
    }

    public Response showAvailableEmployees(String BID, String dayAndType, String role){
        boolean error = false;
        String msg;
        try {
            // Validate branch exists
            Response branchResponse = branchService.validateBranch(Integer.parseInt(BID));
            if (branchResponse.isError) {
                throw new RuntimeException(branchResponse.msg);
            }
            msg = shiftController.showAvailableEmployees(BID, dayAndType, role);
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in ShiftService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public Response pickEmployee(String BID, String dayAndType, String role, String employeeName){
        boolean error = false;
        String msg;
        try {
            // Validate branch exists
            Response branchResponse = branchService.validateBranch(Integer.parseInt(BID));
            if (branchResponse.isError) {
                throw new RuntimeException(branchResponse.msg);
            }
            shiftController.pickEmployee(BID, dayAndType, role, employeeName);
            msg = "Employee picked successfully.";
            // בדוק זמינות ישירות מה-DB
            try {
                DataLayer.EmployeeAvailabilityDAO dao = new DataLayer.EmployeeAvailabilityDAO();
                int branchId = Integer.parseInt(BID);
                int shiftNum = Integer.parseInt(dayAndType);
                java.time.LocalDate weekStart = shiftController.getShift(branchId, shiftNum).getWeekStart();
                java.util.List<String> available = dao.getAvailableUsernamesForShiftAndRole(branchId, weekStart, shiftNum, role);
                if (!available.contains(employeeName)) {
                    msg += " Note: This employee did not mark availability for this shift.";
                }
            } catch (java.sql.SQLException ex) {
                msg += " [WARNING] Could not verify availability from DB: " + ex.getMessage();
            }
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in ShiftService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Success;
        return new Response(error, msg, type);
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
    public Response publishNextWeek(String BID){
        boolean error = false;
        String msg;
        try {
            // Validate branch exists
            Response branchResponse = branchService.validateBranch(Integer.parseInt(BID));
            if (branchResponse.isError) {
                throw new RuntimeException(branchResponse.msg);
            }
            shiftController.publishNextWeek(BID);
            msg = "Next week published successfully.";
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in ShiftService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public Response setAsCurrentWeek(String BID){
        boolean error = false;
        String msg;
        try {
            // Validate branch exists
            Response branchResponse = branchService.validateBranch(Integer.parseInt(BID));
            if (branchResponse.isError) {
                throw new RuntimeException(branchResponse.msg);
            }
            shiftController.setAsCurrentWeek(BID);
            msg = "Week set as current.";
        }
        catch (RuntimeException e){
            e.printStackTrace();
            System.err.println("[ERROR] Exception in ShiftService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Updated;
        return new Response(error, msg, type);
    }

    public ShiftController getShiftController() {
        return shiftController;
    }

    public void setShiftController(ShiftController shiftController) {
        this.shiftController = shiftController;
    }
}