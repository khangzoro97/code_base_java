package com.example.backend.application.service;

import com.example.backend.application.dto.UserRequest;
import com.example.backend.application.dto.UserResponse;
import com.example.backend.application.mapper.UserMapper;
import com.example.backend.domain.model.User;
import com.example.backend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserService - Application Service Layer
 * 
 * Clean Architecture principles:
 * - Service chứa business logic, không chứa data access logic
 * - Service phụ thuộc vào Repository interface (abstraction), không phụ thuộc vào implementation
 * - Transaction management ở Service layer
 * - Validation được handle ở Controller layer (via @Valid)
 * 
 * SOLID:
 * - Single Responsibility: Chỉ quản lý User business logic
 * - Dependency Inversion: Phụ thuộc vào Repository interface, không phụ thuộc vào implementation
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    /**
     * Tạo user mới
     * 
     * Business rules:
     * - Email phải unique
     * - Tự động set timestamps
     */
    public UserResponse createUser(UserRequest request) {
        log.debug("Creating user with email: {}", request.getEmail());
        
        // Business validation: Check email uniqueness
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }
        
        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);
        
        log.info("User created successfully with ID: {}", savedUser.getId());
        return userMapper.toResponse(savedUser);
    }
    
    /**
     * Lấy user theo ID
     */
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        log.debug("Fetching user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        
        return userMapper.toResponse(user);
    }
    
    /**
     * Lấy tất cả users
     */
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        log.debug("Fetching all users");
        
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Update user
     * 
     * Business rules:
     * - Email có thể thay đổi nhưng phải unique
     * - Tự động update updatedAt timestamp
     */
    public UserResponse updateUser(Long id, UserRequest request) {
        log.debug("Updating user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        
        // Business validation: Check email uniqueness (nếu email thay đổi)
        if (!user.getEmail().equals(request.getEmail()) && 
            userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }
        
        userMapper.updateEntity(user, request);
        User updatedUser = userRepository.save(user);
        
        log.info("User updated successfully with ID: {}", updatedUser.getId());
        return userMapper.toResponse(updatedUser);
    }
    
    /**
     * Xóa user
     */
    public void deleteUser(Long id) {
        log.debug("Deleting user with ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        
        userRepository.deleteById(id);
        log.info("User deleted successfully with ID: {}", id);
    }
}

