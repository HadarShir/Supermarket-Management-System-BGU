package DTO;

public record OrderReportDTO(
        int reportId,
        int deliveryId,
        int supplierId,
        int branchId,
        int orderId,
        double weight,
        int shipmentAreaNumber
) {}
