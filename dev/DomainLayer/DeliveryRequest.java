package DomainLayer;

//import domain.orders.SupplyOrderType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class DeliveryRequest {
    private int branchId;
    private int orderId;
    private int supplierId;
    private Set<DayOfWeek> suppliersSupplyDays; // e.g. "MONDAY,SUNDAY"
    private int shipmentArea;
    private LocalDate orderDate;
    private double totalWeight;
    private String status; // e.g. "WAIT"
    private SupplyOrderType orderType; // e.g. "PERIODIC"

    public DeliveryRequest(int branchId, int orderId, int supplierId, Set<DayOfWeek> suppliersSupplyDays,
                           int shipmentArea, LocalDate orderDate, double totalWeight,
                           String status, SupplyOrderType orderType) {
        this.branchId = branchId;
        this.orderId = orderId;
        this.supplierId = supplierId;
        this.suppliersSupplyDays = suppliersSupplyDays;
        this.shipmentArea = branchId; //TODO TELL HADAR AND AVITAL
        this.orderDate = orderDate;
        this.totalWeight = totalWeight;
        this.status = status;
        this.orderType = orderType;
    }
    // Getters
    public int getBranchId() { return branchId; }
    public int getOrderId() { return orderId; }
    public int getSupplierId() { return supplierId; }
    public Set<DayOfWeek> getSuppliersSupplyDays() { return suppliersSupplyDays; }
    public int getShipmentArea() { return shipmentArea; }
    public LocalDate getOrderDate() { return orderDate; }
    public double getTotalWeight() { return totalWeight; }
    public String getStatus() { return status; }
    public SupplyOrderType getOrderType() { return orderType; }

    // Setters
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }
    public void setSuppliersSupplyDays(Set<DayOfWeek> suppliersSupplyDays) { this.suppliersSupplyDays = suppliersSupplyDays; }
    public void setShipmentArea(int shipmentArea) { this.shipmentArea = shipmentArea; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
    public void setTotalWeight(double totalWeight) { this.totalWeight = totalWeight; }
    public void setStatus(String status) { this.status = status; }
    public void setOrderType(SupplyOrderType orderType) { this.orderType = orderType; }

    public void changeToSentStatus(){setStatus("SENT");}
    public void delayForNextSupplyDay() {
        LocalDate today = LocalDate.now();
        DayOfWeek todayDay = today.getDayOfWeek();

        // מיון ימי האספקה
        List<DayOfWeek> sortedDays = new ArrayList<>(suppliersSupplyDays);
        sortedDays.sort(Comparator.naturalOrder());

        // מציאת היום הבא
        DayOfWeek nextDay = null;
        for (DayOfWeek day : sortedDays) {
            if (day.getValue() > todayDay.getValue()) {
                nextDay = day;
                break;
            }
        }

        // אם לא נמצא - קח את היום הראשון ברשימה (לשבוע הבא)
        if (nextDay == null && !sortedDays.isEmpty()) {
            nextDay = sortedDays.get(0);
        }

        // חישוב התאריך הבא שבו היום הוא nextDay
        if (nextDay != null) {
            int daysUntilNext = (nextDay.getValue() - todayDay.getValue() + 7) % 7;
            daysUntilNext = daysUntilNext == 0 ? 7 : daysUntilNext; // לא להחזיר את אותו היום
            this.orderDate = today.plusDays(daysUntilNext);
        }
    }


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
