package com.airtribe.librarymanagementsystem.entity;

import java.util.ArrayList;
import java.util.List;

public class Branch {

    private String name;
    private String id;
    private String address;
    private List<InventoryBook> stock;

    public Branch(String name, String id, String address) {

        this.name = name;
        this.id = id;
        this.address = address;
        this.stock = new ArrayList<>();
    }

    public void restockBook(InventoryBook book) {
        stock.add(book);
    }

    public void removeBook(InventoryBook book) {
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
        return new ArrayList<>(stock); // defensive copy
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
