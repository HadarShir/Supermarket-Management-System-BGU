package DomainLayer;

import DataLayer.*;
import DTO.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliveryRepository {

    private final DeliveryDAO deliveryDAO;
    private final DeliveryReportDAO reportDAO;
    private final Map<Integer, Delivery> deliveries;
    private final Map<Integer, DeliveryReport> deliveryReports;
    private final OrderReportDAO orderReportDAO;
    private final DriverAssignmentDAO driverAssignmentDao;


    public DeliveryRepository() {
        this.deliveryDAO = new jdbcDeliveryDAO();
        this.reportDAO = new jdbcDeliveryReportDAO();
        this.orderReportDAO = new jdbcOrderReportDAO();
        this.driverAssignmentDao = new jdbcDriverAssignmentDAO();
        this.deliveries = new HashMap<>();
        this.deliveryReports = new HashMap<>();
    }

    //********************************** Delivery ****************************************** //

    public void saveDelivery(Delivery delivery) throws SQLException {
        int generatedId = deliveryDAO.save(toDTO(delivery));

        delivery.setDeliveryID(generatedId);

        deliveries.put(generatedId, delivery);
    }

    public Delivery getDelivery(int deliveryId) throws SQLException {
        if (deliveries.containsKey(deliveryId)) {
            return deliveries.get(deliveryId);
        }

        DeliveryDTO dto = deliveryDAO.getDelivery(deliveryId);
        if (dto == null) return null;

        Delivery delivery = fromDTO(dto);
        deliveries.put(deliveryId, delivery);

        return delivery;
    }

    public void updateDelivery(Delivery delivery) throws SQLException {
        deliveryDAO.updateDelivery(toDTO(delivery));
        deliveries.put(delivery.getDeliveryID(), delivery);
    }

    public void deleteDelivery(int deliveryId) throws SQLException {
        deliveryDAO.delete(deliveryId);
        deliveries.remove(deliveryId);
    }

    public List<Delivery> getAllDeliveries() throws SQLException {
        List<DeliveryDTO> deliveryDto = deliveryDAO.getAllDeliveries();
        List<Delivery> allDeliveries = new ArrayList<>();

        for (DeliveryDTO dto : deliveryDto) {
            Delivery delivery = fromDTO(dto);
            deliveries.put(delivery.getDeliveryID(), delivery);
            allDeliveries.add(delivery);
        }

        return allDeliveries;
    }

    public List<String> getAvailableDriversOrLoad(int branchId, int shiftNum) throws Exception {
        if (!driverAssignmentDao.hasAssignments(branchId, shiftNum)) {
            List<Shift.AssignedEmployee> shiftDrivers = ShiftController.getInstance().getDriversInShift(branchId, shiftNum);
            List<DriverAssignmentDTO> dtos = new ArrayList<>();
            for (Shift.AssignedEmployee e : shiftDrivers) {
                dtos.add(new DriverAssignmentDTO(branchId, shiftNum, e.getName(), false));
            }
            driverAssignmentDao.insertDriverAssignments(dtos);
        }
        return driverAssignmentDao.getUnassignedDrivers(branchId, shiftNum);
    }

    public void markDriverAsAssigned(int branchId, int shiftNum, String driverName) throws Exception {
        driverAssignmentDao.markDriverAsAssigned(branchId, shiftNum, driverName);
    }


    //********************************** OrderReport ****************************************** //

    public void saveOrderReport(OrderReport report, int deliveryId) throws SQLException {
        OrderReportDTO dto = toOrderReportDTO(report, deliveryId);
        int generatedId = orderReportDAO.save(dto);
        report.setReportId(generatedId);
    }

    public List<OrderReport> getOrderReportsForDelivery(int deliveryId) throws SQLException {
        List<OrderReportDTO> dtos = orderReportDAO.getAllForDelivery(deliveryId);
        List<OrderReport> reports = new ArrayList<>();
        for (OrderReportDTO dto : dtos) {
            OrderReport report = fromOrderReportDTO(dto);
            reports.add(report);
        }
        return reports;
    }

    public void deleteOrderReportById(int reportId) throws SQLException {
        orderReportDAO.delete(reportId);
    }
    public void deleteOrdersBySupplier(int deliveryId, int supplierId) throws SQLException {
        List<OrderReport> reports = getOrderReportsForDelivery(deliveryId);
        for (OrderReport r : reports) {
            if (r.getSupplierId() == supplierId) {
                orderReportDAO.delete(r.getReportId());
            }
        }
    }
    private OrderReportDTO toOrderReportDTO(OrderReport report, int deliveryId) {
        return new OrderReportDTO(
                report.getReportId(),
                deliveryId,
                report.getSupplierId(),
                report.getBranchId(),
                report.getOrderId(),
                report.getWeight(),
                report.getShipmentAreaNumber()
        );
    }
    private OrderReport fromOrderReportDTO(OrderReportDTO dto) {
        return new OrderReport(
                dto.reportId(),
                dto.supplierId(),
                dto.branchId(),
                dto.orderId(),
                dto.weight(),
                dto.shipmentAreaNumber()
        );
    }



    //********************************** DeliveryReport ****************************************** //

    public void saveDeliveryReport(DeliveryReport report) throws SQLException {
        reportDAO.save(toReportDTO(report));
        deliveryReports.put(report.getDelivery().getDeliveryID(), report);
    }

    public DeliveryReport getDeliveryReport(int deliveryId) throws SQLException {
        if (deliveryReports.containsKey(deliveryId)) {
            return deliveryReports.get(deliveryId);
        }

        DeliveryReportDTO reportDTO = reportDAO.get(deliveryId);
        if (reportDTO == null) return null;

        DeliveryReport report = fromReportDTO(reportDTO);
        deliveryReports.put(deliveryId, report);
        return report;
    }

    public List<DeliveryReport> getAllDeliveryReports() throws SQLException {
        List<DeliveryReportDTO> reportDTOs = reportDAO.getAllDeliveryReport();
        List<DeliveryReport> reports = new ArrayList<>();

        for (DeliveryReportDTO dto : reportDTOs) {
            DeliveryReport report = fromReportDTO(dto);
            deliveryReports.put(dto.deliveryId(), report);
            reports.add(report);
        }

        return reports;
    }


    public void updateDeliveryReport(DeliveryReport report) throws SQLException {
        reportDAO.updateDeliveryReport(toReportDTO(report));
        deliveryReports.put(report.getDelivery().getDeliveryID(), report);
    }

    private DeliveryDTO toDTO(Delivery d) {
        return new DeliveryDTO(
                d.getDeliveryID(),
                d.getDeliveryDate(),
                d.getDeliveryDay(),
                d.getDepartureTime(),
                d.getArrivalTime(),
                d.getTruck() != null ? d.getTruck().getLicensePlate() : null,
                d.getDriver(),
                d.getSourceAdress() != null ? d.getSourceAdress() : null,
                d.getSource(),
                d.getStatus() != null ? d.getStatus().toString() : null,
                d.getTotalWeight()
        );
    }


    private Delivery fromDTO(DeliveryDTO dto) throws SQLException {
        Delivery delivery = new Delivery();
        delivery.setDeliveryID(dto.id());
        delivery.setDeliveryDay(dto.deliveryDay());

        delivery.setDepartureTime(dto.departureTime());
        delivery.setArrivalTime(dto.arrivalTime());

        delivery.setTruck(TruckManager.getInstance().findTruckByLicensePlate(dto.truckLicense()));
        delivery.setDriver(dto.driverId());
        delivery.setSource(dto.branchId());
        delivery.setSourceAdress(dto.sourceAddress());
        delivery.setStatus(DeliveryStatus.valueOf(dto.status()));
//        delivery.setTotalWeight(dto.totalWeight());

        return delivery;
    }



    private DeliveryReportDTO toReportDTO(DeliveryReport report) {
        return new DeliveryReportDTO(
                report.getDelivery() != null ? report.getDelivery().getDeliveryID() : null,
                report.getSource(),
                report.getSourceId(),
                report.getTimeStamp() != null ? report.getTimeStamp().toString() : null,
                report.getDriver(),
                report.getTruck() != null ? report.getTruck().getLicensePlate() : null,
                report.getActualDepartureTime() != null ? report.getActualDepartureTime().toString() : null,
                report.getTotalWeight(),
                report.getStatus() != null ? report.getStatus().toString() : null,
                report.getNotes()
        );
    }


    private DeliveryReport fromReportDTO(DeliveryReportDTO dto) throws SQLException {
        Delivery delivery = new Delivery();
        delivery.setDeliveryID(dto.deliveryId());
        DeliveryReport report = new DeliveryReport(delivery);
        report.setSource(dto.sourceAddress());
        report.setSourceId(dto.sourceId());
        report.setTimeStamp(LocalDateTime.parse(dto.timeStamp()));
        report.setDriver(dto.driverId());
        report.setTruck(TruckManager.getInstance().findTruckByLicensePlate(dto.truckLicense()));

        if (dto.actualDepartureTime() != null) {
            report.setActualDepartureTime(LocalTime.parse(dto.actualDepartureTime()));
        } else {
            report.setActualDepartureTime(null);
        }

        report.setTotalWeight(dto.totalWeight());
        report.setStatus(DeliveryStatus.valueOf(dto.status()));
        report.setNotes(dto.notes());
        return report;
    }


}
