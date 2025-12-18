package com.example.backend.domain.repository;

import com.example.backend.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User Repository - Domain Repository Interface
 * 
 * Repository pattern: Tách biệt data access logic khỏi business logic
 * Spring Data JPA tự động implement interface này
 * 
 * Nguyên tắc:
 * - Repository interface thuộc Domain Layer
 * - Implementation (Spring Data JPA) thuộc Infrastructure Layer
 * - Service layer chỉ phụ thuộc vào interface, không phụ thuộc vào implementation
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Tìm user theo email
     * Spring Data JPA tự động generate query: SELECT * FROM users WHERE email = ?
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Kiểm tra email đã tồn tại chưa
     */
    boolean existsByEmail(String email);
}

