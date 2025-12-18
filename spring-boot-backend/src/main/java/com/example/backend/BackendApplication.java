package com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application entry point.
 * 
 * Spring Boot sẽ tự động scan các components trong package này và sub-packages.
 * Sử dụng @SpringBootApplication thay vì @Configuration + @EnableAutoConfiguration + @ComponentScan
 * để giảm boilerplate code.
 */
@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}

