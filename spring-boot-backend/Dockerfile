# Multi-stage Dockerfile for production
# Stage 1: Build
FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml first (for better layer caching)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Download dependencies (cached layer if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application (skip tests in Docker build, run separately in CI/CD)
RUN mvn clean package -DskipTests

# Stage 2: Runtime
# Sử dụng JRE image (không phải Alpine) để tương thích tốt hơn với multi-platform
# Alternative: Nếu cần Alpine, có thể dùng amazoncorretto:17-alpine-jdk
FROM eclipse-temurin:17-jre

# Add non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose ports (8080 for application, 5005 for debug)
EXPOSE 8080 5005

# Health check (requires Spring Boot Actuator dependency)
# Uncomment và thêm spring-boot-starter-actuator vào pom.xml để enable
# HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
#   CMD curl -f http://localhost:${SERVER_PORT:-8080}/actuator/health || exit 1

# Run application
# Use exec form để signal handling hoạt động đúng
# Enable debug mode if DEBUG_ENABLED=true (default: false)
ENTRYPOINT ["sh", "-c", \
    "if [ \"$DEBUG_ENABLED\" = \"true\" ]; then \
        java -XX:+UseContainerSupport \
             -XX:MaxRAMPercentage=75.0 \
             -Djava.security.egd=file:/dev/./urandom \
             -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 \
             -jar app.jar; \
    else \
        java -XX:+UseContainerSupport \
             -XX:MaxRAMPercentage=75.0 \
             -Djava.security.egd=file:/dev/./urandom \
             -jar app.jar; \
    fi"]

