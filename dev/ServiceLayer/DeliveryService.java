package ServiceLayer;

import DTO.DeliveryRequestDto;
import DomainLayer.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public class DeliveryService {
    private final DeliveryManager deliveryManager;

    public int createNewDelivery() throws Exception {
        return deliveryManager.createNewDelivery().getDeliveryID();
    }


    public DeliveryService() {
        this.deliveryManager = DeliveryManager.getInstance();
    }

    public Status closeDelivery(int deliveryID) throws Exception {
        Status status = deliveryManager.canCloseDelivery(deliveryID);
        if (status == Status.success) {
            deliveryManager.closeDelivery(deliveryID);
            return status;
        }
        return status;
    }

    public Status setTruckForDelivery(int deliveryId, String licensePlate) throws Exception {
        Status status = deliveryManager.canSetTruckForDelivery(deliveryId, licensePlate);
        if (status != Status.success) {
            return status;
        }
        status = deliveryManager.setTruckForDelivery(deliveryId, licensePlate);
        if (status != Status.success) {
            return status;
        }
        if (deliveryManager.isDeliveryOverWeight(deliveryId)) {
            return Status.overWeight;
      }
        return Status.success;
    }

    public Status setSourceForDelivery(int deliveryId, int sourceAddress) throws Exception {
        Status status = deliveryManager.canSetSourceForDelivery(deliveryId, sourceAddress);
        if (status != Status.success) {
            return status;
        }
        return deliveryManager.setSourceForDelivery(deliveryId, sourceAddress);
    }
    public Status assignDriverForDelivery(int deliveryId) throws Exception {
        Status status = deliveryManager.canAssignDriverForDelivery(deliveryId);
        if (status != Status.success) {
            return status;
        }
        return deliveryManager.assignDriverForDelivery(deliveryId);
    }
    public Status doesDeliveryExists(int deliveryId) throws Exception {
        return deliveryManager.doesDeliveryExists(deliveryId);
    }

    public Status dispatchDelivery(int deliveryId) throws DeliveryException {
        try {
            Status status = deliveryManager.canBeDispatchDelivery(deliveryId);
            if (status != Status.success) {
                return status;
            }
            return deliveryManager.dispatchDelivery(deliveryId);
        } catch (WeightException e){
            return Status.overWeight;
        } catch (Exception d){
            System.out.println("[DEBUG] Exception during dispatch: " + d.getMessage());
            d.printStackTrace();
            return Status.failure;
        }

    }
    public Status multipleShipmentAreas(int deliveryId) throws Exception {
        return deliveryManager.multipleShipmentAreas(deliveryId);
    }

    public String showDeliveryReport(int deliveryId) throws Exception {
        return deliveryManager.showDeliveryReport(deliveryId);
    }
    public Status setDeliveryStatus(int deliveryId, String status) throws Exception {
        if (!deliveryManager.canSetDeliveryStatus(deliveryId, status)) {
            return Status.failure;
        }
        return deliveryManager.setDeliveryStatus(deliveryId, status);
    }

    public String showAllDeliveries() throws Exception {
        return deliveryManager.showAllDeliveries();
    }


    public Status completeDelivery(int deliveryId) {
        try {
            Status status = deliveryManager.canCompleteDelivery(deliveryId);
            if (status != Status.success) {
                return status;
            }
            return deliveryManager.completeDelivery(deliveryId);
        } catch (Exception e) {
            return Status.failure;
        }
    }

    public boolean isDeliveryOverWeight(int deliveryId) throws Exception {
        return !deliveryManager.canCreateDelivery(deliveryId);
    }


    public List<DeliveryRequestDto> getAllOrdersOfToday() {
        return deliveryManager.getTodayOrders();
    }
    public List<DeliveryRequestDto> getTodayAllOrders() { return deliveryManager.getTodayAllOrders(); }



        public String getDriverName(int deliveryId) throws Exception {
        return deliveryManager.getDriverName(deliveryId);
    }
    public List<Integer> getBranchesWithOrdersToday() {
        return deliveryManager.getBranchesWithOrdersToday();
    }
    public Status addOrderToDelivery(int deliveryId, int branchId, int orderId) throws Exception {
        Status status = deliveryManager.canAddOrderToDelivery(deliveryId, branchId, orderId);
        if (status != Status.success) {
            return status;
        }
        return deliveryManager.addOrderToDelivery(deliveryId, branchId, orderId);
    }
    public Status removeOrderFromDelivery(int deliveryId, int branchId, int orderId) throws Exception {
        Status status = deliveryManager.canRemoveOrderFromDelivery(deliveryId, branchId, orderId);
        if (status != Status.success) {
            return status;
        }
        return deliveryManager.removeOrderFromDelivery(deliveryId, branchId, orderId);
    }

        public Status removeAllOrdersOfSupplier(int deliveryId, int supplierId) throws Exception {
        Status status = deliveryManager.canRemoveAllOrdersOfSupplier(deliveryId, supplierId);
        if (status != Status.success) {
            return status;
        }
        return deliveryManager.removeAllOrdersOfSupplier(deliveryId, supplierId);
    }
    public Status addAllOrdersBySupplier(int deliveryId, int supplierId) throws Exception{
        Status status = deliveryManager.canAddAllOrdersBySupplier(deliveryId, supplierId);
        if (status != Status.success) {
            return status;
        }
        return deliveryManager.addAllOrdersBySupplier(deliveryId, supplierId);
    }

    public Status delayUnAssignedOrders() {
        Status status = deliveryManager.canDelayOrdersForNextSupplierDay();
        if (status != Status.success) {
            return status;
        }
        return deliveryManager.delayOrdersForNextSupplierDay();
    }
    }
