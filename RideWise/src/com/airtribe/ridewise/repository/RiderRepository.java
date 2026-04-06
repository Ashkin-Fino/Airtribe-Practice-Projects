package com.airtribe.ridewise.repository;

import java.util.*;

import com.airtribe.ridewise.entity.Rider;

public class RiderRepository {
    private Map<String, Rider> riders = new HashMap<>();

    public void save(Rider rider) {
        riders.put(rider.getId(), rider);
    }

    public Rider getById(String id) {
        return riders.get(id);
    }
}
