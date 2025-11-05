package DataLayer;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeAvailabilityDAO {
    public void upsertAvailability(String username, int branchId, LocalDate weekStart, int shiftId, String role) throws SQLException {
        String sql = "INSERT INTO employees.employee_availability (username, branch_id, week_start, shift_id, role) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON CONFLICT (username, branch_id, week_start, shift_id, role) DO UPDATE SET role = EXCLUDED.role";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setInt(2, branchId);
            stmt.setDate(3, Date.valueOf(weekStart));
            stmt.setInt(4, shiftId);
            stmt.setString(5, role);
            stmt.executeUpdate();
        }
    }

    public void deleteAvailability(String username, int branchId, LocalDate weekStart, int shiftId, String role) throws SQLException {
        String sql = "DELETE FROM employees.employee_availability WHERE username=? AND branch_id=? AND week_start=? AND shift_id=? AND role=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setInt(2, branchId);
            stmt.setDate(3, Date.valueOf(weekStart));
            stmt.setInt(4, shiftId);
            stmt.setString(5, role);
            stmt.executeUpdate();
        }
    }

    public List<AvailabilityRecord> getAvailabilityForEmployee(String username, int branchId, LocalDate weekStart) throws SQLException {
        List<AvailabilityRecord> result = new ArrayList<>();
        String sql = "SELECT shift_id, role FROM employees.employee_availability WHERE username=? AND branch_id=? AND week_start=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setInt(2, branchId);
            stmt.setDate(3, Date.valueOf(weekStart));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int shiftId = rs.getInt("shift_id");
                    String role = rs.getString("role");
                    result.add(new AvailabilityRecord(shiftId, role));
                }
            }
        }
        return result;
    }

    public List<String> getAvailableUsernamesForShiftAndRole(int branchId, LocalDate weekStart, int shiftId, String role) throws SQLException {
        List<String> result = new ArrayList<>();
        String sql = "SELECT username FROM employees.employee_availability WHERE branch_id=? AND week_start=? AND shift_id=? AND role=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branchId);
            stmt.setDate(2, Date.valueOf(weekStart));
            stmt.setInt(3, shiftId);
            stmt.setString(4, role);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(rs.getString("username"));
                }
            }
        }
        return result;
    }

    public static class AvailabilityRecord {
        public final int shiftId;
        public final String role;
        public AvailabilityRecord(int shiftId, String role) {
            this.shiftId = shiftId;
            this.role = role;
        }
    }
} 