package DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public record DeliveryDTO (
    int id,
    LocalDate deliveryDate,
    int deliveryDay,
    LocalTime departureTime,
    LocalTime arrivalTime,
    String truckLicense,
    String driverId,
    String sourceAddress,
    int branchId,
    String status,
    double totalWeight
) {}

