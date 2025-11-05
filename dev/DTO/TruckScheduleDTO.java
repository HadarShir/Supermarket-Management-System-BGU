package DTO;

public record TruckScheduleDTO(
        String licensePlate,
        int shiftNumber,
        String status,
        int assignedDeliveryId
) {}
