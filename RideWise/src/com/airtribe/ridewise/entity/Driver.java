package com.airtribe.ridewise.entity;

import java.util.UUID;

public class Driver extends Person {
    private String[] currentLocation;
    private boolean available;

    public Driver(String name, String[] location) {
        this.id = generateId();
        this.name = name;
        this.currentLocation = location;
        this.available = true;
    }

    public String[] getCurrentLocation() { return currentLocation; }
    public boolean isAvailable() { return available; }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String generateId() {
        return "D-" + UUID.randomUUID();
    }
}
