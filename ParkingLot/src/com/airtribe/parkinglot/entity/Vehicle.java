package com.airtribe.parkinglot.entity;

public class Vehicle {
    private String vehicleNumber;
    private VehicleSize size;

    public Vehicle(String vehicleNumber, String size) {
        this.vehicleNumber = vehicleNumber;
        if (size.equalsIgnoreCase("Large")) {
            this.size = VehicleSize.LARGE;
        } else if (size.equalsIgnoreCase("Medium")) {
            this.size = VehicleSize.MEDIUM;
        } else if (size.equalsIgnoreCase("Small")) {
            this.size = VehicleSize.SMALL;
        } else {
            throw new IllegalArgumentException("Invalid vehicle size: " + size);
        }
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public VehicleSize getSize() {
        return size;
    }
}
