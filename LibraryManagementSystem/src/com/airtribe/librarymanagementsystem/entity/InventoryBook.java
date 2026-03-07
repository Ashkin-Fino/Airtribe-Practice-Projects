package com.airtribe.librarymanagementsystem.entity;

import com.airtribe.librarymanagementsystem.enums.InventoryBookStatus;

public class InventoryBook {

    private String id;
    private Book book;
    private String location;
    private InventoryBookStatus status;

    public InventoryBook(String id, Book book, String location) {

        this.id = id;
        this.book = book;
        this.location = location;
        this.status = InventoryBookStatus.AVAILABLE;
    }

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

    public void setId(String id) {
        this.id = id;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStatus(InventoryBookStatus status) {
        this.status = status;
    }
}
