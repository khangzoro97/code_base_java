package com.example.backend.application.service;

import com.example.backend.application.dto.UserRequest;
import com.example.backend.application.mapper.UserMapper;
import com.example.backend.domain.model.User;
import com.example.backend.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Test cho UserService
 * 
 * Test strategy:
 * - Test business logic trong Service
 * - Mock dependencies (Repository, Mapper)
 * - Test cả success và failure cases
 * - Test edge cases
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private UserMapper userMapper;
    
    @InjectMocks
    private UserService userService;
    
    private UserRequest userRequest;
    private User user;
    
    @BeforeEach
    void setUp() {
        userRequest = UserRequest.builder()
                .name("John Doe")
                .email("john@example.com")
                .bio("Software Engineer")
                .build();
        
        user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .bio("Software Engineer")
                .build();
    }
    
    @Test
    @DisplayName("Should create user successfully")
    void testCreateUser_Success() {
        // Given
        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(false);
        when(userMapper.toEntity(userRequest)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        // When
        userService.createUser(userRequest);
        
        // Then
        verify(userRepository).existsByEmail(userRequest.getEmail());
        verify(userMapper).toEntity(userRequest);
        verify(userRepository).save(user);
    }
    
    @Test
    @DisplayName("Should throw exception when email already exists")
    void testCreateUser_EmailExists() {
        // Given
        when(userRepository.existsByEmail(userRequest.getEmail())).thenReturn(true);
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(userRequest)
        );
        
        assertEquals("Email already exists: " + userRequest.getEmail(), exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    @DisplayName("Should get user by ID successfully")
    void testGetUserById_Success() {
        // Given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        
        // When
        userService.getUserById(userId);
        
        // Then
        verify(userRepository).findById(userId);
    }
    
    @Test
    @DisplayName("Should throw exception when user not found")
    void testGetUserById_NotFound() {
        // Given
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.getUserById(userId)
        );
        
        assertEquals("User not found with ID: " + userId, exception.getMessage());
    }
}

