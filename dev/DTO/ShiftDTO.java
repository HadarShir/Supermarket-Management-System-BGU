package DTO;

import java.time.LocalDate;

public record ShiftDTO(
    int shiftId,
    LocalDate weekStart,
    LocalDate date,
    String startTime,
    String endTime,
    int branchId,
    boolean isVacation,
    String shiftType,
    boolean isPublished
) {} 