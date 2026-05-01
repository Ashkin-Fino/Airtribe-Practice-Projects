package com.airtribe.parkinglot.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.airtribe.parkinglot.entity.ParkingSpot;
import com.airtribe.parkinglot.entity.VehicleSize;

public class ParkingSpotRepository {
    private HashMap<Integer, List<ParkingSpot>> parkingFloors;

    public ParkingSpotRepository() {
        this.parkingFloors = new HashMap<>();

        // Default: 
        // Add 2 Floors
        // 10 LARGE, 10 MEDIUM and 10 SMALL parking spots per floor
        for (int floor = 1; floor <= 2; floor++) {
            addParkingFloor(floor);
            for (int i = 0; i < 10; i++) {
                addParkingSpot(new ParkingSpot(floor, "L"+i, VehicleSize.LARGE));
                addParkingSpot(new ParkingSpot(floor, "M"+i, VehicleSize.MEDIUM));
                addParkingSpot(new ParkingSpot(floor, "S"+i, VehicleSize.SMALL));
            }
        }
    }

    public void addParkingFloor(int floorNumber) throws IllegalArgumentException{
        if (parkingFloors.getOrDefault(floorNumber, null) == null) {
            parkingFloors.put(floorNumber, new ArrayList<>());
        } else {
            throw new IllegalArgumentException("Floor already exists");
        }
    }

    public void addParkingSpot(ParkingSpot spot) throws IllegalArgumentException {
        List<ParkingSpot> spots = parkingFloors.get(spot.getFloorNumber());
        if (spots != null) {
            spots.add(spot);
        } else {
            throw new IllegalArgumentException("Floor does not exist");
        }
    }

    public List<ParkingSpot> getParkingSpotsByFloor(int floorNumber) throws IllegalArgumentException {
        List<ParkingSpot> floor = parkingFloors.get(floorNumber);
        if (floor != null) {
            return floor;
        } else {
            throw new IllegalArgumentException("Floor does not exist");
        }
    }

    public ParkingSpot getParkingSpotById(String spotId) throws IllegalArgumentException {
        int floorNumber = Integer.parseInt(spotId.split("-")[0]);
        List<ParkingSpot> floor = parkingFloors.get(floorNumber);
        for (ParkingSpot spot : floor) {
            if (spot.getSpotId().equals(spotId)) return spot;
        }
        throw new IllegalArgumentException("Parking spot not found");
    }

    public List<Integer> getAllFloors() {
        return new ArrayList<>(parkingFloors.keySet());
    }
}
