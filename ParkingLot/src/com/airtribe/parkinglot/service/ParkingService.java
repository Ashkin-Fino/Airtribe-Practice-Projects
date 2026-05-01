package com.airtribe.parkinglot.service;

import java.time.LocalDateTime;

import com.airtribe.parkinglot.entity.ParkingSpot;
import com.airtribe.parkinglot.entity.Ticket;
import com.airtribe.parkinglot.entity.Vehicle;
import com.airtribe.parkinglot.entity.VehicleSize;
import com.airtribe.parkinglot.helper.Utils;
import com.airtribe.parkinglot.repository.ParkingSpotRepository;
import com.airtribe.parkinglot.repository.TicketRepository;
import com.airtribe.parkinglot.service.FareCaluclationStrategy.FareCalculationStrategy;
import com.airtribe.parkinglot.service.FareCaluclationStrategy.PeakTrafficFareCalculationStrategy;
import com.airtribe.parkinglot.service.FareCaluclationStrategy.StandardFareCalculationStrategy;
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

        while (!Utils.validateEnumInput(vehicleSize, VehicleSize.class)) {
            vehicleSize = Utils.scanner.nextLine();
        }

        Vehicle parkedVehicle = new Vehicle(vehicleNumber, vehicleSize);

        ParkingSpotAssignmentStrategy strategy = new NearestParkingSpotAssignmentStrategy();
        ParkingSpot spot = strategy.assignParkingSpot(vehicleSize, parkingSpotRepository);

        Ticket ticket = new Ticket(spot, parkedVehicle, LocalDateTime.now());
        ticketRepository.addTicket(ticket);

        System.out.println("Printing ticket....");
        Views.printTicketView(ticket);
    }   

    public void unparkVehicle() {
        System.out.println("Scanning Ticket Id:");
        String ticketId = Utils.scanner.nextLine();

        Ticket ticket = ticketRepository.getTicketById(ticketId);
        ticket.setExitTime(LocalDateTime.now());

        ParkingSpot spot = ticket.getParkingSpot();
        spot.vacate();

        FareCalculationStrategy strategy = getFareCalculationStrategy();
        double fare = strategy.calculateFare(ticket);
        System.out.println("Total Fare: " + fare);
        ticket.setStatus("CLOSED");

        System.out.println("Thank you for visiting. Please come again!");
    }

    private FareCalculationStrategy getFareCalculationStrategy() {
        System.out.println("Choose Fare Calculation Strategy:");
        Views.fareCalculationStrategiesView();
        int choice = Utils.scanner.nextInt();
        while (!Utils.validateIntInput(choice, 1, 2)) {
            choice = Utils.scanner.nextInt();
        }
        
        FareCalculationStrategy strategy;
        switch (choice) {
            case 1:
                strategy = new PeakTrafficFareCalculationStrategy();
                break;
            case 2:
                strategy = new StandardFareCalculationStrategy();
                break;
            default:
                strategy = new StandardFareCalculationStrategy();
                break;
        }
        return strategy;
    }
}
