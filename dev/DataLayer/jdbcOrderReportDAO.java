package DataLayer;

import DTO.OrderReportDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class jdbcOrderReportDAO implements OrderReportDAO {

    @Override
    public int save(OrderReportDTO dto) throws SQLException {
        String sql = """
    INSERT INTO deliveries.order_reports
    (delivery_id, supplier_id, branch_id, order_id, weight, shipment_area_number)
    VALUES (?, ?, ?, ?, ?, ?)
    RETURNING report_id
""";

        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql)) {
            ps.setInt(1, dto.deliveryId());
            ps.setInt(2, dto.supplierId());
            ps.setInt(3, dto.branchId());
            ps.setInt(4, dto.orderId());
            ps.setDouble(5, dto.weight());
            ps.setInt(6, dto.shipmentAreaNumber());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1); // מזהה הדוח שנוצר
                } else {
                    throw new SQLException("Failed to insert order report: no ID returned.");
                }
            }
        }
    }

    @Override
    public void update(OrderReportDTO dto) throws SQLException {
        String sql = """
    UPDATE deliveries.order_reports
    SET supplier_id = ?, branch_id = ?, order_id = ?, weight = ?, shipment_area_number = ?
    WHERE report_id = ?
""";

        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql)) {
            ps.setInt(1, dto.supplierId());
            ps.setInt(2, dto.branchId());
            ps.setInt(3, dto.orderId());
            ps.setDouble(4, dto.weight());
            ps.setInt(5, dto.shipmentAreaNumber());
            ps.setInt(6, dto.reportId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int reportId) throws SQLException {
        String sql = "DELETE FROM deliveries.order_reports WHERE report_id = ?";
        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql)) {
            ps.setInt(1, reportId);
            ps.executeUpdate();
        }
    }

    @Override
    public List<OrderReportDTO> getAllForDelivery(int deliveryId) throws SQLException {
        String sql = """
    SELECT report_id, delivery_id, supplier_id, branch_id, order_id, weight, shipment_area_number
    FROM deliveries.order_reports
    WHERE delivery_id = ?
""";


        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql)) {
            ps.setInt(1, deliveryId);
            ResultSet rs = ps.executeQuery();

            List<OrderReportDTO> results = new ArrayList<>();
            while (rs.next()) {
                results.add(new OrderReportDTO(
                        rs.getInt("report_id"),
                        rs.getInt("delivery_id"),
                        rs.getInt("supplier_id"),
                        rs.getInt("branch_id"),
                        rs.getInt("order_id"),
                        rs.getDouble("weight"),
                        rs.getInt("shipment_area_number") // << חדש
                ));
            }

            return results;
        }
    }
    @Override
    public void deleteReportsOfNonCompletedDeliveries() throws SQLException {
        String sql = """
        DELETE FROM deliveries.order_reports
        WHERE delivery_id IN (
            SELECT delivery_id
            FROM deliveries.delivery_reports
            WHERE status NOT IN ('completed', 'cancelled')
        )
    """;

        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }
    @Override
    public void deleteOrphanedOrderReports() throws SQLException {
        String sql = """
        DELETE FROM deliveries.order_reports
        WHERE delivery_id NOT IN (SELECT delivery_id FROM deliveries.delivery_reports)
    """;
        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }

}
