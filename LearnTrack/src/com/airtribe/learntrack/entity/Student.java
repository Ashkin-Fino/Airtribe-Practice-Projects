package com.airtribe.learntrack.entity;

import java.util.UUID;

/**
 * Class representing a student in the learning management system.
 * Extends Person class.
 */
public class Student extends Person {
    
    private boolean active;

    // ID generator for student_id
    private static String generateStudentId() {
        return "STU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Parameterized constructor with name, email and age
    public Student(String firstName, String lastName, String email, int age) {
        super(generateStudentId(), firstName, lastName, email, age);
        this.active = true;
    }

    // Getters and Setters with validation

    public boolean is_active() {
        return active;
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
    }

    @Override
    public String toString() {
        return "Student{" +
                "is_active=" + this.active +
                ", " + super.toString() +
                '}';
    }
}
