# Kiến trúc và Quyết định Thiết kế

## 🏗️ Clean Architecture Overview

Codebase này tuân thủ **Clean Architecture** (hay còn gọi là Hexagonal Architecture), với 3 layers chính:

```
┌─────────────────────────────────────┐
│   Infrastructure Layer             │
│   (Controllers, Exception Handlers)│
└──────────────┬──────────────────────┘
               │ depends on
┌──────────────▼──────────────────────┐
│   Application Layer                 │
│   (Services, DTOs, Mappers)         │
└──────────────┬──────────────────────┘
               │ depends on
┌──────────────▼──────────────────────┐
│   Domain Layer                      │
│   (Entities, Repository Interfaces)│
└─────────────────────────────────────┘
```

### Dependency Rule
- **Inner layers không phụ thuộc outer layers**
- Infrastructure phụ thuộc Application
- Application phụ thuộc Domain
- Domain **KHÔNG** phụ thuộc gì cả (pure business logic)

## 📦 Package Structure

### `domain/` - Domain Layer
**Mục đích:** Chứa business logic thuần, không phụ thuộc framework

- **`model/`**: Entities (JPA annotations là cần thiết cho persistence, nhưng business logic vẫn độc lập)
- **`repository/`**: Repository interfaces (abstractions, không phải implementations)

**Ví dụ:**
```java
// Domain Entity - Business logic thuần
@Entity
public class User {
    // Business validation
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
```

### `application/` - Application Layer
**Mục đích:** Orchestrate use cases, coordinate giữa Domain và Infrastructure

- **`service/`**: Application services (business logic orchestration)
- **`dto/`**: Data Transfer Objects (tách biệt external representation khỏi domain)
- **`mapper/`**: Map giữa Entity và DTO

**Ví dụ:**
```java
// Application Service - Orchestrate business logic
@Service
public class UserService {
    // Business rules: Email must be unique
    if (userRepository.existsByEmail(email)) {
        throw new IllegalArgumentException("Email already exists");
    }
}
```

### `infrastructure/` - Infrastructure Layer
**Mục đích:** Framework concerns, external world interactions

- **`controller/`**: REST Controllers (HTTP concerns only)
- **`exception/`**: Exception handlers (framework-specific error handling)

**Ví dụ:**
```java
// Controller - Chỉ handle HTTP, delegate xuống Service
@RestController
public class UserController {
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }
}
```

## 🎯 Tại sao Clean Architecture?

### 1. **Testability**
- Domain logic có thể test độc lập, không cần Spring context
- Service layer có thể mock Repository dễ dàng
- Controller có thể test với MockMvc

### 2. **Maintainability**
- Thay đổi framework (Spring → Quarkus) chỉ ảnh hưởng Infrastructure layer
- Business logic không bị ảnh hưởng bởi framework changes
- Dễ hiểu flow: Controller → Service → Repository

### 3. **Scalability**
- Dễ thêm layers mới (ví dụ: Caching layer, Message Queue layer)
- Dễ split thành microservices (Domain layer có thể reuse)

## 🔧 Các Quyết định Kỹ thuật

### 1. **Tại sao dùng DTO thay vì Entity trực tiếp?**

**Vấn đề nếu dùng Entity:**
- Expose internal fields không cần thiết
- Tight coupling giữa API contract và database schema
- Khó versioning API

**Giải pháp với DTO:**
- `UserRequest`: Chỉ chứa fields cần cho create/update
- `UserResponse`: Chỉ expose fields cần cho client
- Có thể thay đổi Entity mà không ảnh hưởng API contract

### 2. **Tại sao tách Mapper ra riêng?**

**Lý do:**
- Service layer tập trung vào business logic, không phải mapping
- Dễ test: Test mapper riêng, test service riêng
- Có thể thay thế bằng MapStruct nếu cần performance tốt hơn

### 3. **Tại sao Repository là Interface?**

**Dependency Inversion Principle:**
- Service phụ thuộc vào abstraction (interface), không phụ thuộc implementation
- Spring Data JPA tự động implement, nhưng Service không biết điều đó
- Dễ mock trong tests

### 4. **Tại sao Global Exception Handler?**

**Lợi ích:**
- Consistent error response format
- Centralized error handling logic
- Không cần try-catch ở mọi Controller method
- Dễ log và monitor errors

### 5. **Tại sao Profile-based Configuration?**

**Flexibility:**
- Dev: H2 in-memory, debug logging, H2 console
- Test: H2 in-memory, minimal logging
- Prod: PostgreSQL, production logging, no stack traces

**Security:**
- Không hardcode credentials
- Dùng environment variables
- Mỗi environment có config riêng

## 🧪 Testing Strategy

### Unit Tests (Service Layer)
- Test business logic độc lập
- Mock Repository và Mapper
- Test cả success và failure cases

### Integration Tests (Controller Layer)
- Test HTTP layer với MockMvc
- Mock Service layer
- Test validation, status codes, response format

### Future: Repository Tests
- Có thể thêm `@DataJpaTest` để test Repository layer
- Test custom queries, relationships

## 🚀 Production Considerations

### 1. **Database Migrations**
Hiện tại dùng `ddl-auto: validate` (prod) hoặc `create-drop` (dev/test).

**Recommendation:** Thêm Flyway hoặc Liquibase cho production:
- Version control database schema
- Rollback capability
- Team collaboration

### 2. **API Documentation**
**Recommendation:** Thêm SpringDoc OpenAPI (Swagger):
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

### 3. **Security**
**Recommendation:** Thêm Spring Security:
- Authentication (JWT)
- Authorization (Role-based)
- CORS configuration
- Rate limiting

### 4. **Monitoring**
**Recommendation:** Thêm Spring Boot Actuator:
- Health checks
- Metrics (Prometheus)
- Logging (ELK stack)

### 5. **Caching**
**Recommendation:** Thêm Redis:
- Cache frequently accessed data
- Session management
- Rate limiting

## 📚 References

- [Clean Architecture by Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [Spring Boot Best Practices](https://spring.io/guides)

