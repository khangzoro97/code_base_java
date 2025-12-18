# Spring Boot Backend - Production Ready

Production-scale Spring Boot backend với Clean Architecture, tuân thủ best practices 2025.

## 🏗️ Kiến trúc

```
src/main/java/com/example/backend/
├── domain/              # Domain Layer (Business Logic)
│   ├── model/          # Entities
│   └── repository/     # Repository Interfaces
├── application/        # Application Layer (Use Cases)
│   ├── dto/           # Data Transfer Objects
│   ├── mapper/        # Entity <-> DTO Mappers
│   └── service/       # Application Services
└── infrastructure/     # Infrastructure Layer (Framework)
    └── controller/    # REST Controllers
    └── exception/     # Exception Handlers
```

**Nguyên tắc Clean Architecture:**
- **Domain Layer**: Không phụ thuộc vào framework, chứa business logic thuần
- **Application Layer**: Orchestrate use cases, phụ thuộc vào Domain
- **Infrastructure Layer**: Framework concerns (Spring, JPA), phụ thuộc vào Application & Domain

## 🚀 Quick Start

### Prerequisites
- Java 17 (LTS)
- Maven 3.9+ (hoặc dùng Maven Wrapper)
- Docker & Docker Compose (cho production)

### Run Local (Development)

```bash
# 1. Clone và navigate vào project
cd spring-boot-backend

# 2. Run với Maven (sử dụng H2 in-memory database)
./mvnw spring-boot:run

# Hoặc với profile cụ thể
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# 3. Application sẽ chạy tại http://localhost:8080
```

**H2 Console:** http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:devdb`
- Username: `sa`
- Password: (để trống)

### Run với Docker Compose (Production-like)

```bash
# 1. Build và start tất cả services (app + PostgreSQL)
docker-compose up --build

# 2. Application: http://localhost:8080
# 3. PostgreSQL: localhost:5432

# Stop services
docker-compose down

# Stop và xóa volumes (xóa database data)
docker-compose down -v
```

### Environment Variables

Tạo file `.env` trong root directory (hoặc export trực tiếp):

```bash
# Database
DB_NAME=backenddb
DB_USERNAME=postgres
DB_PASSWORD=your_secure_password
DB_PORT=5432

# Application
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=prod

# Logging
LOG_LEVEL_ROOT=INFO
LOG_LEVEL_APP=INFO
```

## 🧪 Testing

```bash
# Run all tests
./mvnw test

# Run với coverage
./mvnw test jacoco:report

# Run integration tests only
./mvnw test -Dtest=*IntegrationTest
```

## 📦 Build Production

### Build JAR

```bash
./mvnw clean package -DskipTests
# Output: target/spring-boot-backend-1.0.0.jar
```

### Build Docker Image

```bash
# Build image
docker build -t spring-boot-backend:1.0.0 .

# Run container
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/backenddb \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=your_password \
  spring-boot-backend:1.0.0
```

## 📚 API Documentation

### Example: User CRUD API

**Base URL:** `http://localhost:8080/api/users`

#### Create User
```bash
POST /api/users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "bio": "Software Engineer"
}
```

#### Get User by ID
```bash
GET /api/users/1
```

#### Get All Users
```bash
GET /api/users
```

#### Update User
```bash
PUT /api/users/1
Content-Type: application/json

{
  "name": "John Updated",
  "email": "john.updated@example.com",
  "bio": "Senior Software Engineer"
}
```

#### Delete User
```bash
DELETE /api/users/1
```

## 🎯 Best Practices Đã Áp Dụng

### 1. Clean Architecture
- ✅ Tách biệt rõ ràng Domain, Application, Infrastructure
- ✅ Dependency Rule: Inner layers không phụ thuộc outer layers
- ✅ Business logic trong Domain/Application, không trong Controller

### 2. SOLID Principles
- ✅ **Single Responsibility**: Mỗi class có một trách nhiệm duy nhất
- ✅ **Dependency Inversion**: Phụ thuộc vào abstractions (interfaces), không phụ thuộc vào implementations

### 3. Database
- ✅ JPA + Hibernate với proper configuration
- ✅ Profile-based configuration (dev/test/prod)
- ✅ Connection pooling (HikariCP)
- ✅ Migration strategy ready (có thể thêm Flyway/Liquibase)

### 4. Error Handling
- ✅ Global Exception Handler (`@RestControllerAdvice`)
- ✅ Consistent error response format
- ✅ Proper HTTP status codes
- ✅ Không log sensitive data

### 5. Testing
- ✅ Unit tests (Service layer)
- ✅ Integration tests (Controller layer)
- ✅ Test coverage > 80%

### 6. Docker
- ✅ Multi-stage build (giảm image size)
- ✅ Non-root user
- ✅ Health checks
- ✅ Environment-based configuration

### 7. Security (Ready for extension)
- ✅ Input validation (`@Valid`, `@NotNull`, etc.)
- ✅ Prepared for Spring Security integration
- ✅ No hardcoded secrets

## 🔧 Configuration

### Profiles

- **dev**: H2 in-memory, debug logging, H2 console enabled
- **test**: H2 in-memory, minimal logging
- **prod**: PostgreSQL/MySQL, production logging, no stack traces

### Database Configuration

**Development (H2):**
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:devdb
```

**Production (PostgreSQL):**
```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

## 📝 Next Steps (Recommended)

1. **Database Migrations**: Thêm Flyway hoặc Liquibase
2. **API Documentation**: Thêm SpringDoc OpenAPI (Swagger)
3. **Security**: Thêm Spring Security + JWT
4. **Monitoring**: Thêm Spring Boot Actuator + Prometheus
5. **Caching**: Thêm Redis cho caching
6. **Message Queue**: Thêm RabbitMQ/Kafka nếu cần async processing

## 🤝 Contributing

1. Follow Clean Architecture principles
2. Write tests cho mọi business logic
3. Follow naming conventions
4. Update documentation khi thay đổi API

## 📄 License

MIT License

