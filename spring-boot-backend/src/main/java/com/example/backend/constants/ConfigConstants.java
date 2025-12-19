package com.example.backend.constants;

/**
 * ConfigConstants - Constants cho Configuration
 * 
 * Chứa các constants liên quan đến configuration keys, property names
 * 
 * Best Practice:
 * - Property keys từ application.yml
 * - Configuration property names
 * - Environment variable names
 */
public final class ConfigConstants {
    
    // Private constructor để prevent instantiation
    private ConfigConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    // JWT Configuration keys
    public static final String JWT_SECRET_KEY = "jwt.secret";
    public static final String JWT_EXPIRATION_KEY = "jwt.expiration";
    
    // Database Configuration keys
    public static final String DB_URL_KEY = "spring.datasource.url";
    public static final String DB_USERNAME_KEY = "spring.datasource.username";
    public static final String DB_PASSWORD_KEY = "spring.datasource.password";
    
    // Server Configuration keys
    public static final String SERVER_PORT_KEY = "server.port";
    
    // Logging Configuration keys
    public static final String LOG_LEVEL_ROOT_KEY = "logging.level.root";
    public static final String LOG_LEVEL_APP_KEY = "logging.level.com.example.backend";
    
    // SpringDoc Configuration keys
    public static final String SWAGGER_ENABLED_KEY = "springdoc.swagger-ui.enabled";
    
    // Default values
    public static final int DEFAULT_SERVER_PORT = 8080;
    public static final String DEFAULT_PROFILE = "dev";
}

