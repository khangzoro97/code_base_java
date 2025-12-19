package com.example.backend.constants;

/**
 * ApplicationConstants - Constants cho Application layer
 * 
 * Chứa các constants liên quan đến application logic, DTOs, services
 * 
 * Best Practice:
 * - Service-level constants
 * - DTO validation messages
 * - Application-specific values
 */
public final class ApplicationConstants {
    
    // Private constructor để prevent instantiation
    private ApplicationConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    // Error messages
    public static final String ERROR_USER_NOT_FOUND = "User not found";
    public static final String ERROR_EMAIL_ALREADY_EXISTS = "Email already registered";
    public static final String ERROR_INVALID_CREDENTIALS = "Invalid email or password";
    public static final String ERROR_UNAUTHORIZED = "Unauthorized access";
    public static final String ERROR_FORBIDDEN = "Access forbidden";
    
    // Success messages
    public static final String SUCCESS_REGISTRATION = "Registration successful";
    public static final String SUCCESS_LOGIN = "Login successful";
    public static final String SUCCESS_USER_CREATED = "User created successfully";
    public static final String SUCCESS_USER_UPDATED = "User updated successfully";
    public static final String SUCCESS_USER_DELETED = "User deleted successfully";
    
    // Validation messages
    public static final String VALIDATION_NAME_REQUIRED = "Name is required";
    public static final String VALIDATION_EMAIL_REQUIRED = "Email is required";
    public static final String VALIDATION_EMAIL_INVALID = "Email should be valid";
    public static final String VALIDATION_PASSWORD_REQUIRED = "Password is required";
    public static final String VALIDATION_PASSWORD_TOO_SHORT = "Password must be at least 6 characters";
    
    // Date formats
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
}

