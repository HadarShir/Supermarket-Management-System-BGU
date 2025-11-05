package PresentationLayer;
import java.sql.Connection;
import java.sql.SQLException;

import DataLayer.DatabaseConnector;
import DataLayer.JdbcDeliveryRequestDao;
import DomainLayer.DeliveryManager;
import DomainLayer.DeliveryReport;
import DomainLayer.DeliveryRequestRepository;
import ServiceLayer.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Scanner;

public class Main {
    public static Service service;

    public static void main(String[] args) throws Exception {
        boolean system_on = true;
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║      Lee-Market Management System        ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║ Please choose how you want to boot:      ║");
        System.out.println("║                                          ║");
        System.out.println("║   1. Boot with no data (empty system)    ║");
        System.out.println("║   2. Boot with data                      ║");
        System.out.println("╚══════════════════════════════════════════╝");
        int choice = 0;
        while (choice != 1 && choice != 2) {
            System.out.print("Enter your choice [1/2]:");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Invalid input. Please enter 1 or 2:\n");
                scanner.nextLine();
            }
        }
        String configFile = (choice == 1) ? "config_empty.properties" : "config.properties";
        DatabaseConnector.initialize(configFile);

        try {
            service = new Service();
            System.out.println("\nSystem successfully booted.\n");
        } catch (Exception e) {
            System.out.println("Failed to initialize system:");
            e.printStackTrace();
            return;
        }
        int user_input;

        while (system_on) {
            System.out.print("Hello, please identify yourself:\n");
            System.out.print("1.Employee\n");
            System.out.print("2.HR\n");
            System.out.print("3.Delivery Manager\n");
            System.out.print("4.Reset system data\n");
            System.out.print("5.Exit system\n");
            System.out.print("Please choose an option: ");

            if (scanner.hasNextInt()) {
                user_input = scanner.nextInt();
                scanner.nextLine();  // Clear the buffer
                switch (user_input) {
                    case 1 -> handleEmployeeLogin(scanner);
                    case 2 -> handleHRLogin(scanner);
                    case 3 -> handleDeliveryManagerLogin(scanner);
                    case 4 -> handleResetData();
                    case 5 -> system_on = false;
                    default -> System.out.println("Enter correct value from the given options.");
                }
            } else {
                System.out.println("Please enter a valid option (1, 2, 3, 4, or 5).");
                scanner.nextLine();  // Clear the invalid input
            }
        }
        scanner.close();
    }

    public static void handleEmployeeLogin(Scanner scanner) {
        String user_name, password;
        System_User system_user = new System_User();
        system_user.service = service;

        System.out.print("Enter username: ");
        user_name = scanner.next();
        scanner.nextLine();
        System.out.print("Enter password: ");
        password = scanner.next();
        scanner.nextLine();

        Response response = service.login(user_name, password);
        if (!response.isError) {
            system_user.userName = user_name;
            system_user.printMenu();
        } else {
            System.out.println(response.msg);
        }
    }

    public static void handleHRLogin(Scanner scanner) {
        String user_name, password;
        System_HR system_hr = new System_HR();
        system_hr.service = service;

        System.out.print("Enter username: ");
        user_name = scanner.next();
        scanner.nextLine();
        System.out.print("Enter password: ");
        password = scanner.next();
        scanner.nextLine();

        Response response = service.login(user_name, password);
        if (!response.isError) {
            // Check if the user is HR
            Response hrCheck = service.HR_Or_KickOut(user_name);
            if (!hrCheck.isError) {
                system_hr.userName = user_name;
                system_hr.printMenu();
            } else {
                System.out.println(hrCheck.msg);
            }
        } else {
            System.out.println(response.msg);
        }
    }

    public static void handleDeliveryManagerLogin(Scanner scanner) throws Exception {
        String user_name, password;

        System.out.print("Enter username: ");
        user_name = scanner.next();
        scanner.nextLine();
        System.out.print("Enter password: ");
        password = scanner.next();
        scanner.nextLine();

        Response response = service.login(user_name, password);
        if (!response.isError) {
            // Check if the user is a delivery manager
            Response managerCheck = service.isDriverManager(user_name);
            if (!managerCheck.isError) {
                //system_dm.userName = user_name;
                new UserMenu().showMainMenu();
            } else {
                System.out.println(managerCheck.msg);
            }
        } else {
            System.out.println(response.msg);
        }
    }

    public static void handleResetData() throws Exception {
//        service.resetDatabase();
    }
}