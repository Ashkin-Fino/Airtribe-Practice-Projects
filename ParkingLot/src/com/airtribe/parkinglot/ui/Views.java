package com.airtribe.parkinglot.ui;

public class Views {
    
    public static void WelcomeView() {
        System.out.println("/---------------------------------\\");
        System.out.println("|Welcome to the Parking Lot System|");
        System.out.println("\\---------------------------------/");
    }

    public static void ChoiceView() {
        System.out.println("1. Park a car");
        System.out.println("2. Retrieve a car");
        System.out.println("3. Exit");
    }

    public static void ExitView() {
        System.out.println("/--------------------------------------\\");
        System.out.println("|Thank you for using Parking Lot system|");
        System.out.println("\\--------------------------------------/");
    }
}
