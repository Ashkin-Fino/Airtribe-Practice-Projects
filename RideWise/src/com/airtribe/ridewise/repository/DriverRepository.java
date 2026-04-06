package com.airtribe.ridewise.repository;

import java.util.*;

import com.airtribe.ridewise.entity.Driver;

public class DriverRepository {
    private Map<String, Driver> drivers = new HashMap<>();

    public void save(Driver driver) {
        drivers.put(driver.getId(), driver);
    }

    public List<Driver> getAll() {
        return new ArrayList<>(drivers.values());
    }
}
