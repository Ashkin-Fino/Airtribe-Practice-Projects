package com.airtribe.learntrack.entity;

import com.airtribe.learntrack.constants.BatchStatus;
import java.time.LocalDate;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class representing a batch in the learning management system.
 */
public class Batch {
    private String name;
    private final String id;
    private LocalDate startDate;
    private LocalDate endDate;
    private BatchStatus status = BatchStatus.UPCOMING;
    private final Course course;
    private Trainer trainer;


    // ID generator for batch id
    private static String generateBatchId() {
        return "BAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Parameterized constructor with course and name.
    public Batch(Course course, String name) {
        this.id = generateBatchId();
        this.course = course;
        this.setName(name);
    }

    // Parameterized constructor with name, start date and course.
    public Batch(String name, LocalDate startDate, Course course) {
        this(course, name);
        this.setStartDate(startDate);

        // Calculate end_date based on course duration
        LocalDate calculatedEndDate = null;
        if (startDate != null && course != null && course.getDuration() != null) {
            calculatedEndDate = calculateEndDate(startDate, course.getDuration());
            this.setEndDate(calculatedEndDate);
        }

        // Set status based on start_date and calculatedEndDate relative to current date
        LocalDate currentDate = LocalDate.now();
        if (startDate.isBefore(currentDate) && calculatedEndDate.isBefore(currentDate)) {
            this.setStatus("COMPLETED");
        } else if (startDate.isBefore(currentDate) || startDate.isEqual(currentDate)) {
            this.setStatus("ONGOING");
        } else {
            this.setStatus("UPCOMING");
        }
    }

    // Getters and Setters with validation

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Batch name cannot be null or empty");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Batch name cannot exceed 100 characters");
        }
        this.name = name.trim();
    }

    public String getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        } else if (startDate.isAfter(this.endDate)) {
            throw new IllegalArgumentException("Course already ended");
        } else if (startDate.isAfter(this.startDate)) {
            throw new IllegalArgumentException("Course already started");
        }
        this.startDate = startDate;
        this.setEndDate(calculateEndDate(startDate, course.getDuration()));
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    private void setEndDate(LocalDate endDate) {
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        this.endDate = endDate;
    }

    public BatchStatus getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = BatchStatus.fromString(status);
    }

    public Course getCourse() {
        return course;
    }

    /**
     * Helper method to calculate end date based on start date and duration string.
     * Parses duration string (e.g., "2 weeks", "5 days", "2 months", "1 year")
     * and adds it to the start date.
     */
    private LocalDate calculateEndDate(LocalDate startDate, String duration) {
        if (startDate == null || duration == null || duration.trim().isEmpty()) {
            throw new IllegalArgumentException("Start date and duration cannot be null or empty");
        }

        // Pattern to match: number followed by space and time unit
        Pattern pattern = Pattern.compile("^(\\d+)\\s+(day|days|week|weeks|month|months|year|years)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(duration.trim());

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid duration format: " + duration);
        }

        int amount = Integer.parseInt(matcher.group(1));
        String unit = matcher.group(2).toLowerCase();

        LocalDate endDate;
        switch (unit) {
            case "day":
            case "days":
                endDate = startDate.plusDays(amount);
                break;
            case "week":
            case "weeks":
                endDate = startDate.plusWeeks(amount);
                break;
            case "month":
            case "months":
                endDate = startDate.plusMonths(amount);
                break;
            case "year":
            case "years":
                endDate = startDate.plusYears(amount);
                break;
            default:
                throw new IllegalArgumentException("Unsupported duration unit: " + unit);
        }

        return endDate;
    }
    
    public Trainer getTrainer() { 
        return trainer; 
    }

    public void setTrainer(Trainer trainer) { 
        this.trainer = trainer; 
    }

    @Override
    public String toString() {
        return "Batch{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", start_date=" + startDate +
                ", end_date=" + endDate +
                ", status='" + status + '\'' +
                ", course=" + course.getName() +
                ", trainer=" + (trainer != null ? trainer.getFirstName() + " " + trainer.getLastName() : "Not Assigned") +
                '}';
    }
}
