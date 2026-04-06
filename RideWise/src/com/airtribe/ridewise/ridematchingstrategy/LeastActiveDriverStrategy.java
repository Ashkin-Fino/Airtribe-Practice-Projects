package com.airtribe.ridewise.ridematchingstrategy;

import java.util.List;

import com.airtribe.ridewise.entity.Driver;

public class LeastActiveDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(String[] riderLocation, List<Driver> drivers) {
        // need to add ride history field to Driver class to track number of rides completed
        return drivers.isEmpty() ? null : drivers.get(drivers.size() - 1);
    }
}
