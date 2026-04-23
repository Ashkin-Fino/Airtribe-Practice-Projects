package com.airtribe.parkinglot.entity;

public class ParkingSpot {
    private String id;
    private boolean isOccupied;
    private VehicleSize spotSize;

    public ParkingSpot(int floorNumber, String parkingLotNumber, VehicleSize vehicleSize) {
        this.id = floorNumber + "-" + parkingLotNumber;
        this.spotSize = vehicleSize;
        this.isOccupied = false;
    }

    public String getSpotId() {
        return id;
    }

    public int getFloorNumber() {
        return Integer.parseInt(id.split("-")[0]);
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void occupy() {
        isOccupied = true;
    }

    public void vacate() {
        isOccupied = false;
    }

    public VehicleSize getVehicleSize() {
        return spotSize;
    }
}
