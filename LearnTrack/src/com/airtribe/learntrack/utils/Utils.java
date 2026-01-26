package com.airtribe.learntrack.utils;

import java.util.Scanner;

public class Utils {
    
    private static final Scanner scanner = new Scanner(System.in);

    public static int getUserInput(int lowerBound, int upperBound) {
        int choice;

        while (true) {
            try {
                choice = scanner.nextInt();
                if (choice < lowerBound || choice > upperBound) {
                    System.out.println("Invalid input! Please enter a number between " + lowerBound + " and " + upperBound);
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
        
        return choice;
    }

    public static void clearConsole() {
        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception e) {
            // ignore
        }
    }
}
