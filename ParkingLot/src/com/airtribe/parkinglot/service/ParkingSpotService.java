package com.airtribe.parkinglot.service;

import com.airtribe.parkinglot.helper.Utils;
import com.airtribe.parkinglot.repository.ParkingSpotRepository;

public class ParkingSpotService {
    private ParkingSpotRepository parkingSpotRepository;

    public ParkingSpotService(ParkingSpotRepository parkingSpotRepo) {
        this.parkingSpotRepository = parkingSpotRepo;
    }

    public void addParkingSpots() {
        System.out.println("Enter the Floor in which want to add parking spot:");
        int input = Utils.scanner.nextInt();
    }
}
