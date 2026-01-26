package com.airtribe.learntrack.entity;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Class representing a student enrollment in a course.
 */
public class Enrollment {
    private Student student;
    private Course course;
    private LocalDate registered_date = LocalDate.now();
    private String status = "ACTIVE";
    private Batch batch;
    private final String enrollment_id;

    // ID generator for enrollment_id
    private static String generateEnrollmentId() {
        return "ENR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Valid status values
    private static final String[] VALID_STATUSES = {"ACTIVE", "COMPLETED", "CANCELLED"};


    // Parameterized constructor
    public Enrollment(Student student, Course course, Batch batch) {
        this.enrollment_id = generateEnrollmentId();
        this.setStudent(student);
        this.setCourse(course);
        this.setBatch(batch);
    }

    // Parameterized constructor
    public Enrollment(Student student, Course course, Batch batch, LocalDate registered_date) {
        this(student, course, batch);
        this.setRegistered_date(registered_date);
    }

    // Getters and Setters with validation

    public String getEnrollment_id() {
        return enrollment_id;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        this.course = course;
    }

    public LocalDate getRegistered_date() {
        return registered_date;
    }

    public void setRegistered_date(LocalDate registered_date) {
        if (registered_date == null) {
            throw new IllegalArgumentException("Registered date cannot be null");
        }
        if (registered_date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Registered date cannot be in the future");
        }
        this.registered_date = registered_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        String upperStatus = status.toUpperCase().trim();
        boolean isValid = false;
        for (String validStatus : VALID_STATUSES) {
            if (validStatus.equals(upperStatus)) {
                isValid = true;
                break;
            }
        }
        if (!isValid) {
            throw new IllegalArgumentException("Invalid status. Must be one of: " + 
                String.join(", ", VALID_STATUSES));
        }
        this.status = upperStatus;
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
                "enrollment_id='" + enrollment_id + '\'' +
                ", student=" + student.getFullName() +
                ", course=" + course.getName() +
                ", batch=" + batch.getName() +
                ", registered_date=" + registered_date +
                ", status='" + status + '\'' +
                '}';
    }
}
