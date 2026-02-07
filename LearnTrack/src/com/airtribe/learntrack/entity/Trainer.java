package com.airtribe.learntrack.entity;

import java.util.UUID;

/**
 * Class representing a trainer in the learning management system.
 * Extends Person class.
 */
public class Trainer extends Person {
    
    private boolean active;
    private String specialisation;

    // ID generator for trainer_id
    private static String generateTrainerId() {
        return "TRN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Parameterized constructor
    public Trainer(String firstName, String lastName, String email, int age, String specialisation) {
        super(generateTrainerId(), firstName, lastName, email, age);
        this.setSpecialisation(specialisation);
        this.active = true;
    }

    // Getters and Setters with validation

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
    }

    public String getSpecialisation() {
        return specialisation;
    }

    public void setSpecialisation(String specialisation) {
        if (specialisation == null || specialisation.trim().isEmpty()) {
            throw new IllegalArgumentException("Specialisation cannot be null or empty");
        }
        if (specialisation.length() > 100) {
            throw new IllegalArgumentException("Specialisation cannot exceed 100 characters");
        }
        this.specialisation = specialisation.trim();
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "is_active=" + active +
                ", specialisation='" + specialisation + '\'' +
                ", " + super.toString() +
                '}';
    }
}
