package com.airtribe.ridewise.entity;

abstract class Person {
    public String id;
    public String name;

    public String getId() { 
        return id; 
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract String generateId();
}
