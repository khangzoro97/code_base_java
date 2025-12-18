package com.example.backend.infrastructure.controller;

import com.example.backend.application.dto.UserRequest;
import com.example.backend.application.dto.UserResponse;
import com.example.backend.application.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Test cho UserController
 * 
 * Test strategy:
 * - Test HTTP layer (Controller) với MockMvc
 * - Mock Service layer
 * - Test HTTP status codes, response format
 * - Test validation
 */
@WebMvcTest(UserController.class)
@DisplayName("UserController Integration Tests")
class UserControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @DisplayName("Should create user successfully")
    void testCreateUser_Success() throws Exception {
        // Given
        UserRequest request = UserRequest.builder()
                .name("John Doe")
                .email("john@example.com")
                .bio("Software Engineer")
                .build();
        
        UserResponse response = UserResponse.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .bio("Software Engineer")
                .build();
        
        when(userService.createUser(any(UserRequest.class))).thenReturn(response);
        
        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }
    
    @Test
    @DisplayName("Should return 400 when validation fails")
    void testCreateUser_ValidationFailed() throws Exception {
        // Given - Invalid request (empty name)
        UserRequest request = UserRequest.builder()
                .name("")  // Invalid: empty name
                .email("invalid-email")  // Invalid: not a valid email
                .build();
        
        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("Should get user by ID successfully")
    void testGetUserById_Success() throws Exception {
        // Given
        Long userId = 1L;
        UserResponse response = UserResponse.builder()
                .id(userId)
                .name("John Doe")
                .email("john@example.com")
                .build();
        
        when(userService.getUserById(userId)).thenReturn(response);
        
        // When & Then
        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }
}

