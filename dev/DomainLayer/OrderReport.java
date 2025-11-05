package DomainLayer;

import java.util.Objects;

public class OrderReport extends Report {
    private int reportId;
    private final int supplierId;
    private final int branchId;
    private final int orderId;
    private final double weight;
    private final int shipmentAreaNumber;

    public OrderReport(int reportId, int supplierId, int branchId, int orderId, double weight, int shipmentAreaNumber) {
        this.reportId = reportId;
        this.supplierId = supplierId;
        this.branchId = branchId;
        this.orderId = orderId;
        this.weight = weight;
        this.shipmentAreaNumber = branchId;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public double getWeight() {
        return weight;
    }

    public int getBranchId() {
        return branchId;
    }

    public int getOrderId() {
        return orderId;
    }

    public Integer getShipmentAreaNumber() {
        return shipmentAreaNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderReport)) return false;
        OrderReport that = (OrderReport) o;
        return orderId == that.orderId && branchId == that.branchId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, branchId);
    }

    @Override
    public String toString() {
        return "OrderReport {" +
                "reportId=" + reportId +
                ", supplierId=" + supplierId +
                ", branchId=" + branchId +
                ", orderId=" + orderId +
                '}';
    }
}
