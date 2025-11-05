package DataLayer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import DomainLayer.Branch;
import DomainLayer.Employee;
import DomainLayer.Shift;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import DomainLayer.BranchRepository;
import DomainLayer.EmployeeRepository;
import DomainLayer.ShiftRepository;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

public class DatabaseTest {
    // Cache for branches and their data
    private static Map<Integer, Branch> branchCache = new HashMap<>();
    private static boolean isCacheInitialized = false;
    private static final LocalDate TARGET_WEEK = LocalDate.of(2025, 6, 1); // השבוע שאנחנו רוצים להציג

    private static void initializeCache() {
        if (!isCacheInitialized) {
            BranchRepository branchRepository = new BranchRepository();
            List<Branch> branches = branchRepository.getAllBranches();
            
            // Store branches in cache
            for (Branch branch : branches) {
                branchCache.put(branch.getBranchID(), branch);
            }
            
            isCacheInitialized = true;
        }
    }

    public static void main(String[] args) {
        initializeCache();
        
        // מיון הסניפים לפי ID
        List<Branch> sortedBranches = branchCache.values().stream()
            .sorted(Comparator.comparing(Branch::getBranchID))
            .collect(Collectors.toList());

        for (Branch branch : sortedBranches) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("Branch " + branch.getBranchID() + ": " + branch.getCity());
            System.out.println("=".repeat(50));

            // הדפסת עובדי הסניף
            System.out.println("\nEmployees in this branch:");
            System.out.println("-".repeat(30));
            List<Employee> sortedEmployees = branch.getEmployees().stream()
                .sorted(Comparator.comparing(Employee::getUserName))
                .collect(Collectors.toList());
            
            for (Employee employee : sortedEmployees) {
                System.out.println(employee);
            }

            // הדפסת משמרות לשבוע המטרה
            System.out.println("\nShifts for week starting " + TARGET_WEEK + ":");
            System.out.println("-".repeat(30));
            
            // מיון המשמרות לפי יום ואז סוג משמרת
            List<Shift> weekShifts = Arrays.stream(branch.getCurrentWeekShifts())
                .filter(shift -> shift.getWeekStart().equals(TARGET_WEEK))
                .sorted(Comparator
                    .comparing(Shift::getDate)
                    .thenComparing(shift -> shift.getShiftType().name()))
                .collect(Collectors.toList());

            if (weekShifts.isEmpty()) {
                System.out.println("No shifts found for this week");
            } else {
                for (Shift shift : weekShifts) {
                    System.out.println(shift);
                }
            }
        }
    }

    // Helper method to get a branch from cache
    public static Branch getBranchFromCache(int branchId) {
        initializeCache();
        return branchCache.get(branchId);
    }

    // Helper method to clear cache if needed
    public static void clearCache() {
        branchCache.clear();
        isCacheInitialized = false;
    }
}
