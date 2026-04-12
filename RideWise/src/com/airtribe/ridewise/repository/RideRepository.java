package com.airtribe.ridewise.repository;

import java.util.*;

import com.airtribe.ridewise.entity.Ride;

public class RideRepository {
    private Map<String, Ride> rides = new HashMap<>();

    public void save(Ride ride) {
        rides.put(ride.getId(), ride);
    }

    public List<Ride> getAll() {
        return new ArrayList<>(rides.values());
    }
}
