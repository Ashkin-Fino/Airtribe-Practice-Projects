package com.airtribe.parkinglot.service;

import java.time.LocalDateTime;

import com.airtribe.parkinglot.entity.ParkingSpot;
import com.airtribe.parkinglot.entity.Ticket;
import com.airtribe.parkinglot.entity.Vehicle;
import com.airtribe.parkinglot.helper.Utils;
import com.airtribe.parkinglot.repository.ParkingSpotRepository;
import com.airtribe.parkinglot.repository.TicketRepository;
import com.airtribe.parkinglot.service.ParkingSpotAssignmentStrategy.NearestParkingSpotAssignmentStrategy;
import com.airtribe.parkinglot.service.ParkingSpotAssignmentStrategy.ParkingSpotAssignmentStrategy;
import com.airtribe.parkinglot.ui.Views;

public class ParkingService {
    ParkingSpotRepository parkingSpotRepository;
    TicketRepository ticketRepository;

    public ParkingService(ParkingSpotRepository parkingSpotRepo, TicketRepository ticketRepo) {
        this.parkingSpotRepository = parkingSpotRepo;
        this.ticketRepository = ticketRepo;
    }

    public void parkVehicle() {
        System.out.println("Scanning vehicle number:");
        String vehicleNumber = Utils.scanner.nextLine();

        System.out.println("Enter vehicle size: (Large, Medium, Small)");
        String vehicleSize = Utils.scanner.nextLine();

        while (!vehicleSize.equalsIgnoreCase("Large") && 
                !vehicleSize.equalsIgnoreCase("Medium") && 
                !vehicleSize.equalsIgnoreCase("Small")) {
                    System.out.println("Invalid vehicle size entered. Please enter a valid size: (Large, Medium, Small)");
                    vehicleSize = Utils.scanner.nextLine();
        }

        Vehicle parkedVehicle = new Vehicle(vehicleNumber, vehicleSize);

        ParkingSpotAssignmentStrategy strategy = new NearestParkingSpotAssignmentStrategy();
        ParkingSpot spot = strategy.assignParkingSpot(vehicleSize, parkingSpotRepository);

        Ticket ticket = new Ticket(spot, parkedVehicle, LocalDateTime.now());

        System.out.println("Printing ticket....");
        Views.printTicketView(ticket);
         
    }   

    public void unparkVehicle() {
        //pass
    }
}
