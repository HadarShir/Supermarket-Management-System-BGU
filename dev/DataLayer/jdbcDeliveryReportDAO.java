package DataLayer;

import DTO.DeliveryReportDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class jdbcDeliveryReportDAO implements DeliveryReportDAO {

    @Override
    public void save(DeliveryReportDTO report) throws SQLException {
        String sql = """
        INSERT INTO deliveries.delivery_reports
        (delivery_id, source_address, source_id, time_stamp, driver_id, truck_license,
         actual_departure_time, total_weight, status, notes)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql)) {
            ps.setInt(1, report.deliveryId());
            ps.setString(2, report.sourceAddress());
            ps.setInt(3, report.sourceId()); // ← זה החדש
            ps.setString(4, report.timeStamp());
            ps.setString(5, report.driverId());
            ps.setString(6, report.truckLicense());
            ps.setString(7, report.actualDepartureTime());
            ps.setDouble(8, report.totalWeight());
            ps.setString(9, report.status());
            ps.setString(10, report.notes());
            ps.executeUpdate();
        }
    }


    @Override
    public DeliveryReportDTO get(int deliveryId) throws SQLException {
        String sql = "SELECT * FROM deliveries.delivery_reports WHERE delivery_id = ?";
        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql)) {
            ps.setInt(1, deliveryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DeliveryReportDTO(
                            rs.getInt("delivery_id"),
                            rs.getString("source_address"),
                            rs.getInt("source_id"),
                            rs.getString("time_stamp"),
                            rs.getString("driver_id"),
                            rs.getString("truck_license"),
                            rs.getString("actual_departure_time"),
                            rs.getDouble("total_weight"),
                            rs.getString("status"),
                            rs.getString("notes")
                    );
                }
                return null;
            }
        }
    }

    @Override
    public List<DeliveryReportDTO> getAllDeliveryReport() throws SQLException {
        String sql = "SELECT * FROM deliveries.delivery_reports ORDER BY delivery_id";
        List<DeliveryReportDTO> reports = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                reports.add(new DeliveryReportDTO(
                        rs.getInt("delivery_id"),
                        rs.getString("source_address"),
                        rs.getInt("source_id"),
                        rs.getString("time_stamp"),
                        rs.getString("driver_id"),
                        rs.getString("truck_license"),
                        rs.getString("actual_departure_time"),
                        rs.getDouble("total_weight"),
                        rs.getString("status"),
                        rs.getString("notes")
                ));
            }
            return reports;
        }
    }

    @Override
    public void delete(int deliveryId) throws SQLException {
        String sql = "DELETE FROM deliveries.delivery_reports WHERE delivery_id = ?";
        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql)) {
            ps.setInt(1, deliveryId);
            ps.executeUpdate();
        }
    }


    @Override
    public void updateDeliveryReport(DeliveryReportDTO report) throws SQLException {
        String sql = """
        UPDATE deliveries.delivery_reports SET
            source_address = ?, 
            source_id = ?, 
            time_stamp = ?, 
            driver_id = ?, 
            truck_license = ?,
            actual_departure_time = ?, 
            total_weight = ?, 
            status = ?, 
            notes = ?
        WHERE delivery_id = ?
    """;

        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql)) {
            ps.setString(1, report.sourceAddress());
            ps.setInt(2, report.sourceId());
            ps.setString(3, report.timeStamp());
            ps.setString(4, report.driverId());
            ps.setString(5, report.truckLicense());
            ps.setString(6, report.actualDepartureTime());
            ps.setDouble(7, report.totalWeight());
            ps.setString(8, report.status());
            ps.setString(9, report.notes());
            ps.setInt(10, report.deliveryId());

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException("No delivery report found to update for ID: " + report.deliveryId());
            }
        }
    }



    @Override
    public void deleteNonCompletedReports() throws SQLException {
        String sql = "DELETE FROM deliveries.delivery_reports WHERE status NOT IN ('completed', 'cancelled')";
        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }

}
