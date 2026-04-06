package com.airtribe.ridewise.entity;

import java.time.LocalDate;
import java.util.UUID;

public class FareReceipt {
    private String id;
    private int amount;
    private LocalDate generatedAt;

    public FareReceipt(int amount) {
        this.id = generateId();
        this.amount = amount;
        this.generatedAt = LocalDate.now();
    }

    public String getId() { 
        return id; 
    }

    public int getAmount() { 
        return amount; 
    }

    public LocalDate getGeneratedAt() { 
        return generatedAt; 
    }

    public String generateId() {
        return "F-" + UUID.randomUUID();
    }
}
