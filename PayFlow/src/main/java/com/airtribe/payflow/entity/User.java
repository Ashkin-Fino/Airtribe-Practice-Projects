package com.airtribe.payflow.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "upi_id", unique = true, nullable = false)
    private String upiId;

    @Column(name = "balance", nullable = false)
    private Double balance;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    public User() {
    }

    public User(String name, String upiId, Double balance, String phoneNumber) {
        this.name = name;
        this.upiId = upiId;
        this.balance = balance;
        this.phoneNumber = phoneNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String toString() {
        return "User {userId=" + userId + 
            ", name=" + name + 
            ", upiId=" + upiId + 
            ", balance=" + balance + 
            ", phoneNumber=" + phoneNumber + "}";
    }
}