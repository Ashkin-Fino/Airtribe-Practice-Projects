package com.airtribe.parkinglot.service.FareCaluclationStrategy;

import com.airtribe.parkinglot.entity.Ticket;
import com.airtribe.parkinglot.entity.VehicleSize;

public class StandardFareCalculationStrategy implements FareCalculationStrategy {
    private static final double HOURLY_RATE_SMALL = 10.0;
    private static final double HOURLY_RATE_MEDIUM = 30.0;
    private static final double HOURLY_RATE_LARGE = 50.0;

    @Override
    public double calculateFare(Ticket ticket) {
        long hoursParked = java.time.Duration.between(ticket.getEntryTime(), ticket.getExitTime()).toHours();
        VehicleSize size = ticket.getVehicle().getSize();
        double rate = 0.0;
        if (size == VehicleSize.SMALL) {
            rate = HOURLY_RATE_SMALL;
        } else if (size == VehicleSize.MEDIUM) {
            rate = HOURLY_RATE_MEDIUM;
        } else if (size == VehicleSize.LARGE) {
            rate = HOURLY_RATE_LARGE;
        }
        return hoursParked * rate;
    }

}
