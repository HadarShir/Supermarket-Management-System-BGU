package DataLayer;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import DomainLayer.Branch;
import DomainLayer.Shift;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import DTO.BranchDTO;

public class BranchDAO implements IDAO<BranchDTO> {
    @Override
    public ArrayList<BranchDTO> getAll() throws SQLException {
        ArrayList<BranchDTO> branches = new ArrayList<>();
        String sql = "SELECT branch_id, city, shipping_area, phone, contact_name FROM employees.branches";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                BranchDTO branch = new BranchDTO(
                    rs.getInt("branch_id"),
                    rs.getString("city"),
                    rs.getInt("shipping_area"),
                    rs.getString("phone"),
                    rs.getString("contact_name")
                );
                branches.add(branch);
            }
        }
        return branches;
    }

    @Override
    public BranchDTO getById(String id) throws SQLException {
        int branchId = Integer.parseInt(id);
        String sql = "SELECT branch_id, city, shipping_area, phone, contact_name FROM employees.branches WHERE branch_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branchId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BranchDTO branch = new BranchDTO(
                        rs.getInt("branch_id"),
                        rs.getString("city"),
                        rs.getInt("shipping_area"),
                        rs.getString("phone"),
                        rs.getString("contact_name")
                    );
                    return branch;
                }
            }
        }
        return null;
    }

    @Override
    public void addT(BranchDTO branch) throws SQLException {
        String sql = "INSERT INTO employees.branches (branch_id, city, shipping_area, phone, contact_name) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branch.branchId());
            stmt.setString(2, branch.city());
            stmt.setInt(3, branch.shippingArea());
            stmt.setString(4, branch.phone());
            stmt.setString(5, branch.contactName());
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateT(BranchDTO branch) throws SQLException {
        String sql = "UPDATE employees.branches SET city = ?, shipping_area = ?, phone = ?, contact_name = ? WHERE branch_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, branch.city());
            stmt.setInt(2, branch.shippingArea());
            stmt.setString(3, branch.phone());
            stmt.setString(4, branch.contactName());
            stmt.setInt(5, branch.branchId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteT(String id) throws SQLException {
        int branchId = Integer.parseInt(id);
        String sql = "DELETE FROM employees.branches WHERE branch_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branchId);
            stmt.executeUpdate();
        }
    }
} 