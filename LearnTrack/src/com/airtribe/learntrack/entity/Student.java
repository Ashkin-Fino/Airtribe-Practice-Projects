package com.airtribe.learntrack.entity;

import java.util.UUID;

/**
 * Class representing a student in the learning management system.
 * Extends Person class.
 */
public class Student extends Person {
    private final String student_id;
    private boolean is_active;

    // ID generator for student_id
    private static String generateStudentId() {
        return "STU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Parameterized constructor with name, email and age
    public Student(String first_name, String last_name, String email, int age) {
        super(first_name, last_name, email, age);
        this.student_id = generateStudentId();
        this.is_active = true;
    }

    // Getters and Setters with validation
    public String getStudent_id() {
        return student_id;
    }

    public boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    @Override
    public String toString() {
        return "Student{" +
                "student_id='" + student_id + '\'' +
                ", is_active=" + is_active +
                ", " + super.toString() +
                '}';
    }
}
