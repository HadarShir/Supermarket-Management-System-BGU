package DTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record ShiftOrginizerDTO(
    int branchId,
    boolean availabilityChangesAllowed,
    List<Map<String, List<String>>> availableEmployeesPerShiftByRole,
    List<Map<String, Integer>> requirementsPerShift,
    List<ShiftDTO> newShifts,
    List<ShiftDTO> currentWeek,
    LocalDate sunday,
    boolean isPublished
) {} 