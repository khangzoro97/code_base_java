package com.example.backend.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserRequest DTO - Data Transfer Object cho Create/Update operations
 * 
 * Tách biệt DTO khỏi Entity để:
 * - Bảo vệ domain model khỏi external changes
 * - Cho phép validation khác nhau giữa create và update
 * - Tránh expose internal fields không cần thiết
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;
}

