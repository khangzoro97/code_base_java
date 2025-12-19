package com.example.backend.constants;

/**
 * DomainConstants - Constants cho Domain layer (Business Logic)
 * 
 * Chứa các constants liên quan đến business logic và domain rules
 * 
 * Best Practice:
 * - Business rules, validation rules
 * - Entity constraints (min/max length, patterns)
 * - Domain-specific values
 * - Default values cho domain entities
 */
public final class DomainConstants {
    
    // Private constructor để prevent instantiation
    private DomainConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    // User entity constraints
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 100;
    public static final int MAX_EMAIL_LENGTH = 255;
    public static final int MAX_BIO_LENGTH = 500;
    
    // Email validation pattern (basic)
    public static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    
    // Default values
    public static final String DEFAULT_USER_ROLE = "USER";
    
    // Pagination defaults
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final int DEFAULT_PAGE_NUMBER = 0;
}

