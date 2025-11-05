package DomainLayer;

import DataLayer.BranchDAO;
import DTO.BranchDTO;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import DomainLayer.EmployeeRepository;
import DomainLayer.Employee;
import java.util.Collections;

/**
 * Repository class for managing Branch entities and their relationships with shifts.
 * Handles conversion between DTOs and domain entities, and manages the loading of related data.
 */
public class BranchRepository {
    private BranchDAO branchDAO = new BranchDAO();
    private ShiftRepository shiftRepository = new ShiftRepository();

    /**
     * Splits shifts into current week, next week, and history based on their week start date.
     * @param allShifts List of all shifts to split
     * @param currentWeek List to store current week shifts
     * @param nextWeek List to store next week shifts
     * @param history List to store historical shifts
     */
    private void splitShiftsByWeek(List<Shift> allShifts, List<Shift> currentWeek, List<Shift> nextWeek, List<Shift> history) {
        LocalDate now = LocalDate.now();
        // Find the most recent Sunday (or today if it's Sunday)
        LocalDate currentWeekStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate nextWeekStart = currentWeekStart.plusWeeks(1);

        for (Shift shift : allShifts) {
            if (shift.getWeekStart().equals(currentWeekStart)) {
                currentWeek.add(shift);
            } else if (shift.getWeekStart().equals(nextWeekStart)) {
                nextWeek.add(shift);
            } else if (shift.getWeekStart().isBefore(currentWeekStart)) {
                history.add(shift);
            }
        }
    }

    /**
     * Converts a BranchDTO to a Branch entity with all its related shifts.
     * @param dto The BranchDTO to convert
     * @param branchMap Map of branch IDs to Branch entities for shift relationships
     * @return A fully populated Branch entity
     */
    private Branch convertFromDTO(BranchDTO dto, Map<Integer, Branch> branchMap) {
        Branch branch = new Branch(
            dto.branchId(),
            dto.city(),
            dto.shippingArea(),
            dto.phone(),
            dto.contactName()
        );

        // Load all shifts for the branch
        List<Shift> allShifts = shiftRepository.getShiftsForBranch(branch.getBranchID(), branchMap);
        
        // Load employee availability for each shift
        for (Shift shift : allShifts) {
            shiftRepository.loadAvailabilityForShift(shift);
        }

        // Split shifts into appropriate categories
        List<Shift> currentWeekShifts = new ArrayList<>();
        List<Shift> nextWeekShifts = new ArrayList<>();
        List<Shift> shiftHistory = new ArrayList<>();
        
        splitShiftsByWeek(allShifts, currentWeekShifts, nextWeekShifts, shiftHistory);

        // Set the shifts in the branch
        Shift[] currentWeekArr = new Shift[14];
        for (Shift shift : currentWeekShifts) {
            int idx = shift.getShiftID();
            if (idx >= 0 && idx < 14) {
                currentWeekArr[idx] = shift;
            }
        }
        branch.setCurrentWeekShifts(currentWeekArr);

        Shift[] nextWeekArr = new Shift[14];
        for (Shift shift : nextWeekShifts) {
            int idx = shift.getShiftID();
            if (idx >= 0 && idx < 14) {
                nextWeekArr[idx] = shift;
            }
        }
        branch.setNextWeekShifts(nextWeekArr);
        // Note: shiftHistory is not stored in the Branch class, it's only used for organization

        // Initialize ShiftOrganizer with all shifts
        branch.setShiftOrganizer(new ShiftOrginizer(
            branch,
            branch.getCurrentWeekShifts(),
            branch.getNextWeekShifts()
        ));

        return branch;
    }

    /**
     * Converts a Branch entity to a BranchDTO.
     * @param branch The Branch entity to convert
     * @return A BranchDTO representation of the Branch
     */
    private BranchDTO convertToDTO(Branch branch) {
        return new BranchDTO(
            branch.getBranchID(),
            branch.getCity(),
            branch.getShippingArea(),
            branch.getPhone(),
            branch.getContactName()
        );
    }

    /**
     * Retrieves all branches with their related shifts.
     * @return List of all branches with their shifts properly loaded and organized
     */
    public List<Branch> getAllBranches() {
        try {
            List<BranchDTO> dtos = branchDAO.getAll();
            // Create all Branch entities without shifts first
            List<Branch> branches = dtos.stream()
                .map(dto -> new Branch(
                    dto.branchId(),
                    dto.city(),
                    dto.shippingArea(),
                    dto.phone(),
                    dto.contactName()
                ))
                .collect(Collectors.toList());

            // Create a map of branchId -> Branch for relationship management
            Map<Integer, Branch> branchMap = new HashMap<>();
            for (Branch b : branches) {
                branchMap.put(b.getBranchID(), b);
            }

            // --- Load all employees in one batch and assign to branches ---
            EmployeeRepository employeeRepository = new EmployeeRepository();
            List<Employee> allEmployees = employeeRepository.getAllEmployees(branchMap);
            for (Employee e : allEmployees) {
                Branch branch = e.getBranch();
                if (branch != null) {
                    if (e.getFiredDate() == null) {
                        branch.addEmployee(e);
                    } else {
                        branch.addFiredEmployee(e);
                    }
                }
            }
            // --- End employee assignment ---

            // --- Batch load all shifts for all branches ---
            List<Shift> allShifts = new ArrayList<>();
            Map<Integer, List<Shift>> branchIdToShifts = new HashMap<>();
            for (Branch b : branches) {
                List<Shift> shifts = shiftRepository.getShiftsForBranch(b.getBranchID(), branchMap);
                branchIdToShifts.put(b.getBranchID(), shifts);
                allShifts.addAll(shifts);
            }

            // --- Batch load all assigned employees for all shifts ---
            try (java.sql.Connection conn = DataLayer.DatabaseConnector.getConnection()) {
                Map<Integer, List<Shift.AssignedEmployee>> assignedEmployeesMap =
                    shiftRepository.getShiftDAO().loadAllAssignedEmployees(conn, allShifts);
                for (Shift shift : allShifts) {
                    List<Shift.AssignedEmployee> assignedEmployees =
                        assignedEmployeesMap.get(shift.getShiftID());
                    if (assignedEmployees != null) {
                        shift.setEmployyes(assignedEmployees);
                    }
                }
            }

            // --- Batch load all availability for all shifts ---
            try (java.sql.Connection conn = DataLayer.DatabaseConnector.getConnection()) {
                for (Shift shift : allShifts) {
                    ArrayList<String> availableEmployees = shiftRepository.getShiftDAO().getAvailableEmployeesForShift(
                        conn,
                        shift.getBranch().getBranchID(),
                        shift.getWeekStart(),
                        shift.getShiftID()
                    );
                    shift.setAvailableEmployees(availableEmployees);
                }
            }

            // --- Organize shifts for each branch ---
            for (Branch b : branches) {
                List<Shift> shifts = branchIdToShifts.get(b.getBranchID());
                if (shifts == null) shifts = new ArrayList<>();
                List<Shift> currentWeekShifts = new ArrayList<>();
                List<Shift> nextWeekShifts = new ArrayList<>();
                List<Shift> shiftHistory = new ArrayList<>();
                splitShiftsByWeek(shifts, currentWeekShifts, nextWeekShifts, shiftHistory);
                Shift[] currentWeekArr = new Shift[14];
                for (Shift shift : currentWeekShifts) {
                    int idx = shift.getShiftID();
                    if (idx >= 0 && idx < 14) {
                        currentWeekArr[idx] = shift;
                    }
                }
                b.setCurrentWeekShifts(currentWeekArr);
                Shift[] nextWeekArr = new Shift[14];
                for (Shift shift : nextWeekShifts) {
                    int idx = shift.getShiftID();
                    if (idx >= 0 && idx < 14) {
                        nextWeekArr[idx] = shift;
                    }
                }
                b.setNextWeekShifts(nextWeekArr);
                // Initialize ShiftOrganizer
                b.setShiftOrganizer(new ShiftOrginizer(
                    b,
                    b.getCurrentWeekShifts(),
                    b.getNextWeekShifts()
                ));
            }
            return branches;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load branches from DB", e);
        }
    }

    /**
     * Adds a new branch to the database.
     * @param branch The branch to add
     */
    public void addBranch(Branch branch) {
        try {
            branchDAO.addT(convertToDTO(branch));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add branch to DB", e);
        }
    }

    /**
     * Removes a branch from the database.
     * @param branch The branch to remove
     */
    public void removeBranch(Branch branch) {
        try {
            branchDAO.deleteT(String.valueOf(branch.getBranchID()));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove branch from DB", e);
        }
    }

    /**
     * Finds a branch by its ID.
     * @param branchID The ID of the branch to find
     * @return The found branch, or null if not found
     */
    public Branch findById(int branchID) {
        try {
            BranchDTO dto = branchDAO.getById(String.valueOf(branchID));
            if (dto == null) {
                return null;
            }

            // Create a map with just this branch for relationship management
            Map<Integer, Branch> branchMap = new HashMap<>();
            Branch branch = new Branch(
                dto.branchId(),
                dto.city(),
                dto.shippingArea(),
                dto.phone(),
                dto.contactName()
            );
            branchMap.put(branch.getBranchID(), branch);

            // Load employees for this branch
            EmployeeRepository employeeRepository = new EmployeeRepository();
            List<Employee> branchEmployees = employeeRepository.getEmployeesByBranch(branchID, branchMap);
            
            for (Employee e : branchEmployees) {
                if (e.getFiredDate() == null) {
                    branch.addEmployee(e);
                } else {
                    branch.addFiredEmployee(e);
                }
            }

            // Load shifts for the branch
            List<Shift> allShifts = shiftRepository.getShiftsForBranch(branch.getBranchID(), branchMap);
            
            // Load employee availability for each shift
            for (Shift shift : allShifts) {
                shiftRepository.loadAvailabilityForShift(shift);
            }

            // Split shifts into appropriate categories
            List<Shift> currentWeekShifts = new ArrayList<>();
            List<Shift> nextWeekShifts = new ArrayList<>();
            List<Shift> shiftHistory = new ArrayList<>();
            
            splitShiftsByWeek(allShifts, currentWeekShifts, nextWeekShifts, shiftHistory);

            // Set the shifts in the branch
            Shift[] currentWeekArr = new Shift[14];
            for (Shift shift : currentWeekShifts) {
                int idx = shift.getShiftID();
                if (idx >= 0 && idx < 14) {
                    currentWeekArr[idx] = shift;
                }
            }
            branch.setCurrentWeekShifts(currentWeekArr);

            Shift[] nextWeekArr = new Shift[14];
            for (Shift shift : nextWeekShifts) {
                int idx = shift.getShiftID();
                if (idx >= 0 && idx < 14) {
                    nextWeekArr[idx] = shift;
                }
            }
            branch.setNextWeekShifts(nextWeekArr);

            // Initialize ShiftOrganizer with all shifts
            branch.setShiftOrganizer(new ShiftOrginizer(
                branch,
                branch.getCurrentWeekShifts(),
                branch.getNextWeekShifts()
            ));

            return branch;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find branch in DB", e);
        }
    }

    public List<String> getActiveEmployeeUsernamesByBranch(int branchId) {
        // Implementation of getActiveEmployeeUsernamesByBranch method
        // This is a placeholder and should be implemented based on your actual implementation
        return new ArrayList<>();
    }

    public void printActiveEmployeeUsernamesByBranch(int branchId) {
        List<String> usernames = getActiveEmployeeUsernamesByBranch(branchId);
        Collections.sort(usernames);
        for (String username : usernames) {
            System.out.println(username);
        }
    }
} 