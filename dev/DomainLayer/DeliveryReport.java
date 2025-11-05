package DomainLayer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DeliveryReport extends Report {
    private LocalDate deliveryDate;
    private String source;
    private int sourceId;
    private ArrayList<OrderReport> ordersReports;
    private Delivery delivery;
    private LocalDateTime timeStamp;
    private String driver;
    private Truck truck;
    private LocalTime actualDepartureTime;
    private double totalWeight;
    private DeliveryStatus status;
    private String notes;

    public DeliveryReport(Delivery delivery) {
        this.deliveryDate = null;
        this.delivery = delivery;
        this.timeStamp = LocalDateTime.now();
        this.driver = null;
        this.truck = null;
        this.actualDepartureTime = null;
        this.totalWeight = 0;
        this.status = DeliveryStatus.planned;
        this.notes = null;
        this.ordersReports = new ArrayList<>();
    }

    public void updateReport() {
        if (this.delivery != null) {
            this.deliveryDate = this.delivery.getDeliveryDate();

            if (this.delivery.getSource() != -1) {
                this.source = this.delivery.getSourceAdress();
                this.sourceId = this.delivery.getSource();
            } else {
                this.source = null;
                this.sourceId = -1;
            }


            this.driver = this.delivery.getDriver() != null ? this.delivery.getDriver() : null;
            this.truck = this.delivery.getTruck() != null ? this.delivery.getTruck() : null;
            double total = 0.0;
            for (OrderReport orderReport : this.ordersReports) {
                total += orderReport.getWeight();
            }
            if (this.delivery.getTruck() != null) {
                total += this.delivery.getTruck().getTruckWeight();
            }
            this.totalWeight = total;
            this.status = this.delivery.getStatus() != null ? this.delivery.getStatus() : null;
        } else {
            this.deliveryDate = null;
            this.source = null;
            this.driver = null;
            this.truck = null;
            this.totalWeight = 0.0;
            this.status = null;
        }
    }
    public Delivery getDelivery() {
        return delivery;
    }
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
    public String getDriver() {
        return driver;
    }
    public void setDriver(String driver) {
        this.driver = driver;
    }
    public Truck getTruck() {
        return truck;
    }
    public void setTruck(Truck truck) {
        this.truck = truck;
    }
    public LocalTime getActualDepartureTime() {
        return actualDepartureTime;
    }
    public void setActualDepartureTime(LocalTime actualDepartureTime) {
        this.actualDepartureTime = actualDepartureTime;
    }
    public double getTotalWeight() {
        return totalWeight;
    }
    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public void setSourceId(int sourceId) {this.sourceId = sourceId;}
    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }
    public List<OrderReport> getOrderReports() {
        return ordersReports;
    }

    public void addOrderReport(OrderReport report) {
        if (!ordersReports.contains(report)) {
            this.ordersReports.add(report);
        }
    }

    public boolean containsOrderReport(int supplierId, int branchId, int orderId) {
        for (OrderReport report : ordersReports) {
            if (report.getSupplierId() == supplierId &&
                    report.getBranchId() == branchId &&
                    report.getOrderId() == orderId) {
                return true;
            }
        }
        return false;
    }

    public void removeAllOrdersFromSupplier(int supplierId) {
        ordersReports.removeIf(report -> report.getSupplierId() == supplierId);
    }

    public void removeSpecificOrder(int supplierId, int branchId, int orderId) {
        ordersReports.removeIf(report ->
                report.getSupplierId() == supplierId &&
                        report.getBranchId() == branchId &&
                        report.getOrderId() == orderId);
    }


public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    private String getDayName(int dayIndex) {
        return switch (dayIndex) {
            case 1 -> "Sunday";
            case 2 -> "Monday";
            case 3 -> "Tuesday";
            case 4 -> "Wednesday";
            case 5 -> "Thursday";
            case 6 -> "Friday";
            case 7 -> "Saturday";
            default -> "Null";
        };
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("Delivery source: ")
                .append(source != null ? source : "Not assigned")
                .append("\nDelivery status: ")
                .append(status).append("\n");
        Delivery d = getDelivery();
        if (d != null) {
            output.append("Scheduled Day: ").append(getDayName(d.getDeliveryDay())).append("\n");
            output.append("Departure Time: ").append(d.getDepartureTime()).append("\n");
            output.append("Arrival Time: ").append(d.getArrivalTime()).append("\n");
        }
        output.append("Orders:\n");
        for (OrderReport report : ordersReports) {
            output.append(" - ").append(report).append("\n");
        }
        return output.toString();
    }

    public String getSource() {
        return source;
    }
    public int getSourceId() {return sourceId;}

    public DeliveryStatus getStatus() {
        return status;
    }
    public void removeOrdersBySupplier(int supplierId) {
        ordersReports.removeIf(r -> r.getSupplierId() == supplierId);
    }
    public OrderReport getOrderReport(int supplierId, int branchId, int orderId) {
        for (OrderReport report : ordersReports) {
            if (report.getSupplierId() == supplierId &&
                    report.getBranchId() == branchId &&
                    report.getOrderId() == orderId) {
                return report;
            }
        }
        return null;
    }


}

