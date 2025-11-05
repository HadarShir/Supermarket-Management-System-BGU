package DataLayer;

import DTO.DeliveryDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class jdbcDeliveryDAO implements DeliveryDAO {

    @Override
    public int save(DeliveryDTO delivery) throws SQLException {
        String sql = """
        INSERT INTO deliveries.deliveries
        (delivery_day, delivery_date, departure_time, arrival_time, truck_license,
         driver_id, source_address, branch_id, status, total_weight)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        RETURNING id
    """;
        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql)) {
            ps.setInt(1, delivery.deliveryDay());

            if (delivery.deliveryDate() != null)
                ps.setDate(2, Date.valueOf(delivery.deliveryDate()));
            else
                ps.setNull(2, Types.DATE);

            if (delivery.departureTime() != null)
                ps.setTime(3, Time.valueOf(delivery.departureTime()));
            else
                ps.setNull(3, Types.TIME);

            if (delivery.arrivalTime() != null)
                ps.setTime(4, Time.valueOf(delivery.arrivalTime()));
            else
                ps.setNull(4, Types.TIME);

            if (delivery.truckLicense() != null)
                ps.setString(5, delivery.truckLicense());
            else
                ps.setNull(5, Types.VARCHAR);

            if (delivery.driverId() != null)
                ps.setString(6, delivery.driverId());
            else
                ps.setNull(6, Types.VARCHAR);

            if (delivery.sourceAddress() != null)
                ps.setString(7, delivery.sourceAddress());
            else
                ps.setNull(7, Types.VARCHAR);

            if (delivery.branchId() != -1)
                ps.setInt(8, delivery.branchId());
            else
                ps.setNull(8, Types.INTEGER);

            ps.setString(9, delivery.status());
            ps.setDouble(10, delivery.totalWeight());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Failed to insert delivery: no ID returned.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save delivery: " + e.getMessage(), e);
        }
    }


    @Override
    public DeliveryDTO getDelivery(int id) throws SQLException {
        String sql = "SELECT * FROM deliveries.deliveries WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Date sqlDate = rs.getDate("delivery_date");
                    LocalDate deliveryDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;

                    Time dep = rs.getTime("departure_time");
                    Time arr = rs.getTime("arrival_time");

                    return new DeliveryDTO(
                            rs.getInt("id"),
                            deliveryDate,
                            rs.getInt("delivery_day"),
                            (dep != null) ? dep.toLocalTime() : null,
                            (arr != null) ? arr.toLocalTime() : null,
                            rs.getString("truck_license"),
                            rs.getString("driver_id"),
                            rs.getString("source_address"),
                            rs.getInt("branch_id"),
                            rs.getString("status"),
                            rs.getDouble("total_weight")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get delivery: " + e.getMessage(), e);
        }
    }


    @Override
    public List<DeliveryDTO> getAllDeliveries() throws SQLException {
        String sql = "SELECT * FROM deliveries.deliveries ORDER BY id";
        List<DeliveryDTO> deliveries = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Date sqlDate = rs.getDate("delivery_date");
                LocalDate deliveryDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;

                Time dep = rs.getTime("departure_time");
                Time arr = rs.getTime("arrival_time");

                deliveries.add(new DeliveryDTO(
                        rs.getInt("id"),
                        deliveryDate,
                        rs.getInt("delivery_day"),
                        (dep != null) ? dep.toLocalTime() : null,
                        (arr != null) ? arr.toLocalTime() : null,
                        rs.getString("truck_license"),
                        rs.getString("driver_id"),
                        rs.getString("source_address"),
                        rs.getInt("branch_id"),
                        rs.getString("status"),
                        rs.getDouble("total_weight")
                ));
            }
            return deliveries;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all deliveries: " + e.getMessage(), e);
        }
    }


    @Override
    public void delete(int deliveryId) throws SQLException {
        String sql = "DELETE FROM deliveries.deliveries WHERE id = ?";
        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql)) {
            ps.setInt(1, deliveryId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete delivery: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateDelivery(DeliveryDTO delivery) throws SQLException {
        String sql = """
        UPDATE deliveries.deliveries SET
        delivery_day = ?, delivery_date = ?, departure_time = ?, arrival_time = ?,
        truck_license = ?, driver_id = ?, source_address = ?, status = ?, total_weight = ?, branch_id = ?
        WHERE id = ?
    """;
        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql)) {
            ps.setInt(1, delivery.deliveryDay());

            if (delivery.deliveryDate() != null)
                ps.setDate(2, Date.valueOf(delivery.deliveryDate()));
            else
                ps.setNull(2, Types.DATE);

            if (delivery.departureTime() != null)
                ps.setTime(3, Time.valueOf(delivery.departureTime()));
            else
                ps.setNull(3, Types.TIME);

            if (delivery.arrivalTime() != null)
                ps.setTime(4, Time.valueOf(delivery.arrivalTime()));
            else
                ps.setNull(4, Types.TIME);

            if (delivery.truckLicense() != null)
                ps.setString(5, delivery.truckLicense());
            else
                ps.setNull(5, Types.VARCHAR);

            if (delivery.driverId() != null)
                ps.setString(6, delivery.driverId());
            else
                ps.setNull(6, Types.VARCHAR);

            if (delivery.sourceAddress() != null)
                ps.setString(7, delivery.sourceAddress());
            else
                ps.setNull(7, Types.VARCHAR);

            ps.setString(8, delivery.status());
            ps.setDouble(9, delivery.totalWeight());

            if (delivery.branchId() != -1)
                ps.setInt(10, delivery.branchId());
            else
                ps.setNull(10, Types.INTEGER);

            ps.setInt(11, delivery.id());

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException("No delivery found to update for ID: " + delivery.id());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update delivery: " + e.getMessage(), e);
        }
    }


    @Override
    public void deleteNonCompletedDeliveries() throws SQLException {
        String sql = "DELETE FROM deliveries.deliveries WHERE status NOT IN ('completed', 'cancelled')";
        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }

}
