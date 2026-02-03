package com.airtribe.learntrack.utils;

import java.util.Scanner;

public class Utils {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Get validated integer input within a range.
     */
    public static int getUserInput(int lowerBound, int upperBound) {
        int choice;

        while (true) {
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice < lowerBound || choice > upperBound) {
                    System.out.println("Invalid input! Please enter a number between " + lowerBound + " and " + upperBound);
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }

        return choice;
    }

    /**
     * Get a string input from console.
     */
    public static String getStringInput(Boolean is_mandatory)  {
        String input;
        while (true) {
            input = scanner.nextLine().trim();
            if (is_mandatory && input.isEmpty()) {
                System.out.println("Input cannot be empty for a mandatory field. Please enter a value:");
            } else if (input.isEmpty()) {
                input = null;
            } else {
                break;
            }
        }
        return input;
    }

    /**
     * Get an integer input from console(no range restriction).
     */
    public static Integer getIntInput(Boolean is_mandatory) {
        String input;
        Integer number;
        while (true) {
            input = scanner.nextLine().trim();
            if (is_mandatory && input.isEmpty()) {
                System.out.println("Input cannot be empty for a mandatory field. Please enter a value:");
            } else if (input.isEmpty()){
                number = null;
                break;
            } else {
                number = Integer.parseInt(input); 
                break;
            }
        }
        return number;
    }

    /**
     * Clear console (works in most terminals).
     */
    public static void clearConsole() {
        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception e) {
            // ignore
        }
    }
}