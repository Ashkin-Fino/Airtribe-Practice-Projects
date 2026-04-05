package com.airtribe.librarymanagementsystem.entity;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Queue;

import com.airtribe.librarymanagementsystem.observer.PatronObserver;

public class Book {

    private final String name;
    private final String isbn;
    private final int publishedYear;
    private final String author;
    private boolean isAvailable;
    private int totalStock;
    private Queue<PatronObserver> reservationQueue;

    public Book(String name, String isbn, int publishedYear, String author) {
        /*
        Parameterized constructor with name, isbn, author and publishedYear.
        */
        this.name = name;
        this.isbn = isbn;
        this.author = author;

        if (publishedYear <= LocalDate.now().getYear()) {
            this.publishedYear = publishedYear;
        } else {
            throw new IllegalArgumentException("Invalid Year");
        }
        this.isAvailable = false;
        this.totalStock = 0;
        this.reservationQueue = new LinkedList<>();
    }

    // Getters and Setters

    public void addStock(int count) {
        this.totalStock = totalStock + count;
    }

    public void removeStock(int count) throws IllegalArgumentException {
        if (count < this.totalStock) {
            this.totalStock = totalStock - count;
        } else {
            throw new IllegalArgumentException("Stock not present. Cannot remove.");
        }
    }

    public int getTotalStock(){
        return this.totalStock;
    }

    public void addReservation(PatronObserver observer) {
        reservationQueue.offer(observer);
    }

    public PatronObserver getNextReservation() {
        return reservationQueue.poll();
    }

    public boolean hasReservations() {
        return !reservationQueue.isEmpty();
    }

    public Queue<PatronObserver> getReservationQueue() {
        return reservationQueue;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public String getName() {
        return name;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public String getAuthor() {
        return author;
    }
}
