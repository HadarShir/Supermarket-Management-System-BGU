package DataLayer;

import DTO.DeliveryDTO;
import DomainLayer.Delivery;

import java.sql.SQLException;
import java.util.List;

public interface DeliveryDAO {

    int save(DeliveryDTO delivery) throws SQLException;
    DeliveryDTO getDelivery(int id) throws SQLException;
    List<DeliveryDTO> getAllDeliveries() throws SQLException;
    void delete(int deliveryId) throws SQLException;
    void updateDelivery(DeliveryDTO delivery) throws SQLException;
    void deleteNonCompletedDeliveries() throws SQLException;
}
