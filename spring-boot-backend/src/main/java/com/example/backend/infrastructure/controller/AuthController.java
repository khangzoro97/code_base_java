package com.example.backend.infrastructure.controller;

import com.example.backend.application.dto.AuthResponse;
import com.example.backend.application.dto.LoginRequest;
import com.example.backend.application.dto.RegisterRequest;
import com.example.backend.application.service.AuthenticationService;
import com.example.backend.constants.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController - REST Controller cho authentication endpoints
 * 
 * Public endpoints (không cần authentication):
 * - POST /api/auth/register - Register new user
 * - POST /api/auth/login - Login user
 */
@RestController
@RequestMapping(ApiConstants.AUTH_BASE_PATH)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication API endpoints")
public class AuthController {
    
    private final AuthenticationService authenticationService;
    
    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Create a new user account")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("POST {} - Registering new user", ApiConstants.AUTH_REGISTER);
        AuthResponse response = authenticationService.register(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user and get JWT token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("POST {} - User login", ApiConstants.AUTH_LOGIN);
        AuthResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }
}

