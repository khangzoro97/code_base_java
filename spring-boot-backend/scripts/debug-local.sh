#!/bin/bash

# Script to run Spring Boot application in debug mode locally
# Matches Java version with Docker (Java 17)

echo "=========================================="
echo "Starting Spring Boot in Debug Mode"
echo "=========================================="
echo ""

# Set Java 17 (match Docker version)
echo "0. Setting Java 17..."
JAVA_17_PATH=$(/usr/libexec/java_home -v 17 2>/dev/null)
if [ -z "$JAVA_17_PATH" ]; then
    echo "   ⚠️  Java 17 not found. Install with: brew install openjdk@17"
    echo "   Using current Java version"
else
    export JAVA_HOME=$JAVA_17_PATH
    export PATH=$JAVA_HOME/bin:$PATH
    echo "   ✅ Java 17 active: $(java -version 2>&1 | head -1)"
fi
echo ""

# Check port 8080
echo "0.5. Checking port 8080..."
if lsof -ti:8080 > /dev/null 2>&1; then
    echo "   ⚠️  Port 8080 is already in use!"
    if docker-compose ps backend 2>/dev/null | grep -q "Up"; then
        echo "   Docker backend container is running. Stopping it..."
        docker-compose stop backend
        sleep 2
        echo "   ✅ Docker container stopped"
    else
        echo "   Please stop the process using port 8080 or use a different port"
        echo "   Run: lsof -i :8080"
        exit 1
    fi
else
    echo "   ✅ Port 8080 is available"
fi
echo ""

# Check if PostgreSQL is running
echo "1. Checking PostgreSQL..."
if docker-compose ps postgres | grep -q "Up"; then
    echo "   ✅ PostgreSQL is running"
else
    echo "   ⚠️  PostgreSQL is not running. Starting..."
    docker-compose up -d postgres
    sleep 5
fi

# Set debug port
DEBUG_PORT=${DEBUG_PORT:-5005}
PROFILE=${SPRING_PROFILES_ACTIVE:-dev}

echo ""
echo "2. Configuration:"
echo "   Profile: $PROFILE"
echo "   Debug Port: $DEBUG_PORT"
echo "   Application Port: 8080"
echo ""

echo "3. Starting application in debug mode..."
echo "   Connect debugger to: localhost:$DEBUG_PORT"
echo ""

# Run with Maven (using Java 17)
./mvnw spring-boot:run \
    -Dspring-boot.run.profiles=$PROFILE \
    -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=*:$DEBUG_PORT"
