package com.airtribe.librarymanagementsystem.entity;

public class Patron {

    private final String id;
    private String name;
    private String address;
    private String email;
    private int age;
    private int borrowedBooksCount;

    public Patron(String name) {
        /*
        Parameterized constructor with name only.
        */
        this.id = generateUUID();
        this.name = name;
        this.borrowedBooksCount = 0;
    }

    public Patron(String name, String address, String email, int age) {
        /*
        Parameterized constructor with name, address, email and age.
        */
        this.id = generateUUID();
        this.name = name;
        this.address = address;
        this.email = email;
        this.age = age;
        this.borrowedBooksCount = 0;
    }

    private String generateUUID() {
        /*
        Generates unique ID using UUID.
        */
        return "PAT_" + java.util.UUID.randomUUID().toString();
    }

    
    public boolean canBorrow() {
        return borrowedBooksCount < 5;
    }

    // Getters and Setters
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

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(int age) throws IllegalArgumentException {
        if (age >= 8) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Minimum age for patron is 8");
        }
    }

    public void setBorrowedBooksCount(int count) {
        this.borrowedBooksCount = count;
    }

    @Override
    public String toString() {
        return "Patron{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", borrowedBooksCount=" + borrowedBooksCount +
                '}';
    }
}
