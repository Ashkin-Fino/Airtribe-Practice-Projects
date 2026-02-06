package com.airtribe.learntrack.entity;

/**
 * Base class representing a person in the learning management system.
 */
public abstract class Person {

    private final String id;
    private String firstName;
    private String lastName;
    private String email;
    private int age;

    // Parameterized constructor
    public Person(String id, String firstName, String lastName, String email, int age) {
        this.id = id;
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        this.setAge(age);
    }

    // Getters and Setters with validation

    public String getId() {
        return id;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (firstName.length() > 50) {
            throw new IllegalArgumentException("First name cannot exceed 50 characters");
        }
        // Check for numbers and special characters (only allow letters, spaces, hyphens, and apostrophes)
        if (!firstName.matches("^[a-zA-Z\\s'-]+$")) {
            throw new IllegalArgumentException("First name cannot contain numbers or special characters (only letters, spaces, hyphens, and apostrophes are allowed)");
        }
        this.firstName = firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        if (lastName.length() > 50) {
            throw new IllegalArgumentException("Last name cannot exceed 50 characters");
        }
        // Check for numbers and special characters (only allow letters, spaces, hyphens, and apostrophes)
        if (!lastName.matches("^[a-zA-Z\\s'-]+$")) {
            throw new IllegalArgumentException("Last name cannot contain numbers or special characters (only letters, spaces, hyphens, and apostrophes are allowed)");
        }
        this.lastName = lastName.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        // Basic email validation
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email.trim().toLowerCase();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
        if (age > 150) {
            throw new IllegalArgumentException("Age cannot exceed 150");
        }
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "first_name='" + firstName + '\'' +
                ", last_name='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
