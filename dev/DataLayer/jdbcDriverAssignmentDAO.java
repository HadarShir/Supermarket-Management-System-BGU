package DataLayer;

import DTO.DriverAssignmentDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class jdbcDriverAssignmentDAO implements DriverAssignmentDAO {
    @Override
    public List<String> getUnassignedDrivers(int branchId, int shiftNum) throws Exception {
        List<String> result = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT driver_name FROM deliveries.driver_assignments WHERE branch_id = ? AND shift_num = ? AND assigned = FALSE");
            stmt.setInt(1, branchId);
            stmt.setInt(2, shiftNum);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("driver_name"));
            }
        }
        return result;
    }

    @Override
    public boolean hasAssignments(int branchId, int shiftNum) throws Exception {
        try (Connection conn = DatabaseConnector.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM deliveries.driver_assignments WHERE branch_id = ? AND shift_num = ?");
            stmt.setInt(1, branchId);
            stmt.setInt(2, shiftNum);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    @Override
    public void insertDriverAssignments(List<DriverAssignmentDTO> assignments) throws Exception {
        try (Connection conn = DatabaseConnector.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO deliveries.driver_assignments (branch_id, shift_num, driver_name, assigned) VALUES (?, ?, ?, FALSE)");
            for (DriverAssignmentDTO dto : assignments) {
                stmt.setInt(1, dto.getBranchId());
                stmt.setInt(2, dto.getShiftNum());
                stmt.setString(3, dto.getDriverName());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    @Override
    public void markDriverAsAssigned(int branchId, int shiftNum, String driverName) throws Exception {
        try (Connection conn = DatabaseConnector.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE deliveries.driver_assignments SET assigned = TRUE WHERE branch_id = ? AND shift_num = ? AND driver_name = ?");
            stmt.setInt(1, branchId);
            stmt.setInt(2, shiftNum);
            stmt.setString(3, driverName);
            stmt.executeUpdate();
        }
    }
}
