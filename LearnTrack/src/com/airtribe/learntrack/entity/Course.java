package com.airtribe.learntrack.entity;

import com.airtribe.learntrack.constants.EnrollmentMethod;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Class representing a course in the learning management system.
 */
public class Course {
    private final String course_id;
    private String name;
    private List<EnrollmentMethod> enrollment_methods = new ArrayList<EnrollmentMethod>(List.of(EnrollmentMethod.fromString("ONLINE")));
    private boolean is_active = true;
    private String duration;
    private String description;
    
    // ID generator for course_id
    private static String generateCourseId() {
        return "CRS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }


    // Parameterized constructor with name and duration
    public Course(String name, String duration) {
        this.course_id = generateCourseId();
        this.setName(name);
        this.setDuration(duration);
    }

    // Parameterized constructor with name, duration and enrollment methods
    public Course(String name, List<String> enrollment_methods, String duration) {
        this(name, duration);
        this.setEnrollment_methods(enrollment_methods);
    }

    // Getters and Setters with validation
    public String getCourse_id() {
        return course_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be null or empty");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Course name cannot exceed 100 characters");
        }
        this.name = name.trim();
    }

    public List<EnrollmentMethod> getEnrollment_methods() {
        return new ArrayList<>(enrollment_methods); // Return a copy to prevent external modification
    }

    public void setEnrollment_methods(List<String> enrollment_methods) {
        if (enrollment_methods == null) {
            throw new IllegalArgumentException("Enrollment methods cannot be null");
        }
        this.enrollment_methods = new ArrayList<>();
        for (String method : enrollment_methods) {
            addEnrollmentMethod(method);
        }
    }

    public void addEnrollmentMethod(String method) {
        EnrollmentMethod enrollmentMethod = EnrollmentMethod.fromString(method);
        if (!this.enrollment_methods.contains(enrollmentMethod)) {
            this.enrollment_methods.add(enrollmentMethod);
        }
    }

    public void removeEnrollmentMethod(String method) {
        if (method != null) {
            this.enrollment_methods.remove(EnrollmentMethod.fromString(method.toUpperCase().trim()));
        }
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        if (duration == null || duration.trim().isEmpty()) {
            throw new IllegalArgumentException("Duration cannot be null or empty");
        }
        String trimmedDuration = duration.trim();
        // Regex pattern to validate duration format: number followed by space and time unit
        // Matches formats like: "2 weeks", "5 days", "2 months", "1 year"
        if (!trimmedDuration.matches("^\\d+\\s+(day|days|week|weeks|month|months|year|years)$")) {
            throw new IllegalArgumentException("Duration must be in format: 'number time_unit' (e.g., '2 weeks', '5 days', '2 months', '1 year')");
        }
        this.duration = trimmedDuration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null && description.length() > 1000) {
            throw new IllegalArgumentException("Description cannot exceed 1000 characters");
        }
        this.description = description != null ? description.trim() : null;
    }

    @Override
    public String toString() {
        return "Course{" +
                "course_id='" + course_id + '\'' +
                ", name='" + name + '\'' +
                ", enrollment_methods=" + enrollment_methods +
                ", is_active=" + is_active +
                '}';
    }
}
