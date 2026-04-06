package com.airtribe.ridewise.farestrategy;

import com.airtribe.ridewise.entity.Ride;
import com.airtribe.ridewise.entity.FareReceipt;

public interface FareStrategy {
    FareReceipt calculateFare(Ride ride);
}
