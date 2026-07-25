# PayFlow API - Technical Context

## Project Information

Project Name: PayFlow API

Purpose:
A REST API built using Spring Boot to simulate a simple digital payment backend similar to PhonePe or Google Pay.

The project demonstrates:
- Spring Boot
- REST APIs
- Spring MVC
- Spring Data JPA
- H2 Database
- Dependency Injection
- Auto Configuration
- Repository Pattern

This project is intentionally simple and is designed for learning backend development.

---

# Java Version

Recommended Java Version - Java 25

Reason:
- Supported by Spring Boot 3.5.x
- Excellent IDE support
- Future-proof

Avoid using Java 8 or Java 11 with Spring Boot 3.x.

---

# Spring Boot Version

Recommended Version - Spring Boot 3.5.4

Reason:
- Latest stable release
- Compatible with Java 21
- Compatible with latest H2 Database
- Compatible with Hibernate 6.x
- Uses Jakarta EE (jakarta.* packages)

Avoid Spring Boot 2.x because:
- Uses javax packages
- Different dependency versions
- Different Hibernate version
- Different H2 compatibility

---

# Maven

Build Tool - Apache Maven

Reason:
- Default Spring Boot build tool
- Large ecosystem
- Easy dependency management
- Widely used in industry

---

# Dependencies

The project requires only three dependencies.

## 1. Spring Web

Artifact - spring-boot-starter-web
Purpose - Provides everything required for building REST APIs.

Includes:
- Spring MVC
- Embedded Tomcat Server
- Jackson JSON
- Validation Support
- DispatcherServlet
- REST Controllers

Used For:
- @RestController
- @GetMapping
- @PostMapping
- @RequestBody
- @PathVariable
- HTTP Request Handling

---

## 2. Spring Data JPA

Artifact - spring-boot-starter-data-jpa
Purpose - Provides ORM support using Hibernate.

Includes
- Hibernate ORM
- JPA API
- Repository support
- Transaction management
- Entity Manager

Used For
- @Entity
- @Id
- @GeneratedValue
- JpaRepository
- @Query
- Automatic table creation

Hibernate Version - Hibernate ORM 6.x

---

## 3. H2 Database

Artifact - com.h2database:h2
Purpose - Lightweight in-memory relational database.

Features: 
- Zero installation
- Fast startup
- SQL support
- Browser console
- Great for learning

Modes

1. In-memory
Database disappears after application stops -> jdbc:h2:mem:testdb

2. File Mode
Database is stored on disk -> jdbc:h2:file:./data/payflow

For this project we will use: File Mode

Reason:
- Data survives restart
- Easier testing
- H2 Console always works
- Better for assignment screenshots

---

# Dependency Compatibility

┌─────────────────┬─────────┐
|    Component    | Version |
├─────────────────┼─────────┤
| Java            | 21      |
| Spring Boot     | 3.5.4   |
| Spring Framework| 6.2.x   |
| Spring Data JPA | 3.5.x   |
| Hibernate       | 6.6.x   |
| H2 Database     | 2.3.x   |
| Maven           | 3.9+    |
└─────────────────┴─────────┘

These versions are fully compatible.

---

# Why these versions?

Using the latest compatible versions avoids

- Hibernate errors
- H2 Console issues
- Jakarta vs javax conflicts
- Unsupported Java version
- Dependency mismatch

---

# H2 Console

The H2 Console allows viewing the database in a browser.

Default URL - http://localhost:8080/h2-console
Typical JDBC URL - jdbc:h2:file:./data/payflow
Driver - org.h2.Driver
Username - sa
Password - (blank)

---

# Spring Boot Features Used

## Embedded Server

Spring Boot starts an embedded Tomcat server automatically.
No external Tomcat installation is required.

---

## Auto Configuration

Spring Boot automatically configures
- Spring MVC
- Jackson
- Hibernate
- DataSource
- H2 Database
- Repository Beans

without XML configuration.

---

## Production Ready Defaults

Spring Boot provides
- Sensible default configurations
- Error handling
- Logging
- Health endpoints (if actuator is added)
- Dependency version management

---

# Project Structure

src
 ├── controller
 ├── service
 ├── repository
 ├── entity
 └── PayflowApplication.java

---

# Architecture

Client
↓
Controller
↓
Service
↓
Repository
↓
Hibernate
↓
H2 Database

---

# Learning Goals

This project demonstrates
- REST API Design
- Spring MVC
- Dependency Injection
- Repository Pattern
- ORM using Hibernate
- CRUD Operations
- Database Persistence
- JSON Serialization
- Spring Boot Auto Configuration