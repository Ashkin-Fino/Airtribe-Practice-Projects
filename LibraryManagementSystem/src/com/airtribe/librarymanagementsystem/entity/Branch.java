package com.airtribe.librarymanagementsystem.entity;

import java.util.ArrayList;
import java.util.List;

public class Branch {

    private String name;
    private final String id;
    private String address;
    private List<InventoryBook> stock;

    public Branch(String name, String address) {
        /*
        Parameterized constructor with name and address.
        */
        this.name = name;
        this.id = generateId();
        this.address = address;
        this.stock = new ArrayList<>();
    }
    
    private String generateId() {
        /*
        Generates id using UUID.
        */
        return "BRANCH_" + java.util.UUID.randomUUID();
    }

    // Getters and Setters
    
    public void restockBook(InventoryBook book) {
        stock.add(book);
    }

    public void discardBook(InventoryBook book) {
        stock.remove(book);
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public List<InventoryBook> getStock() {
        return new ArrayList<>(stock);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Branch Name: " + name + ", Branch Id: " + id + ", Address: " + address;
    }
}
