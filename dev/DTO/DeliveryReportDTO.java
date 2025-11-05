package DTO;

public record DeliveryReportDTO (
        int deliveryId,
        String sourceAddress,
        int sourceId,
        String timeStamp,
        String driverId,
        String truckLicense,
        String actualDepartureTime,
        double totalWeight,
        String status,
        String notes
) {
}
