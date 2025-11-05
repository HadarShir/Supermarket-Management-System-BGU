package DomainLayer;
import DataLayer.*;
import DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
@Repository
public class DeliveryRequestRepository {

    private final JdbcDeliveryRequestDao dao;

    @Autowired
    public DeliveryRequestRepository(JdbcDeliveryRequestDao dao) {
        this.dao = dao;
    }

    public List<Integer> getBranchesWithOrdersToday() {
        LocalDate today = LocalDate.now();
        return dao.findBranchesWithOrdersOn(today);
    }
    public void save(DeliveryRequest request) {
        DeliveryRequestDto dto = toDto(request);
        dao.insert(dto);
    }
    public void update(DeliveryRequest request) {
        DeliveryRequestDto dto = toDto(request);
        dao.update(dto);
    }

    public DeliveryRequest getSpecificDeliveryRequest(int branchId, int orderId) {
        DeliveryRequestDto dto = dao.find(branchId, orderId);
        return fromDto(dto);
    }

    public List<DeliveryRequest> getAllDeliveryRequest() {
        return dao.findAll().stream()
                .map(this::fromDto)
                .collect(Collectors.toList());
    }
    public List<DeliveryRequest> getTodayAllOrders() {
        return dao.getTodayAllOrders().stream()
                .map(this::fromDto)
                .collect(Collectors.toList());
    }

    public List<DeliveryRequest> getDeliveryRequestByBranchID(int branchId) {
        return dao.findAll().stream()
                .map(this::fromDto)
                .filter(req -> req.getBranchId() == branchId)
                .collect(Collectors.toList());

    }
    public List<DeliveryRequest> getTodayOrders() {
        return dao.getTodayOrders().stream()
                .map(this::fromDto)
                .collect(Collectors.toList());
    }
    public List<DeliveryRequest> getOrdersBySupplier(int supplierId) {
        List<DeliveryRequestDto> dtos = dao.getOrdersBySupplier(supplierId);  // ← כבר יעבוד
        return dtos.stream().map(this::fromDto).toList();
    }
    public void updateOrderStatus(int branchId, int orderId, String status) {
        dao.updateOrderStatus(branchId, orderId, status);
    }


    private DeliveryRequestDto toDto(DeliveryRequest request) {
        String daysStr = request.getSuppliersSupplyDays().stream().map(DayOfWeek::name).collect(Collectors.joining(","));

        return new DeliveryRequestDto(
                request.getBranchId(),
                request.getOrderId(),
                request.getSupplierId(),
                daysStr,
                request.getShipmentArea(),
                request.getOrderDate(),
                request.getTotalWeight(),
                request.getStatus(),
                request.getOrderType().toString()
        );
    }

    private DeliveryRequest fromDto(DeliveryRequestDto dto) {
        Set<DayOfWeek> supplyDays = Arrays.stream(dto.getSuppliersSupplyDays().split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .map(DayOfWeek::valueOf)
                .collect(Collectors.toSet());
        SupplyOrderType orderType = SupplyOrderType.valueOf(dto.getOrderType());

        return new DeliveryRequest(
                dto.getBranchId(),
                dto.getOrderId(),
                dto.getSupplierId(),
                supplyDays,
                dto.getShipmentArea(),
                dto.getOrderDate(),
                dto.getTotalWeight(),
                dto.getStatus(),
                orderType
        );
    }


    public void updateOrderDate(int branchId, int orderId, LocalDate newDate) {
        dao.updateOrderDate(branchId, orderId, newDate);
    }

}
