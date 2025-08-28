# Makefile for Polizas Service
# Provides convenient shortcuts for common development tasks

.PHONY: help clean build test run dev docker-build docker-run docker-stop format checkstyle

# Default target
help:
	@echo "Available targets:"
	@echo "  help         - Show this help message"
	@echo "  clean        - Clean the project"
	@echo "  build        - Build the project"
	@echo "  test         - Run tests"
	@echo "  run          - Run the application"
	@echo "  dev          - Run in development mode"
	@echo "  format       - Format code with Spotless"
	@echo "  checkstyle   - Run Checkstyle checks"
	@echo "  docker-build - Build Docker image"
	@echo "  docker-run   - Run with Docker Compose"
	@echo "  docker-stop  - Stop Docker containers"

# Clean the project
clean:
	./mvnw clean

# Build the project
build: clean
	./mvnw compile

# Run tests
test: build
	./mvnw test

# Package the application
package: test
	./mvnw package

# Run the application
run: package
	java -jar target/quarkus-app/quarkus-run.jar

# Run in development mode
dev:
	./mvnw quarkus:dev

# Format code with Spotless
format:
	./mvnw spotless:apply

# Check code style
checkstyle:
	./mvnw checkstyle:check

# Build Docker image
docker-build: package
	docker build -t polizas-service .

# Run with Docker Compose
docker-run:
	docker-compose up -d

# Stop Docker containers
docker-stop:
	docker-compose down

# Run all quality checks
quality: format checkstyle test

# Generate reports
reports: test
	./mvnw jacoco:report
	@echo "Reports generated in target/site/"

# Install Git hooks
install-hooks:
	chmod +x .git/hooks/pre-commit
	@echo "Git hooks installed successfully"

# Setup development environment
setup: install-hooks
	./mvnw dependency:resolve
	@echo "Development environment setup complete"

# Show project status
status:
	@echo "Project: Polizas Service"
	@echo "Java version: $(shell java -version 2>&1 | head -n 1)"
	@echo "Maven version: $(shell ./mvnw --version 2>/dev/null | head -n 1 || echo 'Maven not available')"
	@echo "Docker version: $(shell docker --version 2>/dev/null || echo 'Docker not available')"
