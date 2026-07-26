# PayFlow API Overview

PayFlow is a REST API built using Spring Boot that simulates a simplified digital payment backend similar to PhonePe or Google Pay. The application allows users to register, retrieve user information, and record money transfer transactions through REST endpoints. It uses Spring Data JPA for persistence and an H2 database for storing application data.

This project is intended as a learning exercise to understand the fundamentals of Spring Boot, REST APIs, dependency injection, and database integration.

---

# Technology Stack

* Java 25
* Spring Boot 3.5.4
* Spring Web
* Spring Data JPA
* Hibernate ORM
* H2 Database
* Maven

---

# Project Structure

src
└── main
    └── java
        └── com.airtribe.payflow
            │
            ├── controller
            │      ├── UserController.java
            │      └── TransactionController.java
            │
            ├── service
            │      ├── UserService.java
            │      └── TransactionService.java
            │
            ├── repository
            │      ├── UserRepository.java
            │      └── TransactionRepository.java
            │
            ├── entity
            │      ├── User.java
            │      └── Transaction.java
            │
            └── PayflowApplication.java

---

# Project Layers

The application follows a layered architecture where each layer has a single responsibility.

## Controller Layer

The Controller layer receives HTTP requests from clients and exposes REST endpoints.

Responsibilities:
* Handle incoming HTTP requests
* Accept request parameters and request bodies
* Invoke the appropriate service methods
* Return HTTP responses

Example:
* Register a new user
* Retrieve all users
* Record a transaction

---

## Service Layer

The Service layer contains the application's business logic.

Responsibilities:
* Process client requests
* Coordinate between controllers and repositories
* Implement application logic
* Prepare data before saving or returning it

The controller never communicates directly with the database. Every request passes through the service layer.

---

## Repository Layer

The Repository layer is responsible for interacting with the database.

Responsibilities:
* Save entities
* Retrieve entities
* Update entities
* Execute queries

The repositories extend 'JpaRepository', allowing Spring Data JPA to generate database operations automatically without writing SQL.

---

## Entity Layer

Entities represent the database tables.

In this project there are two entities:
* User
* Transaction

Hibernate uses these entity classes to automatically create the corresponding database tables.

---

# How to Run the Application

## Prerequisites

* Java 25
* Maven 3.9 or above

---

## Step 1 - Clone the Repository

git clone <repository-url>

---

## Step 2 - Navigate to the Project

cd PayFlow

---

## Step 3 - Build the Project

Using Maven:
    mvn clean install

or using the Maven Wrapper:
    ./mvnw clean install

Windows:
    mvnw.cmd clean install

---

## Step 4 - Run the Application

Using Maven: mvn spring-boot:run

---

## Step 5 - Verify Application Startup

If the application starts successfully, you should see messages indicating that:
* Tomcat has started
* Spring Boot has initialized the application context
* Hibernate has created the database tables
* The application is listening on port 8080

---

## Step 6 - Access H2 Console

Open your browser and navigate to "http://localhost:8080/h2-console".
Use the configured JDBC URL, username, and password from `application.properties`.

---

# Spring Boot Features Used in PayFlow

Spring Boot provides several features that simplify backend application development. Three of these features are demonstrated in this project.

## 1. Embedded Server

Spring Boot includes an embedded Tomcat server within the application.

In the PayFlow project, no external web server needs to be installed or configured. When the application starts, Spring Boot automatically starts the embedded Tomcat server and deploys the REST API.

This allows the application to begin accepting HTTP requests immediately by simply running the main application class.

---

## 2. Auto-Configuration

Spring Boot automatically configures many components based on the project's dependencies.

In this project, Spring Boot automatically configures:
* Spring MVC for REST APIs
* Hibernate as the JPA implementation
* H2 database connection
* Entity scanning
* Repository implementations
* Jackson for JSON serialization and deserialization

Because of auto-configuration, very little manual configuration is required. Adding the appropriate dependencies is sufficient for Spring Boot to configure most of the application automatically.

---

## 3. Production-Ready Defaults

Spring Boot provides sensible default configurations that allow developers to build applications quickly.

In the PayFlow project, Spring Boot automatically provides:
* Embedded Tomcat configuration
* Default logging
* Error handling
* Dependency version management
* Automatic JSON conversion
* Hibernate integration
* Application startup configuration

These defaults allow the application to run with minimal configuration while still following recommended practices.

---

# Summary

PayFlow demonstrates the core concepts of Spring Boot by building a simple REST API using a layered architecture. Spring Boot reduces boilerplate through embedded server support, auto-configuration, and production-ready defaults, allowing developers to focus on implementing application logic rather than configuring infrastructure.

# Note
    Answers to the questions, screenshots and conceptual write ups are present in /docs/write_up folder.