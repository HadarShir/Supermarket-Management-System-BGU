package DTO;

import java.time.LocalDate;
import java.util.List;

public record EmployeeDTO(
    String userName,
    String password,
    int branchId,
    boolean isLoggedIn,
    String bankAccount,
    boolean isShiftManager,
    boolean isHrManager,
    List<String> roles,
    int hourlySalary,
    LocalDate startContract,
    LocalDate endContract,
    LocalDate firedDate
) {} 