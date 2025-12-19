# Hướng dẫn Debug Code trên Local

## 📋 Tổng quan

Có nhiều cách để debug Spring Boot application:
1. **Local Debug** (chạy trực tiếp trên máy, không dùng Docker)
2. **Remote Debug** (debug application đang chạy trong Docker)
3. **IDE Debug** (IntelliJ IDEA, Eclipse, VS Code)

---

## 🚀 Cách 1: Debug Local (Khuyến nghị cho Development) ⭐

### Quick Start với Script

```bash
# Chạy script (tự động check PostgreSQL và start debug mode)
./scripts/debug-local.sh

# Hoặc với custom port
DEBUG_PORT=5006 ./scripts/debug-local.sh
```

Script sẽ:
- ✅ Kiểm tra PostgreSQL đang chạy
- ✅ Start PostgreSQL nếu chưa chạy
- ✅ Start application với debug mode
- ✅ Expose debug port 5005

### Setup

#### Bước 1: Đảm bảo PostgreSQL đang chạy
```bash
# Start PostgreSQL container
docker-compose up -d postgres

# Hoặc chạy PostgreSQL local nếu có
```

#### Bước 2: Chạy application với debug mode

**Với Maven:**
```bash
# Chạy với debug port 5005
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=*:5005"

# Hoặc với profile dev
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=*:5005"
```

**Với IDE (IntelliJ IDEA):**
1. Mở project trong IntelliJ
2. Tạo Run Configuration:
   - **Run** → **Edit Configurations...**
   - Click **+** → **Spring Boot**
   - **Name**: `BackendApplication (Debug)`
   - **Main class**: `com.example.backend.BackendApplication`
   - **Active profiles**: `dev`
   - **VM options**: `-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=*:5005`
   - Click **OK**
3. Click **Debug** (hoặc Shift+F9)

**Với IDE (Eclipse/STS):**
1. Right-click vào `BackendApplication.java`
2. **Debug As** → **Spring Boot App**
3. Hoặc tạo Debug Configuration:
   - **Run** → **Debug Configurations...**
   - **Spring Boot App** → **New**
   - **Project**: `spring-boot-backend`
   - **Main class**: `com.example.backend.BackendApplication`
   - **Arguments** → **VM arguments**: `-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=*:5005`

**Với VS Code:**
1. Tạo file `.vscode/launch.json`:
```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Debug Spring Boot",
            "request": "launch",
            "mainClass": "com.example.backend.BackendApplication",
            "projectName": "spring-boot-backend",
            "args": "",
            "vmArgs": "-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=*:5005 -Dspring.profiles.active=dev"
        }
    ]
}
```
2. Press **F5** để start debug

### Kết nối Debugger

Sau khi application start, kết nối debugger:

**IntelliJ IDEA:**
1. **Run** → **Edit Configurations...**
2. Click **+** → **Remote JVM Debug**
3. **Name**: `Remote Debug`
4. **Host**: `localhost`
5. **Port**: `5005`
6. Click **OK**
7. Click **Debug** để kết nối

**Eclipse:**
1. **Run** → **Debug Configurations...**
2. **Remote Java Application** → **New**
3. **Host**: `localhost`
4. **Port**: `5005`
5. Click **Debug**

**VS Code:**
1. Tạo configuration trong `.vscode/launch.json`:
```json
{
    "type": "java",
    "name": "Attach to Remote",
    "request": "attach",
    "hostName": "localhost",
    "port": 5005
}
```
2. Press **F5**

---

## 🐳 Cách 2: Remote Debug (Application chạy trong Docker)

### Setup Docker để hỗ trợ Remote Debug

#### Bước 1: Cập nhật Dockerfile

Thêm JVM arguments cho debug vào Dockerfile:

```dockerfile
# Thêm vào ENTRYPOINT
ENTRYPOINT ["java", \
    "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", \
    "app.jar"]
```

#### Bước 2: Cập nhật docker-compose.yml

Thêm port mapping cho debug port:

```yaml
backend:
  ports:
    - "${SERVER_PORT:-8080}:8080"
    - "5005:5005"  # Debug port
```

#### Bước 3: Rebuild và start

```bash
docker-compose down
docker-compose build backend
docker-compose up -d
```

#### Bước 4: Kết nối Debugger

Sử dụng cùng cách như trên với:
- **Host**: `localhost`
- **Port**: `5005`

---

## 🔧 Cách 3: Debug với Maven Wrapper

### Chạy với debug mode:

```bash
# Debug mode với suspend (đợi debugger kết nối)
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=*:5005"

# Debug mode không suspend (chạy ngay)
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=*:5005"
```

**Lưu ý:**
- `suspend=y`: Application sẽ đợi debugger kết nối trước khi start
- `suspend=n`: Application start ngay, debugger có thể kết nối sau

---

## 💡 Tips và Tricks

### 1. Conditional Breakpoints

**IntelliJ IDEA:**
- Right-click vào breakpoint → **More** → Đặt condition
- Ví dụ: `user.getId() == 1`

**Eclipse:**
- Right-click vào breakpoint → **Breakpoint Properties** → **Condition**

### 2. Logpoints (IntelliJ IDEA)

Thay vì breakpoint, dùng logpoint để log mà không dừng:
- Right-click vào breakpoint → **More** → **Log evaluated expression**
- Ví dụ: `User: ${user.getName()}`

### 3. Evaluate Expression

Khi ở breakpoint, có thể evaluate code:
- **IntelliJ**: Alt+F8
- **Eclipse**: Ctrl+Shift+D hoặc **Display** view

### 4. Step Through Code

- **Step Over** (F8): Chạy dòng hiện tại, không vào function
- **Step Into** (F7): Vào trong function
- **Step Out** (Shift+F8): Thoát khỏi function hiện tại
- **Resume** (F9): Tiếp tục chạy đến breakpoint tiếp theo

### 5. Watch Variables

Thêm variables vào watch để theo dõi:
- **IntelliJ**: Right-click variable → **Add to Watches**
- **Eclipse**: Drag variable vào **Expressions** view

### 6. Debug HTTP Requests

Sử dụng breakpoint trong Controller:
```java
@PostMapping("/register")
public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
    // Đặt breakpoint ở đây
    log.info("POST /api/auth/register - Registering user: {}", request.getEmail());
    AuthResponse response = authenticationService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}
```

### 7. Debug Database Queries

Enable SQL logging trong `application-dev.yml`:
```yaml
logging:
  level:
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

### 8. Debug với Postman/curl

1. Đặt breakpoint trong Controller
2. Gọi API từ Postman hoặc curl
3. Application sẽ dừng ở breakpoint
4. Debug như bình thường

---

## 🎯 Common Debug Scenarios

### Scenario 1: Debug Authentication Flow

1. Đặt breakpoint trong `AuthController.register()`
2. Đặt breakpoint trong `AuthenticationService.register()`
3. Đặt breakpoint trong `JwtService.generateToken()`
4. Gọi API register từ Postman
5. Step through từng method

### Scenario 2: Debug Database Issues

1. Enable SQL logging (đã có trong dev profile)
2. Đặt breakpoint sau query:
```java
User user = userRepository.findByEmail(email)
    .orElseThrow(...);
// Đặt breakpoint ở đây để xem user object
```

### Scenario 3: Debug Exception

1. Đặt breakpoint trong `GlobalExceptionHandler`
2. Hoặc enable "Break on Exception" trong IDE:
   - **IntelliJ**: **Run** → **View Breakpoints** → **Exception Breakpoints**
   - **Eclipse**: **Run** → **Add Java Exception Breakpoint**

---

## 🔍 Debug Configuration Files

### IntelliJ IDEA - Run Configuration Template

**Local Debug:**
```
Main class: com.example.backend.BackendApplication
VM options: -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=*:5005
Program arguments: --spring.profiles.active=dev
Environment variables: (optional)
```

**Remote Debug:**
```
Host: localhost
Port: 5005
```

### VS Code - launch.json

```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Debug Spring Boot (Local)",
            "request": "launch",
            "mainClass": "com.example.backend.BackendApplication",
            "projectName": "spring-boot-backend",
            "args": "--spring.profiles.active=dev",
            "vmArgs": "-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=*:5005"
        },
        {
            "type": "java",
            "name": "Attach to Remote (Docker)",
            "request": "attach",
            "hostName": "localhost",
            "port": 5005
        }
    ]
}
```

---

## 🐛 Troubleshooting

### Lỗi: "Cannot connect to debugger"

**Nguyên nhân**: Port 5005 bị chặn hoặc application chưa start

**Giải pháp**:
```bash
# Kiểm tra port
lsof -i :5005

# Kiểm tra application đang chạy
ps aux | grep BackendApplication
```

### Lỗi: "Address already in use"

**Nguyên nhân**: Port 5005 đã được sử dụng

**Giải pháp**:
```bash
# Tìm process đang dùng port
lsof -i :5005

# Kill process hoặc đổi port
# Đổi port trong JVM arguments: address=*:5006
```

### Breakpoints không hoạt động

**Nguyên nhân**: Code không match hoặc không được compile

**Giải pháp**:
1. Rebuild project: `./mvnw clean compile`
2. Đảm bảo source code match với compiled code
3. Check breakpoint có được enable không

### Remote debug không kết nối được

**Nguyên nhân**: Firewall hoặc port không được expose

**Giải pháp**:
1. Kiểm tra docker-compose.yml có expose port 5005
2. Kiểm tra firewall settings
3. Test connection: `nc -zv localhost 5005`

---

## 📝 Best Practices

1. **Sử dụng Conditional Breakpoints**: Tránh dừng ở mọi lần gọi
2. **Log thay vì Breakpoint**: Dùng log cho những case đơn giản
3. **Debug từng layer**: Controller → Service → Repository
4. **Sử dụng Evaluate Expression**: Test code nhanh mà không cần sửa file
5. **Watch Variables**: Theo dõi state changes
6. **Step Over thay vì Step Into**: Với library code, step over để tiết kiệm thời gian

---

## 🎓 Quick Reference

| Action | IntelliJ | Eclipse | VS Code |
|--------|----------|---------|---------|
| Start Debug | Shift+F9 | F11 | F5 |
| Resume | F9 | F8 | F5 |
| Step Over | F8 | F6 | F10 |
| Step Into | F7 | F5 | F11 |
| Step Out | Shift+F8 | F7 | Shift+F11 |
| Evaluate | Alt+F8 | Ctrl+Shift+D | - |
| View Breakpoints | Ctrl+Shift+F8 | - | - |

---

## 📚 Resources

- [IntelliJ Debugging Guide](https://www.jetbrains.com/help/idea/debugging-code.html)
- [Eclipse Debugging Guide](https://www.eclipse.org/community/eclipse_newsletter/2017/june/article1.php)
- [VS Code Java Debugging](https://code.visualstudio.com/docs/java/java-debugging)
- [Spring Boot Debugging](https://spring.io/guides/gs/actuator-service/)

