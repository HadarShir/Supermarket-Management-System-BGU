package DataLayer;

import DTO.RoleDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {
    // שליפת כל התפקידים
    public RoleDTO getAllRoles() throws SQLException {
        List<String> roles = new ArrayList<>();
        String sql = "SELECT role_name FROM employees.roles";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                roles.add(rs.getString("role_name"));
            }
        }
        return new RoleDTO(roles);
    }

    // הוספת תפקיד חדש
    public void addRole(String roleName) throws SQLException {
        String sql = "INSERT INTO employees.roles (role_name) VALUES (?) ON CONFLICT (role_name) DO NOTHING";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, roleName);
            stmt.executeUpdate();
        }
    }

    // מחיקת תפקיד
    public void deleteRole(String roleName) throws SQLException {
        String sql = "DELETE FROM employees.roles WHERE role_name = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, roleName);
            stmt.executeUpdate();
        }
    }

    // שליפת תפקידים של עובד
    public RoleDTO getRolesForEmployee(String username) throws SQLException {
        List<String> roles = new ArrayList<>();
        String sql = "SELECT role FROM employees.employee_roles WHERE username = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    roles.add(rs.getString("role"));
                }
            }
        }
        return new RoleDTO(roles);
    }

    // שיוך תפקיד לעובד
    public void assignRoleToEmployee(String username, String role) throws SQLException {
        String sql = "INSERT INTO employees.employee_roles (username, role) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, role);
            stmt.executeUpdate();
        }
    }

    // הסרת תפקיד מעובד
    public void removeRoleFromEmployee(String username, String role) throws SQLException {
        String sql = "DELETE FROM employees.employee_roles WHERE username = ? AND role = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, role);
            stmt.executeUpdate();
        }
    }
} 