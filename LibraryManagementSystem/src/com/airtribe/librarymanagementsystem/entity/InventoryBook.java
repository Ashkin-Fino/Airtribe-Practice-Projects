package com.airtribe.librarymanagementsystem.entity;

import com.airtribe.librarymanagementsystem.enums.InventoryBookStatus;

public class InventoryBook {

    private final String id;
    private final Book book;
    private String location;
    private InventoryBookStatus status;

    public InventoryBook(Book book) {
        /*
        Parameterized constructor with Book only.
        */
        this.id = generateId(book);
        this.book = book;
        this.status = InventoryBookStatus.AVAILABLE;
    }

    public InventoryBook(Book book, String location) {
        /*
        Parameterized constructor with Book and location.
        */
        this.id = generateId(book);
        this.book = book;
        this.location = location;
        this.status = InventoryBookStatus.AVAILABLE;
    }

    private String generateId(Book book) {
        /*
        Generates an ID using isbn of book and a counter.
        */
        return "STOCK_" + book.getIsbn() + "_" + book.getTotalStock();
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public String getLocation() {
        return location;
    }

    public InventoryBookStatus getStatus() {
        return status;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStatus(InventoryBookStatus status) {
        this.status = status;
    }
}
