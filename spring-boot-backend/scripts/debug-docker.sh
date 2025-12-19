#!/bin/bash

# Script to run Spring Boot application in Docker with debug mode enabled
# Debug port: 5005

echo "=========================================="
echo "Starting Spring Boot in Docker (Debug Mode)"
echo "=========================================="
echo ""

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker first."
    exit 1
fi

# Set debug port
DEBUG_PORT=${DEBUG_PORT:-5005}

echo "Configuration:"
echo "   Profile: prod (Docker)"
echo "   Application Port: 8080"
echo "   Debug Port: $DEBUG_PORT"
echo ""

# Stop existing containers
echo "1. Stopping existing containers..."
docker-compose stop backend 2>/dev/null || true

# Build and start with debug enabled
echo ""
echo "2. Building and starting application with debug mode..."
echo "   Connect debugger to: localhost:$DEBUG_PORT"
echo ""

# Export DEBUG_ENABLED and DEBUG_PORT for docker-compose
export DEBUG_ENABLED=true
export DEBUG_PORT=$DEBUG_PORT

# Start services
docker-compose up -d postgres

# Wait for PostgreSQL to be healthy
echo ""
echo "3. Waiting for PostgreSQL to be ready..."
sleep 5

# Start backend with debug
docker-compose up --build backend

echo ""
echo "=========================================="
echo "Application started with debug mode!"
echo "=========================================="
echo ""
echo "Debug Information:"
echo "   - Application: http://localhost:8080"
echo "   - Swagger UI: http://localhost:8080/swagger-ui.html"
echo "   - Debug Port: localhost:$DEBUG_PORT"
echo ""
echo "To connect debugger:"
echo "   1. Open IDE (IntelliJ/VS Code)"
echo "   2. Create Remote Debug configuration"
echo "   3. Host: localhost, Port: $DEBUG_PORT"
echo ""
echo "To view logs:"
echo "   docker-compose logs -f backend"
echo ""
echo "To stop:"
echo "   docker-compose stop backend"
echo ""

