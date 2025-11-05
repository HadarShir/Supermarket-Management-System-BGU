package DomainLayer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Delivery {
    private LocalDate deliveryDate;
    private int deliveryDay;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private int shiftID;
    private Truck truck;
    private String driver;
    private int source;
    private String sourceAdress;
    private ArrayList<OrderReport> ordersReports;
    private Integer deliveryID;
    private DeliveryStatus status;

    public Delivery() {
        this.deliveryDate = null;
        this.deliveryDay = 0;
        this.departureTime = null;
        this.arrivalTime = null;
        this.shiftID = -1;
        this.truck = null;
        this.driver = null;
        this.source = -1;
        this.sourceAdress = null;
        this.ordersReports = new ArrayList<>();
        this.deliveryID = 0;
        this.status = DeliveryStatus.planned;
    }


    public int getDeliveryDay() {
        return this.deliveryDay;
    }

    public LocalTime getDepartureTime() {
        return this.departureTime;
    }
    public LocalDate getDeliveryDate() {
        return this.deliveryDate;
    }
    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Truck getTruck() {
        return this.truck;
    }
    public String getDriver() {
        return this.driver;
    }
    public int getSource() {
        return this.source;
    }
    public String getSourceAdress() {
        return this.sourceAdress;
    }

    public boolean doesOrderInDelivery(OrderReport orderReport) {
        for (OrderReport report : ordersReports) {
            if (report.getOrderId() == orderReport.getOrderId()
                && report.getBranchId() == orderReport.getBranchId()){;
                return true;
            }
        }
        return false;
    }

    public boolean doesOrderExist(int branchId, int orderId) {
        for (OrderReport report : ordersReports) {
            if (report.getBranchId() == branchId && report.getOrderId() == orderId) {
                return true;
            }
        }
        return false;
    }


    public ArrayList<OrderReport> getOrdersReports() {
        return this.ordersReports;
    }
    public Integer getDeliveryID() {
        return this.deliveryID;
    }
    public DeliveryStatus getStatus() {
        return this.status;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setDeliveryDay(int deliveryDay) {
        this.deliveryDay = deliveryDay;
    }
    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public void setShiftID (int id) {
        shiftID = id;
    }
    public int getShiftID() {
        return shiftID;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }


    public void setSource(int source) {
        this.source = source;
    }

    public void setSourceAdress(String sourceAdress) {
        this.sourceAdress = sourceAdress;
    }


    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setDeliveryID(Integer deliveryID) {
        this.deliveryID = deliveryID;
    }
    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }
    public void addOrderReport(OrderReport report) throws WeightException {
        this.ordersReports.add(report);
    }
    public boolean containsOrderReport(int branchId, int orderId) {
        for (OrderReport report : ordersReports) {
            if (report.getBranchId() == branchId && report.getOrderId() == orderId) {
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
    public double getTotalWeight() {
        double total = 0.0;

        for (OrderReport report : this.ordersReports) {
            total += report.getWeight(); // ← תמיד עדכני לפי ההזמנות
        }

        if (this.truck != null) {
            total += this.truck.getTruckWeight(); // ← מוסיפים גם את המשקל העצמי
        }

        return total;
    }
    public OrderReport getOrderReportByBranchAndOrder(int branchId, int orderId) {
        for (OrderReport report : ordersReports) {
            if (report.getBranchId() == branchId && report.getOrderId() == orderId) {
                return report;
            }
        }
        return null;
    }





}

