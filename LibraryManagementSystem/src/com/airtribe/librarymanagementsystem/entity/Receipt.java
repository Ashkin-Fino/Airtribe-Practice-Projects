package com.airtribe.librarymanagementsystem.entity;

import java.time.LocalDate;

public class Receipt {

    private InventoryBook book;
    private LocalDate lendDate;
    private LocalDate dueDate;
    private Patron patron;

    public Receipt(InventoryBook book,
                   LocalDate lendDate,
                   LocalDate dueDate,
                   Patron patron) {

        this.book = book;
        this.lendDate = lendDate;
        this.dueDate = dueDate;
        this.patron = patron;
    }

    public InventoryBook getBook() {
        return book;
    }

    public LocalDate getLendDate() {
        return lendDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Patron getPatron() {
        return patron;
    }

    public void setBook(InventoryBook book) {
        this.book = book;
    }

    public void setLendDate(LocalDate lendDate) {
        this.lendDate = lendDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setPatron(Patron patron) {
        this.patron = patron;
    }
}
