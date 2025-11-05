package PresentationLayer;

import DTO.DeliveryRequestDto;
import DomainLayer.Status;
import ServiceLayer.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class DeliveryMenu {


    private final DeliveryService deliveryService;
    private final TruckService truckService;
    private final BranchService branchService;
    private final Scanner scanner = new Scanner(System.in);


    public DeliveryMenu(DeliveryService deliveryService,
                        TruckService truckService, BranchService branchService) {
        this.deliveryService = deliveryService;
        this.truckService = truckService;
        this.branchService = branchService;
    }

    public void createNewDelivery() throws Exception {
        List<Integer> branches = deliveryService.getBranchesWithOrdersToday();
        System.out.println("Branches with today's orders:");
        branches.forEach(id -> System.out.println("Branch ID: " + id));
        if (branches.isEmpty()) {
            System.out.println("There are no orders for today");
            return;
        }
        int deliveryId = deliveryService.createNewDelivery();
        setSourceFlow(deliveryId);
        assignDriver(deliveryId);
        String driverName = deliveryService.getDriverName(deliveryId);
        System.out.println("The name of the driver is " + driverName);
        assignTruckFlow(deliveryId);
        addOrderToDeliveryFlow(deliveryId);
        System.out.println("Initial planning of delivery completed.");
    }

    private void setSourceFlow(int deliveryId) throws Exception {
        while (true) {
            int sourceAddress = InputValidator.validateIntInput("Enter branch id:");
            Response response = branchService.validateBranch(sourceAddress);
            if (response.isError){
                System.out.println("branch does not exists: " + response.msg);
            } else {
                Status status = deliveryService.setSourceForDelivery(deliveryId, sourceAddress);
                if (status == Status.success) {
                    System.out.println("New delivery has been created with id:" + deliveryId);
                    System.out.println("Branch " + sourceAddress + " updated successfully !");
                    // THE DELIVERY WILL BE DISPATCHED FROM BRANCH
                    return;
                } else if (status == Status.SourceSameAsDestination) {
                    System.out.println("Branch is already exists as destination");
                } else {
                    System.out.println("Cannot set branch for delivery");
                }
            }
        }
    }
    private void addOrderToDeliveryFlow(int deliveryId) throws Exception {
        while (true) {
            System.out.println("These are all the orders due for today:");
            System.out.println("Choose from the orders below:");
            List<DeliveryRequestDto> orders = deliveryService.getAllOrdersOfToday();
            for (DeliveryRequestDto dto : orders) {
                System.out.println(dto);
            }

            Status status = Status.failure;
            while (true) {
                try {
                    int branchId = InputValidator.validateIntInput("Enter branch ID of the order:");
                    int orderId = InputValidator.validateIntInput("Enter order ID:");

                    status = deliveryService.addOrderToDelivery(deliveryId, branchId, orderId);

                    switch (status) {
                        case success -> {
                            System.out.println("Order added successfully.");
                            break;
                        }
                        case notFound -> {
                            System.out.println("Order not found. Please try again.");
                            continue;
                        }
                        case sourceMismatch -> {
                            System.out.println("Order must be from the source branch.");
                            continue;
                        }
                        case destinationNotFound -> {
                            System.out.println("Branch not found.");
                            continue;
                        }
                        case invalidOrderStatus -> {
                            System.out.println("This order has already been assigned or sent and cannot be added.");
                            continue;
                        }
                        default -> {
                            System.out.println("Unexpected error occurred.");
                            continue;
                        }
                    }

                    break;
                } catch (Exception e) {
                    System.out.println("Invalid input or error occurred: " + e.getMessage());
                }
            }

            String answer;
            while (true) {
                System.out.print("Would you like to add another order? (y/n): ");
                answer = scanner.nextLine().trim().toLowerCase();

                if (answer.equals("y") || answer.equals("n")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'y' for yes or 'n' for no.");
                }
            }

            if (answer.equals("n")) {
                if (deliveryService.isDeliveryOverWeight(deliveryId)) {
                    System.out.println("Warning: Delivery is overweight!");
                    handleOverWeight(deliveryId);
                    if (!deliveryService.isDeliveryOverWeight(deliveryId)) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
    }


    private Status assignTruckFlow(int deliveryId) throws Exception {
        Status result;
        while (true) {
            System.out.println("Enter truck license plate:");
            String licensePlate = scanner.nextLine().trim();
            result = deliveryService.setTruckForDelivery(deliveryId, licensePlate);

            switch (result) {
                case success -> {
                    System.out.println("Truck added successfully !");
                    return Status.success;
                }
                case overWeight -> {
                    System.out.println("Warning: Delivery exceeds truck's weight limit");
                    return Status.overWeight;
                }
                case truckNotFound -> System.out.println("Truck does not exist. Please try again.");
                case truckUnavailable -> System.out.println("Truck is unavailable. Please try another truck.");
                case driverMismatch -> System.out.println("Driver is not qualified to drive this truck.");
                default -> System.out.println("Truck assignment failed: " + result);
            }
        }
    }

    public void assignDriver(int deliveryId) throws Exception { //TODO maybe call assign driver

        if (deliveryService.doesDeliveryExists(deliveryId) == Status.failure) {
            System.out.println("Delivery does not exist");
            return;
        }
        Status status = deliveryService.assignDriverForDelivery(deliveryId);
        if (status == Status.success) {
            System.out.println("Driver assigned successfully");
        } else {
            System.out.println("Could not assign driver");
        }
    }

    public void dispatchDelivery() throws Exception {
        int deliveryId = InputValidator.validateIntInput("Enter delivery Id:");
        if (deliveryService.doesDeliveryExists(deliveryId) == Status.failure) {
            System.out.println("Delivery does not exist");
            return;
        }
        if (deliveryService.multipleShipmentAreas(deliveryId) == Status.success) {
            int choice5 = InputValidator.validateIntInput("Beware, delivery has multiple shipment areas, would you like to continue?\n" +
                    "1. Yes, continue\n" +
                    "2. Exit");
            if (choice5 != 1) {
                System.out.println("Dispatch is cancelled");
                return;
            }
        }
        Status dispatchStatus = deliveryService.dispatchDelivery(deliveryId);
        switch (dispatchStatus) {
            case success -> {
                System.out.println("Delivery has been dispatched");
            }
            case failure -> {
                System.out.println("Delivery cannot be dispatched");
            }
        }
    }
    //TODO add order flow and order from a different branch

    private void handleOverWeight(int deliveryId) throws Exception {
        while (deliveryService.isDeliveryOverWeight(deliveryId)) {
            System.out.println("Entered handleOverWeight");
            System.out.println("""
                The delivery has returned to planning
                Choose option:
                1. Remove/ replace all orders from supplier
                2. Choose a different truck
                3. Remove order by ID in delivery
                """);

            int choice = InputValidator.validateIntInput("Enter choice: ");
            switch (choice) {
                case 1 -> {
                    while (true) {
                        System.out.println("""
                            Choose option:
                            1. Remove all orders of a supplier
                            2. Replace all orders of a supplier
                            3. Exit
                            """);
                        int choice1 = InputValidator.validateIntInput("Enter choice: ");
                        if (choice1 == 3) return;

                        switch (choice1) {
                            case 1 -> {
                                System.out.println(deliveryService.showDeliveryReport(deliveryId));
                                while (true) {
                                    int supplierId = InputValidator.validateIntInput("Enter supplier ID you want to remove:");
                                    Status result = deliveryService.removeAllOrdersOfSupplier(deliveryId, supplierId);
                                    if (result == Status.success) {
                                        System.out.println("Supplier orders removed.");
                                        break;
                                    } else if (result == Status.cannotRemoveFirstSourceOrder) {
                                        System.out.println("Only the first order exists — it cannot be removed.");
                                        break;
                                    } else if (result == Status.notFound) {
                                        System.out.println("No orders found for this supplier. Try again.");
                                    } else {
                                        System.out.println("Removal failed. Try again.");
                                    }
                                }
                            }
                            case 2 -> {
                                System.out.println(deliveryService.showDeliveryReport(deliveryId));
                                while (true) {
                                    int supplierIdRemove = InputValidator.validateIntInput("Enter supplier ID to remove:");
                                    Status removeStatus = deliveryService.removeAllOrdersOfSupplier(deliveryId, supplierIdRemove);
                                    if (removeStatus == Status.success) {
                                        System.out.println("Supplier's orders removed.");
                                        int supplierIdAdd = InputValidator.validateIntInput("Enter supplier ID to add:");
                                        Status addStatus = deliveryService.addAllOrdersBySupplier(deliveryId, supplierIdAdd);

                                        switch (addStatus) {
                                            case alreadyExists -> System.out.println("All orders already exist.");
                                            case success -> System.out.println("Supplier replaced successfully.");
                                            case overWeight -> System.out.println("Adding caused overweight again.");
                                            default -> System.out.println("Failed to add supplier.");
                                        }
                                        break;
                                    } else if (removeStatus == Status.cannotRemoveFirstOrderOnly) {
                                        System.out.println("Cannot remove only the first order.");
                                    } else if (removeStatus == Status.notFound) {
                                        System.out.println("No orders found for this supplier. Try again.");
                                    } else {
                                        System.out.println("Failed to remove. Try again.");
                                    }
                                }
                            }
                            default -> System.out.println("Invalid choice. Try again.");
                        }
                    }
                }

                case 2 -> {
                    while (true) {
                        System.out.println(truckService.showAllTrucks());
                        String licensePlate = scanner.nextLine();
                        Status status = deliveryService.setTruckForDelivery(deliveryId, licensePlate);
                        switch (status) {
                            case success -> {
                                System.out.println("Truck assigned successfully.");
                                break;
                            }
                            case truckNotFound -> System.out.println("Truck not found.");
                            case truckUnavailable -> System.out.println("Truck is unavailable.");
                            case driverMismatch -> System.out.println("Driver doesn't match truck license.");
                            default -> System.out.println("Truck assignment failed.");
                        }
                        if (status == Status.success) break;
                    }
                }

                case 3 -> {
                    System.out.println(deliveryService.showDeliveryReport(deliveryId));
                    while (true) {
                        int branchId = InputValidator.validateIntInput("Enter branch ID of the order:");
                        int orderId = InputValidator.validateIntInput("Enter order ID you want to remove:");
                        Status status = deliveryService.removeOrderFromDelivery(deliveryId, branchId, orderId);
                        if (status == Status.success) {
                            System.out.println("Order removed successfully.");
                            break;
                        } else {
                            System.out.println("Failed to remove order. Try again.");
                        }
                    }
                }

                default -> System.out.println("Invalid main menu option. Try again.");
            }
        }
    }

    public void sendDriverRequirementsToHR() throws SQLException {
        Status status = truckService.sendShifts();
        switch (status) {
            case success -> System.out.println("Requirements have been sent successfully.");
            case noEstimates -> System.out.println("There are no driver requirements to be sent.");
            default -> System.out.println("Requirements sent failed.");
        }

    }

    public void completeDelivery () throws Exception {
        int deliveryId = InputValidator.validateIntInput("Enter delivery Id: ");
        if (deliveryService.doesDeliveryExists(deliveryId) == Status.failure) {
            System.out.println("Delivery does not exist");
            return;
        }
        Status status = deliveryService.completeDelivery(deliveryId);
        if (status == Status.success) {
            System.out.println("Delivery has been completed");
        } else {
            System.out.println("Could not complete delivery");
        }
    }

    public void delayOrders() {
        Status status = deliveryService.delayUnAssignedOrders();

        switch (status) {
            case success -> System.out.println("Orders delayed successfully to next supplier days.");
            case failure -> System.out.println("Failed to delay orders. There might be no eligible WAIT orders or an internal error occurred.");
            default -> System.out.println("Unexpected status: " + status);
        }
    }

}
