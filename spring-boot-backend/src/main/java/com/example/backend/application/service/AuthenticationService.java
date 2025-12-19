package com.example.backend.application.service;

import com.example.backend.constants.ApplicationConstants;
import com.example.backend.application.dto.AuthResponse;
import com.example.backend.application.dto.LoginRequest;
import com.example.backend.application.dto.RegisterRequest;
import com.example.backend.application.mapper.UserMapper;
import com.example.backend.domain.model.Role;
import com.example.backend.domain.model.User;
import com.example.backend.domain.repository.UserRepository;
import com.example.backend.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AuthenticationService - Service xử lý authentication và authorization
 * 
 * Business logic:
 * - Register new user (hash password, set default role)
 * - Login user (authenticate, generate JWT token)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthenticationService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    
    /**
     * Register new user
     */
    public AuthResponse register(RegisterRequest request) {
        log.debug("Registering new user with email: {}", request.getEmail());
        
        // Check email uniqueness
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(ApplicationConstants.ERROR_EMAIL_ALREADY_EXISTS);
        }
        
        // Create user entity
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Hash password
                .bio(request.getBio())
                .role(Role.USER) // Default role
                .build();
        
        User savedUser = userRepository.save(user);
        
        // Generate JWT token
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
        String jwtToken = jwtService.generateToken(userDetails);
        
        log.info("User registered successfully with ID: {}", savedUser.getId());
        
        return AuthResponse.builder()
                .token(jwtToken)
                .user(userMapper.toResponse(savedUser))
                .build();
    }
    
    /**
     * Login user
     */
    public AuthResponse login(LoginRequest request) {
        log.debug("Login attempt for email: {}", request.getEmail());
        
        // Authenticate user (Spring Security sẽ check password)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        
        // Load user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(ApplicationConstants.ERROR_INVALID_CREDENTIALS));
        
        // Generate JWT token
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String jwtToken = jwtService.generateToken(userDetails);
        
        log.info("User logged in successfully with ID: {}", user.getId());
        
        return AuthResponse.builder()
                .token(jwtToken)
                .user(userMapper.toResponse(user))
                .build();
    }
}

