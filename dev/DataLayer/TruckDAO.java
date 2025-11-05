package DataLayer;

import DTO.TruckDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface TruckDAO {
    TruckDTO save(TruckDTO truck) throws SQLException;
    ArrayList<TruckDTO> findAll() throws SQLException;
    TruckDTO findByLicensePlate(String licensePlate) throws SQLException;
    void update(TruckDTO truck) throws SQLException; // עדכון כללי של המשאית
    void delete(String licensePlate) throws SQLException;
}
