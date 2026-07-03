# 💰 Expense Tracker

A full-stack personal expense tracking application built with **Spring Boot** and **Ember.js**.

Track income, expenses, set monthly budgets, and visualize spending patterns with category-based analytics and daily charts.

> **One command to run everything** — the Ember frontend is built into the Spring Boot server. Start the backend and open `http://localhost:8080` to use the full app — no separate frontend server needed.

---

## 🛠 Tech Stack

### Backend
- **Java 17** (Zulu OpenJDK)
- **Spring Boot 3.5** — REST API framework
- **Spring Data JPA** — ORM with Hibernate
- **Spring Security** — Authentication & authorization
- **JWT (JSON Web Token)** — Stateless authentication via HttpOnly cookies
- **BCrypt** — Password hashing
- **MySQL 8** — Relational database
- **Gradle** — Build tool
- **JUnit 5 + MockMvc** — Integration testing

### Frontend
- **Ember.js (Octane)** — Single Page Application framework
- **Ember Data** — Data management via `this.store`
- **Handlebars (.hbs)** — Templating engine
- **ember-pikaday** — Date/month picker
- **SCSS** — Styling

---

## 📋 Features

- **User Authentication** — Register and login with JWT tokens stored as HttpOnly cookies
- **Expense & Income Tracking** — Add and delete transactions with categories
- **Category Management** — Create custom categories with icons and colors
- **Dashboard Analytics** — Monthly summary with total income, expense, and balance
- **Month Filter** — Switch between months to view historical data
- **Spending by Category** — Category-wise expense breakdown with bar charts
- **Daily Spending Trend** — Day-by-day expense visualization
- **Budget Management** — Set monthly budgets per category
- **Input Validation** — Server-side validation with meaningful error messages
- **Global Exception Handling** — Clean JSON error responses

---

## 🏗 Architecture

```
Browser (localhost:8080)
         │
         ▼
┌─────────────────────────────────────────┐
│           Spring Boot Server            │
│                                         │
│  GET /          → serves index.html     │  ← Ember SPA boots here
│  GET /assets/*  → serves JS/CSS bundles │
│  POST /api/auth/login   ┐               │
│  GET  /api/transactions ├─ REST API     │
│  GET  /api/dashboard    ┘               │
└────────────────┬────────────────────────┘
                 │
                 ▼
        ┌────────────────┐
        │   MySQL 8      │
        │  (local/cloud) │
        └────────────────┘
```

### How the frontend is served

The Ember app is compiled into static files (`index.html` + `assets/`) and placed inside Spring Boot's `src/main/resources/static/` directory. Spring Boot automatically serves these files — no separate frontend server or Node.js process is needed in production.

Hash-based routing (`locationType: 'hash'`) is used so all Ember routes (e.g. `/#/dashboard`, `/#/transactions`) resolve to `/` on the server — Spring Boot always serves `index.html` and Ember handles the rest client-side.

---

## 📁 Project Structure

```
Spring_Expense_Tracker/
├── backend/                              # Spring Boot application
│   ├── build.gradle
│   ├── src/main/java/com/expensetracker/
│   │   ├── BackendApplication.java       # Entry point
│   │   ├── config/
│   │   │   ├── SecurityConfig.java       # Spring Security + CORS + JWT filter
│   │   │   ├── JwtAuthFilter.java        # JWT cookie validation per request
│   │   │   └── JwtProperties.java        # JWT config properties
│   │   ├── controller/
│   │   │   ├── AuthController.java       # POST /api/auth/register, /login, /logout
│   │   │   ├── CategoryController.java   # CRUD /api/categories
│   │   │   ├── TransactionController.java# CRUD /api/transactions
│   │   │   └── DashboardController.java  # GET /api/dashboard/summary
│   │   ├── service/
│   │   │   ├── AuthService.java
│   │   │   ├── CategoryService.java
│   │   │   ├── TransactionService.java
│   │   │   └── DashboardService.java
│   │   ├── repository/
│   │   │   ├── UserRepository.java
│   │   │   ├── CategoryRepository.java
│   │   │   ├── TransactionRepository.java
│   │   │   └── BudgetRepository.java
│   │   ├── model/
│   │   │   ├── User.java
│   │   │   ├── Category.java
│   │   │   ├── Transaction.java
│   │   │   ├── Budget.java
│   │   │   └── TransactionType.java      # INCOME / EXPENSE enum
│   │   ├── dto/
│   │   │   ├── RegisterRequest.java
│   │   │   ├── LoginRequest.java
│   │   │   ├── CategoryDTO.java
│   │   │   ├── TransactionDTO.java
│   │   │   ├── BudgetDTO.java
│   │   │   └── DashboardDTO.java
│   │   └── exception/
│   │       ├── ResourceNotFoundException.java
│   │       ├── BadRequestException.java
│   │       └── GlobalExceptionHandler.java
│   └── src/main/resources/
│       ├── application.properties
│       └── static/                       # ← Ember production build lives here
│           ├── index.html
│           └── assets/
│               ├── frontend.js
│               ├── frontend.css
│               ├── vendor.js
│               └── vendor.css
│
└── frontend/                             # Ember.js application
    ├── app/
    │   ├── routes/
    │   │   ├── index.js                  # Redirects to login or dashboard
    │   │   ├── login.js
    │   │   ├── register.js
    │   │   ├── dashboard.js
    │   │   ├── transactions.js
    │   │   └── categories.js
    │   ├── controllers/
    │   ├── templates/
    │   ├── components/
    │   │   ├── app-sidebar.js/.hbs
    │   │   └── dashboard-summary.js/.hbs
    │   └── services/
    │       ├── api.js                    # All API calls via this.store.ajax()
    │       ├── auth.js                   # Session state + login/logout
    │       └── store.js                  # Extended store with ajax() method
    ├── config/
    │   └── environment.js                # API_HOST per environment
    └── ember-cli-build.js
```

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- MySQL 8+
- Node.js 18+ (only needed to rebuild the frontend)
- Ember CLI (only needed to rebuild the frontend)

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/expense-tracker.git
cd expense-tracker
```

### 2. Create the MySQL database

```bash
mysql -u root -e "CREATE DATABASE IF NOT EXISTS expense_tracker;"
```

Or run the full schema manually:

```sql
USE expense_tracker;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME(6),
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    icon VARCHAR(20),
    color VARCHAR(7),
    type ENUM('INCOME','EXPENSE') NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    amount FLOAT(53) NOT NULL,
    type ENUM('INCOME','EXPENSE') NOT NULL,
    note VARCHAR(255),
    date DATE NOT NULL,
    category_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at DATETIME(6),
    PRIMARY KEY (id),
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS budgets (
    id BIGINT NOT NULL AUTO_INCREMENT,
    monthly_limit FLOAT(53) NOT NULL,
    month INTEGER NOT NULL,
    year INTEGER NOT NULL,
    category_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;
```

### 3. Configure the database

Edit `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker
spring.datasource.username=root
spring.datasource.password=your_password
```

### 4. Start the server

```bash
cd backend
./gradlew bootRun
```

### 5. Open the app

```
http://localhost:8080
```

The Ember frontend loads automatically — register a new account and start tracking.

> The frontend is already built and bundled inside `backend/src/main/resources/static/`. No Node.js or Ember CLI needed just to run the app.

---

## 🔄 Rebuilding the Frontend

Only needed if you make changes to the Ember source code.

```bash
# 1. Build Ember for production
cd frontend
ember build --environment=production

# 2. Copy build output into Spring Boot's static folder
cp -r dist/* ../backend/src/main/resources/static/

# 3. Restart the backend
cd ../backend
./gradlew bootRun
```

### Development mode (frontend + backend separately)

If you want live-reload while developing the Ember frontend:

```bash
# Terminal 1 — Spring Boot backend
cd backend
./gradlew bootRun

# Terminal 2 — Ember dev server
cd frontend
ember serve
```

Then open `http://localhost:4200` — Ember proxies API calls to Spring Boot at `http://localhost:8080`.

The `API_HOST` in `frontend/config/environment.js` controls this:

```js
APP: {
  API_HOST: 'http://localhost:8080',  // dev — separate servers
},

if (environment === 'production') {
  ENV.APP.API_HOST = '';              // prod — same origin, relative URLs
}
```

---

## 📡 API Endpoints

### Authentication

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|:----:|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/login` | Login, sets JWT cookie | No |
| POST | `/api/auth/logout` | Logout, clears cookie | No |
| GET | `/api/auth/me` | Get current user | Yes |

### Categories

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|:----:|
| GET | `/api/categories?userId={id}` | List all categories | Yes |
| POST | `/api/categories?userId={id}` | Create category | Yes |
| DELETE | `/api/categories/{id}?userId={id}` | Delete category | Yes |

### Transactions

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|:----:|
| GET | `/api/transactions?userId={id}` | List all transactions | Yes |
| GET | `/api/transactions?userId={id}&startDate=YYYY-MM-DD&endDate=YYYY-MM-DD` | Filter by date | Yes |
| GET | `/api/transactions/{id}?userId={id}` | Get single transaction | Yes |
| POST | `/api/transactions?userId={id}` | Create transaction | Yes |
| PUT | `/api/transactions/{id}?userId={id}` | Update transaction | Yes |
| DELETE | `/api/transactions/{id}?userId={id}` | Delete transaction | Yes |

### Dashboard

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|:----:|
| GET | `/api/dashboard/summary?userId={id}&month={M}&year={Y}` | Monthly summary | Yes |

---

## 📝 API Usage Examples

### Register

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice","email":"alice@test.com","password":"password123"}'
```

```json
{
  "userId": 1,
  "name": "Alice",
  "email": "alice@test.com",
  "message": "Registered successfully"
}
```

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"alice@test.com","password":"password123"}'
```

JWT token is returned as an **HttpOnly cookie** — not in the response body. This protects the token from JavaScript access (XSS protection).

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
    { "category": "Food", "total": 1300.0 }
  ],
  "dailyExpenses": [
    { "date": "2025-07-01", "total": 15000.0 },
    { "date": "2025-07-15", "total": 500.0 }
  ]
}
```

---

## ✅ Testing

```bash
cd backend
./gradlew test

# View HTML report
open build/reports/tests/test/index.html
```

| Test Class | Tests | Covers |
|------------|:-----:|--------|
| AuthControllerTest | 4 | Register, duplicate email, login, wrong password |
| CategoryControllerTest | 5 | Create, validation, list, delete |
| TransactionControllerTest | 8 | CRUD, validation, 404 handling |
| DashboardControllerTest | 3 | Monthly summary, empty month |

---

## 🗄 Database Schema

```
┌──────────────┐       ┌──────────────────┐       ┌──────────────────┐
│    users     │       │   categories     │       │   transactions   │
├──────────────┤       ├──────────────────┤       ├──────────────────┤
│ id (PK)      │◄──┐   │ id (PK)          │◄──┐   │ id (PK)          │
│ name         │   │   │ name             │   │   │ amount           │
│ email (UQ)   │   │   │ icon             │   │   │ type (ENUM)      │
│ password     │   │   │ color            │   │   │ note             │
│ created_at   │   │   │ type (ENUM)      │   │   │ date             │
└──────────────┘   └───│ user_id (FK)     │   └───│ category_id (FK) │
                       └──────────────────┘       │ user_id (FK)     │
                                                  │ created_at       │
                       ┌──────────────────┐       └──────────────────┘
                       │    budgets       │
                       ├──────────────────┤
                       │ id (PK)          │
                       │ monthly_limit    │
                       │ month            │
                       │ year             │
                       │ user_id (FK)     │
                       │ category_id (FK) │
                       └──────────────────┘
```

---

## 🔒 Security

- **BCrypt** — passwords hashed before storage, never stored in plain text
- **JWT via HttpOnly cookie** — token inaccessible to JavaScript, protects against XSS
- **Spring Security filter** — every `/api/*` request (except auth endpoints) validated against JWT cookie
- **CORS** — only `localhost:4200` and `localhost:8080` allowed in development
- **Input Validation** — Jakarta Bean Validation on all request DTOs
- **No stack traces** — GlobalExceptionHandler returns clean JSON errors only

---

## ⚙️ Configuration

`backend/src/main/resources/application.properties`:

| Property | Default | Description |
|----------|---------|-------------|
| `server.port` | `8080` | Server port |
| `spring.jpa.hibernate.ddl-auto` | `none` | Schema management — `none` to protect data |
| `spring.jpa.show-sql` | `true` | Print SQL in console |
| `jwt.secret` | (set in properties) | JWT signing secret |
| `jwt.expiration` | `86400000` | Token expiry in ms (24 hours) |

> **Important**: Set `spring.jpa.hibernate.ddl-auto=none` to prevent Hibernate from dropping your tables on restart. Run the schema SQL once manually, then leave it on `none`.

---

## ☁️ Deployment (Railway)

1. Push to GitHub
2. Create a new project on [railway.app](https://railway.app)
3. Add a MySQL database service
4. Set environment variables:
   ```
   SPRING_DATASOURCE_URL=jdbc:mysql://...
   SPRING_DATASOURCE_USERNAME=root
   SPRING_DATASOURCE_PASSWORD=...
   JWT_SECRET=your-strong-secret-here
   JWT_EXPIRATION=86400000
   ```
5. Railway auto-deploys on every push to `main`

---

## 🗺 Roadmap

- [x] REST API — CRUD for users, categories, transactions
- [x] Dashboard analytics — SUM, GROUP BY queries
- [x] Integration tests — 21 tests
- [x] BCrypt password hashing
- [x] JWT authentication via HttpOnly cookies
- [x] Ember.js frontend — login, register, dashboard, transactions, categories
- [x] Frontend served from Spring Boot (single server, one port)
- [x] Month filter on dashboard
- [ ] CSV export
- [ ] Budget alerts
- [ ] Docker deployment
- [ ] Password reset flow