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

**Lưu ý**: Development profile giờ sử dụng PostgreSQL. Đảm bảo PostgreSQL đang chạy trước khi start application.

```bash
# 1. Start PostgreSQL (nếu chưa chạy)
docker-compose up -d postgres

# 2. Clone và navigate vào project
cd spring-boot-backend

# 3. Run với Maven (sử dụng PostgreSQL)
./mvnw spring-boot:run

# Hoặc với profile cụ thể
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# 4. Application sẽ chạy tại http://localhost:8080
```

**PostgreSQL Connection (Development):**
- Host: `localhost`
- Port: `5432`
- Database: `backenddb`
- Username: `postgres`
- Password: `postgres`

**Lưu ý**: Nếu bạn muốn sử dụng H2 (in-memory) cho development, có thể tạo profile `dev-h2` riêng hoặc chỉnh sửa `application-dev.yml`.

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

**Quick Setup:**
```bash
# 1. Copy example file
cp env.example .env

# 2. Edit .env với các giá trị thực tế của bạn
# Đặc biệt quan trọng: Thay đổi JWT_SECRET cho production!
```

**File `env.example`** chứa tất cả các biến môi trường cần thiết với giá trị mặc định và giải thích chi tiết.

**Các biến quan trọng cần cấu hình:**
- `JWT_SECRET`: **BẮT BUỘC** - Generate secret key an toàn cho production (ít nhất 32 ký tự)
  ```bash
  # Generate random secret key
  openssl rand -base64 32
  ```
- `DB_PASSWORD`: Mật khẩu database (không dùng mặc định trong production)
- `SPRING_PROFILES_ACTIVE`: `dev` (local), `test` (testing), `prod` (production)
- `SWAGGER_ENABLED`: `true` (dev), `false` (production)

**Docker Compose** sẽ tự động load file `.env` nếu có trong cùng thư mục với `docker-compose.yml`.

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

## 🔐 Authentication & Security

### JWT Authentication

API sử dụng JWT (JSON Web Token) cho authentication. Sau khi đăng ký hoặc đăng nhập, bạn sẽ nhận được JWT token để sử dụng cho các requests tiếp theo.

#### Register New User
```bash
POST /api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "bio": "Software Engineer"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "bio": "Software Engineer",
    "createdAt": "2025-12-18T10:00:00",
    "updatedAt": "2025-12-18T10:00:00"
  }
}
```

#### Login
```bash
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

#### Using JWT Token
Sau khi có token, thêm vào header của mọi request:
```bash
Authorization: Bearer <your-jwt-token>
```

**Example:**
```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### Security Configuration

- **Password Encoding**: BCrypt
- **JWT Secret**: Cấu hình qua `JWT_SECRET` environment variable
- **JWT Expiration**: 24 hours (có thể config qua `JWT_EXPIRATION`)
- **Public Endpoints**: `/api/auth/**`, `/swagger-ui/**`, `/v3/api-docs/**`
- **Protected Endpoints**: Tất cả endpoints khác cần JWT token

## 📚 API Documentation

### Swagger UI

API documentation có sẵn tại: **http://localhost:8080/swagger-ui.html**

Swagger UI cho phép:
- Xem tất cả API endpoints
- Test API trực tiếp từ browser
- Xem request/response schemas
- Authenticate với JWT token trong Swagger UI

**Lưu ý:** Swagger được disable mặc định trong production profile. Enable bằng cách set `SWAGGER_ENABLED=true`.

### Example: User CRUD API

**Base URL:** `http://localhost:8080/api/users`

**⚠️ Tất cả endpoints này yêu cầu JWT authentication!**

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

**Development (PostgreSQL):**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/backenddb
    username: postgres
    password: postgres
```

**Production (PostgreSQL):**
```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

## 🗄️ Database Migrations (Flyway)

Project sử dụng **Flyway** để quản lý database schema migrations.

### Migration Files

Migrations được đặt trong: `src/main/resources/db/migration/`

**Naming Convention:**
- `V{version}__{description}.sql`
- Ví dụ: `V1__Create_users_table.sql`

### Tạo Migration Mới

1. Tạo file SQL mới trong `src/main/resources/db/migration/`
2. Đặt tên theo convention: `V{next_version}__{description}.sql`
3. Flyway sẽ tự động chạy migration khi app khởi động

**Example:**
```sql
-- V2__Add_user_status_column.sql
ALTER TABLE users ADD COLUMN status VARCHAR(20) DEFAULT 'ACTIVE';
```

### Flyway Commands

```bash
# Check migration status (via Spring Boot Actuator - nếu có)
curl http://localhost:8080/actuator/flyway

# Hoặc check trong logs khi app start
```

### Migration Best Practices

- ✅ Mỗi migration phải idempotent (có thể chạy nhiều lần an toàn)
- ✅ Sử dụng `IF NOT EXISTS` cho CREATE statements
- ✅ Test migrations trên dev/test trước khi deploy production
- ✅ Không sửa migrations đã chạy trong production (tạo migration mới)

## 📝 Next Steps (Recommended)

1. ✅ **Database Migrations**: Đã implement Flyway
2. ✅ **API Documentation**: Đã implement SpringDoc OpenAPI (Swagger)
3. ✅ **Security**: Đã implement Spring Security + JWT
4. **Monitoring**: Thêm Spring Boot Actuator + Prometheus
5. **Caching**: Thêm Redis cho caching
6. **Message Queue**: Thêm RabbitMQ/Kafka nếu cần async processing
7. **Rate Limiting**: Thêm rate limiting cho API endpoints
8. **Email Service**: Thêm email verification cho user registration

## 🤝 Contributing

1. Follow Clean Architecture principles
2. Write tests cho mọi business logic
3. Follow naming conventions
4. Update documentation khi thay đổi API

## 📄 License

MIT License

