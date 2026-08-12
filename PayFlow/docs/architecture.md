# PayFlow API - Architecture

## 1. Overview

PayFlow is a simple REST API built using Spring Boot to simulate a digital payment backend.

The application follows a layered architecture consisting of:
- Controller Layer
- Service Layer
- Repository Layer
- Persistence Layer (H2 Database)

The application exposes REST endpoints that allow clients to:
- Register users
- Retrieve users
- Search users by UPI ID
- Record money transfer transactions

This project intentionally keeps the design simple for learning Spring Boot fundamentals.

---

# 2. Architecture

            HTTP Request
                    │
                    ▼
        +-------------------+
        |   Controller      |
        +-------------------+
                    │
                    ▼
        +-------------------+
        |     Service       |
        +-------------------+
                    │
                    ▼
        +-------------------+
        |   Repository      |
        +-------------------+
                    │
                    ▼
        +-------------------+
        | Hibernate (JPA)   |
        +-------------------+
                    │
                    ▼
        +-------------------+
        |    H2 Database    |
        +-------------------+

---

# 3. Package Structure

src
└── main
    └── java
        └── com.airtribe.payflow
            │
            ├── controller
            │      ├── UserController
            │      └── TransactionController
            │
            ├── service
            │      ├── UserService
            │      └── TransactionService
            │
            ├── repository
            │      ├── UserRepository
            │      └── TransactionRepository
            │
            ├── entity
            │      ├── User
            │      └── Transaction
            │
            └── PayflowApplication

---

# 4. Layer Responsibilities

## Controller Layer

Responsible for:
    - Receiving HTTP requests
    - Mapping URLs to methods
    - Accepting request bodies
    - Returning HTTP responses

Controllers do not directly interact with the database.

Classes:
    - UserController
    - TransactionController

---

## Service Layer

Responsible for:
    - Business logic
    - Calling repository methods
    - Coordinating operations

Services act as the bridge between controllers and repositories.

Classes:
    - UserService
    - TransactionService

---

## Repository Layer

Responsible for:
    - Database interaction
    - CRUD operations
    - Query execution

Spring Data JPA automatically generates implementations at runtime.

Classes:
    - UserRepository
    - TransactionRepository

---

## Persistence Layer

Responsible for:
    - Storing data
    - Creating tables
    - Executing SQL

Implemented using:
    - Hibernate ORM
    - H2 Database

---

# 5. Entity Design

The assignment specifies two entities.

No foreign key relationships should be created between them.

---

## User Entity

Represents a registered user.

Fields:
┌─────────────┬────────┐
|    Field    |  Type  |
├─────────────┼────────┤
| userId      | Long   |
| name        | String |
| upiId       | String |
| balance     | Double |
| phoneNumber | String |
└─────────────┴────────┘

Primary Key - userId
Auto Generated - Yes

Constraints:
- upiId must be unique
- phoneNumber must be unique

---

## Transaction Entity

Represents one money transfer.

Fields:
┌───────────────┬────────┐
|     Field     |  Type  |
├───────────────┼────────┤
| transactionId | Long   |
| senderUpiId   | String |
| receiverUpiId | String |
| amount        | Double |
| note          | String |
└───────────────┴────────┘

Primary Key - transactionId
Auto Generated - Yes

Important Restriction:

Do NOT create
- @ManyToOne
- @OneToMany
- Foreign Keys

Sender and receiver should be stored as plain String values containing UPI IDs.

---

# 6. Class Relationships


UserController
       │
       ▼
UserService
       │
       ▼
UserRepository
       │
       ▼
User Entity


TransactionController
          │
          ▼
TransactionService
          │
          ▼
TransactionRepository
          │
          ▼
Transaction Entity

There is intentionally **no relationship** between the User entity and the Transaction entity.

---

# 7. Repository Design

## UserRepository

Extends JpaRepository<User, Long>

Methods:
    - save()
    - findAll()
    - findById()
    - findByUpiId()
    - findUsersWithBalanceGreaterThan()

---

## TransactionRepository

Extends JpaRepository<Transaction, Long>

Methods:
    - save()
    - findAll()
    - findById()

---

# 8. Service Design

## UserService

Responsibilities:
    - Register user
    - Get all users
    - Get user by ID
    - Find user by UPI ID

Methods:
    - registerUser(User user)
    - getAllUsers()
    - getUserById(Long id)
    - findByUpiId(String upiId)

---

## TransactionService

Responsibilities:
    - Record a transaction.
    - The assignment explicitly states that balance deduction and validation are **not required** in this phase.

Methods:
    - sendMoney(Transaction transaction)

---

# 9. Controller Design

## UserController

Base URL - /users

Endpoints:
1. POST /users
2. GET /users
3. GET /users/{id}
4. GET /users/upi/{upiId}

---

## TransactionController

Base URL - /transactions

Endpoints:
1. POST /transactions

---

# 10. Database Design

Two tables will be created automatically by Hibernate.

## USER

Columns:
    - user_id
    - name
    - upi_id
    - balance
    - phone_number

## TRANSACTION

Columns:
    - transaction_id
    - sender_upi_id
    - receiver_upi_id
    - amount
    - note

No foreign key constraints exist between these tables.

---

# 11. Dependency Flow

    Application startup
        │
        ▼
    Spring Boot
        │
        ▼
    Creates Controllers
        │
        ▼
    Creates Services
        │
        ▼
    Creates Repository Implementations
        │
        ▼
    Configures Hibernate
        │
        ▼
    Creates Database Tables


Request flow:

    Client
    │
    ▼
    Controller
    │
    ▼
    Service
    │
    ▼
    Repository
    │
    ▼
    Hibernate
    │
    ▼
    H2 Database

---

# 12. Design Decisions

The architecture intentionally follows a simple layered design to align with the learning objectives of the assignment.

Key decisions:
- Layered architecture (Controller → Service → Repository → Database)
- Automatic table creation using Spring Data JPA
- Embedded H2 database
- No SQL for table creation
- No entity relationships
- No foreign keys
- Sender and receiver stored as plain UPI ID strings
- Repository pattern using JpaRepository
- Dependency Injection using Spring's IoC container
- Spring Boot auto-configuration for web server, database, and JPA

This keeps the project focused on understanding Spring Boot fundamentals before introducing more advanced architectural patterns.