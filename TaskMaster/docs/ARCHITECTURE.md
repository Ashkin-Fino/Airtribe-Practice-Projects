# Task Master Architecture

## 1. Overview

Task Master is a backend-only task management and collaboration application.

The system uses a layered Spring Boot architecture with PostgreSQL as the primary persistent store.

```text
                         Client
                           |
                           v
                    REST Controllers
                           |
                           v
                      DTO / Validation
                           |
                           v
                     Service Layer
                           |
             +-------------+-------------+
             |                           |
             v                           v
       Repository Layer          Security / Authorization
             |                           |
             v                           v
        PostgreSQL              JWT Authentication

Attachments:
Service Layer
     |
     +----> PostgreSQL metadata
     |
     +----> Local filesystem
```

## 2. Package Structure

```text
com.airtribe.task_master
|
+-- controller
|   +-- AuthController
|   +-- UserController
|   +-- TaskController
|   +-- TeamController
|   +-- CommentController
|   +-- AttachmentController
|
+-- dto
|   +-- Authentication DTOs
|   +-- User DTOs
|   +-- Task DTOs
|   +-- Comment DTOs
|   +-- Attachment DTOs
|
+-- entity
|   +-- User
|   +-- Task
|   +-- Team
|   +-- TeamMember
|   +-- TeamInvitation
|   +-- Comment
|   +-- Attachment
|   +-- RefreshToken
|
+-- repository
|
+-- service
|   +-- AuthService
|   +-- UserService
|   +-- TaskService
|   +-- TeamService
|   +-- CommentService
|   +-- AttachmentService
|   +-- TaskAccessService
|
+-- security
|   +-- JwtService
|   +-- JwtAuthenticationFilter
|   +-- CustomUserDetailsService
|
+-- exception
|   +-- GlobalExceptionHandler
|   +-- ErrorResponse
|   +-- Custom exceptions
```

## 3. Authentication Flow

```text
Register
   |
   v
AuthController
   |
   v
AuthService
   |
   v
Password Encoder
   |
   v
UserRepository
   |
   v
PostgreSQL
```

Login:

```text
Credentials
    |
    v
AuthController
    |
    v
AuthService
    |
    +--> Authenticate user
    |
    +--> Generate JWT
    |
    +--> Generate/validate refresh token
    |
    v
AuthResponse
```

Protected request:

```text
HTTP Request
     |
     v
JwtAuthenticationFilter
     |
     v
Extract JWT
     |
     v
Validate token
     |
     v
Load UserDetails
     |
     v
SecurityContext
     |
     v
Controller
```

## 4. Task Access Control

Task access is centralized to avoid duplicated authorization logic.

```text
                 TaskAccessService
                         |
          +--------------+--------------+
          |              |              |
          v              v              v
       Creator       Assignee       Team Member
          |              |              |
          +--------------+--------------+
                         |
                         v
                  Access Granted
```

`TaskAccessService` is reused by task-related collaboration operations.

This separation makes authorization easier to maintain and reduces the chance that different services implement inconsistent access rules.

## 5. Task Visibility

The task listing query is team-aware.

A relevant task can be:

```text
created by current user
        OR
assigned to current user
        OR
associated with a team of which the current user is a member
```

Pagination is handled through Spring Data's `Pageable`.

## 6. Collaboration

### Comments

```text
Client
  |
  v
CommentController
  |
  v
CommentService
  |
  +--> TaskAccessService
  |
  v
CommentRepository
  |
  v
PostgreSQL
```

### Attachments

```text
Client
  |
  v
AttachmentController
  |
  v
AttachmentService
  |
  +--> TaskAccessService
  |
  +----------------------+
  |                      |
  v                      v
PostgreSQL           Local filesystem
(metadata)             (file bytes)
```

## 7. Data Model

Main entities:

```text
User
 |
 +----< Task
 |
 +----< Comment
 |
 +----< Attachment
 |
 +----< TeamMember >---- Team
 |
 +----< TeamInvitation > Team

Task
 |
 +----> User (creator)
 |
 +----> User (assignee)
 |
 +----> Team (optional)
 |
 +----< Comment
 |
 +----< Attachment
```

The design requires foreign-key relationships for integrity and timestamps for audit tracking.

## 8. Error Handling

Controllers do not need to implement repetitive error response logic.

```text
Service
   |
   v
Exception
   |
   v
GlobalExceptionHandler
   |
   v
ErrorResponse
   |
   v
HTTP response
```

Expected categories:

```text
400 Validation / bad request
401 Authentication failure
403 Authorization failure
404 Resource not found
500 Unexpected server error
```

## 9. Validation

Validation occurs at the API boundary using Bean Validation.

```text
HTTP Request
     |
     v
@Valid DTO
     |
     +---- invalid ----> 400
     |
     v
Service
```

This prevents malformed data from reaching the service and persistence layers.

## 10. Persistence

PostgreSQL stores application data including:

- users
- refresh tokens
- tasks
- teams
- team memberships/invitations
- comments
- attachment metadata

Attachment bytes are stored separately on the local filesystem for the MVP.

## 11. Design Decisions

### PostgreSQL

Chosen as the relational database for persistent application data.

### JWT

Chosen for stateless authentication using access and refresh tokens.

### Layered architecture

Controllers, services, repositories, and entities have separate responsibilities.

### Local attachment storage

Simple for an MVP and leaves an upgrade path to object storage such as S3.

### Centralized authorization

`TaskAccessService` prevents authorization rules from being duplicated across services.

## 12. Production Considerations

Before production deployment:

- Use HTTPS.
- Move secrets to environment variables or a secret manager.
- Do not commit database credentials or JWT secrets.
- Replace local attachment storage with durable object storage if multiple application instances are deployed.
- Add comprehensive automated tests.
- Review JWT expiry, refresh-token revocation, and rotation behavior.
- Add database migrations such as Flyway or Liquibase.
- Review indexes and query performance with realistic data volumes.
