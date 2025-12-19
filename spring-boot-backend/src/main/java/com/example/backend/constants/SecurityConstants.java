package com.example.backend.constants;

/**
 * SecurityConstants - Constants cho Security configuration
 * 
 * Tập trung tất cả security-related constants
 * 
 * Best Practice:
 * - JWT token prefix, header names
 * - Security configuration values
 * - Public endpoints patterns
 */
public final class SecurityConstants {
    
    // Private constructor để prevent instantiation
    private SecurityConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    // JWT Token
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final int BEARER_PREFIX_LENGTH = BEARER_PREFIX.length();
    
    // JWT Claims
    public static final String JWT_SUBJECT_CLAIM = "sub";
    public static final String JWT_ISSUED_AT_CLAIM = "iat";
    public static final String JWT_EXPIRATION_CLAIM = "exp";
    
    // Security Headers
    public static final String CORS_EXPOSED_HEADERS = "Authorization";
    
    // Password validation
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 100;
    
    // Token expiration (in milliseconds)
    public static final long DEFAULT_TOKEN_EXPIRATION = 86400000L; // 24 hours
    public static final long SHORT_TOKEN_EXPIRATION = 3600000L; // 1 hour
    public static final long LONG_TOKEN_EXPIRATION = 604800000L; // 7 days
}

