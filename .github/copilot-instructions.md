<!-- Use this file to provide workspace-specific custom instructions to Copilot. For more details, visit https://code.visualstudio.com/docs/copilot/copilot-customization#_use-a-githubcopilotinstructionsmd-file -->

# Banking System Microservices Project

This is a Spring Boot microservices project for a banking system. The project includes:

## Services
- **Eureka Server**: Service registry and discovery
- **Config Server**: Centralized configuration management  
- **API Gateway**: Routing and entry point
- **Accounts Service**: Account management with CRUD operations
- **Customers Service**: Customer profile management with CRUD operations
- **Transactions Service**: Transaction processing with CRUD operations

## Technologies
- Spring Boot 3.3.2
- Spring Cloud 2023.0.3
- Spring Data JPA with H2 database
- Lombok for boilerplate code reduction
- Bean Validation for input validation
- OpenAPI/Swagger for API documentation
- JUnit 5 and Mockito for testing

## Key Features
- Full CRUD operations for all entities
- Field validation with proper error handling
- Swagger documentation for all APIs
- Microservice architecture with service discovery
- API Gateway for unified access
- Comprehensive unit tests

When helping with this project:
- Follow Spring Boot best practices
- Use Lombok annotations appropriately
- Maintain validation annotations on DTOs and entities
- Keep error handling consistent across services
- Follow RESTful API conventions
- Use appropriate HTTP status codes
