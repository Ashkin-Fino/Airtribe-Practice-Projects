package com.airtribe.parkinglot.service.ParkingSpotAssignmentStrategy;

import com.airtribe.parkinglot.entity.ParkingSpot;
import com.airtribe.parkinglot.repository.ParkingSpotRepository;

public interface ParkingSpotAssignmentStrategy {
    ParkingSpot assignParkingSpot(String vehicleSize, ParkingSpotRepository psRepo);
}
