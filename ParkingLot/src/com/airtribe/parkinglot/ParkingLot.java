package com.airtribe.parkinglot;

import com.airtribe.parkinglot.helper.Utils;
import com.airtribe.parkinglot.repository.ParkingSpotRepository;
import com.airtribe.parkinglot.repository.TicketRepository;
import com.airtribe.parkinglot.service.ParkingService;
import com.airtribe.parkinglot.service.ParkingSpotService;
import com.airtribe.parkinglot.ui.Views;

public class ParkingLot {
    public static void main(String[] args) {
        ParkingSpotRepository parkingSpotRepository = new ParkingSpotRepository();
        TicketRepository ticketRepository = new TicketRepository();
        ParkingService parkingService = new ParkingService(parkingSpotRepository, ticketRepository);
        ParkingSpotService parkingSpotService = new ParkingSpotService(parkingSpotRepository);

        Views.welcomeView();
        
        while (true) {
            Views.choiceView();
            int choice = Utils.scanner.nextInt();
            switch (choice) {
                case 1:
                    parkingService.parkVehicle();
                    break;
                case 2:
                    parkingService.unparkVehicle();
                    break;
                case 3:
                    parkingSpotService.addParkingSpots();
                    break;
                case 4:
                    Views.exitView();
                    Utils.scanner.close();
                    return;
                default:
                    System.out.println("Invalid option selected. Please select a valid option:");
            }
        }
    }
}
