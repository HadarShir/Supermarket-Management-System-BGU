package DataLayer;

import DTO.DeliveryRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
@Repository
public class JdbcDeliveryRequestDao implements DeliveryRequestDao {
    private final JdbcTemplate jdbc;

    @Autowired
    public JdbcDeliveryRequestDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void insert(DeliveryRequestDto dto) {
        String sql = "INSERT INTO deliveries.delivery_requests (branch_id, order_id, supplier_id, suppliers_supply_days, shipment_area, order_date, total_weight, status, order_type) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbc.update(sql,
                dto.getBranchId(),
                dto.getOrderId(),
                dto.getSupplierId(),
                dto.getSuppliersSupplyDays(),
                dto.getShipmentArea(),
                dto.getOrderDate(),
                dto.getTotalWeight(),
                dto.getStatus(),
                dto.getOrderType()
        );
    }

    @Override
    public DeliveryRequestDto find(int branchID, int orderID) {
        String sql = "SELECT * FROM deliveries.delivery_requests WHERE branch_id = ? AND order_id = ?";
        return jdbc.queryForObject(sql, deliveryRequestRowMapper() , branchID, orderID);
    }

    @Override
    public List<DeliveryRequestDto> findAll() {
        String sql = "SELECT * FROM deliveries.delivery_requests";
        return jdbc.query(sql, deliveryRequestRowMapper());
    }

    @Override
    public void update(DeliveryRequestDto dto) {
        String sql = "UPDATE deliveries.delivery_requests SET " +
                "supplier_id = ?, suppliers_supply_days = ?, shipment_area = ?, " +
                "order_date = ?, total_weight = ?, status = ?, order_type = ? " +
                "WHERE branch_id = ? AND order_id = ?";
        jdbc.update(sql,
                dto.getSupplierId(),
                dto.getSuppliersSupplyDays(),
                dto.getShipmentArea(),
                dto.getOrderDate(),
                dto.getTotalWeight(),
                dto.getStatus(),
                dto.getOrderType(),
                dto.getBranchId(),
                dto.getOrderId()
        );
    }
    public List<DeliveryRequestDto> getTodayOrders() {
        String sql = "SELECT * FROM deliveries.delivery_requests WHERE order_date = ? AND status = 'WAIT'";
        LocalDate today = LocalDate.now();

        return jdbc.query(sql, deliveryRequestRowMapper(), today);
    }
    public List<DeliveryRequestDto> getTodayAllOrders() {
        String sql = "SELECT * FROM deliveries.delivery_requests WHERE order_date = ?";
        LocalDate today = LocalDate.now();

        return jdbc.query(sql, deliveryRequestRowMapper(), today);
    }

    @Override
    public void deleteByBranchIDAndOrderID(int branchID, int orderID) {
        String sql = "DELETE FROM deliveries.delivery_requests WHERE branch_id = ? AND order_id = ?";
        jdbc.update(sql, branchID, orderID);
    }
    public List<Integer> findBranchesWithOrdersOn(java.time.LocalDate date) {
        String sql = "SELECT DISTINCT branch_id FROM deliveries.delivery_requests WHERE order_date = ?";
        return jdbc.query(sql,
                (rs, rowNum) -> rs.getInt("branch_id"), date);
    }

    @Override
    public void updateOrderDate(int branchId, int orderId, LocalDate newDate) {
        String sql = "UPDATE deliveries.delivery_requests SET order_date = ? WHERE branch_id = ? AND order_id = ?";
        jdbc.update(sql, newDate, branchId, orderId);
    }


    private RowMapper<DeliveryRequestDto> deliveryRequestRowMapper() {
        return new RowMapper<>() {
            @Override
            public DeliveryRequestDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new DeliveryRequestDto(
                        rs.getInt("branch_id"),
                        rs.getInt("order_id"),
                        rs.getInt("supplier_id"),
                        rs.getString("suppliers_supply_days"),
                        rs.getInt("shipment_area"),
                        rs.getDate("order_date").toLocalDate(),
                        rs.getDouble("total_weight"),
                        rs.getString("status"),
                        rs.getString("order_type")
                );
            }
        };
    }
    public List<DeliveryRequestDto> getOrdersBySupplier(int supplierId) {
        String sql = "SELECT * FROM deliveries.delivery_requests WHERE supplier_id = ?";
        return jdbc.query(sql,deliveryRequestRowMapper() , supplierId);
    }
    public void updateOrderStatus(int branchId, int orderId, String newStatus) {
        String sql = "UPDATE deliveries.delivery_requests SET status = ? WHERE branch_id = ? AND order_id = ?";
        jdbc.update(sql, newStatus, branchId, orderId);
    }






}
