package PresentationLayer;
import DomainLayer.DeliveryException;
import DomainLayer.LicenseType;
import DomainLayer.Status;
import ServiceLayer.*;

import java.sql.SQLException;
import java.util. *;

import static PresentationLayer.InputValidator.validateDoubleInput;
import static PresentationLayer.InputValidator.validateIntInput;

public class SystemOperationsMenu {
    private final DeliveryService deliveryService;
    private final TruckService truckService;

    private final Scanner scanner = new Scanner(System.in);
    private String curRole;

    SystemOperationsMenu() {
        this.deliveryService = new DeliveryService();
        this.truckService = new TruckService();
    }


    public void addNewTruck() throws Exception {
        System.out.println("Enter truck license plate: ");
        String licensePlate = scanner.nextLine();

        String license;
        while (true) {
            System.out.println("Enter required license(A/B/C/D): ");
            license = scanner.nextLine().trim().toUpperCase();
            try {
                LicenseType.valueOf(license);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid license type, please try again");
            }
        }
        double tw;
        do {
            tw = validateDoubleInput("Enter truck weight:\n");
            if (tw < 0) {
                System.out.println("Weight must be greater then zero\n");
            }
        } while (tw < 0);

        double mw;
        do {
            mw = validateDoubleInput("Enter truck max weight:\n");
            if (mw < 0) {
                System.out.println("Weight must be greater then zero\n");
            }
        } while (mw < 0);

        System.out.println("Enter truck model: ");
        String model = scanner.nextLine();
        Status status = truckService.createTruck(licensePlate,license,tw,mw,true,model);
        switch (status) {
            case success -> {
                System.out.println("Truck created successfully");
            }
            case truckAlreadyExists -> {
                System.out.println("Truck already exists");
            }
            default -> {
                System.out.println("Failed to create truck");
            }
        }
    }




}
