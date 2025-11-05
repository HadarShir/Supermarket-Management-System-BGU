package DataLayer;

import DTO.EmployeeDTO;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO implements IDAO<EmployeeDTO> {
    @Override
    public ArrayList<EmployeeDTO> getAll() throws SQLException {
        ArrayList<EmployeeDTO> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees.employees";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                employees.add(new EmployeeDTO(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getInt("branch_id"),
                    rs.getBoolean("is_logged_in"),
                    rs.getString("bank_account"),
                    rs.getBoolean("is_shift_manager"),
                    rs.getBoolean("is_hr_manager"),
                    getRolesForEmployee(conn, rs.getString("username")),
                    rs.getInt("hourly_salary"),
                    rs.getDate("start_contract").toLocalDate(),
                    rs.getDate("end_contract").toLocalDate(),
                    rs.getDate("fired_date") != null ? rs.getDate("fired_date").toLocalDate() : null
                ));
            }
        }
        return employees;
    }

    @Override
    public EmployeeDTO getById(String userName) throws SQLException {
        String sql = "SELECT * FROM employees.employees WHERE username = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new EmployeeDTO(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("branch_id"),
                        rs.getBoolean("is_logged_in"),
                        rs.getString("bank_account"),
                        rs.getBoolean("is_shift_manager"),
                        rs.getBoolean("is_hr_manager"),
                        getRolesForEmployee(conn, rs.getString("username")),
                        rs.getInt("hourly_salary"),
                        rs.getDate("start_contract").toLocalDate(),
                        rs.getDate("end_contract").toLocalDate(),
                        rs.getDate("fired_date") != null ? rs.getDate("fired_date").toLocalDate() : null
                    );
                }
            }
        }
        return null;
    }

    @Override
    public void addT(EmployeeDTO emp) throws SQLException {
        String sql = "INSERT INTO employees.employees (username, password, branch_id, is_logged_in, bank_account, is_shift_manager, is_hr_manager, hourly_salary, start_contract, end_contract, fired_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, emp.userName());
            stmt.setString(2, emp.password());
            stmt.setInt(3, emp.branchId());
            stmt.setBoolean(4, emp.isLoggedIn());
            stmt.setString(5, emp.bankAccount());
            stmt.setBoolean(6, emp.isShiftManager());
            stmt.setBoolean(7, emp.isHrManager());
            stmt.setInt(8, emp.hourlySalary());
            stmt.setDate(9, Date.valueOf(emp.startContract()));
            stmt.setDate(10, Date.valueOf(emp.endContract()));
            stmt.setDate(11, emp.firedDate() != null ? Date.valueOf(emp.firedDate()) : null);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateT(EmployeeDTO emp) throws SQLException {
        String sql = "UPDATE employees.employees SET password=?, branch_id=?, is_logged_in=?, bank_account=?, is_shift_manager=?, is_hr_manager=?, hourly_salary=?, start_contract=?, end_contract=?, fired_date=? WHERE username=?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, emp.password());
            stmt.setInt(2, emp.branchId());
            stmt.setBoolean(3, emp.isLoggedIn());
            stmt.setString(4, emp.bankAccount());
            stmt.setBoolean(5, emp.isShiftManager());
            stmt.setBoolean(6, emp.isHrManager());
            stmt.setInt(7, emp.hourlySalary());
            stmt.setDate(8, Date.valueOf(emp.startContract()));
            stmt.setDate(9, Date.valueOf(emp.endContract()));
            stmt.setDate(10, emp.firedDate() != null ? Date.valueOf(emp.firedDate()) : null);
            stmt.setString(11, emp.userName());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteT(String userName) throws SQLException {
        String sql = "DELETE FROM employees.employees WHERE username = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.executeUpdate();
        }
    }

    private List<String> getRolesForEmployee(Connection conn, String userName) throws SQLException {
        ArrayList<String> roles = new ArrayList<>();
        String sql = "SELECT role FROM employees.employee_roles WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    roles.add(rs.getString("role"));
                }
            }
        }
        return roles;
    }

    public List<EmployeeDTO> getByBranchId(int branchId) throws SQLException {
        List<EmployeeDTO> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees.employees WHERE branch_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branchId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EmployeeDTO employee = new EmployeeDTO(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("branch_id"),
                        rs.getBoolean("is_logged_in"),
                        rs.getString("bank_account"),
                        rs.getBoolean("is_shift_manager"),
                        rs.getBoolean("is_hr_manager"),
                        getRolesForEmployee(conn, rs.getString("username")),
                        rs.getInt("hourly_salary"),
                        rs.getDate("start_contract").toLocalDate(),
                        rs.getDate("end_contract").toLocalDate(),
                        rs.getDate("fired_date") != null ? rs.getDate("fired_date").toLocalDate() : null
                    );
                    employees.add(employee);
                }
            }
        }
        return employees;
    }
} 