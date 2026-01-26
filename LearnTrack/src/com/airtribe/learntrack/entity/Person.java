package com.airtribe.learntrack.entity;

/**
 * Base class representing a person in the learning management system.
 */
public class Person {
    private String first_name;
    private String last_name;
    private String email;
    private int age;

    // Parameterized constructor
    public Person(String first_name, String last_name, String email, int age) {
        this.setFirst_name(first_name);
        this.setLast_name(last_name);
        this.setEmail(email);
        this.setAge(age);
    }

    // Getters and Setters with validation

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        if (first_name == null || first_name.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (first_name.length() > 50) {
            throw new IllegalArgumentException("First name cannot exceed 50 characters");
        }
        // Check for numbers and special characters (only allow letters, spaces, hyphens, and apostrophes)
        if (!first_name.matches("^[a-zA-Z\\s'-]+$")) {
            throw new IllegalArgumentException("First name cannot contain numbers or special characters (only letters, spaces, hyphens, and apostrophes are allowed)");
        }
        this.first_name = first_name.trim();
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        if (last_name == null || last_name.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        if (last_name.length() > 50) {
            throw new IllegalArgumentException("Last name cannot exceed 50 characters");
        }
        // Check for numbers and special characters (only allow letters, spaces, hyphens, and apostrophes)
        if (!last_name.matches("^[a-zA-Z\\s'-]+$")) {
            throw new IllegalArgumentException("Last name cannot contain numbers or special characters (only letters, spaces, hyphens, and apostrophes are allowed)");
        }
        this.last_name = last_name.trim();
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
        return first_name + " " + last_name;
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
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
