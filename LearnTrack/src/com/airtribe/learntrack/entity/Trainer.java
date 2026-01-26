package com.airtribe.learntrack.entity;

import java.util.UUID;

/**
 * Class representing a trainer in the learning management system.
 * Extends Person class.
 */
public class Trainer extends Person {
    private final String trainer_id;
    private boolean is_active;
    private String specialisation;

    // ID generator for trainer_id
    private static String generateTrainerId() {
        return "TRN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Parameterized constructor
    public Trainer(String first_name, String last_name, String email, int age, String specialisation) {
        super(first_name, last_name, email, age);
        this.trainer_id = generateTrainerId();
        this.setSpecialisation(specialisation);
        this.is_active = true;
    }

    // Getters and Setters with validation

    public String getTrainer_id() {
        return trainer_id;
    }

    public boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
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
                "trainer_id='" + trainer_id + '\'' +
                ", is_active=" + is_active +
                ", specialisation='" + specialisation + '\'' +
                ", " + super.toString() +
                '}';
    }
}
