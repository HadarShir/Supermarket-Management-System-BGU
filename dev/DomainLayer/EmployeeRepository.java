package DomainLayer;

import DataLayer.EmployeeDAO;
import DTO.EmployeeDTO;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Map;

public class EmployeeRepository {
    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private BranchRepository branchRepository = new BranchRepository();

    private Employee convertFromDTO(EmployeeDTO dto) {
        Branch branch = branchRepository.findById(dto.branchId());
        Employee employee = new Employee(
            branch,
            dto.userName(),
            dto.bankAccount(),
            dto.hourlySalary(),
            dto.password()
        );
        employee.setIsLoggedIn(dto.isLoggedIn());
        employee.setShiftManager(dto.isShiftManager());
        employee.setHrManager(dto.isHrManager());
        employee.setRoles(new ArrayList<>(dto.roles()));
        employee.setStartContract(dto.startContract());
        employee.setEndContract(dto.endContract());
        employee.setFiredDate(dto.firedDate());
        return employee;
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        int branchId = (employee.getBranch() != null) ? employee.getBranch().getBranchID() : 0;
        return new EmployeeDTO(
            employee.getUserName(),
            employee.getPassword(),
            branchId,
            employee.getIsLoggedIn(),
            employee.getBankAccount(),
            employee.isShiftManager(),
            employee.isHrManager(),
            new ArrayList<>(employee.getRoles()),
            employee.getHourlySalary(),
            employee.getStartContract(),
            employee.getEndContract(),
            employee.getFiredDate()
        );
    }

    // Efficient batch loading: get all employees and assign branches from a provided map
    public List<Employee> getAllEmployees(Map<Integer, Branch> branchMap) {
        try {
            List<EmployeeDTO> dtos = employeeDAO.getAll();
            List<Employee> employees = new ArrayList<>();
            for (EmployeeDTO dto : dtos) {
                Branch branch = branchMap != null ? branchMap.get(dto.branchId()) : null;
                Employee employee = new Employee(
                    branch,
                    dto.userName(),
                    dto.bankAccount(),
                    dto.hourlySalary(),
                    dto.password()
                );
                employee.setIsLoggedIn(dto.isLoggedIn());
                employee.setShiftManager(dto.isShiftManager());
                employee.setHrManager(dto.isHrManager());
                employee.setRoles(new ArrayList<>(dto.roles()));
                employee.setStartContract(dto.startContract());
                employee.setEndContract(dto.endContract());
                employee.setFiredDate(dto.firedDate());
                employees.add(employee);
            }
            return employees;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load employees from DB", e);
        }
    }

    // Legacy method (kept for compatibility, but now uses batch loading if possible)
    public List<Employee> getAllEmployees() {
        // Try to use a branch map if available
        // If not, fallback to old logic (may be less efficient)
        return getAllEmployees(null);
    }

    public void addEmployee(Employee employee) {
        try {
            employeeDAO.addT(convertToDTO(employee));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add employee to DB", e);
        }
    }

    public void removeEmployee(Employee employee) {
        try {
            employeeDAO.deleteT(employee.getUserName());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove employee from DB", e);
        }
    }

    public Employee findByUserName(String userName) {
        try {
            EmployeeDTO dto = employeeDAO.getById(userName);
            return dto != null ? convertFromDTO(dto) : null;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find employee in DB", e);
        }
    }

    public void updateEmployee(Employee employee) {
        try {
            employeeDAO.updateT(convertToDTO(employee));
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update employee in DB", e);
        }
    }

    public List<Employee> getEmployeesByBranch(int branchId, Map<Integer, Branch> branchMap) {
        try {
            List<EmployeeDTO> dtos = employeeDAO.getByBranchId(branchId);
            List<Employee> employees = new ArrayList<>();
            for (EmployeeDTO dto : dtos) {
                Branch branch = branchMap.get(dto.branchId());
                if (branch != null) {
                    Employee employee = new Employee(
                        branch,
                        dto.userName(),
                        dto.bankAccount(),
                        dto.hourlySalary(),
                        dto.password()
                    );
                    employee.setIsLoggedIn(dto.isLoggedIn());
                    employee.setShiftManager(dto.isShiftManager());
                    employee.setHrManager(dto.isHrManager());
                    employee.setRoles(new ArrayList<>(dto.roles()));
                    employee.setStartContract(dto.startContract());
                    employee.setEndContract(dto.endContract());
                    employee.setFiredDate(dto.firedDate());
                    employees.add(employee);
                }
            }
            return employees;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load employees for branch " + branchId, e);
        }
    }
} 