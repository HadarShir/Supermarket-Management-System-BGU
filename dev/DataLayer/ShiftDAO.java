package DataLayer;

import DTO.ShiftDTO;
import DomainLayer.Shift;
import DomainLayer.Branch;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShiftDAO implements IDAO<ShiftDTO> {
    @Override
    public ArrayList<ShiftDTO> getAll() throws SQLException {
        ArrayList<ShiftDTO> shifts = new ArrayList<>();
        String sql = "SELECT DISTINCT ON (branch_id, week_start, shift_id) * FROM employees.shifts ORDER BY branch_id, week_start, shift_id";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ShiftDTO shift = new ShiftDTO(
                        rs.getInt("shift_id"),
                        rs.getDate("week_start").toLocalDate(),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("start_time"),
                        rs.getString("end_time"),
                        rs.getInt("branch_id"),
                        rs.getBoolean("is_vacation"),
                        rs.getString("shift_type"),
                        rs.getBoolean("is_published")
                );
                shifts.add(shift);
            }
        }
        // טעינת עובדים משובצים (assigned employees) לכל Shift
        // (הטעינה בפועל מתבצעת ב-ShiftRepository, שם יש גישה ל-Entity Shift)
        return shifts;
    }
    public ArrayList<ShiftDTO> getAll(Map<Integer, Branch> branchMap) throws SQLException {
        ArrayList<ShiftDTO> shifts = new ArrayList<>();
        String sql = "SELECT DISTINCT ON (branch_id, week_start, shift_id) * FROM employees.shifts ORDER BY branch_id, week_start, shift_id";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int branchId = rs.getInt("branch_id");
                Branch branch = branchMap.get(branchId);
                if (branch == null) continue;
                LocalDate weekStart = rs.getDate("week_start").toLocalDate();
                ShiftDTO shift = new ShiftDTO(
                        rs.getInt("shift_id"),
                        rs.getDate("week_start").toLocalDate(),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("start_time"),
                        rs.getString("end_time"),
                        rs.getInt("branch_id"),
                        rs.getBoolean("is_vacation"),
                        rs.getString("shift_type"),
                        rs.getBoolean("is_published")
                );
                shifts.add(shift);
            }
        }
        return shifts;
    }

    @Override
    public ShiftDTO getById(String id) throws SQLException {
        // id בפורמט "branchId:weekStart:shiftId"
        String[] parts = id.split(":");
        int branchId = Integer.parseInt(parts[0]);
        LocalDate weekStart = LocalDate.parse(parts[1]);
        int shiftId = Integer.parseInt(parts[2]);
        String sql = "SELECT * FROM employees.shifts WHERE branch_id = ? AND week_start = ? AND shift_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branchId);
            stmt.setDate(2, Date.valueOf(weekStart));
            stmt.setInt(3, shiftId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ShiftDTO shift = new ShiftDTO(
                            rs.getInt("shift_id"),
                            rs.getDate("week_start").toLocalDate(),
                            rs.getDate("date").toLocalDate(),
                            rs.getString("start_time"),
                            rs.getString("end_time"),
                            rs.getInt("branch_id"),
                            rs.getBoolean("is_vacation"),
                            rs.getString("shift_type"),
                            rs.getBoolean("is_published")
                    );
                    // טעינת עובדים משובצים (assigned employees) מתבצעת ב-ShiftRepository
                    return shift;
                }
            }
        }
        return null;
    }

    @Override
    public void addT(ShiftDTO shift) throws SQLException {
        String sql = "INSERT INTO employees.shifts (shift_id, week_start, date, start_time, end_time, branch_id, is_vacation, shift_type, is_published) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shift.shiftId());
            stmt.setDate(2, Date.valueOf(shift.weekStart()));
            stmt.setDate(3, Date.valueOf(shift.date()));
            stmt.setTime(4, java.sql.Time.valueOf(shift.startTime()));
            stmt.setTime(5, java.sql.Time.valueOf(shift.endTime()));
            stmt.setInt(6, shift.branchId());
            stmt.setBoolean(7, shift.isVacation());
            stmt.setString(8, shift.shiftType());
            stmt.setBoolean(9, shift.isPublished());
            stmt.executeUpdate();
            // שמירת עובדים משובצים (assigned employees) - יש להניח שה-Shift entity מועבר ל-DAO דרך ה-Repository
            // saveAssignedEmployees(conn, shiftEntity); // יש לממש ב-ShiftRepository
        }
    }

    @Override
    public void updateT(ShiftDTO shift) throws SQLException {
        String sql = "UPDATE employees.shifts SET week_start=?, date=?, start_time=?, end_time=?, branch_id=?, is_vacation=?, shift_type=?, is_published=? WHERE shift_id=? AND week_start=? AND branch_id=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            System.out.println("[DEBUG] updateT: startTime=" + shift.startTime() + ", endTime=" + shift.endTime());

            stmt.setDate(1, Date.valueOf(shift.weekStart()));
            stmt.setDate(2, Date.valueOf(shift.date()));
            stmt.setTime(3, java.sql.Time.valueOf(shift.startTime()));
            stmt.setTime(4, java.sql.Time.valueOf(shift.endTime()));
            stmt.setInt(5, shift.branchId());
            stmt.setBoolean(6, shift.isVacation());
            stmt.setString(7, shift.shiftType());
            stmt.setBoolean(8, shift.isPublished());
            stmt.setInt(9, shift.shiftId());
            stmt.setDate(10, Date.valueOf(shift.weekStart()));
            stmt.setInt(11, shift.branchId());
            stmt.executeUpdate();
            // עדכון עובדים משובצים (assigned employees) - יש להניח שה-Shift entity מועבר ל-DAO דרך ה-Repository
            // saveAssignedEmployees(conn, shiftEntity); // יש לממש ב-ShiftRepository
        }
    }

    @Override
    public void deleteT(String id) throws SQLException {
        // id בפורמט "branchId:weekStart:shiftId"
        String[] parts = id.split(":");
        int branchId = Integer.parseInt(parts[0]);
        LocalDate weekStart = LocalDate.parse(parts[1]);
        int shiftId = Integer.parseInt(parts[2]);
        String sql = "DELETE FROM employees.shifts WHERE branch_id = ? AND week_start = ? AND shift_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branchId);
            stmt.setDate(2, Date.valueOf(weekStart));
            stmt.setInt(3, shiftId);
            stmt.executeUpdate();
            // מחיקת עובדים משובצים (assigned employees)
            String deleteAssignedSql = "DELETE FROM employees.assigned_employees WHERE branch_id = ? AND week_start = ? AND shift_id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteAssignedSql)) {
                deleteStmt.setInt(1, branchId);
                deleteStmt.setDate(2, Date.valueOf(weekStart));
                deleteStmt.setInt(3, shiftId);
                deleteStmt.executeUpdate();
            }
        }
    }

    public void loadAssignedEmployees(Connection conn, Shift shift) throws SQLException {
        shift.getEmployyes().clear();
        String sql = "SELECT DISTINCT ON (username, role) username, role, start_time, end_time " +
                "FROM employees.assigned_employees " +
                "WHERE branch_id = ? AND week_start = ? AND shift_id = ? " +
                "ORDER BY username, role, start_time";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shift.getBranch().getBranchID());
            stmt.setDate(2, Date.valueOf(shift.getWeekStart()));
            stmt.setInt(3, shift.getShiftID());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("username");
                    String role = rs.getString("role");
                    String start = rs.getString("start_time");
                    String end = rs.getString("end_time");
                    try {
                        shift.addAssignedEmployee(name, role, start, end);
                    } catch (RuntimeException e) {
                        System.err.println("Error loading employee " + name + " for role " + role + ": " + e.getMessage());
                    }
                }
            }
        }
    }

    private void saveAssignedEmployees(Connection conn, Shift shift) throws SQLException {
        String deleteSql = "DELETE FROM employees.assigned_employees " +
                "WHERE branch_id = ? AND week_start = ? AND shift_id = ?";
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            deleteStmt.setInt(1, shift.getBranch().getBranchID());
            deleteStmt.setDate(2, Date.valueOf(shift.getWeekStart()));
            deleteStmt.setInt(3, shift.getShiftID());
            deleteStmt.executeUpdate();
        }
        String insertSql = "INSERT INTO employees.assigned_employees " +
                "(branch_id, week_start, shift_id, username, role, start_time, end_time) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            for (Shift.AssignedEmployee emp : shift.getEmployyes()) {
                insertStmt.setInt(1, shift.getBranch().getBranchID());
                insertStmt.setDate(2, Date.valueOf(shift.getWeekStart()));
                insertStmt.setInt(3, shift.getShiftID());
                insertStmt.setString(4, emp.getName());
                insertStmt.setString(5, emp.getRole());
                insertStmt.setTime(6, java.sql.Time.valueOf(emp.getStart()));
                insertStmt.setTime(7, java.sql.Time.valueOf(emp.getEnd()));
                insertStmt.executeUpdate();
            }
        }
    }

    public void updateRequirement(int branchId, LocalDate weekStart, int shiftId, String role, int requiredAmount) throws SQLException {
        String sql = "INSERT INTO employees.shift_requirements (branch_id, week_start, shift_id, role, required_amount) " +
                     "VALUES (?, ?, ?, ?, ?) " +
                     "ON CONFLICT (branch_id, week_start, shift_id, role) DO UPDATE SET required_amount = EXCLUDED.required_amount";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branchId);
            stmt.setDate(2, Date.valueOf(weekStart));
            stmt.setInt(3, shiftId);
            stmt.setString(4, role);
            stmt.setInt(5, requiredAmount);
            stmt.executeUpdate();
        }
    }

    public ArrayList<ShiftDTO> getShiftsForBranchAndWeek(int branchId, LocalDate weekStart) throws SQLException {
        ArrayList<ShiftDTO> shifts = new ArrayList<>();
        String query = "SELECT * FROM employees.shifts WHERE branch_id = ? AND week_start = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, branchId);
            stmt.setDate(2, Date.valueOf(weekStart));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ShiftDTO shift = new ShiftDTO(
                    rs.getInt("shift_id"),
                    rs.getDate("week_start").toLocalDate(),
                    rs.getDate("date").toLocalDate(),
                    rs.getString("start_time"),
                    rs.getString("end_time"),
                    rs.getInt("branch_id"),
                    rs.getBoolean("is_vacation"),
                    rs.getString("shift_type"),
                    rs.getBoolean("is_published")
                );
                shifts.add(shift);
            }
        }
        return shifts;
    }

    public Map<String, Integer> getRequirementsForShift(int branchId, LocalDate weekStart, int shiftId) throws SQLException {
        Map<String, Integer> requirements = new java.util.HashMap<>();
        String sql = "SELECT role, required_amount FROM employees.shift_requirements WHERE branch_id = ? AND week_start = ? AND shift_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branchId);
            stmt.setDate(2, Date.valueOf(weekStart));
            stmt.setInt(3, shiftId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    requirements.put(rs.getString("role"), rs.getInt("required_amount"));
                }
            }
        }
        return requirements;
    }

    public ArrayList<ShiftDTO> getShiftsForBranch(int branchId) throws SQLException {
        ArrayList<ShiftDTO> shifts = new ArrayList<>();
        String sql = "SELECT * FROM employees.shifts WHERE branch_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branchId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    shifts.add(new ShiftDTO(
                        rs.getInt("shift_id"),
                        rs.getDate("week_start").toLocalDate(),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("start_time"),
                        rs.getString("end_time"),
                        rs.getInt("branch_id"),
                        rs.getBoolean("is_vacation"),
                        rs.getString("shift_type"),
                        rs.getBoolean("is_published")
                    ));
                }
            }
        }
        return shifts;
    }

    public ArrayList<String> getAvailableEmployeesForShift(Connection conn, int branchId, LocalDate weekStart, int shiftId) throws SQLException {
        ArrayList<String> availableEmployees = new ArrayList<>();
        String sql = "SELECT username FROM employees.employee_availability WHERE branch_id = ? AND week_start = ? AND shift_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branchId);
            stmt.setDate(2, Date.valueOf(weekStart));
            stmt.setInt(3, shiftId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    availableEmployees.add(rs.getString("username"));
                }
            }
        }
        return availableEmployees;
    }

    /**
     * Loads all assigned employees for a list of shifts in one batch operation.
     * @param conn Database connection
     * @param shifts List of shifts to load employees for
     * @return Map of shift ID to list of assigned employees
     */
    public Map<Integer, List<Shift.AssignedEmployee>> loadAllAssignedEmployees(Connection conn, List<Shift> shifts) throws SQLException {
        Map<Integer, List<Shift.AssignedEmployee>> result = new HashMap<>();
        
        // Create a list of shift IDs for the IN clause
        List<Integer> shiftIds = shifts.stream()
            .map(Shift::getShiftID)
            .collect(Collectors.toList());
            
        if (shiftIds.isEmpty()) {
            return result;
        }
        
        // Build the IN clause placeholders
        String placeholders = String.join(",", Collections.nCopies(shiftIds.size(), "?"));
        
        String sql = "SELECT shift_id, username, role, start_time, end_time " +
                    "FROM employees.assigned_employees " +
                    "WHERE shift_id IN (" + placeholders + ")";
                    
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Set the shift IDs in the IN clause
            for (int i = 0; i < shiftIds.size(); i++) {
                stmt.setInt(i + 1, shiftIds.get(i));
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int shiftId = rs.getInt("shift_id");
                    String username = rs.getString("username");
                    String role = rs.getString("role");
                    LocalTime startTime = rs.getTime("start_time").toLocalTime();
                    LocalTime endTime = rs.getTime("end_time").toLocalTime();
                    
                    Shift.AssignedEmployee employee = new Shift.AssignedEmployee(username, role, startTime, endTime);
                    
                    result.computeIfAbsent(shiftId, k -> new ArrayList<>())
                          .add(employee);
                }
            }
        }
        
        return result;
    }
}
