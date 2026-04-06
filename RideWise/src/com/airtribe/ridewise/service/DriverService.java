package com.airtribe.ridewise.service;

import java.util.*;

import com.airtribe.ridewise.entity.Driver;
import com.airtribe.ridewise.repository.DriverRepository;

class DriverService {
    private DriverRepository repo;

    public DriverService(DriverRepository repo) {
        this.repo = repo;
    }

    public Driver registerDriver(String name, String[] location) {
        Driver driver = new Driver(name, location);
        repo.save(driver);
        return driver;
    }

    public List<Driver> listAvailableDrivers() {
        List<Driver> result = new ArrayList<>();
        for (Driver d : repo.getAll()) {
            if (d.isAvailable()) result.add(d);
        }
        return result;
    }

    public void updateAvailability(Driver driver, boolean status) {
        driver.setAvailable(status);
    }
}
