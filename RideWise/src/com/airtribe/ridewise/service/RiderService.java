package com.airtribe.ridewise.service;

import com.airtribe.ridewise.entity.Rider;
import com.airtribe.ridewise.repository.RiderRepository;

class RiderService {
    private RiderRepository repo;

    public RiderService(RiderRepository repo) {
        this.repo = repo;
    }

    public Rider registerRider(String name) {
        Rider rider = new Rider(name);
        repo.save(rider);
        return rider;
    }

    public Rider getRiderById(String id) {
        return repo.getById(id);
    }
}
