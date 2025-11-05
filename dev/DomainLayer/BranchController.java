package DomainLayer;

import java.util.List;
import java.util.ArrayList;

public class BranchController {
    private static BranchController instance;
    private BranchRepository branchRepository;

    public BranchController(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public static BranchController getInstance(BranchRepository branchRepository) {
        if (instance == null) {
            instance = new BranchController(branchRepository);
        }
        return instance;
    }

    public void addBranch(Branch branch) {
        branchRepository.addBranch(branch);
    }

    public void removeBranch(Branch branch) {
        branchRepository.removeBranch(branch);
    }

    public Branch findBranchById(int branchID) {
        return branchRepository.findById(branchID);
    }

    public List<Branch> getAllBranches() {
        return branchRepository.getAllBranches();
    }

    public String showHistoryByBranch(int branchID) {
        Branch branch = branchRepository.findById(branchID);
        if (branch == null) throw new RuntimeException("Branch not found");
        List<Shift> history = branch.getShiftHistory();
        if (history.isEmpty()) {
            return "No shifts in history for this branch";
        }
        StringBuilder sb = new StringBuilder();
        for (Shift shift : history) {
            sb.append(shift.toString()).append("\n");
        }
        return sb.toString();
    }

    public List<Employee> getFiredEmployeesByBranch(int branchID) {
        Branch branch = branchRepository.findById(branchID);
        if (branch == null) throw new RuntimeException("Branch not found");
        return branch.getFiredEmployees();
    }

    public List<Shift> getCurrentWeekShiftsByBranch(int branchID) {
        Branch branch = branchRepository.findById(branchID);
        if (branch == null) throw new RuntimeException("Branch not found");
        List<Shift> result = new ArrayList<>();
        for (Shift s : branch.getCurrentWeekShifts()) if (s != null) result.add(s);
        return result;
    }

    public List<Shift> getNextWeekShiftsByBranch(int branchID) {
        Branch branch = branchRepository.findById(branchID);
        if (branch == null) throw new RuntimeException("Branch not found");
        List<Shift> result = new ArrayList<>();
        for (Shift s : branch.getNextWeekShifts()) if (s != null) result.add(s);
        return result;
    }

    public void addBranch(int branchId, String city, int shippingArea, String phone, String contactName) {
        Branch newBranch = new Branch(branchId, city, shippingArea, phone, contactName);
        branchRepository.addBranch(newBranch);
    }
} 