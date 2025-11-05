package PresentationLayer;
import DomainLayer.DeliveryException;
import DomainLayer.Status;
import ServiceLayer.*;


import java.sql.SQLException;
import java.util. *;

public class UserMenu {
    private final DeliveryService deliveryService;
    private final TruckService truckService;
    private final BranchService branchService;
    private final Scanner scanner = new Scanner(System.in);

    UserMenu() throws Exception {
        this.deliveryService = new DeliveryService();
        this.truckService = new TruckService();
        this.branchService = new BranchService();
    }


    public void showMainMenu() throws Exception {

        //showLoginWindow();
        boolean exit = false;
        while (!exit) {
            int choice = InputValidator.validateIntInput("Hello welcome to lee-market delivery system\n" +
                    "Choose option:\n" +
                    "1. Create new delivery\n" +
                    "2. Show system reports\n" +
                    "3. Dispatch delivery\n" +
                    "4. Complete delivery\n" +
                    "5. Send drivers requirements to HR based on weekly weight estimates\n" +
                    "6. Delay all un assigned orders to next supply day\n" +
                    "7. Add a new truck\n" +
                    "8. Exit DriverManager menu");

            switch (choice) {
                case 1 ->
                        new DeliveryMenu(deliveryService, truckService,branchService).createNewDelivery();

                case 2 -> new SystemReportsMenu().showSystemReports();

                case 3 ->
                        new DeliveryMenu(deliveryService, truckService,branchService).dispatchDelivery();
                case 4 ->
                        new DeliveryMenu(deliveryService, truckService,branchService).completeDelivery();
                case 5 -> {
                    new DeliveryMenu(deliveryService, truckService, branchService).sendDriverRequirementsToHR();
                }
                case 6 -> {
                    new DeliveryMenu(deliveryService, truckService, branchService).delayOrders();
                }
                case 7 -> {
                    new SystemOperationsMenu().addNewTruck();
                }
                case 8 -> exit = true;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}



