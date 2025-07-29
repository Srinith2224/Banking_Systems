# Banking System Microservices

Modern Banking System with Spring Boot Microservices architecture for scalable banking operations.

## Architecture Overview

This banking system implements a robust microservice-based architecture with the following components:

### Microservices
- **Eureka Server** (Port 8761) - Service registry and discovery
- **Config Server** (Port 8888) - Centralized configuration management
- **API Gateway** (Port 8080) - Unified routing and entry point
- **Accounts Service** (Port 8081) - Account management operations
- **Customers Service** (Port 8082) - Customer profile management
- **Transactions Service** (Port 8083) - Transaction processing

### Technologies Used
- Spring Boot 3.3.2
- Spring Cloud 2023.0.3
- Spring Data JPA
- Spring Cloud Gateway
- Netflix Eureka
- H2 Database (In-memory)
- Lombok
- OpenAPI/Swagger Documentation
- JUnit 5 & Mockito for Testing

## Features

### CRUD Operations
All services support complete CRUD operations:
- **POST** - Create new records
- **GET** - Retrieve records
- **PUT** - Update existing records
- **DELETE** - Remove records

### Field Validations
- Required field validation
- Format validation (email, phone, etc.)
- Unique constraints (account number, email)
- Positive amount validation
- Balance cannot be negative

### Error Handling
- Descriptive JSON error responses
- Standard HTTP status codes (200, 201, 400, 404, 409, 500)
- Validation error messages
- No internal stack traces exposed

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Running the Services

1. **Start Eureka Server**
   ```powershell
   cd eureka-server
   mvn spring-boot:run
   ```

2. **Start Config Server**
   ```powershell
   cd config-server
   mvn spring-boot:run
   ```

3. **Start API Gateway**
   ```powershell
   cd api-gateway
   mvn spring-boot:run
   ```

4. **Start Microservices** (can be run in parallel)
   ```powershell
   # Accounts Service
   cd accounts-service
   mvn spring-boot:run
   
   # Customers Service
   cd customers-service
   mvn spring-boot:run
   
   # Transactions Service
   cd transactions-service
   mvn spring-boot:run
   ```

### Access Points

- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8080
- **Accounts API**: http://localhost:8080/api/accounts
- **Customers API**: http://localhost:8080/api/customers
- **Transactions API**: http://localhost:8080/api/transactions

### API Documentation

Swagger UI is available for each service:
- **Accounts Service**: http://localhost:8081/swagger-ui.html
- **Customers Service**: http://localhost:8082/swagger-ui.html
- **Transactions Service**: http://localhost:8083/swagger-ui.html

### Database Console

H2 Console is available for each service:
- **Accounts DB**: http://localhost:8081/h2-console
- **Customers DB**: http://localhost:8082/h2-console
- **Transactions DB**: http://localhost:8083/h2-console

## API Examples

### Create Customer
```json
POST http://localhost:8080/api/customers
{
  "firstName": "Jordan",
  "lastName": "Lee",
  "email": "jordan.lee@bank.com",
  "phone": "+31 690000000",
  "address": "10 Bank Avenue, Rotterdam"
}
```

### Create Account
```json
POST http://localhost:8080/api/accounts
{
  "accountNumber": "NL91ABNA0417164300",
  "customerId": 1,
  "type": "Savings",
  "balance": 5000.00
}
```

### Create Transaction
```json
POST http://localhost:8080/api/transactions
{
  "accountId": 1,
  "type": "Deposit",
  "amount": 750.00
}
```

## Testing

Run tests for all services:
```powershell
mvn test
```

Run tests for specific service:
```powershell
cd accounts-service
mvn test
```

## Project Structure

```
banking-system/
├── eureka-server/           # Service registry
├── config-server/          # Configuration server
├── api-gateway/            # API Gateway
├── accounts-service/       # Account management
├── customers-service/      # Customer profiles
├── transactions-service/   # Transaction processing
├── pom.xml                # Parent POM
└── README.md              # This file
```

## Future Enhancements

- Security implementation with Spring Security
- Database integration with PostgreSQL/MySQL
- Distributed tracing with Zipkin
- Circuit breaker with Resilience4j
- Caching with Redis
- Message queuing with RabbitMQ/Kafka
