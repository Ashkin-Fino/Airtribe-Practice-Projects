package com.airtribe.librarymanagementsystem.entity;

import java.time.LocalDate;

import com.airtribe.librarymanagementsystem.enums.ReservationStatus;

public class Reservation {

    private final String id;
    private final InventoryBook book;
    private final LocalDate lendDate;
    private LocalDate dueDate;
    private final Patron patron;
    private ReservationStatus status;

    public Reservation(InventoryBook book, int durationInDays, Patron patron) {
        /*
        Parameterized constructor using book, duration and patron. 
        */
        this.id = generateId();
        this.book = book;
        this.lendDate = LocalDate.now();
        this.dueDate = LocalDate.now().plusDays(durationInDays);
        this.patron = patron;
        this.status = ReservationStatus.IN_PROGRESS;
    }

    private String generateId() {
        /*
        Generates id using UUID.
        */
        return "RECEIPT_" + java.util.UUID.randomUUID();
    }

    // Getters and Setters

    public String getId() {
        return this.id;
    }

    public InventoryBook getBook() {
        return this.book;
    }

    public LocalDate getLendDate() {
        return this.lendDate;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public Patron getPatron() {
        return this.patron;
    }

    public ReservationStatus getstatus() {
        return this.status;
    }

    public void extendDueDate(int durationInDays) {
        this.dueDate = this.dueDate.plusDays(durationInDays);
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
