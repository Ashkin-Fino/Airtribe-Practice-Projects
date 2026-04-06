package com.airtribe.ridewise.farestrategy;

import com.airtribe.ridewise.entity.FareReceipt;
import com.airtribe.ridewise.entity.Ride;

class PeakHourFareStrategy implements FareStrategy {

    @Override
    public FareReceipt calculateFare(Ride ride) {
        int amount = ride.getDistance() * 15;
        return new FareReceipt(amount);
    }
}
