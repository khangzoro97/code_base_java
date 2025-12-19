package com.example.backend.constants;

/**
 * ApiConstants - Constants cho API paths
 * 
 * Tập trung tất cả API endpoints để dễ maintain và tránh hardcode
 * 
 * Best Practice:
 * - Tất cả API paths nên được định nghĩa ở đây
 * - Sử dụng trong Controllers và SecurityConfig
 * - Dễ dàng refactor và thay đổi paths
 */
public final class ApiConstants {
    
    // Private constructor để prevent instantiation
    private ApiConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    // Base paths
    public static final String API_BASE_PATH = "/api";
    public static final String API_V1 = API_BASE_PATH + "/v1";
    
    // Auth endpoints
    public static final String AUTH_BASE_PATH = API_BASE_PATH + "/auth";
    public static final String AUTH_REGISTER = AUTH_BASE_PATH + "/register";
    public static final String AUTH_LOGIN = AUTH_BASE_PATH + "/login";
    public static final String AUTH_REFRESH = AUTH_BASE_PATH + "/refresh";
    public static final String AUTH_LOGOUT = AUTH_BASE_PATH + "/logout";
    
    // User endpoints
    public static final String USERS_BASE_PATH = API_BASE_PATH + "/users";
    public static final String USERS_BY_ID = USERS_BASE_PATH + "/{id}";
    
    // Swagger/OpenAPI endpoints
    public static final String SWAGGER_UI_BASE = "/swagger-ui";
    public static final String SWAGGER_UI_HTML = "/swagger-ui.html";
    public static final String SWAGGER_UI_INDEX = SWAGGER_UI_BASE + "/index.html";
    public static final String API_DOCS_BASE = "/v3/api-docs";
    public static final String SWAGGER_RESOURCES = "/swagger-resources/**";
    public static final String WEBJARS = "/webjars/**";
    
    // H2 Console (for development)
    public static final String H2_CONSOLE = "/h2-console/**";
    
    // Health check (if using Actuator)
    public static final String ACTUATOR_HEALTH = "/actuator/health";
    public static final String ACTUATOR_INFO = "/actuator/info";
}

