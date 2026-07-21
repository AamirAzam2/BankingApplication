# Banking App APIs

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

A **RESTful Banking Backend** built with **Spring Boot 4** and **Java 21**. The application provides secure APIs for user account management, fund transfers, transaction history, bank statement generation (PDF), and email notifications — all protected via **JWT-based authentication**.

---

## ✨ Features

🔐**Security & Authentication**
- JWT-based stateless authentication using Spring Security
- Secure password encryption using BCrypt
- Role-based access control for protected APIs
- Authentication and authorization using Spring Security filters

👤**User & Account Management**
- User registration with automatic account number generation
- Secure login with JWT token generation
- Email notifications for account creation and login activities
- Account profile and balance management

💳**Banking Operations**
- Credit and debit transactions
- Account-to-account fund transfers
- Balance enquiry services
- Account holder name enquiry
- Transaction history management

📄**Reporting & Notifications**
- Bank statement generation in PDF format
- Date-range based transaction filtering
- Pagination and sorting for transaction records
- Email delivery of generated bank statements using Spring Mail

📚**API Documentation**
- Interactive API documentation using Swagger/OpenAPI
- Clear request and response models for API consumers

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0.2 |
| Security | Spring Security + JWT (JJWT 0.13.0) |
| Database | MySQL (production) |
| ORM | Spring Data JPA / Hibernate |
| PDF Generation | iTextPDF 5.5.13.5 |
| API Docs | Springdoc OpenAPI (Swagger UI) |
| Boilerplate | Lombok |
| Build Tool | Maven (with Maven Wrapper) |

---

## 📁 Project Structure

```
com.bank.application
│
├── src/
│   └── main/
│       │
│       ├── java/
│       │   └── com/
│       │       └── bank/
│       │           └── application/
│       │               │
│       │               ├── BankingApplication.java
│       │               │
│       │               ├── config/
│       │               │   ├── SecurityConfig.java
│       │               │   ├── JwtAuthenticationFilter.java
│       │               │   ├── JwtTokenProvider.java
│       │               │   └── JwtAuthenticationEntryPoint.java
│       │               │
│       │               ├── controller/
│       │               │   ├── UserController.java
│       │               │   └── TransactionController.java
│       │               │
│       │               ├── dto/
│       │               │   ├── AccountInfo.java
│       │               │   ├── BankResponse.java
│       │               │   ├── CreditDebitRequest.java
│       │               │   ├── EmailDetails.java
│       │               │   ├── EnquiryRequest.java
│       │               │   ├── LoginRequest.java
│       │               │   ├── LoginResponse.java
│       │               │   ├── NameEnquiryResponse.java
│       │               │   ├── TransactionDto.java
│       │               │   ├── TransactionRequest.java
│       │               │   ├── TransactionResponse.java
│       │               │   ├── TransferRequest.java
│       │               │   └── UserRequest.java
│       │               │
│       │               ├── entity/
│       │               │   ├── Role.java
│       │               │   ├── Transaction.java
│       │               │   └── User.java
│       │               │
│       │               ├── repository/
│       │               │   ├── TransactionRepository.java
│       │               │   └── UserRepository.java
│       │               │
│       │               ├── service/
│       │               │   ├── CustomUserDetailsService.java
│       │               │   ├── EmailService.java
│       │               │   ├── StatementService.java
│       │               │   ├── TransactionService.java
│       │               │   ├── UserService.java
│       │               │   │
│       │               │   └── impl/
│       │               │       ├── EmailServiceImpl.java
│       │               │       ├── StatementServiceImpl.java
│       │               │       ├── TransactionServiceImpl.java
│       │               │       └── UserServiceImpl.java
│       │               │
│       │               ├── exception/
│       │               │   ├── BankingValidationException.java
│       │               │   ├── DuplicateAccountException.java
│       │               │   ├── GlobalExceptionHandler.java
│       │               │   ├── InsufficientBalanceException.java
│       │               │   └── ResourceNotFoundException.java
│       │               │
│       │               └── utils/
│       │                   └── AccountUtils.java
│       │
│       └── resources/
│           │
│           ├── application.properties
│
├── pom.xml
│
├── mvnw
├── mvnw.cmd
│
├── .gitignore
│
├── README.md
│
└── application.properties

---

## 🚀 Quick Start

### Prerequisites

- **Java 21** or higher
- **MySQL** running locally (or any MySQL-compatible server)
- **Maven** (or use the included `mvnw` wrapper)
- A Gmail account with an **App Password** for email notifications

---

### 1. Clone the Repository

```bash
git clone
cd bank-application
```

### 2. Set Up the Database

Create a MySQL database:

```sql
CREATE DATABASE banking_app;
```

### 3. Configure `application.properties`

Edit `application.properties` in the project root:

```properties
spring.application.name=banking-app-apis

# MySQL Database
spring.datasource.url=jdbc:mysql://localhost:3306/banking_app
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Email (Gmail SMTP)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# JWT
app.jwt-secret=your-jwt-secret-key
app.jwt-expiration=864000
```

### 4. Run the Application

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

The API will start at **http://localhost:8080**

---

## 📖 API Documentation (Swagger UI)

Once the app is running, visit:

```
http://localhost:8080/swagger-ui/index.html
```

Swagger UI provides interactive documentation for all endpoints.

---

## 📡 API Endpoints

### 🔐 Authentication & User Management — `/api/user`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/api/user` | Register a new user account | ❌ |
| `POST` | `/api/user/login` | Login and receive a JWT token | ❌ |
| `GET` | `/api/user/balanceEnquiry` | Check account balance | ✅ |
| `GET` | `/api/user/nameEnquiry` | Look up account holder name | ✅ |
| `POST` | `/api/user/credit` | Credit an account | ✅ |
| `POST` | `/api/user/debit` | Debit an account | ✅ |
| `POST` | `/api/user/transfer` | Transfer funds between accounts | ✅ |

### 📄 Bank Statements — `/bankStatement`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `GET` | `/bankStatement` | Generate & retrieve statement for a date range (PDF) | ✅ |

**Query params:** `accountNumber`, `startDate` (yyyy-MM-dd), `endDate` (yyyy-MM-dd)

---

