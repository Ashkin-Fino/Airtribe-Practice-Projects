package com.airtribe.ridewise.service;

import java.util.List;

import com.airtribe.ridewise.entity.Driver;
import com.airtribe.ridewise.entity.FareReceipt;
import com.airtribe.ridewise.entity.Ride;
import com.airtribe.ridewise.entity.RideStatus;
import com.airtribe.ridewise.entity.Rider;
import com.airtribe.ridewise.repository.FareReceiptRepository;
import com.airtribe.ridewise.repository.RideRepository;
import com.airtribe.ridewise.ridematchingstrategy.RideMatchingStrategy;

class RideService {

    private RiderService riderService;
    private DriverService driverService;
    private RideMatchingStrategy matchingStrategy;
    private FareReceiptService fareService;
    private RideRepository rideRepo;
    private FareReceiptRepository receiptRepo;

    public RideService(RiderService rs, DriverService ds,
                       RideMatchingStrategy rms,
                       FareReceiptService fs,
                       RideRepository rr,
                       FareReceiptRepository fr) {

        this.riderService = rs;
        this.driverService = ds;
        this.matchingStrategy = rms;
        this.fareService = fs;
        this.rideRepo = rr;
        this.receiptRepo = fr;
    }

    public Ride requestRide(String riderId, String[] start, String[] end) {

        Rider rider = riderService.getRiderById(riderId);
        Ride ride = new Ride(start, end, rider);

        Driver driver = assignDriver(start);
        ride.setDriver(driver);

        if (driver != null) {
            driverService.updateAvailability(driver, false);
            ride.setStatus(RideStatus.INPROGRESS);
        }

        rideRepo.save(ride);
        return ride;
    }

    private Driver assignDriver(String[] location) {
        List<Driver> availableDrivers = driverService.listAvailableDrivers();
        return matchingStrategy.findDriver(location, availableDrivers);
    }

    public void completeRide(Ride ride, int distance) {
        ride.setDistance(distance);
        ride.setStatus(RideStatus.COMPLETED);

        FareReceipt receipt = fareService.generateReceipt(ride);
        ride.setReceipt(receipt);
        receiptRepo.save(receipt);

        driverService.updateAvailability(ride.getDriver(), true);
    }

    public List<Ride> viewAllRides() {
        return rideRepo.getAll();
    }
}
