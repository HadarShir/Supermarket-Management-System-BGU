package DomainLayer;

import DataLayer.ShiftDAO;
import DTO.ShiftDTO;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.HashMap;
import java.time.format.DateTimeFormatter;

public class ShiftRepository {
    private ShiftDAO shiftDAO = new ShiftDAO();
    private static final DateTimeFormatter SQL_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private Shift convertFromDTO(ShiftDTO dto, Map<Integer, Branch> branchMap) {
        Branch branch = branchMap != null ? branchMap.get(dto.branchId()) : null;
        Shift shift = new Shift(
                dto.date(),
                Shift.shiftType.valueOf(dto.shiftType()),
                branch,
                dto.isVacation(),
                dto.shiftId(),
                dto.startTime(),
                dto.endTime(),
                dto.weekStart()
        );
        shift.setPublished(dto.isPublished());
        // Load requirements from DB
        try {
            Map<String, Integer> reqs = shiftDAO.getRequirementsForShift(
                    dto.branchId(), dto.weekStart(), dto.shiftId()
            );
            if (reqs != null && !reqs.isEmpty()) {
                shift.getRequirements().clear();
                shift.getRequirements().putAll(reqs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[ERROR] Exception: " + e.getMessage());
        }
        return shift;
    }

    private ShiftDTO convertToDTO(Shift shift) {
        return new ShiftDTO(
                shift.getShiftID(),
                shift.getWeekStart(),
                shift.getDate(),
                shift.getStart().format(SQL_TIME_FORMAT),
                shift.getEnd().format(SQL_TIME_FORMAT),
                shift.getBranch() != null ? shift.getBranch().getBranchID() : 0,
                shift.isVacation(),
                shift.getShiftType().name(),
                shift.isPublished()
        );
    }

    public List<Shift> getAllShifts() {
        try {
            // Load all shifts and branches in parallel
            List<ShiftDTO> dtos = shiftDAO.getAll();
            BranchRepository branchRepository = new BranchRepository();
            List<Branch> allBranches = branchRepository.getAllBranches();
            Map<Integer, Branch> branchMap = allBranches.stream()
                    .collect(Collectors.toMap(Branch::getBranchID, branch -> branch));
            List<Shift> shifts = new ArrayList<>(dtos.size());
            for (ShiftDTO dto : dtos) {
                Shift shift = convertFromDTO(dto, branchMap);
                if (shift.getBranch() != null) {
                    shifts.add(shift);
                }
            }
            // Load all assigned employees in one batch
            try (Connection conn = DataLayer.DatabaseConnector.getConnection()) {
                Map<Integer, List<Shift.AssignedEmployee>> assignedEmployeesMap =
                        shiftDAO.loadAllAssignedEmployees(conn, shifts);
                for (Shift shift : shifts) {
                    List<Shift.AssignedEmployee> assignedEmployees =
                            assignedEmployeesMap.get(shift.getShiftID());
                    if (assignedEmployees != null) {
                        shift.setEmployyes(assignedEmployees);
                    }
                }
            }
            return shifts;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load shifts from DB", e);
        }
    }

    public void addShift(Shift shift) {
        try {
            shiftDAO.addT(convertToDTO(shift));
            try (Connection conn = DataLayer.DatabaseConnector.getConnection()) {
                saveAssignedEmployees(conn, shift);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add shift to DB", e);
        }
    }

    public void removeShift(Shift shift) {
        try {

            String id = shift.getBranch().getBranchID() + ":" + shift.getWeekStart() + ":" + shift.getShiftID();
            shiftDAO.deleteT(id);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove shift from DB", e);
        }
    }

    public Shift findById(int branchId, LocalDate weekStart, int shiftID) {
        try {
            String id = branchId + ":" + weekStart + ":" + shiftID;
            ShiftDTO dto = shiftDAO.getById(id);
            // Load the correct Branch and pass it in a branchMap
            BranchRepository branchRepository = new BranchRepository();
            Branch branch = branchRepository.findById(branchId);
            Map<Integer, Branch> branchMap = new HashMap<>();
            branchMap.put(branchId, branch);
            Shift shift = dto != null ? convertFromDTO(dto, branchMap) : null;
            if (shift != null) {
                try (Connection conn = DataLayer.DatabaseConnector.getConnection()) {
                    shiftDAO.loadAssignedEmployees(conn, shift);
                }
            }
            return shift;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find shift in DB", e);
        }
    }

    public void updateShift(Shift shift) {
        try {
//            // Debug print for shift main fields
//            System.out.println("[DEBUG] updateShift: branchID=" + (shift.getBranch() != null ? shift.getBranch().getBranchID() : "null")
//                + ", weekStart=" + shift.getWeekStart()
//                + ", shiftID=" + shift.getShiftID()
//                + ", isVacation=" + shift.isVacation());
//            // Debug print for assigned employees
//            for (Shift.AssignedEmployee emp : shift.getEmployyes()) {
//                System.out.println("[DEBUG] AssignedEmployee: name=" + emp.getName() + ", role=" + emp.getRole() + ", start=" + emp.getStart() + ", end=" + emp.getEnd());
//            }
            shiftDAO.updateT(convertToDTO(shift));
            try (Connection conn = DataLayer.DatabaseConnector.getConnection()) {
                saveAssignedEmployees(conn, shift);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("[ERROR] Exception in updateShift: " + e.getMessage());
            throw new RuntimeException("Failed to update shift in DB: " + e.getMessage(), e);
        }
    }

    public List<Shift> getShiftsForBranch(int branchId, Map<Integer, Branch> branchMap) {
        try {
            List<ShiftDTO> dtos = shiftDAO.getShiftsForBranch(branchId);
            List<Shift> shifts = new ArrayList<>();
            for (ShiftDTO dto : dtos) {
                Shift shift = convertFromDTO(dto, branchMap);
                if (shift.getBranch() != null) {
                    shifts.add(shift);
                }
            }

            try (Connection conn = DataLayer.DatabaseConnector.getConnection()) {
                for (Shift shift : shifts) {
                    shiftDAO.loadAssignedEmployees(conn, shift);
                }
            }
            return shifts;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load shifts for branch from DB", e);
        }
    }

    public void loadAvailabilityForShift(Shift shift) {
        try (Connection conn = DataLayer.DatabaseConnector.getConnection()) {
            ArrayList<String> availableEmployees = shiftDAO.getAvailableEmployeesForShift(
                    conn,
                    shift.getBranch().getBranchID(),
                    shift.getWeekStart(),
                    shift.getShiftID()
            );
            shift.setAvailableEmployees(availableEmployees); // ודא שיש setAvailableEmployees ב-Shift
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load availability for shift", e);
        }
    }


    private void saveAssignedEmployees(Connection conn, Shift shift) throws SQLException {

        String deleteSql = "DELETE FROM assigned_employees WHERE branch_id = ? AND week_start = ? AND shift_id = ?";
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            deleteStmt.setInt(1, shift.getBranch().getBranchID());
            deleteStmt.setDate(2, java.sql.Date.valueOf(shift.getWeekStart()));
            deleteStmt.setInt(3, shift.getShiftID());
            deleteStmt.executeUpdate();
        }
        String insertSql = "INSERT INTO assigned_employees (branch_id, week_start, shift_id, username, role, start_time, end_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            for (Shift.AssignedEmployee emp : shift.getEmployyes()) {
//                System.out.println(
//                    "[DEBUG] saveAssignedEmployees: branchID=" + shift.getBranch().getBranchID() +
//                    ", weekStart=" + shift.getWeekStart() +
//                    ", shiftID=" + shift.getShiftID() +
//                    ", username=" + emp.getName() +
//                    ", role=" + emp.getRole() +
//                    ", start=" + emp.getStart() +
//                    ", end=" + emp.getEnd()
//                );

                insertStmt.setInt(1, shift.getBranch().getBranchID());
                insertStmt.setDate(2, java.sql.Date.valueOf(shift.getWeekStart()));
                insertStmt.setInt(3, shift.getShiftID());
                insertStmt.setString(4, emp.getName());
                insertStmt.setString(5, emp.getRole());
                insertStmt.setTime(6, java.sql.Time.valueOf(emp.getStart()));
                insertStmt.setTime(7, java.sql.Time.valueOf(emp.getEnd()));
                insertStmt.executeUpdate();
            }
        }
    }

    public ShiftDAO getShiftDAO() {
        return shiftDAO;
    }
} 