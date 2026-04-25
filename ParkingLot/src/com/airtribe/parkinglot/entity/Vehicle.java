package com.airtribe.parkinglot.entity;

public class Vehicle {
    private String vehicleNumber;
    private VehicleSize size;

    public Vehicle(String vehicleNumber, String size) {
        this.vehicleNumber = vehicleNumber;
        this.size = Enum.valueOf(VehicleSize.class, size);
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public VehicleSize getSize() {
        return size;
    }
}
