package com.airtribe.parkinglot.ui;

import com.airtribe.parkinglot.entity.Ticket;

public class Views {
    
    public static void welcomeView() {
        System.out.println("/---------------------------------\\");
        System.out.println("|Welcome to the Parking Lot System|");
        System.out.println("\\---------------------------------/");
    }

    public static void choiceView() {
        System.out.println("1. Park a car");
        System.out.println("2. Retrieve a car");
        System.out.println("3. Add parking spots");
        System.out.println("4. Exit");
    }

    public static void printTicketView(Ticket ticket) {
        System.out.println("Ticket Id: " + ticket.getTicketId());
        System.out.println("Parking Floor Number: " + ticket.getParkingSpot().getFloorNumber());
        System.out.println("Parking Spot: " + ticket.getParkingSpot().getSpotId());
        System.out.println("Vehicle: " + ticket.getVehicle().getVehicleNumber());
    }

    public static void exitView() {
        System.out.println("/--------------------------------------\\");
        System.out.println("|Thank you for using Parking Lot system|");
        System.out.println("\\--------------------------------------/");
    }
}
