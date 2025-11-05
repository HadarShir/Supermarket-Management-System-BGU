package PresentationLayer;

import java.util.Scanner;

public class InputValidator {

    private static Scanner scanner;

    public static int validateIntInput(String message) {
        Scanner scanner = new Scanner(System.in);
        int input;
        while (true) {
            System.out.println(message);
            String line = scanner.nextLine();
            try {
                input = Integer.parseInt(line);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
        return input;
    }


    public static double validateDoubleInput(String message) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(message + " ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please enter a number.");
                continue;
            }
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

}
