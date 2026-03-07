package com.airtribe.librarymanagementsystem.entity;

public class Patron {

    private String id;
    private String name;
    private String address;
    private String email;
    private int age;

    private int borrowedBooksCount;

    public Patron(String id, String name, String address,
                  String email, int age) {

        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.age = age;
        this.borrowedBooksCount = 0;
    }

    public boolean canBorrow() {
        return borrowedBooksCount < 5;
    }

    public void incrementBorrowedBooks() {
        borrowedBooksCount++;
    }

    public void decrementBorrowedBooks() {
        borrowedBooksCount--;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public int getBorrowedBooksCount() {
        return borrowedBooksCount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
