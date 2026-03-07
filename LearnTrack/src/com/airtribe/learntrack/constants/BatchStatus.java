package com.airtribe.learntrack.constants;

public enum BatchStatus {
    UPCOMING,
    ONGOING,
    COMPLETED,
    CANCELLED;

    /**
     * Converts a string to an BatchStatus enum value.
     * Case-insensitive matching.
     * 
     * @param status the string to convert
     * @return the corresponding BatchStatus enum value
     * @throws IllegalArgumentException if the string is not a valid batch status
     */
    public static BatchStatus fromString(String status) {
        if (status == null ) {
            throw new IllegalArgumentException("Batch status cannot be null.");
        }
        if (status.trim().isEmpty()) {
            throw new IllegalArgumentException("Batch status cannot be empty.");
        }
        try {
            return valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Batch status: '" + status + "'. Must be one of: " + 
            getValidStatusesString());
        }
    }

    /**
     * Returns a comma-separated string of all valid Batch Statuses.
     * 
     * @return string representation of all valid statuses
     */
    public static String getValidStatusesString() {
        StringBuilder sb = new StringBuilder();
        BatchStatus[] values = BatchStatus.values();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(values[i].name());
        }
        return sb.toString();
    }
}

