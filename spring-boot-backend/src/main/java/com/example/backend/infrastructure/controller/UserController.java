package com.example.backend.infrastructure.controller;

import com.example.backend.application.dto.UserRequest;
import com.example.backend.application.dto.UserResponse;
import com.example.backend.application.service.UserService;
import com.example.backend.constants.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserController - Infrastructure Layer (Presentation Layer)
 * 
 * Clean Architecture:
 * - Controller chỉ handle HTTP concerns (request/response, status codes)
 * - Không chứa business logic
 * - Delegate tất cả business logic xuống Service layer
 * - Validation được handle ở đây (via @Valid)
 * 
 * RESTful API Design:
 * - POST /api/users - Create
 * - GET /api/users/{id} - Read one
 * - GET /api/users - Read all
 * - PUT /api/users/{id} - Update
 * - DELETE /api/users/{id} - Delete
 */
@RestController
@RequestMapping(ApiConstants.USERS_BASE_PATH)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Users", description = "User management API endpoints")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    
    private final UserService userService;
    
    @PostMapping
    @Operation(summary = "Create user", description = "Create a new user (requires authentication)")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        log.info("POST {} - Creating new user", ApiConstants.USERS_BASE_PATH);
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID (requires authentication)")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        log.info("GET {}/{} - Fetching user", ApiConstants.USERS_BASE_PATH, id);
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve all users (requires authentication)")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("GET {} - Fetching all users", ApiConstants.USERS_BASE_PATH);
        List<UserResponse> responses = userService.getAllUsers();
        return ResponseEntity.ok(responses);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update an existing user (requires authentication)")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest request) {
        log.info("PUT {}/{} - Updating user", ApiConstants.USERS_BASE_PATH, id);
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Delete a user by ID (requires authentication)")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("DELETE {}/{} - Deleting user", ApiConstants.USERS_BASE_PATH, id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

