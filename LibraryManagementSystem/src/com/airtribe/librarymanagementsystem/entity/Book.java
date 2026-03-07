package com.airtribe.librarymanagementsystem.entity;

import java.util.LinkedList;
import java.util.Queue;

import com.airtribe.librarymanagementsystem.observer.PatronObserver;

public class Book {

    private String name;
    private String isbn;
    private int publishedYear;
    private String author;
    private boolean isAvailable;

    private Queue<PatronObserver> reservationQueue;

    public Book(String name, String isbn, int publishedYear, String author) {
        this.name = name;
        this.isbn = isbn;
        this.publishedYear = publishedYear;
        this.author = author;
        this.isAvailable = false;
        this.reservationQueue = new LinkedList<>();
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

    public void setName(String name) {
        this.name = name;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
