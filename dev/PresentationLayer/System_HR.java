package PresentationLayer;

import ServiceLayer.*;
import java.util.Scanner;
import java.util.List;

// User menu - for HR manager

public class System_HR {
    public String in;
    public String userName;
    public Service service;


    public void printMenu(){
        System.out.println("========================================");
        System.out.println("         HR Management - Main Menu      ");
        System.out.println("========================================");
        System.out.println("--- Branch Management ---");
        System.out.println(" 1.  Show all shifts history");
        System.out.println(" 2.  Show shifts history by branch");
        System.out.println(" 3.  Show branches IDs");
        System.out.println(" 4.  Add branch");
        System.out.println("----------------------------------------");
        System.out.println("--- Role Management ---");
        System.out.println(" 5.  Show roles");
        System.out.println(" 6.  Create new role");
        System.out.println(" 7. Add role to employee");
        System.out.println("----------------------------------------");
        System.out.println("--- Shift Management ---");
        System.out.println(" 8.  Publish next week");
        System.out.println(" 9.  Set as current week");
        System.out.println("10.  Change shift's requirement");
        System.out.println("11.  Show available employees for shift by role");
        System.out.println("12.  Assign employee");
        System.out.println("13.  Set shift as vacation");
        System.out.println("14.  Allow availability changes");
        System.out.println("15.  Disable availability changes");
        System.out.println("16.  Show current week");
        System.out.println("17.  Show next week");
        System.out.println("18.  Change shift's time");
        System.out.println("----------------------------------------");
        System.out.println("--- Employee Management ---");
        System.out.println("19.  Hire employee");
        System.out.println("20.  Fire employee");
        System.out.println("21.  Change employee's hourly salary");
        System.out.println("22.  Extend employee's contract");
        System.out.println("23.  Show all employees and fired employees");
        System.out.println("24.  Show employee details");
        System.out.println("----------------------------------------");
        System.out.println("25.  Logout");
        System.out.println("========================================");
        getAnswer();
    }

    public void getAnswer(){
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();
        scanner.nextLine();

        switch (input) {
            //Show all shifts history
            case 1 -> {
                Response r = service.showAllHistory(userName);
                System.out.println(r.toString());
                printMenu();
            }

            //Show shifts history by branch
            case 2 -> {
                System.out.println("---choose Branch id");
                in = scanner.nextLine();
                Response r = service.showAllHistoryByBranch(userName, in);
                System.out.println(r.toString());
                printMenu();
            }

            //Show branches IDs
            case 3 -> {
                Response r = service.showBranchesID(userName);
                System.out.println(r.toString());
                printMenu();
            }

            //Add branch
            case 4 -> {
                System.out.println("---insert city:");
                String city = scanner.nextLine();
                System.out.print("Enter shipping area (number): ");
                int shippingArea = Integer.parseInt(scanner.nextLine());
                System.out.println("---insert phone number:");
                String phone = scanner.nextLine();
                System.out.println("---insert contact name:");
                String contact = scanner.nextLine();
                Response r = service.addBranch(userName, city, shippingArea, phone, contact);
                System.out.println(r.toString());
                printMenu();
            }

            //Show roles
            case 5 -> {
                Response r = service.showRoles(userName);
                System.out.println(r.toString());
                printMenu();
            }

            //Create new role
            case 6 -> {
                System.out.println("--insert new role");
                in = scanner.nextLine();
                Response r = service.createNewRole(userName, in);
                System.out.println(r.toString());
                printMenu();
            }

            //Add role to employee
            case 7 -> {
                Response r = service.showRoles(userName);
                System.out.println(r.toString());
                System.out.println("---insert role to change");
                String role = scanner.nextLine();
                System.out.println("---insert employee's name");
                String name = scanner.nextLine();
                System.out.println("---insert branch id");
                String BID = scanner.nextLine();
                r = service.addRoleToEmployee(userName, name, BID, role);
                System.out.println(r.toString());
                printMenu();
            }

            //Publish next week
            case 8 -> {
                Response r = service.showBranchesID(userName);
                System.out.println(r.toString());
                System.out.println("---insert branch id");
                in = scanner.nextLine();
                r = service.publishNextWeek(userName, in);
                System.out.println(r.toString());
                printMenu();
            }

            //Set as current week
            case 9 -> {
                Response r = service.showBranchesID(userName);
                System.out.println(r.toString());
                System.out.println("---insert branch id");
                in = scanner.nextLine();
                r = service.setAsCurrentWeek(userName, in);
                System.out.println(r.toString());
                printMenu();
            }

            //Change shift's requirement
            case 10 -> {
                Response r = service.showBranchesID(userName);
                System.out.println(r.toString());
                System.out.println("---insert branch id");
                String BID = scanner.nextLine();
                System.out.println("---insert required shift (0-13)");
                String dayAndType = scanner.nextLine();
                r = service.showRoles(userName);
                System.out.println(r.toString());
                System.out.println("---insert the role you want to change");
                String role = scanner.nextLine();
                System.out.println("---insert new amount of employees");
                String num = scanner.nextLine();
                r = service.changeRequirement(userName, BID, dayAndType, role, num);
                System.out.println(r.toString());
                printMenu();
            }

            //Show available employees for shift by role
            case 11 -> {
                Response r = service.showBranchesID(userName);
                System.out.println(r.toString());
                System.out.println("---insert branch id");
                String BID = scanner.nextLine();
                System.out.println("---insert required shift (0-13)");
                String dayAndType = scanner.nextLine();
                r = service.showRoles(userName);
                System.out.println(r.toString());
                System.out.println("---insert role");
                String role = scanner.nextLine();
                r = service.showAvailableEmployees(userName, BID, dayAndType, role);
                System.out.println(r.toString());
                printMenu();
            }

            //Assign employee
            case 12 -> {
                Response r = service.showBranchesID(userName);
                System.out.println(r.toString());
                System.out.println("---insert branch id");
                String BID = scanner.nextLine();
                System.out.println("---insert required shift (0-13)");
                String dayAndType = scanner.nextLine();
                r = service.showRoles(userName);
                System.out.println(r.toString());
                System.out.println("---insert role");
                String role = scanner.nextLine();
                Response available = service.showAvailableEmployees(userName, BID, dayAndType, role);
                System.out.println("Available employees for this shift/role: " + available.toString());
                System.out.println("---insert employee's name");
                String name = scanner.nextLine();
                r = service.pickEmployee(userName, BID, dayAndType, role, name);
                System.out.println(r.toString());
                printMenu();
            }

            //Set shift as vacation
            case 13 -> {
                Response r = service.showBranchesID(userName);
                System.out.println(r.toString());
                System.out.println("---insert branch id");
                String BID = scanner.nextLine();
                System.out.println("---insert required shift (0-13)");
                String dayAndType = scanner.nextLine();
                r = service.setVacation(userName, BID, dayAndType);
                System.out.println(r.toString());
                printMenu();
            }

            //Allow availability changes
            case 14 -> {
                Response r = service.showBranchesID(userName);
                System.out.println(r.toString());
                System.out.println("---insert branch id");
                String BID = scanner.nextLine();
                r = service.allowAvailabilityChanges(userName, BID);
                System.out.println(r.toString());
                printMenu();
            }

            //Disable availability changes
            case 15 -> {
                Response r = service.showBranchesID(userName);
                System.out.println(r.toString());
                System.out.println("---insert branch id");
                String BID = scanner.nextLine();
                r = service.disableAvailabilityChanges(userName, BID);
                System.out.println(r.toString());
                printMenu();
            }

            //Show current week
            case 16 -> {
                System.out.println("insert branchID");
                String BID = scanner.nextLine();
                Response r = service.HR_showCurrentWeek(userName, BID);
                System.out.println(r.toString());
                printMenu();
            }

            //Show current week
            case 17 -> {
                System.out.println("insert branchID");
                String BID = scanner.nextLine();
                Response r = service.HR_showNextWeek(userName, BID);
                System.out.println(r.toString());
                printMenu();
            }

            //Change shift's time
            case 18 -> {
                System.out.println("insert branchID");
                String BID = scanner.nextLine();
                System.out.println("insert shift number (0-13)");
                String num = scanner.nextLine();
                System.out.println("insert start time in format HH:MM");
                String start = scanner.nextLine();
                System.out.println("insert start time in format HH:MM");
                String end = scanner.nextLine();
                Response r = service.changeTime(userName, BID, num, start, end);
                System.out.println(r.toString());
                printMenu();
            }

            //Hire employee
            case 19 -> {
                Response r = service.showBranchesID(userName);
                System.out.println(r.toString());
                System.out.println("---insert branch id");
                String BID = scanner.nextLine();
                r = service.showRoles(userName);
                System.out.println(r.toString());
                System.out.println("---insert role to change");
                String role = scanner.nextLine();
                System.out.println("---insert bank account");
                String bank = scanner.nextLine();
                System.out.println("---insert name");
                String name = scanner.nextLine();
                System.out.println("---insert password");
                String enterPassword = scanner.nextLine();
                System.out.println("---insert hour salary");
                String salaryStr = scanner.nextLine();
                try {
                    Integer salary = Integer.parseInt(salaryStr);
                    r = service.hireEmployee(userName, BID, name, bank, role, enterPassword, salary);
                    System.out.println(r.toString());
                } catch (NumberFormatException e) {
                    System.out.println("Error: Salary must be a valid number");
                }
                printMenu();
            }

            //Fire employee
            case 20 -> {
                System.out.println("---insert employee's name");
                String name = scanner.nextLine();
                Response r = service.fireEmployee(userName, name);
                System.out.println(r.toString());
                printMenu();
            }

            //Change employee's hourly salary
            case 21 -> {
                System.out.println("---insert employee's name");
                String name = scanner.nextLine();
                System.out.println("---insert employee's new salary");
                String salary = scanner.nextLine();
                try {
                    // Validate that the input is a valid number
                    Integer.parseInt(salary);
                    Response r = service.changeEmployeeHourly(userName, name, salary);
                    System.out.println(r.toString());
                } catch (NumberFormatException e) {
                    System.out.println("Error: Salary must be a valid number");
                }
                printMenu();
            }

            //Extend employee's contract
            case 22 -> {
                System.out.println("---insert employee's name");
                String name = scanner.nextLine();
                System.out.println("---insert employee's date of end contract in format yyyy-MM-dd");
                String date = scanner.nextLine();
                Response r = service.extendContract(userName, name, date);
                System.out.println(r.toString());
                printMenu();
            }

            //Show all employees and fired employees
            case 23 -> {
                Scanner inputScanner = new Scanner(System.in);
                System.out.println("Choose an option:");
                System.out.println("1. Show all employees");
                System.out.println("2. Show only fired employees");
                String choice = inputScanner.nextLine();

                Response r = service.showBranchesID(userName);
                System.out.println(r.toString());
                System.out.println("---insert branch id");
                String BID = inputScanner.nextLine();

                if (choice.equals("1")) {
                    List<String> employees = service.getActiveEmployeeUsernamesByBranch(BID);
                    System.out.println("=== Active Employees in Branch " + BID + " ===\n");
                    for (String emp : employees) {
                        System.out.println(emp);
                    }
                } else if (choice.equals("2")) {
                    r = service.showFiredEmployeesByBranch(userName, BID);
                    System.out.println("=== Fired Employees in Branch " + BID + " ===\n");
                    String[] employees = r.toString().split("\n");
                    for (String emp : employees) {
                        if (!emp.isBlank()) {
                            System.out.println(emp);
                        }
                    }
                } else {
                    System.out.println("Invalid choice. Returning to menu.");
                }

                printMenu();
            }

            //Show employee details
            case 24 -> {
                System.out.println("---insert employee's name");
                String name = scanner.nextLine();
                Response r = service.showEmployeeDetails(userName, name);
                System.out.println(r.toString());
                printMenu();
            }

            //Logout
            case 25 -> {
                Response r = service.logout(userName);
                System.out.println(r.toString());
            }

            default -> {
                System.out.print("invalid input, please try again\n");
                printMenu();
            }
        }

    }

}