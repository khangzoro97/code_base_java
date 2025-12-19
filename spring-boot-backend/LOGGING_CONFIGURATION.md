# Cấu hình Logging

## 📋 Tổng quan

Project đã được cấu hình để ghi logs vào **file** ngoài việc hiển thị trên console.

## 📁 Cấu trúc Log Files

### Development (dev):
- **Location**: `./logs/application-dev.log`
- **Error logs**: `./logs/application-dev-error.log`
- **Max size**: 10MB per file
- **Max history**: 7 files (7 ngày)

### Production (prod):
- **Location**: `/var/log/backend/application.log` (trong container)
- **Error logs**: `/var/log/backend/application-error.log`
- **Max size**: 10MB per file
- **Max history**: 30 files (30 ngày)

## 🔧 Cấu hình

### 1. Logback Configuration (`logback-spring.xml`)

File `src/main/resources/logback-spring.xml` quản lý:
- **Console Appender**: Hiển thị logs trên console
- **File Appender**: Ghi tất cả logs vào file
- **Error File Appender**: Ghi riêng ERROR logs vào file riêng
- **Rolling Policy**: Tự động rotate logs theo size và date

### 2. Application Configuration

#### Development (`application-dev.yml`):
```yaml
logging:
  file:
    name: ./logs/application-dev.log
    max-size: 10MB
    max-history: 7
```

#### Production (`application-prod.yml`):
```yaml
logging:
  file:
    name: /var/log/backend/application.log
    max-size: 10MB
    max-history: 30
```

## 📝 Log Files được tạo

### Development:
```
logs/
├── application-dev.log          # Tất cả logs
├── application-dev-2025-12-18.0.log  # Rotated logs
├── application-dev-error.log    # Chỉ ERROR logs
└── application-dev-error-2025-12-18.0.log
```

### Production:
```
/var/log/backend/
├── application.log
├── application-2025-12-18.0.log
├── application-error.log
└── application-error-2025-12-18.0.log
```

## 🎯 Cách sử dụng

### Xem logs trong file:

#### Development (local):
```bash
# Xem logs real-time
tail -f logs/application-dev.log

# Xem error logs
tail -f logs/application-dev-error.log

# Xem 100 dòng cuối
tail -n 100 logs/application-dev.log

# Tìm ERROR trong logs
grep ERROR logs/application-dev.log
```

#### Production (Docker):
```bash
# Xem logs trong container
docker-compose exec backend tail -f /var/log/backend/application.log

# Xem error logs
docker-compose exec backend tail -f /var/log/backend/application-error.log

# Copy logs ra host
docker-compose cp backend:/var/log/backend/application.log ./logs/
```

### Tùy chỉnh log file location:

#### Qua Environment Variable:
```bash
# Development
export LOG_FILE_PATH=./logs/my-app.log
./mvnw spring-boot:run

# Production (Docker)
docker-compose up -d -e LOG_FILE_PATH=/var/log/backend/my-app.log
```

#### Qua application.yml:
```yaml
logging:
  file:
    name: ./logs/custom-name.log
```

## 🔍 Log Levels

### Development:
- **Root**: INFO
- **Application**: DEBUG
- **Spring Web**: DEBUG
- **Hibernate SQL**: DEBUG
- **SQL Parameters**: TRACE

### Production:
- **Root**: INFO
- **Application**: INFO
- **Spring Web**: WARN
- **Hibernate**: WARN

## 📊 Log Format

### Console:
```
2025-12-18 08:30:00 - Application started
```

### File:
```
2025-12-18 08:30:00 [main] INFO  com.example.backend.BackendApplication - Application started
```

## 🛠️ Tùy chỉnh nâng cao

### Thay đổi log format:

Edit `logback-spring.xml`:
```xml
<pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
```

### Thêm appender mới:

Ví dụ: Ghi logs vào file riêng cho một package cụ thể:
```xml
<appender name="USER_LOGS" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <file>${LOG_PATH}/user-operations.log</file>
    <encoder>
        <pattern>%d{yyyy-MM-dd HH:mm:ss} - %msg%n</pattern>
    </encoder>
    <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
        <fileNamePattern>${LOG_PATH}/user-operations-%d{yyyy-MM-dd}.%i.log</fileNamePattern>
        <maxFileSize>10MB</maxFileSize>
        <maxHistory>30</maxHistory>
    </rollingPolicy>
</appender>

<logger name="com.example.backend.domain.model.User" level="DEBUG" additivity="false">
    <appender-ref ref="USER_LOGS"/>
</logger>
```

## 🚀 Best Practices

1. **Development**: Logs vào `./logs/` để dễ truy cập
2. **Production**: Logs vào `/var/log/backend/` trong container
3. **Error logs**: Luôn có file riêng cho ERROR để dễ debug
4. **Log rotation**: Tự động rotate để tránh file quá lớn
5. **Log retention**: Giữ logs trong khoảng thời gian hợp lý (7 ngày dev, 30 ngày prod)

## 📝 Lưu ý

- **Log files được ignore trong Git** (đã thêm vào `.gitignore`)
- **Tự động tạo thư mục logs** khi application start
- **Log rotation tự động** theo size và date
- **Error logs tách riêng** để dễ theo dõi

## 🔧 Troubleshooting

### Logs không được ghi vào file:

1. **Kiểm tra quyền ghi**:
   ```bash
   # Development
   ls -la logs/
   
   # Production
   docker-compose exec backend ls -la /var/log/backend/
   ```

2. **Kiểm tra cấu hình**:
   ```bash
   # Xem logback config
   cat src/main/resources/logback-spring.xml
   ```

3. **Kiểm tra environment variables**:
   ```bash
   env | grep LOG
   ```

### Logs file quá lớn:

- Log rotation tự động xử lý
- Có thể giảm `max-history` trong config
- Có thể giảm `max-size` để rotate sớm hơn

