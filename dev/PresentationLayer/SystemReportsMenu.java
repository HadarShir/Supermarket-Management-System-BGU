package PresentationLayer;
import DomainLayer.DeliveryException;
import DomainLayer.Status;
import ServiceLayer.*;

import java.util. *;

import static PresentationLayer.InputValidator.validateIntInput;

public class SystemReportsMenu {
    private final DeliveryService deliveryService;
    private final TruckService truckService;
    private final Scanner scanner = new Scanner(System.in);
    private String curRole;

    SystemReportsMenu() {
        this.deliveryService = new DeliveryService();
        this.truckService = new TruckService();
    }

    public void showSystemReports() throws Exception{
        boolean exit = false;
        while (!exit) {
            int choice = validateIntInput("Choose option:\n" +
                    "1. Show all trucks\n" +
                    "2. Show all deliveries\n" +
                    "3. Show delivery by id\n" +
                    "4. Exit");
            switch (choice) {
                case 1 -> {
                    System.out.println(truckService.showAllTrucks());
                }

                case 2 -> {
                    System.out.println(deliveryService.showAllDeliveries());
                }
                case 3 -> {
                    int deliveryId = validateIntInput("Enter delivery id: ");
                    if (deliveryService.doesDeliveryExists(deliveryId) == Status.failure) {
                        System.out.println("Delivery does not exist");
                    } else {
                        System.out.println(deliveryService.showDeliveryReport(deliveryId));
                    }
                }
                case 4 -> {
                    exit = true;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
