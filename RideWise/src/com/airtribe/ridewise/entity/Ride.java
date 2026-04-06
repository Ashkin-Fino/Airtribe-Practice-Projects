package com.airtribe.ridewise.entity;

import java.util.UUID;

public class Ride {
    private String id;
    private String[] startLocation;
    private String[] endLocation;
    private Rider rider;
    private Driver driver;
    private int distance;
    private RideStatus status;
    private FareReceipt receipt;

    public Ride(String[] start, String[] end, Rider rider) {
        this.id = generateId();
        this.startLocation = start;
        this.endLocation = end;
        this.rider = rider;
        this.status = RideStatus.YETTOBOARD;
    }

    public String getId() { 
        return id; 
    }

    public Rider getRider() { 
        return rider; 
    }

    public Driver getDriver() { 
        return driver; 
    }

    public int getDistance() { 
        return distance; 
    }

    public RideStatus getStatus() { 
        return status; 
    }

    public void setDriver(Driver driver) { 
        this.driver = driver; 
    }

    public void setDistance(int distance) { 
        this.distance = distance; 
    }

    public void setStatus(RideStatus status) { 
        this.status = status; 
    }

    public void setReceipt(FareReceipt receipt) { 
        this.receipt = receipt; 
    }
    
    public String generateId() {
        return "Ride-" + UUID.randomUUID();
    }
}
