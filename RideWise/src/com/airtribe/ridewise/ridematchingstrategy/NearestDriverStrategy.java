package com.airtribe.ridewise.ridematchingstrategy;

import java.util.List;

import com.airtribe.ridewise.entity.Driver;

public class NearestDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(String[] riderLocation, List<Driver> drivers) {
        return drivers.isEmpty() ? null : drivers.get(0);
    }
}
