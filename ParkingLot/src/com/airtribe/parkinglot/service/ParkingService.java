package com.airtribe.parkinglot.service;

import com.airtribe.parkinglot.repository.ParkingSpotRepository;
import com.airtribe.parkinglot.repository.TicketRepository;

public class ParkingService {
    ParkingSpotRepository parkingSpotRepository;
    TicketRepository ticketRepository;

    public ParkingService(ParkingSpotRepository parkingSpotRepo, TicketRepository ticketRepo) {
        this.parkingSpotRepository = parkingSpotRepo;
        this.ticketRepository = ticketRepo;
    }

    public void parkVehicle() {
        //pass
    }

    public void unparkVehicle() {
        //pass
    }
}
