package com.airtribe.parkinglot.service.ParkingSpotAssignmentStrategy;

import java.util.Collections;
import java.util.List;

import com.airtribe.parkinglot.entity.ParkingSpot;
import com.airtribe.parkinglot.repository.ParkingSpotRepository;

public class NearestParkingSpotAssignmentStrategy implements ParkingSpotAssignmentStrategy{

    @Override
    public ParkingSpot assignParkingSpot(String vehicleSize, ParkingSpotRepository psRepo) {
        List<Integer> floors = psRepo.getAllFloors();
        Collections.sort(floors);
        for(int floor: floors) {
            List<ParkingSpot> parkingSpots = psRepo.getParkingSpotsByFloor(floor);
            for (ParkingSpot parkingSpot : parkingSpots) {
                if (parkingSpot.getSize().equalsIgnoreCase(vehicleSize) && !parkingSpot.isOccupied()) {
                    return parkingSpot;
                }
            }
        }
        return null;
    }
}
