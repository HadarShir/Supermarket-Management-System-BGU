package DataLayer;

//import domain.discount.DiscountTargetType;
import DTO.DeliveryRequestDto;

import java.time.LocalDate;
import java.util.List;

public interface DeliveryRequestDao {
    void insert(DeliveryRequestDto dto);
    DeliveryRequestDto find(int branchID, int orderID);
    List<DeliveryRequestDto> findAll();
    void update(DeliveryRequestDto dto);
    void deleteByBranchIDAndOrderID(int branchID, int orderID);
    List<Integer> findBranchesWithOrdersOn(LocalDate date);

    void updateOrderDate(int branchId, int orderId, LocalDate newDate);
}
