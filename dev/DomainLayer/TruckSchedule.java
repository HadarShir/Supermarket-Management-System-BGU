package DomainLayer;

import DTO.TruckScheduleDTO;

public class TruckSchedule {
    private String licensePlate;
    private int shiftNumber;
    private String status;
    private int assignedDeliveryId;

    public TruckSchedule(String licensePlate, int shiftNumber, String status, int assignedDeliveryId) {
        this.licensePlate = licensePlate;
        this.shiftNumber = shiftNumber;
        this.status = status;
        this.assignedDeliveryId = assignedDeliveryId;
    }

    public static TruckSchedule fromDTO(TruckScheduleDTO dto) {
        return new TruckSchedule(
                dto.licensePlate(),
                dto.shiftNumber(),
                dto.status(),
                dto.assignedDeliveryId()
        );
    }

    public TruckScheduleDTO toDTO() {
        return new TruckScheduleDTO(
                this.licensePlate,
                this.shiftNumber,
                this.status,
                this.assignedDeliveryId
        );
    }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public int getShiftNumber() { return shiftNumber; }
    public void setShiftNumber(int shiftNumber) { this.shiftNumber = shiftNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getAssignedDeliveryId() { return assignedDeliveryId; }
    public void setAssignedDeliveryId(int assignedDeliveryId) { this.assignedDeliveryId = assignedDeliveryId; }

    @Override
    public String toString() {
        return "TruckSchedule{" +
                "licensePlate='" + licensePlate + '\'' +
                ", shiftNumber=" + shiftNumber +
                ", status='" + status + '\'' +
                ", assignedDeliveryId='" + assignedDeliveryId + '\'' +
                '}';
    }
}
