package com.airtribe.parkinglot.service.FareCaluclationStrategy;

import com.airtribe.parkinglot.entity.Ticket;

public interface FareCalculationStrategy {
    double calculateFare(Ticket ticket);
}
