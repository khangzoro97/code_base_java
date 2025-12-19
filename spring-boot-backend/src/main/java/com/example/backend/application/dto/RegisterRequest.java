package com.example.backend.application.dto;

import com.example.backend.constants.ApplicationConstants;
import com.example.backend.constants.DomainConstants;
import com.example.backend.constants.SecurityConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RegisterRequest DTO - Dùng cho user registration
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    
    @NotBlank(message = ApplicationConstants.VALIDATION_NAME_REQUIRED)
    @Size(min = DomainConstants.MIN_NAME_LENGTH, max = DomainConstants.MAX_NAME_LENGTH, 
          message = ApplicationConstants.VALIDATION_NAME_REQUIRED)
    private String name;
    
    @NotBlank(message = ApplicationConstants.VALIDATION_EMAIL_REQUIRED)
    @Email(message = ApplicationConstants.VALIDATION_EMAIL_INVALID)
    private String email;
    
    @NotBlank(message = ApplicationConstants.VALIDATION_PASSWORD_REQUIRED)
    @Size(min = SecurityConstants.MIN_PASSWORD_LENGTH, max = SecurityConstants.MAX_PASSWORD_LENGTH, 
          message = ApplicationConstants.VALIDATION_PASSWORD_TOO_SHORT)
    private String password;
    
    @Size(max = DomainConstants.MAX_BIO_LENGTH)
    private String bio;
}

