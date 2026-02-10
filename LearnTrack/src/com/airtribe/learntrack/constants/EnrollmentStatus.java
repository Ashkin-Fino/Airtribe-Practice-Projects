package com.airtribe.learntrack.constants;

public enum EnrollmentStatus {
    ACTIVE,
    COMPLETED,
    CANCELLED;

    /**
     * Converts a string to an EnrollmentStatus enum value.
     * Case-insensitive matching.
     * 
     * @param status the string to convert
     * @return the corresponding EnrollmentStatus enum value
     * @throws IllegalArgumentException if the string is not a valid enrollment status
     */
    public static EnrollmentStatus fromString(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Enrollment status cannot be null or empty");
        }
        try {
            return valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid enrollment status. Must be one of: " + 
            getValidStatusesString());
        }
    }

    /**
     * Returns a comma-separated string of all valid Enrollment Statuses.
     * 
     * @return string representation of all valid statuses
     */
    public static String getValidStatusesString() {
        StringBuilder sb = new StringBuilder();
        EnrollmentStatus[] values = values();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(values[i].name());
        }
        return sb.toString();
    }
}
