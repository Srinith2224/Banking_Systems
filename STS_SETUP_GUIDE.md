# Running Banking System Microservices in Spring Tool Suite (STS)

## Import Project into STS

1. **Open Spring Tool Suite (STS)**
2. **Import the Project:**
   - File → Import → Existing Maven Projects
   - Browse to: `C:\Users\2423051\OneDrive - Cognizant\Documents\spring\Final Project`
   - Select all modules (banking-system, eureka-server, config-server, api-gateway, accounts-service, customers-service, transactions-service)
   - Click Finish

## Running Order (Important!)

**Always start services in this order:**

### 1. Eureka Server (Port 8761)
- Right-click on `eureka-server` → Run As → Spring Boot App
- Wait until you see: "Started EurekaServerApplication"
- Verify at: http://localhost:8761

### 2. Config Server (Port 8888)
- Right-click on `config-server` → Run As → Spring Boot App
- Wait until you see: "Started ConfigServerApplication"

### 3. API Gateway (Port 8080)
- Right-click on `api-gateway` → Run As → Spring Boot App
- Wait until you see: "Started ApiGatewayApplication"

### 4. Business Services (Can run in any order)
- Right-click on `accounts-service` → Run As → Spring Boot App (Port 8081)
- Right-click on `customers-service` → Run As → Spring Boot App (Port 8082)
- Right-click on `transactions-service` → Run As → Spring Boot App (Port 8083)

## Verification

### Check Service Registration
- Open Eureka Dashboard: http://localhost:8761
- You should see all services registered:
  - API-GATEWAY
  - ACCOUNTS-SERVICE
  - CUSTOMERS-SERVICE
  - TRANSACTIONS-SERVICE

### Test APIs via Gateway
- Accounts API: http://localhost:8080/api/accounts
- Customers API: http://localhost:8080/api/customers
- Transactions API: http://localhost:8080/api/transactions

### Access Swagger Documentation
- Accounts Service: http://localhost:8081/swagger-ui.html
- Customers Service: http://localhost:8082/swagger-ui.html
- Transactions Service: http://localhost:8083/swagger-ui.html

### Access H2 Database Consoles
- Accounts DB: http://localhost:8081/h2-console
- Customers DB: http://localhost:8082/h2-console
- Transactions DB: http://localhost:8083/h2-console

**Database Connection Details:**
- Driver Class: `org.h2.Driver`
- JDBC URL: `jdbc:h2:mem:accountsdb` (or customersdb/transactionsdb)
- Username: `sa`
- Password: (leave empty)

## STS-Specific Tips

### 1. Boot Dashboard
- Open Window → Show View → Boot Dashboard
- This shows all running Spring Boot applications
- You can start/stop services from here

### 2. Console Management
- Each service runs in its own console tab
- Switch between consoles to see logs for each service

### 3. Hot Reload
- STS supports hot reload with Spring Boot DevTools
- Changes to code will automatically restart the application

### 4. Debug Mode
- Right-click service → Debug As → Spring Boot App
- Set breakpoints in your code for debugging

## Testing the APIs

### Create a Customer
```
POST http://localhost:8080/api/customers
Content-Type: application/json

{
  "firstName": "Jordan",
  "lastName": "Lee",
  "email": "jordan.lee@bank.com",
  "phone": "+31690000000",
  "address": "10 Bank Avenue, Rotterdam"
}
```

### Create an Account
```
POST http://localhost:8080/api/accounts
Content-Type: application/json

{
  "accountNumber": "NL91ABNA0417164300",
  "customerId": 1,
  "type": "Savings",
  "balance": 5000.00
}
```

### Create a Transaction
```
POST http://localhost:8080/api/transactions
Content-Type: application/json

{
  "accountId": 1,
  "type": "Deposit",
  "amount": 750.00
}
```

## Troubleshooting

### If a service fails to start:
1. Check if the port is already in use
2. Ensure Eureka Server is running first
3. Check console logs for specific errors

### If services don't appear in Eureka:
1. Wait 30-60 seconds for registration
2. Check Eureka server logs
3. Verify application.properties configuration

### If API calls fail:
1. Ensure API Gateway is running
2. Check service registration in Eureka
3. Verify correct URL paths

## Project Structure in STS
```
banking-system/
├── eureka-server/           # Service Discovery
├── config-server/          # Configuration Management
├── api-gateway/            # API Gateway
├── accounts-service/       # Account Operations
├── customers-service/      # Customer Management
└── transactions-service/   # Transaction Processing
```

This setup provides a complete microservices architecture that's easy to develop, test, and demonstrate!
