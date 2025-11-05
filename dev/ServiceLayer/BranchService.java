package ServiceLayer;

import DomainLayer.*;
import java.util.ArrayList;
import java.util.List;

public class BranchService {
    private BranchController branchController;
    private EmployeeController employeeController;
    private BranchRepository branchRepository;

    public BranchService() {
        branchRepository = new BranchRepository();
        branchController = new BranchController(branchRepository);
        employeeController = EmployeeController.getInstance(branchRepository);
    }

    /**
     * Validates and adds a new branch
     */
    public Response addBranch(String city, int shippingArea, String phone, String contactName) {
        if (city == null || city.trim().isEmpty()) {
            return new Response(true, "City cannot be empty", ResponseType.InvalidInput);
        }
        if (phone == null || phone.trim().isEmpty()) {
            return new Response(true, "Phone cannot be empty", ResponseType.InvalidInput);
        }
        if (contactName == null || contactName.trim().isEmpty()) {
            return new Response(true, "Contact name cannot be empty", ResponseType.InvalidInput);
        }
        // shippingArea is int, so no need to check for empty string
        int branchId = branchController.getAllBranches().size();
        branchController.addBranch(branchId, city, shippingArea, phone, contactName);
        return new Response(false, "Branch added successfully", ResponseType.Created);
    }

    /**
     * Gets all branches with their details
     */
    public Response getAllBranches() {
        boolean error = false;
        String msg;
        try {
            StringBuilder sb = new StringBuilder();
            for (Branch branch : branchController.getAllBranches()) {
                sb.append("Branch ").append(branch.getBranchID()).append(": ")
                  .append(branch.getCity()).append(", ")
                  .append(branch.getShippingArea()).append("\n");
            }
            msg = sb.toString();
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            System.err.println("[ERROR] Exception in BranchService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.SystemError : ResponseType.Success;
        return new Response(error, msg, type);
    }

    /**
     * Validates if a branch exists and is valid
     */
    public Response validateBranch(int branchID) {
        boolean error = false;
        String msg;
        try {
            Branch branch = branchController.findBranchById(branchID);
            if (branch == null) {
                throw new RuntimeException("Branch not found");
            }
            msg = "Branch is valid";
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            System.err.println("[ERROR] Exception in BranchService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.NotFound : ResponseType.Success;
        return new Response(error, msg, type);
    }

    /**
     * Gets current week shifts for a branch
     */
    public Response getCurrentWeekShifts(int branchID) {
        boolean error = false;
        String msg;
        try {
            Branch branch = branchController.findBranchById(branchID);
            if (branch == null) {
                throw new RuntimeException("Branch not found");
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Current Week Shifts for Branch ").append(branchID).append(":\n");
            for (Shift shift : branch.getCurrentWeekShifts()) {
                if (shift != null) {
                    sb.append(shift.toString()).append("\n");
                }
            }
            msg = sb.toString();
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            System.err.println("[ERROR] Exception in BranchService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.NotFound : ResponseType.Success;
        return new Response(error, msg, type);
    }

    /**
     * Gets next week shifts for a branch
     */
    public Response getNextWeekShifts(int branchID) {
        boolean error = false;
        String msg;
        try {
            // Reload branch from DB to get up-to-date shifts and assignments
            BranchRepository branchRepository = new BranchRepository();
            Branch branch = branchRepository.findById(branchID);
            if (branch == null) {
                throw new RuntimeException("Branch not found");
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Next Week Shifts for Branch ").append(branchID).append(":\n");
            for (Shift shift : branch.getNextWeekShifts()) {
                if (shift != null) {
                    sb.append(shift.toString()).append("\n");
                }
            }
            msg = sb.toString();
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            System.err.println("[ERROR] Exception in BranchService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.NotFound : ResponseType.Success;
        return new Response(error, msg, type);
    }

    /**
     * Validates shift requirements for a branch
     */
    public Response validateShiftRequirements(int branchID, int shiftNum) {
        boolean error = false;
        String msg;
        try {
            Branch branch = branchController.findBranchById(branchID);
            if (branch == null) {
                throw new RuntimeException("Branch not found");
            }

            Shift shift = branch.getNextWeekShifts()[shiftNum];
            if (shift == null) {
                throw new RuntimeException("Shift not found");
            }

            // Validate shift requirements
            if (!shift.isValid()) {
                throw new RuntimeException("Shift does not meet HR requirements");
            }

            msg = "Shift requirements are valid";
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            System.err.println("[ERROR] Exception in BranchService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.InvalidInput : ResponseType.Success;
        return new Response(error, msg, type);
    }

    /**
     * Gets branch controller for internal use
     */
    public BranchController getBranchController() {
        return branchController;
    }

    public Response getBranchShiftHistory(int branchID) {
        boolean error = false;
        String msg;
        try {
            Branch branch = branchController.findBranchById(branchID);
            if (branch == null) {
                error = true;
                msg = "Branch not found.";
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Branch ").append(branchID).append(" Shift History:\n");
                for (Shift shift : branch.getShiftHistory()) {
                    sb.append(shift.toString()).append("\n");
                }
                msg = sb.toString();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.err.println("[ERROR] Exception in BranchService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.NotFound : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public Response getBranchEmployeeHistory(int branchID) {
        boolean error = false;
        String msg;
        try {
            Branch branch = branchController.findBranchById(branchID);
            if (branch == null) {
                error = true;
                msg = "Branch not found.";
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Branch ").append(branchID).append(" Employee History:\n");
                sb.append("Current Employees:\n");
                for (Employee employee : branch.getEmployees()) {
                    sb.append(employee.toString()).append("\n");
                }
                sb.append("\nFormer Employees:\n");
                for (Employee employee : branch.getFiredEmployees()) {
                    sb.append(employee.toString()).append("\n");
                }
                msg = sb.toString();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.err.println("[ERROR] Exception in BranchService: " + e.getMessage());
            error = true;
            msg = e.getMessage();
        }
        ResponseType type = error ? ResponseType.NotFound : ResponseType.Success;
        return new Response(error, msg, type);
    }

    public List<String> getActiveEmployeeUsernamesByBranch(int branchId) {
        Branch branch = branchController.findBranchById(branchId);
        List<String> usernames = new ArrayList<>();
        if (branch != null) {
            for (Employee e : branch.getEmployees()) {
                if (e.getFiredDate() == null) {
                    usernames.add(e.getUserName());
                }
            }
        }
        return usernames;
    }
} 