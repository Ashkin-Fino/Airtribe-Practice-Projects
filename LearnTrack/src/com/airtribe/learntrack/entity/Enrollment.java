package com.airtribe.learntrack.entity;

import java.time.LocalDate;
import java.util.UUID;

import com.airtribe.learntrack.constants.EnrollmentStatus;

/**
 * Class representing a student enrollment in a course.
 */
public class Enrollment {
    private Student student;
    private LocalDate registeredDate = LocalDate.now();
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;
    private Batch batch;
    private final String enrollmentId;

    // ID generator for enrollment_id
    private static String generateEnrollmentId() {
        return "ENR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Parameterized constructor
    public Enrollment(Student student, Batch batch) {
        this.enrollmentId = generateEnrollmentId();
        this.setStudent(student);
        this.setBatch(batch);
    }

    // Parameterized constructor
    public Enrollment(Student student, Batch batch, LocalDate registeredDate) {
        this(student, batch);
        this.setRegisteredDate(registeredDate);
    }

    // Getters and Setters with validation

    public String getEnrollmentId() {
        return enrollmentId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        this.student = student;
    }

    public LocalDate getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(LocalDate registeredDate) {
        if (registeredDate == null) {
            throw new IllegalArgumentException("Registered date cannot be null");
        }
        if (registeredDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Registered date cannot be in the future");
        }
        this.registeredDate = registeredDate;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = EnrollmentStatus.fromString(status);
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        if (batch == null) {
            throw new IllegalArgumentException("Batch cannot be null");
        }
        this.batch = batch;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "enrollment_id='" + enrollmentId + '\'' +
                ", student=" + student.getFullName() +
                ", batch=" + batch.getName() +
                ", registered_date=" + registeredDate +
                ", status='" + status + '\'' +
                '}';
    }
}
