package com.example.backend.application.mapper;

import com.example.backend.application.dto.UserRequest;
import com.example.backend.application.dto.UserResponse;
import com.example.backend.domain.model.User;
import org.springframework.stereotype.Component;

/**
 * UserMapper - Map giữa Entity và DTO
 * 
 * Tách mapping logic ra khỏi Service để:
 * - Service tập trung vào business logic
 * - Dễ test và maintain
 * - Có thể thay thế bằng MapStruct nếu cần performance tốt hơn
 */
@Component
public class UserMapper {
    
    /**
     * Map UserRequest -> User Entity
     */
    public User toEntity(UserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .bio(request.getBio())
                .build();
    }
    
    /**
     * Map User Entity -> UserResponse
     */
    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .bio(user.getBio())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
    
    /**
     * Update existing User entity với data từ UserRequest
     */
    public void updateEntity(User user, UserRequest request) {
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setBio(request.getBio());
    }
}

