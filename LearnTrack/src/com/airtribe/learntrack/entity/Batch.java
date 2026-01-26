package com.airtribe.learntrack.entity;

import com.airtribe.learntrack.constants.EnrollmentMethod;
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
    private LocalDate start_date;
    private LocalDate end_date;
    private String status = "UPCOMING";
    private EnrollmentMethod enrollment_type;
    private final Course course;

    // ID generator for batch id
    private static String generateBatchId() {
        return "BAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Valid status values
    private static final String[] VALID_STATUSES = {"UPCOMING", "ONGOING", "COMPLETED", "CANCELLED"};

    // Parameterized constructor with course.
    public Batch(Course course, String name) {
        this.id = generateBatchId();
        this.course = course;
        this.setName(name);
    }

    // Parameterized constructor with name, start date and course.
    public Batch(String name, LocalDate start_date, Course course) {
        this(course, name);
        this.setStart_date(start_date);

        // Calculate end_date based on course duration
        LocalDate calculatedEndDate = null;
        if (start_date != null && course != null && course.getDuration() != null) {
            calculatedEndDate = calculateEndDate(start_date, course.getDuration());
            this.setEnd_date(calculatedEndDate);
        }

        // Set status based on start_date and calculatedEndDate relative to current date
        LocalDate currentDate = LocalDate.now();
        if (start_date.isBefore(currentDate) && calculatedEndDate.isBefore(currentDate)) {
            this.setStatus("COMPLETED");
        } else if (start_date.isBefore(currentDate) || start_date.isEqual(currentDate)) {
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

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        if (start_date == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        } else if (start_date.isAfter(this.end_date)) {
            throw new IllegalArgumentException("Course already ended");
        } else if (start_date.isAfter(this.start_date)) {
            throw new IllegalArgumentException("Course already started");
        }
        this.start_date = start_date;
        this.setEnd_date(calculateEndDate(start_date, course.getDuration()));
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    private void setEnd_date(LocalDate end_date) {
        if (end_date == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        this.end_date = end_date;
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

    public Course getCourse() {
        return course;
    }

    public EnrollmentMethod getEnrollment_type() {
        return enrollment_type;
    }

    public void setEnrollment_type(String enrollment_type) {
        EnrollmentMethod enrollmentMethod = EnrollmentMethod.fromString(enrollment_type);
        this.enrollment_type = enrollmentMethod;
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

    @Override
    public String toString() {
        return "Batch{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", status='" + status + '\'' +
                ", course=" + course.getName() +
                '}';
    }
}
