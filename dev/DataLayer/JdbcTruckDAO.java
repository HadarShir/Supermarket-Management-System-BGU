package DataLayer;

import DTO.TruckDTO;

import java.sql.*;
import java.util.ArrayList;

public class JdbcTruckDAO implements TruckDAO {

    @Override
    public TruckDTO save(TruckDTO t) throws SQLException {
        String sql = """
                INSERT INTO deliveries.trucks(license_plate, required_license, truck_weight, max_weight, model)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql)) {
            ps.setString(1, t.licensePlate());
            ps.setString(2, t.requiredLicense());
            ps.setDouble(3, t.truckWeight());
            ps.setDouble(4, t.maxWeight());
            ps.setString(5, t.model());
            ps.executeUpdate();
        }
        return t;
    }

    @Override
    public ArrayList<TruckDTO> findAll() throws SQLException {
        String sql = "SELECT * FROM deliveries.trucks ORDER BY license_plate";
        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            ArrayList<TruckDTO> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new TruckDTO(
                        rs.getString("license_plate"),
                        rs.getString("required_license"),
                        rs.getDouble("truck_weight"),
                        rs.getDouble("max_weight"),
                        rs.getString("model")
                ));
            }
            return list;
        }
    }

    @Override
    public TruckDTO findByLicensePlate(String licensePlate) throws SQLException {
        String sql = "SELECT * FROM deliveries.trucks WHERE license_plate = ?";
        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql)) {
            ps.setString(1, licensePlate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TruckDTO(
                            rs.getString("license_plate"),
                            rs.getString("required_license"),
                            rs.getDouble("truck_weight"),
                            rs.getDouble("max_weight"),
                            rs.getString("model")
                    );
                }
                return null;
            }
        }
    }

    @Override
    public void update(TruckDTO t) throws SQLException {
        String sql = """
                UPDATE deliveries.trucks
                SET required_license = ?, truck_weight = ?, max_weight = ?, model = ?
                WHERE license_plate = ?
                """;
        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql)) {
            ps.setString(1, t.requiredLicense());
            ps.setDouble(2, t.truckWeight());
            ps.setDouble(3, t.maxWeight());
            ps.setString(4, t.model());
            ps.setString(5, t.licensePlate());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String licensePlate) throws SQLException {
        String sql = "DELETE FROM deliveries.trucks WHERE license_plate = ?";
        try (PreparedStatement ps = DatabaseConnector.getConnection().prepareStatement(sql)) {
            ps.setString(1, licensePlate);
            ps.executeUpdate();
        }
    }
}
