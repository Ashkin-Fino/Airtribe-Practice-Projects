package com.airtribe.parkinglot;

import java.util.Scanner;

import com.airtribe.parkinglot.repository.ParkingSpotRepository;
import com.airtribe.parkinglot.repository.TicketRepository;
import com.airtribe.parkinglot.service.ParkingService;
import com.airtribe.parkinglot.ui.Views;

public class ParkingLot {
    public static void main(String[] args) {
        ParkingSpotRepository parkingSpotRepository = new ParkingSpotRepository();
        TicketRepository ticketRepository = new TicketRepository();
        ParkingService parkingService = new ParkingService(parkingSpotRepository, ticketRepository);

        Scanner scanner = new Scanner(System.in);

        Views.WelcomeView();
        
        while (true) {
            Views.ChoiceView();
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    parkingService.parkVehicle();
                    break;
                case 2:
                    parkingService.unparkVehicle();
                    break;
                case 3:
                    Views.ExitView();
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option selected. Please select a valid option:");
            }
        }
    }
}
