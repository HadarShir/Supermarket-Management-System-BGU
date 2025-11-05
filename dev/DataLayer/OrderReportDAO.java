package DataLayer;

import DTO.OrderReportDTO;
import java.sql.SQLException;
import java.util.List;

public interface OrderReportDAO {
    int save(OrderReportDTO dto) throws SQLException;

    void update(OrderReportDTO dto) throws SQLException;

    void delete(int reportId) throws SQLException;

    List<OrderReportDTO> getAllForDelivery(int deliveryId) throws SQLException;

    void deleteReportsOfNonCompletedDeliveries() throws SQLException;
    void deleteOrphanedOrderReports() throws SQLException;

}