package DataLayer;

import DTO.DeliveryReportDTO;
import java.sql.SQLException;
import java.util.List;

public interface DeliveryReportDAO {
    void save(DeliveryReportDTO report) throws SQLException;
    DeliveryReportDTO get(int deliveryId) throws SQLException;
    List<DeliveryReportDTO> getAllDeliveryReport() throws SQLException;
    void delete(int deliveryId) throws SQLException;
    void updateDeliveryReport(DeliveryReportDTO report) throws SQLException;

    void deleteNonCompletedReports() throws SQLException;
}
