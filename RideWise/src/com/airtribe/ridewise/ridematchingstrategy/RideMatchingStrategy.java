package com.airtribe.ridewise.ridematchingstrategy;

import java.util.List;

import com.airtribe.ridewise.entity.Driver;

public interface RideMatchingStrategy {
    Driver findDriver(String[] riderLocation, List<Driver> drivers);
}
