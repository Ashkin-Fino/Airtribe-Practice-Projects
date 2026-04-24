package com.airtribe.parkinglot.entity;

import java.time.LocalDateTime;

public class Ticket {
    private String ticketId;
    private ParkingSpot parkingSpot;
    private String vehicleNumber;
    private VehicleSize vehicleSize;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;

    public Ticket(ParkingSpot parkingSpot, 
            Vehicle vehicle, LocalDateTime entryTime) {
        this.ticketId = this.generateTicketId();
        this.parkingSpot = parkingSpot;
        this.vehicleNumber = vehicle.getVehicleNumber();
        this.vehicleSize = vehicle.getSize();
        this.entryTime = entryTime;
    }

    private String generateTicketId() {
        // Generates ID using UUID
        
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public String getTicketId() {
        return ticketId;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public Vehicle getVehicle() {
        return new Vehicle(vehicleNumber, vehicleSize.toString());
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }
}
