package DataLayer;

import DomainLayer.TruckSchedule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcTruckScheduleDAO implements TruckScheduleDAO {

    @Override
    public void save(TruckSchedule schedule) {
        String sql = "INSERT INTO deliveries.truck_schedule (license_plate, shift_number, status, assigned_delivery_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, schedule.getLicensePlate());
            stmt.setInt(2, schedule.getShiftNumber());
            stmt.setString(3, schedule.getStatus());
            stmt.setInt(4, schedule.getAssignedDeliveryId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public TruckSchedule find(String licensePlate, int shiftNumber) {
        String sql = "SELECT * FROM deliveries.truck_schedule WHERE license_plate = ? AND shift_number = ?";
        try (PreparedStatement stmt = DatabaseConnector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, licensePlate);
            stmt.setInt(2, shiftNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new TruckSchedule(
                        rs.getString("license_plate"),
                        rs.getInt("shift_number"),
                        rs.getString("status"),
                        rs.getInt("assigned_delivery_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TruckSchedule> findAll() {
        List<TruckSchedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM deliveries.truck_schedule";
        try (PreparedStatement stmt = DatabaseConnector.getConnection().prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                schedules.add(new TruckSchedule(
                        rs.getString("license_plate"),
                        rs.getInt("shift_number"),
                        rs.getString("status"),
                        rs.getInt("assigned_delivery_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    @Override
    public void update(TruckSchedule schedule) {
        String sql = "UPDATE deliveries.truck_schedule SET status = ?, assigned_delivery_id = ? WHERE license_plate = ? AND shift_number = ?";
        try (PreparedStatement stmt = DatabaseConnector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, schedule.getStatus());
            stmt.setInt(2, schedule.getAssignedDeliveryId());
            stmt.setString(3, schedule.getLicensePlate());
            stmt.setInt(4, schedule.getShiftNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String licensePlate, int shiftNumber) {
        String sql = "DELETE FROM deliveries.truck_schedule WHERE license_plate = ? AND shift_number = ?";
        try (PreparedStatement stmt = DatabaseConnector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, licensePlate);
            stmt.setInt(2, shiftNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearAllSchedules() {
        String sql = "DELETE FROM deliveries.truck_schedule";
        try (PreparedStatement stmt = DatabaseConnector.getConnection().prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
