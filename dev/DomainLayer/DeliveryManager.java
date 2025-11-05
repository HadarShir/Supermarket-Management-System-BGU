package DomainLayer;

import DTO.DeliveryRequestDto;
import DTO.EstimateWeeklyWeightDto;
import DTO.TruckScheduleDTO;
import DataLayer.DatabaseConnector;
import DataLayer.JdbcDeliveryRequestDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class DeliveryManager {

    private final DeliveryRepository deliveryRepository = new DeliveryRepository();
    private DeliveryRequestRepository deliveryRequestRepository;
    private static DeliveryManager instance = null;



    private DeliveryManager() {
        deliveryRequestRepository = new DeliveryRequestRepository(
                new JdbcDeliveryRequestDao(new JdbcTemplate(DatabaseConnector.getDataSource()))
        );
    }
    public void setDeliveryRequestRepository(DeliveryRequestRepository dao) {
        this.deliveryRequestRepository = dao;
    }


    public static DeliveryManager getInstance() {
        if (instance == null) {
            instance = new DeliveryManager();
        }
        return instance;
    }

    private int getShiftOfToday() {
        DayOfWeek today = LocalDate.now().getDayOfWeek();

        switch (today) {
            case SUNDAY:
                return 0;
            case MONDAY:
                return 2;
            case TUESDAY:
                return 4;
            case WEDNESDAY:
                return 6;
            case THURSDAY:
                return 8;
            case FRIDAY:
                return 10;
            case SATURDAY:
                return 12;
            default:
                throw new IllegalStateException("Unknown day: " + today);
        }
    }


    public Delivery createNewDelivery() throws Exception {
        Delivery delivery = new Delivery();
        DeliveryReport deliveryReport = new DeliveryReport(delivery);
        delivery.setShiftID(getShiftOfToday());

        deliveryRepository.saveDelivery(delivery);
        deliveryRepository.saveDeliveryReport(deliveryReport);
        return delivery;
    }

    public Delivery getDeliveryById(int id) throws Exception {
        return deliveryRepository.getDelivery(id);
    }

    public Status doesDeliveryExists(int deliveryId) throws Exception {
        if (getDeliveryById(deliveryId) == null) {
            return Status.failure;
        }
        return Status.success;
    }

    public DeliveryReport getDeliveryReportById(int id) throws SQLException {
        return deliveryRepository.getDeliveryReport(id);
    }

    public boolean canSetDeliveryStatus(int deliveryId, String status) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        if (delivery == null) {
            return false;
        }

        DeliveryStatus statusEnum;
        try {
            statusEnum = DeliveryStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return false;
        }
        ArrayList<DeliveryStatus> possibleNextStatus = delivery.getStatus().getPossibleNextStatus();

        return possibleNextStatus.contains(statusEnum);
    }

    public Status setDeliveryStatus(int deliveryId, String status) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        if (delivery == null) {
            return Status.failure;
        }
        DeliveryStatus statusString = DeliveryStatus.valueOf(status.toUpperCase());

        delivery.setStatus(statusString);
        deliveryRepository.saveDelivery(delivery);
        DeliveryReport deliveryReport = this.getDeliveryReportById(delivery.getDeliveryID());
        if (deliveryReport != null) {
            deliveryReport.updateReport();
            deliveryRepository.updateDeliveryReport(deliveryReport);
        }
        return Status.success;
    }



    // new function
    private Shift.AssignedEmployee matchDriverToTruck(List<Shift.AssignedEmployee> drivers, Truck truck) {
        String requiredRole = "Driver_" + truck.getRequiredLicense().name().toUpperCase();
        for (Shift.AssignedEmployee driver : drivers) {
            if (driver.getRole().equals(requiredRole)) {
                return driver;
            }
        }
        return null;
    }


    public Status canSetTruckForDelivery(int deliveryId, String licensePlate) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        if (delivery == null) {
            return Status.failure;
        }

        Truck truck = TruckManager.getInstance().findTruckByLicensePlate(licensePlate);
        if (truck == null) {
            return Status.truckNotFound;
        }


        if (delivery.getStatus() != DeliveryStatus.assignedDriver) {
            return Status.failure;
        }

        if (!truck.canCarry(delivery.getTotalWeight())){
            return Status.overWeight;
        }
        TruckSchedule schedule = TruckManager.getInstance()
                .getTruckSchedule(licensePlate, delivery.getShiftID());
        if (schedule != null) {
        }
        if (schedule == null) {
            return Status.success;
        }
        if (!"Available".equals(schedule.getStatus())) {
            return Status.truckUnavailable;
        }

        return Status.success;
    }


    public Status setTruckForDelivery(int deliveryId, String licensePlate) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        Truck truck = TruckManager.getInstance().findTruckByLicensePlate(licensePlate);

        if (delivery == null || truck == null) {
            return Status.failure;
        }

        String driverName = delivery.getDriver();
        if (driverName != null) {
            List<Shift.AssignedEmployee> allDrivers =
                    ShiftController.getInstance().getDriversInShift(
                            delivery.getSource(), delivery.getShiftID());
            Optional<Shift.AssignedEmployee> assignedDriver = allDrivers.stream()
                    .filter(d -> d.getName().equals(driverName))
                    .findFirst();

            if (assignedDriver.isEmpty()) {
                return Status.driverMismatch;
            }

            String driverRole = assignedDriver.get().getRole(); // למשל: "Driver_C1"
            String driverLicenseStr = driverRole.split("_")[1]; // מוציא את "C1"
            LicenseType driverLicense = LicenseType.valueOf(driverLicenseStr);

            LicenseType requiredTruckLicense = truck.getRequiredLicense();


            if (!driverLicense.equals(requiredTruckLicense)) {
                return Status.driverMismatch;
            }

        }

        TruckSchedule schedule = TruckManager.getInstance().getTruckSchedule(licensePlate, delivery.getShiftID());

        if (schedule == null) {
            TruckManager.getInstance().addTruckSchedule(
                    new TruckScheduleDTO(licensePlate, delivery.getShiftID(), "Available", deliveryId));
            schedule = TruckManager.getInstance().getTruckSchedule(licensePlate, delivery.getShiftID());
        }

        if (!"Available".equals(schedule.getStatus())) {
            return Status.truckUnavailable;
        }

        schedule.setStatus("Assigned");
        schedule.setAssignedDeliveryId(deliveryId);
        TruckManager.getInstance().updateTruckSchedule(schedule);

        // updating old truck schedule
        Truck oldTruck = delivery.getTruck();
        if (oldTruck != null) {
            TruckSchedule oldTruckSchedule = TruckManager.getInstance().getTruckSchedule(oldTruck.getLicensePlate(), delivery.getShiftID());
            if (oldTruckSchedule != null) {
                oldTruckSchedule.setStatus("Available");
                oldTruckSchedule.setAssignedDeliveryId(-1);
                TruckManager.getInstance().updateTruckSchedule(oldTruckSchedule);
            }
        }

        delivery.setTruck(truck);
        if (delivery.getTotalWeight() > truck.getMaxWeight()) {
            return Status.overWeight;
        }

        DeliveryReport deliveryReport = getDeliveryReportById(deliveryId);
        deliveryReport.updateReport();
        deliveryRepository.updateDelivery(delivery);
        deliveryRepository.updateDeliveryReport(deliveryReport);
        return Status.success;
    }

    private Status setDriverToDelivery(Delivery delivery, String driver) throws SQLException {
        if (driver == null || delivery == null) {
            return Status.failure;
        }

        delivery.setDriver(driver);
        delivery.setStatus(DeliveryStatus.assignedDriver);

        DeliveryReport report = getDeliveryReportById(delivery.getDeliveryID());
        report.updateReport();
        deliveryRepository.updateDelivery(delivery);
        deliveryRepository.updateDeliveryReport(report);

        return Status.success;
    }


    public Status assignDriverForDelivery(int deliveryId) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        if (delivery == null) {
            return Status.failure;
        }

        int branchId = delivery.getSource();
        int shiftId = delivery.getShiftID();

        List<String> availableDriverNames = deliveryRepository.getAvailableDriversOrLoad(branchId, shiftId);
        if (availableDriverNames.isEmpty()) {
            System.out.println("DEBUG - No drivers available in DB or ShiftController");
            return Status.failure;
        }

        String selectedDriver = availableDriverNames.getFirst();
        List<Shift.AssignedEmployee> allDrivers = ShiftController.getInstance().getDriversInShift(branchId, shiftId);
        Shift.AssignedEmployee driverObj = allDrivers.stream()
                .filter(d -> d.getName().equals(selectedDriver))
                .findFirst()
                .orElse(null);

        if (driverObj == null) {
            System.out.println("DEBUG - Driver object not found in shift list");
            return Status.failure;
        }

        deliveryRepository.markDriverAsAssigned(branchId, shiftId, selectedDriver);
        return setDriverToDelivery(delivery, selectedDriver);
    }



    public String getDriverName(int deliveryId) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        return delivery.getDriver();
    }



    public Status canAssignDriverForDelivery(int deliveryId) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        if (delivery == null ||
                (delivery.getStatus() != DeliveryStatus.planned && delivery.getStatus() != DeliveryStatus.locked) ||
                delivery.getSource() == -1) {
            return Status.failure;
        }

        int branchId = delivery.getSource();
        int shiftID = delivery.getShiftID();

        List<String> availableDrivers = deliveryRepository.getAvailableDriversOrLoad(branchId, shiftID);
        return availableDrivers.isEmpty() ? Status.failure : Status.success;
    }




    public boolean isDeliveryUpdatable(int deliveryId) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        return delivery != null && delivery.getStatus() == DeliveryStatus.planned;
    }


    public Status canSetSourceForDelivery(int deliveryId, int sourceAddress) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        ;
        Branch source = BranchController.getInstance(new BranchRepository()).findBranchById(sourceAddress);

        if (delivery == null || source == null) {
            return Status.failure;
        }
        if (delivery.getStatus() != DeliveryStatus.planned) {
            return Status.failure;
        }
        return Status.success;
    }

    public Status setSourceForDelivery(int deliveryId, int sourceId) throws Exception {

        Delivery delivery = getDeliveryById(deliveryId);
        Branch source = BranchController.getInstance(new BranchRepository()).findBranchById(sourceId);

        delivery.setSource(sourceId);
        delivery.setSourceAdress(source.getCity());
        DeliveryReport deliveryReport = this.getDeliveryReportById(delivery.getDeliveryID());
        deliveryReport.updateReport();
        deliveryRepository.updateDelivery(delivery);
        deliveryRepository.updateDeliveryReport(deliveryReport);
        return Status.success;
    }

    public Status canCloseDelivery(int deliveryId) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        if (delivery == null) {
            return Status.failure;
        }
        if (delivery.getStatus() != DeliveryStatus.planned) {
            return Status.failure;
        }
        return Status.success;
    }

    public void closeDelivery(int deliveryId) throws Exception {
        Delivery delivery = deliveryRepository.getDelivery(deliveryId);

        String licenseType = delivery.getTruck().getRequiredLicense().toString().toUpperCase();
        String sourceId = String.valueOf(delivery.getSource());
        String shiftId = String.valueOf(delivery.getShiftID());
        String role = "Driver" + "_" + licenseType;
        ShiftController.getInstance().changeRequirement(sourceId, shiftId, role, "1");

        ShiftController.getInstance().changeRequirement(sourceId, shiftId, "StoreKeeper", "1");

        Set<Integer> destinationBranchIds = new HashSet<>();
        for (OrderReport report : delivery.getOrdersReports()) {
            if (report.getBranchId() != delivery.getSource()) {
                destinationBranchIds.add(report.getBranchId());
            }
        }
        for (int branchId : destinationBranchIds) {
            ShiftController.getInstance().changeRequirement(String.valueOf(branchId), shiftId, "StoreKeeper", "1");
        }
        delivery.setStatus(DeliveryStatus.locked);

        deliveryRepository.updateDelivery(delivery);
        DeliveryReport report = getDeliveryReportById(deliveryId);
        report.updateReport();
        deliveryRepository.updateDeliveryReport(report);
    }

    public String showDeliveryReport(int deliveryId) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        DeliveryReport dr = getDeliveryReportById(delivery.getDeliveryID());
        dr.updateReport();
        return dr.toString();
    }

    public String showAllDeliveries() throws SQLException {
        StringBuilder sb = new StringBuilder();

        List<DeliveryReport> reportList = deliveryRepository.getAllDeliveryReports();
        for (DeliveryReport dr : reportList) {
            sb.append(dr.toString());
        }
        return sb.toString();
    }

    public Status multipleShipmentAreas(int deliveryId) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        if (delivery == null) {
            return Status.failure;
        }

        Set<Integer> uniqueAreas = new HashSet<>();
        for (OrderReport report : delivery.getOrdersReports()) {
            uniqueAreas.add(report.getShipmentAreaNumber());
        }

        return uniqueAreas.size() > 1 ? Status.success : Status.failure;
    }


    public Status canBeDispatchDelivery(int deliveryId) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        if (delivery == null) {
            return Status.failure;
        }

        String d = delivery.getDriver();
        Truck t = delivery.getTruck();
        if (d == null) {
            throw new DeliveryException("Driver not assigned");
        }
        if (t == null) {
            throw new DeliveryException("Truck not assigned");
        }
        for (OrderReport report : delivery.getOrdersReports()) {
            DeliveryRequest req = deliveryRequestRepository.getSpecificDeliveryRequest(report.getBranchId(), report.getOrderId());
            if (!req.getStatus().equalsIgnoreCase("ASSIGNED")) {
                throw new DeliveryException("All orders must be assigned before dispatch.");
            }
        }
        if (delivery.getStatus() != DeliveryStatus.assignedDriver) {
            throw new DeliveryException("Driver has not been assigned to a delivery");
        }
        if (delivery.getDriver() == null) {
            throw new DeliveryException("Driver not assigned");
        }
        if (delivery.getTruck() == null) {
            throw new DeliveryException("Truck not assigned");
        }
        if (delivery.getOrdersReports().isEmpty()) {
            throw new DeliveryException("Delivery has no destinations");
        }
        if (delivery.getSource() == -1) {
            throw new DeliveryException("Source not assigned");
        }
        if (delivery.getTotalWeight() > delivery.getTruck().getMaxWeight()) {
            throw new WeightException("Truck is overloaded");
        }
        return Status.success;
    }


    public Status dispatchDelivery(int deliveryId) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);

        delivery.setStatus(DeliveryStatus.inProgress);
        delivery.setDepartureTime(LocalTime.now());
        DeliveryReport deliveryReport = getDeliveryReportById(delivery.getDeliveryID()); // ← שמירה למשתנה
        deliveryReport.updateReport();
        deliveryRepository.updateDelivery(delivery);
        deliveryRepository.updateDeliveryReport(deliveryReport);

        for (OrderReport order : delivery.getOrdersReports()) {
            deliveryRequestRepository.updateOrderStatus(order.getBranchId(), order.getOrderId(), "SENT");
        }
        return Status.success;
    }

    public Status canCompleteDelivery(int deliveryId) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        if (delivery == null) {
            return Status.failure;
        }
        if (delivery.getStatus() != DeliveryStatus.inProgress) {
            throw new DeliveryException("Delivery is not in-progress");
        }
        return Status.success;
    }


    public Status completeDelivery(int deliveryId) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        String licensePlate = delivery.getTruck().getLicensePlate();

        delivery.setStatus(DeliveryStatus.completed);
        DeliveryReport deliveryReport = getDeliveryReportById(delivery.getDeliveryID());
        deliveryReport.updateReport();
        deliveryRepository.updateDelivery(delivery);
        deliveryRepository.updateDeliveryReport(deliveryReport);
        return Status.success;
    }


    public Status setDeliveryTimes(int deliveryId, int deliveryDay, String departureTimeStr, String arrivalTimeStr) {
        try {
            LocalTime departureTime = parseTimeFromString(departureTimeStr);
            LocalTime arrivalTime = parseTimeFromString(arrivalTimeStr);

            if (!arrivalTime.isAfter(departureTime)) {
                return Status.invalidTimeOrder;
            }
            Delivery delivery = getDeliveryById(deliveryId);
            if (delivery == null) {
                return Status.failure;
            }
            delivery.setDeliveryDay(deliveryDay);
            delivery.setDepartureTime(departureTime);
            delivery.setArrivalTime(arrivalTime);
            delivery.setShiftID(calculateShiftNumber(deliveryDay, departureTime));
            deliveryRepository.updateDelivery(delivery);
            deliveryRepository.updateDeliveryReport(deliveryRepository.getDeliveryReport(deliveryId));

            return Status.success;
        } catch (Exception e) {
            return Status.failure;
        }
    }

    public int calculateShiftNumber(int deliveryDay, LocalTime departureTime) {
        int dayIndex = deliveryDay - 1;
        int shiftInDay = departureTime.isBefore(LocalTime.of(15, 00)) ? 0 : 1;
        return dayIndex * 2 + shiftInDay;
    }

    private LocalTime parseTimeFromString(String timeStr) {
        try {
            return LocalTime.parse(timeStr);
        } catch (Exception e) {
            throw new RuntimeException("Invalid time format");
        }
    }

    public boolean isDeliveryOverWeight(int deliveryId) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        if (delivery == null || delivery.getTruck() == null) return false;
        return delivery.getTotalWeight() > delivery.getTruck().getMaxWeight();
    }

    public boolean isTruckAssigned(int deliveryId) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        return delivery != null && delivery.getTruck() != null;
    }

    public static DeliveryRequestDto toDto(DeliveryRequest r) {
            return new DeliveryRequestDto(
                    r.getBranchId(),
                    r.getOrderId(),
                    r.getSupplierId(),
                    String.join(",", r.getSuppliersSupplyDays().stream().map(DayOfWeek::name).toList()),
                    r.getShipmentArea(),
                    r.getOrderDate(),
                    r.getTotalWeight(),
                    r.getStatus(),
                    r.getOrderType().name()
            );
        }


    public List<DeliveryRequestDto> getTodayOrders() {
        if (deliveryRequestRepository == null) {
            JdbcTemplate jdbc = new JdbcTemplate(DatabaseConnector.getDataSource());
            JdbcDeliveryRequestDao deliveryDao = new JdbcDeliveryRequestDao(jdbc);
            deliveryRequestRepository = new DeliveryRequestRepository(deliveryDao);
        }
        List<DeliveryRequest> domainOrders = deliveryRequestRepository.getTodayOrders();

        List<DeliveryRequestDto> dtoList = new ArrayList<>();
        for (DeliveryRequest r : domainOrders) {
            dtoList.add(toDto(r));
        }
        return dtoList;
    }



    public List<DeliveryRequestDto> getTodayAllOrders() {
        if (deliveryRequestRepository == null) {
            JdbcTemplate jdbc = new JdbcTemplate(DatabaseConnector.getDataSource());
            JdbcDeliveryRequestDao deliveryDao = new JdbcDeliveryRequestDao(jdbc);
            deliveryRequestRepository = new DeliveryRequestRepository(deliveryDao);
        }
        List<DeliveryRequest> domainOrders = deliveryRequestRepository.getTodayAllOrders(); // שים לב לשם החדש

        List<DeliveryRequestDto> dtoList = new ArrayList<>();
        for (DeliveryRequest r : domainOrders) {
            dtoList.add(toDto(r));
        }
        return dtoList;
    }

    public List<Integer> getBranchesWithOrdersToday() {
        if (deliveryRequestRepository == null) {
            JdbcTemplate jdbc = new JdbcTemplate(DatabaseConnector.getDataSource());
            JdbcDeliveryRequestDao deliveryDao = new JdbcDeliveryRequestDao(jdbc);
            deliveryRequestRepository = new DeliveryRequestRepository(deliveryDao);
        }
        return deliveryRequestRepository.getBranchesWithOrdersToday();
    }
    public Status canAddOrderToDelivery(int deliveryId, int branchId, int orderId) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        if (delivery == null)  {
            return Status.failure;
        }

        DeliveryRequest order = deliveryRequestRepository.getSpecificDeliveryRequest(branchId, orderId);
        if (order == null) {
            return Status.notFound;
        }
        if (!order.getStatus().equalsIgnoreCase("WAIT")) {
            return Status.invalidOrderStatus;
        }
        if (delivery.getOrdersReports().isEmpty()) {
            if (branchId != delivery.getSource()) {
                return Status.sourceMismatch;
            }
        }

        if (delivery.containsOrderReport(branchId, orderId)) {
            return Status.failure;
        }
        return Status.success;
    }
    public Status addOrderToDelivery(int deliveryId, int branchId, int orderId) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        DeliveryRequest order = deliveryRequestRepository.getSpecificDeliveryRequest(branchId, orderId);

        double newWeight = delivery.getTotalWeight() + order.getTotalWeight();
        double maxAllowedWeight = delivery.getTruck().getMaxWeight();

        OrderReport report = new OrderReport(0, order.getSupplierId(), branchId, orderId, order.getTotalWeight(), branchId);
        deliveryRepository.saveOrderReport(report, deliveryId);
        delivery.addOrderReport(report);

        DeliveryReport deliveryReport = getDeliveryReportById(deliveryId);
        deliveryReport.addOrderReport(report);

        deliveryRepository.updateDelivery(delivery);
        deliveryReport.updateReport();
        deliveryRepository.updateDeliveryReport(deliveryReport);

        deliveryRequestRepository.updateOrderStatus(branchId, orderId, "ASSIGNED");

        return Status.success;
    }


    public Status canRemoveOrderFromDelivery(int deliveryId, int branchId, int orderId) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        if (delivery == null)  {
            return Status.failure;
        }
        if (!delivery.containsOrderReport(branchId, orderId)) {
            return Status.failure;
        }
        OrderReport toRemove = delivery.getOrderReportByBranchAndOrder(branchId, orderId);

        List<OrderReport> currentOrders = delivery.getOrdersReports();
        if (!currentOrders.isEmpty() && currentOrders.getFirst().equals(toRemove)) {
            return Status.cannotRemoveFirstSourceOrder;
        }

        DeliveryRequest order = deliveryRequestRepository.getSpecificDeliveryRequest(branchId, orderId);
        if (order == null){
            return Status.failure;
        }
        return Status.success;
    }
    public Status removeOrderFromDelivery(int deliveryId, int branchId, int orderId) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        DeliveryReport report = getDeliveryReportById(deliveryId);
        DeliveryRequest order = deliveryRequestRepository.getSpecificDeliveryRequest(branchId, orderId);
        OrderReport toRemove = delivery.getOrderReportByBranchAndOrder(branchId, orderId);

        if (toRemove == null) {
            return Status.failure;
        }
        delivery.removeSpecificOrder(order.getSupplierId(), branchId, orderId);
        report.removeSpecificOrder(order.getSupplierId(), branchId, orderId);

        deliveryRepository.deleteOrderReportById(toRemove.getReportId());

        deliveryRepository.updateDelivery(delivery);
        report.updateReport();
        deliveryRepository.updateDeliveryReport(report);
        deliveryRequestRepository.updateOrderStatus(branchId, orderId, "WAIT");

        return Status.success;
    }
    public Status canAddAllOrdersBySupplier(int deliveryId, int supplierId) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        if (delivery == null) {
            return Status.failure;
        }

        List<DeliveryRequest> orders = deliveryRequestRepository.getOrdersBySupplier(supplierId);
        if (orders.isEmpty()) {
            return Status.noOrders;
        }

        boolean allExist = true;
        for (DeliveryRequest order : orders) {
            if (!delivery.containsOrderReport(order.getBranchId(), order.getOrderId())) {
                allExist = false;
                break;
            }
        }

        if (allExist) {
            return Status.alreadyExists;
        }

        return Status.success;
    }


    public Status addAllOrdersBySupplier(int deliveryId, int supplierId) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        DeliveryReport report = getDeliveryReportById(deliveryId);

        List<DeliveryRequest> orders = deliveryRequestRepository.getOrdersBySupplier(supplierId);
        List<DeliveryRequest> added = new ArrayList<>();

        for (DeliveryRequest order : orders) {
            try {
                if (delivery.containsOrderReport(order.getBranchId(), order.getOrderId())) {
                    continue;
                }
                OrderReport reportObj = new OrderReport(
                        0,
                        order.getSupplierId(),
                        order.getBranchId(),
                        order.getOrderId(),
                        order.getTotalWeight(),
                        order.getShipmentArea()
                );

                delivery.addOrderReport(reportObj);
                report.addOrderReport(reportObj);
                deliveryRepository.saveOrderReport(reportObj, deliveryId);
                added.add(order);

            } catch (WeightException e) {
                for (DeliveryRequest r : added) {
                    delivery.removeSpecificOrder(r.getSupplierId(), r.getBranchId(), r.getOrderId());
                    report.removeSpecificOrder(r.getSupplierId(), r.getBranchId(), r.getOrderId());

                    OrderReport toDelete = report.getOrderReport(r.getSupplierId(), r.getBranchId(), r.getOrderId());
                    if (toDelete != null) {
                        deliveryRepository.deleteOrderReportById(toDelete.getReportId());
                    }
                }

                deliveryRepository.updateDelivery(delivery);
                report.updateReport();
                deliveryRepository.updateDeliveryReport(report);
                return Status.overWeight;
            }
        }

        report.updateReport();
        deliveryRepository.updateDeliveryReport(report);
        return Status.success;
    }
    public Status canRemoveAllOrdersOfSupplier(int deliveryId, int supplierId) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        if (delivery == null) {
            return Status.failure;
        }

        boolean found = false;
        for (OrderReport report : delivery.getOrdersReports()) {
            if (report.getSupplierId() == supplierId) {
                found = true;
                break;
            }
        }

        return found ? Status.success : Status.notFound;
    }


    public Status removeAllOrdersOfSupplier(int deliveryId, int supplierId) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        DeliveryReport report = getDeliveryReportById(deliveryId);

        List<OrderReport> orders = delivery.getOrdersReports();
        if (orders.isEmpty()) return Status.notFound;

        OrderReport first = orders.getFirst();

        List<OrderReport> toRemove = orders.stream()
                .filter(r -> r.getSupplierId() == supplierId)
                .filter(r -> !r.equals(first))
                .toList();

        if (toRemove.isEmpty()) {
            return Status.cannotRemoveFirstOrderOnly;
        }

        for (OrderReport r : toRemove) {
            delivery.removeSpecificOrder(r.getSupplierId(), r.getBranchId(), r.getOrderId());
            report.removeSpecificOrder(r.getSupplierId(), r.getBranchId(), r.getOrderId());
            deliveryRepository.deleteOrderReportById(r.getReportId());
            deliveryRequestRepository.updateOrderStatus(r.getBranchId(), r.getOrderId(), "WAIT");
        }

        report.updateReport();
        deliveryRepository.updateDelivery(delivery);
        deliveryRepository.updateDeliveryReport(report);

        return Status.success;
    }

    public boolean  canCreateDelivery(int deliveryId) throws Exception {
        Delivery delivery = getDeliveryById(deliveryId);
        List<OrderReport> orders = delivery.getOrdersReports();

        double maxWeight = delivery.getTruck().getMaxWeight();
        double totalWeight = delivery.getTruck().getTruckWeight();

        for (OrderReport order : orders) {
            totalWeight += order.getWeight();
        }

        return totalWeight < maxWeight;
    }

    public Status canDelayOrdersForNextSupplierDay() {
        try {
            List<DeliveryRequest> waitingOrders = deliveryRequestRepository.getTodayAllOrders()
                    .stream()
                    .filter(order -> order.getStatus().equalsIgnoreCase("WAIT"))
                    .toList();

            return waitingOrders.isEmpty() ? Status.failure : Status.success;
        } catch (Exception e) {
            System.err.println("Error while checking if delay is possible: " + e.getMessage());
            return Status.failure;
        }
    }

    public Status delayOrdersForNextSupplierDay() {
        try {
            List<DeliveryRequest> waitingOrders = this.deliveryRequestRepository.getTodayAllOrders()
                    .stream()
                    .filter(order -> order.getStatus().equalsIgnoreCase("WAIT"))
                    .toList();

            LocalDate today = LocalDate.now();

            for (DeliveryRequest order : waitingOrders) {
                Set<DayOfWeek> supplyDays = order.getSuppliersSupplyDays();

                LocalDate nextValidDate = null;
                for (int i = 1; i <= 7; i++) {
                    LocalDate candidate = today.plusDays(i);
                    if (supplyDays.contains(candidate.getDayOfWeek())) {
                        nextValidDate = candidate;
                        break;
                    }
                }

                if (nextValidDate != null) {
                    deliveryRequestRepository.updateOrderDate(order.getBranchId(), order.getOrderId(), nextValidDate);
                }
            }

            return Status.success;

        } catch (Exception e) {
            System.err.println("Error while delaying orders: " + e.getMessage());
            return Status.failure;
        }
    }

}