package com.airtribe.ridewise.entity;

import java.util.UUID;

public class Rider extends Person {

    public Rider(String name) {
        this.id = generateId();
        this.name = name;
    }

    @Override
    public String generateId() {
        return "R-" + UUID.randomUUID();
    }
}
