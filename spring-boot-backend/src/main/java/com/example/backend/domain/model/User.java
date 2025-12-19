package com.example.backend.domain.model;

import com.example.backend.constants.ApplicationConstants;
import com.example.backend.constants.DomainConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * User Entity - Domain Model
 * 
 * Nguyên tắc Clean Architecture:
 * - Entity này thuộc Domain Layer, không phụ thuộc vào framework
 * - Chỉ chứa business logic và data validation
 * - JPA annotations là cần thiết cho persistence, nhưng domain logic vẫn độc lập
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = ApplicationConstants.VALIDATION_NAME_REQUIRED)
    @Size(min = DomainConstants.MIN_NAME_LENGTH, max = DomainConstants.MAX_NAME_LENGTH, 
          message = ApplicationConstants.VALIDATION_NAME_REQUIRED)
    @Column(nullable = false, length = DomainConstants.MAX_NAME_LENGTH)
    private String name;

    @NotBlank(message = ApplicationConstants.VALIDATION_EMAIL_REQUIRED)
    @Email(message = ApplicationConstants.VALIDATION_EMAIL_INVALID)
    @Column(nullable = false, unique = true, length = DomainConstants.MAX_EMAIL_LENGTH)
    private String email;

    @Size(max = DomainConstants.MAX_BIO_LENGTH)
    @Column(length = DomainConstants.MAX_BIO_LENGTH)
    private String bio;

    @NotBlank(message = ApplicationConstants.VALIDATION_PASSWORD_REQUIRED)
    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Role role = Role.USER;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Lifecycle callback - tự động set createdAt trước khi persist
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Lifecycle callback - tự động update updatedAt trước khi update
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

