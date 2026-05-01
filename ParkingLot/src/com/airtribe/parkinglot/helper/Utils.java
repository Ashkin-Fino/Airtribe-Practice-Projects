package com.airtribe.parkinglot.helper;

import java.util.Scanner;

public class Utils {
    public static final Scanner scanner = new Scanner(System.in);

    public static boolean validateIntInput(int input, int startRange, int endRange) {
        if (input < startRange || input > endRange) {
            System.out.println("Invalid input. Please enter a number between " + startRange + " and " + endRange);
            return false;
        }
        return true;
    }

    public static <T extends Enum<T>> boolean validateEnumInput(String input, Class<T> enumClass) {
        try {
            Enum.valueOf(enumClass, input.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input. Enter one of the valid options: ");
            for (T constant : enumClass.getEnumConstants()) {
                System.out.print(constant.name() + " ");
            }
            System.out.println();
            return false;
        }
    }
}
