# 💰 Expense Tracker

A full-stack personal expense tracking application built with **Spring Boot** and **Ember.js**.

Track income, expenses, set monthly budgets, and visualize spending patterns with category-based analytics and daily charts.

## 🛠 Tech Stack

### Backend
- **Java 17** (Zulu OpenJDK)
- **Spring Boot 3.5** — REST API framework
- **Spring Data JPA** — ORM with Hibernate
- **Spring Security** — Authentication & authorization
- **JWT (JSON Web Token)** — Stateless authentication
- **MySQL 8** — Relational database
- **Gradle** — Build tool
- **JUnit 5 + MockMvc** — Integration testing

### Frontend
- **Ember.js (Octane)** — SPA framework
- **Ember Data** — Data management
- **Handlebars** — Templating engine

## 📋 Features

- **User Authentication** — Register, login with JWT tokens, BCrypt password hashing
- **Expense & Income Tracking** — Add, edit, delete transactions with categories
- **Category Management** — Create custom categories with icons and colors
- **Dashboard Analytics** — Monthly summary with total income, expense, and balance
- **Spending by Category** — Category-wise expense breakdown (pie chart data)
- **Daily Spending Trend** — Day-by-day expense data (bar chart data)
- **Date Range Filtering** — Filter transactions by custom date ranges
- **Budget Management** — Set monthly budgets per category
- **Input Validation** — Server-side validation with meaningful error messages
- **Global Exception Handling** — Clean JSON error responses for all error types

## 🏗 Architecture

```
HTTP Request (Postman / Ember.js / Browser)
         │
         ▼
┌─────────────────────────┐
│  Controller Layer        │   Handles HTTP requests & responses
│  (REST API endpoints)    │   @RestController, @GetMapping, @PostMapping
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│  Service Layer           │   Business logic & validation
│  (Business Logic)        │   @Service, @Transactional
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│  Repository Layer        │   Database operations (auto-generated SQL)
│  (Data Access)           │   JpaRepository, @Query (JPQL)
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│  MySQL Database          │   Persistent storage
│  (InnoDB Engine)         │   Auto-created tables via Hibernate
└─────────────────────────┘
```

## 📁 Project Structure

```
expense-tracker/
├── backend/                          # Spring Boot application
│   ├── build.gradle                  # Dependencies & build config
│   ├── src/main/java/com/expensetracker/
│   │   ├── BackendApplication.java   # Entry point (@SpringBootApplication)
│   │   ├── config/
│   │   │   └── SecurityConfig.java   # Spring Security + BCrypt config
│   │   ├── controller/
│   │   │   ├── AuthController.java           # POST /api/auth/register, /login
│   │   │   ├── CategoryController.java       # CRUD /api/categories
│   │   │   ├── TransactionController.java    # CRUD /api/transactions
│   │   │   └── DashboardController.java      # GET /api/dashboard/summary
│   │   ├── service/
│   │   │   ├── AuthService.java              # Registration + login logic
│   │   │   ├── CategoryService.java          # Category CRUD logic
│   │   │   ├── TransactionService.java       # Transaction CRUD logic
│   │   │   └── DashboardService.java         # Analytics (SUM, GROUP BY)
│   │   ├── repository/
│   │   │   ├── UserRepository.java           # User queries
│   │   │   ├── CategoryRepository.java       # Category queries
│   │   │   ├── TransactionRepository.java    # Transaction queries + custom JPQL
│   │   │   └── BudgetRepository.java         # Budget queries
│   │   ├── model/
│   │   │   ├── User.java                     # users table
│   │   │   ├── Category.java                 # categories table
│   │   │   ├── Transaction.java              # transactions table
│   │   │   ├── Budget.java                   # budgets table
│   │   │   └── TransactionType.java          # INCOME / EXPENSE enum
│   │   ├── dto/
│   │   │   ├── RegisterRequest.java          # Registration input + validation
│   │   │   ├── LoginRequest.java             # Login input + validation
│   │   │   ├── AuthResponse.java             # Auth response (token + user)
│   │   │   ├── CategoryDTO.java              # Category input + validation
│   │   │   ├── TransactionDTO.java           # Transaction input + validation
│   │   │   ├── BudgetDTO.java                # Budget input + validation
│   │   │   └── DashboardDTO.java             # Dashboard response
│   │   └── exception/
│   │       ├── ResourceNotFoundException.java    # 404 errors
│   │       ├── BadRequestException.java          # 400 errors
│   │       └── GlobalExceptionHandler.java       # Central error handler
│   ├── src/main/resources/
│   │   └── application.properties    # MySQL, JPA, JWT, logging config
│   └── src/test/java/com/expensetracker/
│       ├── TestCleanup.java          # Database cleanup between tests
│       └── controller/
│           ├── AuthControllerTest.java
│           ├── CategoryControllerTest.java
│           ├── TransactionControllerTest.java
│           └── DashboardControllerTest.java
└── frontend/                         # Ember.js application (coming soon)
```

## 🗄 Database Schema

```sql
┌──────────────┐       ┌──────────────────┐       ┌──────────────────┐
│    users     │       │   categories     │       │   transactions   │
├──────────────┤       ├──────────────────┤       ├──────────────────┤
│ id (PK)      │◄──┐   │ id (PK)          │◄──┐   │ id (PK)          │
│ name         │   │   │ name             │   │   │ amount           │
│ email (UQ)   │   │   │ icon             │   │   │ type (ENUM)      │
│ password     │   │   │ color            │   │   │ note             │
│ created_at   │   │   │ type (ENUM)      │   │   │ date             │
└──────────────┘   │   │ user_id (FK) ────┘   │   │ category_id (FK)─┘
                   │   └──────────────────┘   │   │ user_id (FK) ────┐
                   │                           │   │ created_at       │
                   │   ┌──────────────────┐   │   └──────────────────┘
                   │   │    budgets       │   │
                   │   ├──────────────────┤   │
                   │   │ id (PK)          │   │
                   │   │ monthly_limit    │   │
                   │   │ month            │   │
                   │   │ year             │   │
                   └───│ user_id (FK)     │   │
                       │ category_id (FK)─┘   │
                       └──────────────────┘
```

## 🚀 Getting Started

### Prerequisites

- Java 17+
- MySQL 8+
- Node.js 18+ (for frontend)
- Ember CLI (for frontend)

### Backend Setup

```bash
# 1. Clone the repository
git clone https://github.com/YOUR_USERNAME/expense-tracker.git
cd expense-tracker/backend

# 2. Start MySQL and create database (auto-created on first run)
mysql -u root -e "SELECT 1;"   # verify MySQL is running

# 3. Run the application
./gradlew bootRun

# Server starts at http://localhost:8080
```

### Run Tests

```bash
# Run all 21 integration tests
./gradlew test

# View HTML test report
open build/reports/tests/test/index.html
```

## 📡 API Endpoints

### Authentication

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|:---:|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/login` | Login (returns JWT) | No |

### Categories

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|:---:|
| GET | `/api/categories?userId={id}` | List all categories | Yes |
| POST | `/api/categories?userId={id}` | Create category | Yes |
| DELETE | `/api/categories/{id}?userId={id}` | Delete category | Yes |

### Transactions

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|:---:|
| GET | `/api/transactions?userId={id}` | List all transactions | Yes |
| GET | `/api/transactions?userId={id}&startDate=YYYY-MM-DD&endDate=YYYY-MM-DD` | Filter by date | Yes |
| GET | `/api/transactions/{id}?userId={id}` | Get single transaction | Yes |
| POST | `/api/transactions?userId={id}` | Create transaction | Yes |
| PUT | `/api/transactions/{id}?userId={id}` | Update transaction | Yes |
| DELETE | `/api/transactions/{id}?userId={id}` | Delete transaction | Yes |

### Dashboard

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|:---:|
| GET | `/api/dashboard/summary?userId={id}&month={M}&year={Y}` | Monthly summary | Yes |

## 📝 API Usage Examples

### Register

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice","email":"alice@test.com","password":"password123"}'
```

```json
{
  "token": "eyJhbGciOiJIUzI1...",
  "name": "Alice",
  "email": "alice@test.com"
}
```

### Create Category

```bash
curl -X POST "http://localhost:8080/api/categories?userId=1" \
  -H "Content-Type: application/json" \
  -d '{"name":"Food","icon":"🍔","color":"#FF5733","type":"EXPENSE"}'
```

### Create Transaction

```bash
curl -X POST "http://localhost:8080/api/transactions?userId=1" \
  -H "Content-Type: application/json" \
  -d '{"amount":500,"type":"EXPENSE","note":"Lunch","date":"2025-07-15","categoryId":1}'
```

### Dashboard Summary

```bash
curl "http://localhost:8080/api/dashboard/summary?userId=1&month=7&year=2025"
```

```json
{
  "totalIncome": 85000.0,
  "totalExpense": 19150.0,
  "balance": 65850.0,
  "byCategory": [
    { "category": "Rent", "total": 15000.0 },
    { "category": "Food", "total": 1300.0 },
    { "category": "Transport", "total": 350.0 }
  ],
  "dailyExpenses": [
    { "date": "2025-07-01", "total": 15000.0 },
    { "date": "2025-07-10", "total": 800.0 },
    { "date": "2025-07-15", "total": 500.0 }
  ]
}
```

## ✅ Testing

21 integration tests covering all API endpoints:

| Test Class | Tests | Covers |
|------------|:-----:|--------|
| AuthControllerTest | 4 | Register, duplicate email, login, wrong password |
| CategoryControllerTest | 5 | Create, validation, list, delete |
| TransactionControllerTest | 8 | CRUD operations, validation, 404 handling |
| DashboardControllerTest | 3 | Monthly summary, empty month |

```bash
./gradlew test
# 21 tests completed, 0 failed
```

## 🔒 Security Features

- **BCrypt Password Hashing** — Passwords are never stored in plain text
- **JWT Authentication** — Stateless token-based auth
- **Input Validation** — Server-side validation using Jakarta Bean Validation
- **SQL Injection Prevention** — JPA/Hibernate uses parameterized queries
- **Global Exception Handling** — No stack traces exposed to clients

## 🔧 Configuration

Key settings in `application.properties`:

| Property | Value | Description |
|----------|-------|-------------|
| `server.port` | 8080 | Server port |
| `spring.jpa.hibernate.ddl-auto` | update | Auto-create/update tables |
| `spring.jpa.show-sql` | true | Print SQL to console |
| `jwt.expiration` | 86400000 | Token expiry (24 hours) |

## 📌 Key Learnings Demonstrated

- **Layered Architecture** — Controller → Service → Repository separation
- **ORM with Hibernate** — Entity mapping, relationships, JPQL queries
- **DTO Pattern** — Separate API shape from database entities
- **Dependency Injection** — Constructor-based injection throughout
- **Validation** — Declarative validation with custom error handling
- **Testing** — Integration tests with MockMvc and Spring Boot Test
- **Database Design** — Foreign keys, enums, timestamps, constraints

## 🗺 Roadmap

- [x] REST API (CRUD for users, categories, transactions)
- [x] Dashboard analytics (SUM, GROUP BY)
- [x] Integration tests (21 tests)
- [x] BCrypt password hashing
- [ ] JWT token authentication (in progress)
- [ ] Ember.js frontend with charts
- [ ] CSV export
- [ ] Budget alerts
- [ ] Docker deployment