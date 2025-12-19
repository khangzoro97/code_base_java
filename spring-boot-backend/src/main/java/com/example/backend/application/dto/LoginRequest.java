package com.example.backend.application.dto;

import com.example.backend.constants.ApplicationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoginRequest DTO - Dùng cho user authentication
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    
    @NotBlank(message = ApplicationConstants.VALIDATION_EMAIL_REQUIRED)
    @Email(message = ApplicationConstants.VALIDATION_EMAIL_INVALID)
    private String email;
    
    @NotBlank(message = ApplicationConstants.VALIDATION_PASSWORD_REQUIRED)
    private String password;
}

