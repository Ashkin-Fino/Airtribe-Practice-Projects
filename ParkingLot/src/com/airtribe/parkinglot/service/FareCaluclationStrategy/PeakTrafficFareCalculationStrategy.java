package com.airtribe.parkinglot.service.FareCaluclationStrategy;

import com.airtribe.parkinglot.entity.Ticket;
import com.airtribe.parkinglot.entity.VehicleSize;

public class PeakTrafficFareCalculationStrategy implements FareCalculationStrategy {
    private static final double PEAK_HOUR_RATE_SMALL = 20.0;
    private static final double PEAK_HOUR_RATE_MEDIUM = 50.0;
    private static final double PEAK_HOUR_RATE_LARGE = 80.0;

    @Override
    public double calculateFare(Ticket ticket) {
        long hoursParked = java.time.Duration.between(ticket.getEntryTime(), ticket.getExitTime()).toHours();
        VehicleSize size = ticket.getVehicle().getSize();
        double rate = 0.0;
        if (size == VehicleSize.SMALL) {
            rate = PEAK_HOUR_RATE_SMALL;
        } else if (size == VehicleSize.MEDIUM) {
            rate = PEAK_HOUR_RATE_MEDIUM;
        } else if (size == VehicleSize.LARGE) {
            rate = PEAK_HOUR_RATE_LARGE;
        }
        return hoursParked * rate;
    }

}
