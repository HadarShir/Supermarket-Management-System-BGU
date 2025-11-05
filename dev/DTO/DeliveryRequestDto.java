package DTO;

import java.time.LocalDate;

public class DeliveryRequestDto {
    private int branchId;
    private int orderId;
    private int supplierId;
    private String suppliersSupplyDays; // לדוגמה: "MONDAY,SUNDAY"
    private int shipmentArea;
    private LocalDate orderDate;
    private double totalWeight;
    private String status;
    private String orderType;

    public DeliveryRequestDto(int branchId, int orderId, int supplierId, String suppliersSupplyDays,
                              int shipmentArea, LocalDate orderDate, double totalWeight,
                              String status, String orderType) {
        this.branchId = branchId;
        this.orderId = orderId;
        this.supplierId = supplierId;
        this.suppliersSupplyDays = suppliersSupplyDays;
        this.shipmentArea = shipmentArea;
        this.orderDate = orderDate;
        this.totalWeight = totalWeight;
        this.status = status;
        this.orderType = orderType;
    }


    // Getters
    public int getBranchId() { return branchId; }
    public int getOrderId() { return orderId; }
    public int getSupplierId() { return supplierId; }
    public String getSuppliersSupplyDays() { return suppliersSupplyDays; }
    public int getShipmentArea() { return shipmentArea; }
    public LocalDate getOrderDate() { return orderDate; }
    public double getTotalWeight() { return totalWeight; }
    public String getStatus() { return status; }
    public String getOrderType() { return orderType; }

    // Setters
    public void setBranchId(int branchId) { this.branchId = branchId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }
    public void setSuppliersSupplyDays(String suppliersSupplyDays) { this.suppliersSupplyDays = suppliersSupplyDays; }
    public void setShipmentArea(int shipmentArea) { this.shipmentArea = shipmentArea; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
    public void setTotalWeight(double totalWeight) { this.totalWeight = totalWeight; }
    public void setStatus(String status) { this.status = status; }
    public void setOrderType(String orderType) { this.orderType = orderType; }

    @Override
    public String toString() {
        return "\nDelivery Request" +
                "\n----------------------------------------" +
                "\n*Branch ID          : [" + branchId + "]" +
                "\n*Order ID           : [" + orderId + "]" +
                "\n*Supplier ID        : [" + supplierId + "]" +
                "\n*Supply Days        : [" + suppliersSupplyDays + "]" +
                "\n*Shipment Area      : [" + shipmentArea + "]" +
                "\n*Order Date         : [" + orderDate + "]" +
                "\n*Total Weight (kg)  : [" + totalWeight + "]" +
                "\n*Status             : [" + status + "]" +
                "\n*Order Type         : [" + orderType + "]" +
                "\n----------------------------------------";
    }
}