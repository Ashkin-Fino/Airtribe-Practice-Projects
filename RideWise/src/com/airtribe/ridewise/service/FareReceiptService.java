package com.airtribe.ridewise.service;

import com.airtribe.ridewise.entity.FareReceipt;
import com.airtribe.ridewise.entity.Ride;
import com.airtribe.ridewise.farestrategy.FareStrategy;

class FareReceiptService {
    private FareStrategy strategy;

    public FareReceiptService(FareStrategy strategy) {
        this.strategy = strategy;
    }

    public FareReceipt generateReceipt(Ride ride) {
        return strategy.calculateFare(ride);
    }
}
