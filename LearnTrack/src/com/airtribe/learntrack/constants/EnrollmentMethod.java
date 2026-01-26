package com.airtribe.learntrack.constants;

/**
 * Enum representing valid enrollment methods in the learning management system.
 */
public enum EnrollmentMethod {
    ONLINE,
    OFFLINE;

    /**
     * Converts a string to an EnrollmentMethod enum value.
     * Case-insensitive matching.
     * 
     * @param method the string to convert
     * @return the corresponding EnrollmentMethod enum value
     * @throws IllegalArgumentException if the string is not a valid enrollment method
     */
    public static EnrollmentMethod fromString(String method) {
        if (method == null || method.trim().isEmpty()) {
            throw new IllegalArgumentException("Enrollment method cannot be null or empty");
        }
        try {
            return valueOf(method.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid enrollment method. Must be one of: " + 
                getValidMethodsString());
        }
    }

    /**
     * Checks if a string represents a valid enrollment method.
     * 
     * @param method the string to check
     * @return true if the string is a valid enrollment method, false otherwise
     */
    public static boolean isValid(String method) {
        if (method == null || method.trim().isEmpty()) {
            return false;
        }
        try {
            valueOf(method.trim().toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Returns a comma-separated string of all valid enrollment methods.
     * 
     * @return string representation of all valid methods
     */
    public static String getValidMethodsString() {
        StringBuilder sb = new StringBuilder();
        EnrollmentMethod[] values = values();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(values[i].name());
        }
        return sb.toString();
    }
}
