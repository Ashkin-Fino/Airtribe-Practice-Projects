package com.airtribe.parkinglot.service;

import java.util.List;

import com.airtribe.parkinglot.entity.ParkingSpot;
import com.airtribe.parkinglot.entity.VehicleSize;
import com.airtribe.parkinglot.helper.Utils;
import com.airtribe.parkinglot.repository.ParkingSpotRepository;

public class ParkingSpotService {
    private ParkingSpotRepository parkingSpotRepository;

    public ParkingSpotService(ParkingSpotRepository parkingSpotRepo) {
        this.parkingSpotRepository = parkingSpotRepo;
    }

    public void addParkingSpots() {
        System.out.println("Enter the Floor in which you want to add parking spot:");
        int input = Utils.scanner.nextInt();

        List<ParkingSpot> floorSpots;
        try {
            floorSpots = parkingSpotRepository.getParkingSpotsByFloor(input);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("Do You want to create new floor " + input + " ? (Y/N)");
            String createFloor = Utils.scanner.nextLine();
            if (createFloor.equalsIgnoreCase("Y")) {
                parkingSpotRepository.addParkingFloor(input);
                floorSpots = parkingSpotRepository.getParkingSpotsByFloor(input);
            } else {
                System.out.println("Aborting parking spot addition.");
                return;
            }
        }
        
        System.out.println("Enter the size of parking spot to be added (LARGE, MEDIUM, SMALL):");
        String size = Utils.scanner.nextLine();
        while (!Utils.validateEnumInput(size, VehicleSize.class)) {
            System.out.println("Invalid size. Please enter a valid size (LARGE, MEDIUM, SMALL):");
            size = Utils.scanner.nextLine();
        }
        VehicleSize spotSize = VehicleSize.valueOf(size.toUpperCase());
        
        System.out.println("Enter the number of parking spots to add:");
        int countOfSpots = Utils.scanner.nextInt();

        List<ParkingSpot> filteredSpots =floorSpots.stream()
            .filter(spot -> spot.getSize().equalsIgnoreCase(spotSize.toString()))
            .toList();
        int lastIndex = filteredSpots.size() > 0 ? Integer.parseInt(filteredSpots.get(filteredSpots.size() - 1).getSpotId().split("-")[1].substring(1)) : -1;
        
        for (int i = lastIndex+1; i <= countOfSpots+lastIndex; i++) {
            String spotId = spotSize.toString().charAt(0) + String.valueOf(i);
            parkingSpotRepository.addParkingSpot(new ParkingSpot(input, spotId, spotSize));
        }

        System.out.println(countOfSpots + " " + spotSize.toString() + " parking spots added to floor " + input);
    }
}
