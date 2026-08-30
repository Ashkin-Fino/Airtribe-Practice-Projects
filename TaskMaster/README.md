# Task Master

A backend task management and collaboration system built with **Java, Spring Boot, Spring Security, JWT, JPA, and PostgreSQL**.

The application provides secure user authentication, task management, search/filtering, team collaboration, comments, and task attachments.

## Features

- User registration and login
- JWT-based authentication
- Access-token validation and refresh-token support
- User profile management
- Create, read, update, and delete tasks
- Task status management
- Task search, filtering, sorting, and pagination
- Assign tasks to users
- Team creation and membership/invitation flow
- Team-aware task visibility
- Task comments
- Task attachments using local filesystem storage
- Centralized task authorization through `TaskAccessService`
- Request validation
- Centralized REST exception handling
- PostgreSQL persistence

## Technology Stack

| Technology       | Purpose                          |
|------------------|----------------------------------|
|Java              | Backend language                 |
| Spring Boot      | Application framework            |
| Spring Web       | REST APIs                        |
| Spring Data JPA  | Persistence layer                |
| Spring Security  | Authentication and authorization |
| JWT              | Stateless API authentication     |
| PostgreSQL       | Relational database              |
| Maven Wrapper    | Build and dependency management  |
| Local filesystem | MVP attachment storage           |

The original design specifies Java 17+, Spring Boot 3.x, Spring Web, Spring Data JPA, Spring Security, PostgreSQL, JWT, validation, and local filesystem storage for the MVP.

## Architecture

The application follows a layered architecture:

```text
Client
  |
  v
Controller Layer
  |
  v
Service Layer
  |
  +--------------------+
  |                    |
  v                    v
Repository Layer   Security / Authorization
  |
  v
PostgreSQL

Attachments:
Controller -> Service -> Local Filesystem
                    \
                     -> PostgreSQL metadata
```

### Main packages

```text
com.airtribe.task_master
|
+-- controller
+-- dto
+-- entity
+-- repository
+-- service
+-- security
+-- exception
```

## Security Model

Protected endpoints require a valid JWT access token:

```http
Authorization: Bearer <access-token>
```

Passwords are stored using a password encoder rather than plaintext.

Task access is centralized through `TaskAccessService`.

A user can access a task when they are:

1. The task creator
2. The assigned user
3. A member of the task's team

This authorization is reused by task-related collaboration operations such as comments and attachments.

## Database

PostgreSQL is used for persistent data.

Expected local database configuration for this project:

```text
Database: task_master_db
Username: task_master_user
```

Do not commit database passwords or JWT secrets to source control.

## Prerequisites

Install:

- Java
- PostgreSQL
- Git

Maven does not need to be installed globally because the project uses the Maven Wrapper.

Check Java:

```powershell
java -version
```

Check PostgreSQL:

```powershell
psql --version
```

## Configuration

Configure the database connection and JWT settings in your application configuration.

Example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/task_master_db
spring.datasource.username=task_master_user
spring.datasource.password=<your-password>

# Keep secrets outside source control in a real deployment.
jwt.secret=<your-secret>
```

If attachment storage is enabled:

```properties
file.upload-dir=uploads
```

The `uploads/` directory is used for physical attachment files while attachment metadata is stored in PostgreSQL.

## Running the Application

### 1. Clone the repository

```powershell
git clone <repository-url>
cd <project-directory>
```

### 2. Configure PostgreSQL

Create the database and application user, then configure the credentials in the Spring Boot configuration.

### 3. Build

Windows:

```powershell
.\mvnw.cmd clean install
```

Linux/macOS:

```bash
./mvnw clean install
```

### 4. Run

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

## API Overview

### Authentication

```text
POST /api/auth/register
POST /api/auth/login
```

### Users

```text
GET  /api/users
GET  /api/users/{id}
GET  /api/users/...
PUT  /api/users/...
```

The exact user endpoints should be verified against the current controller implementation.

### Tasks

```text
POST /api/tasks
GET  /api/tasks
GET  /api/tasks/{id}
PUT  /api/tasks/{id}
DELETE /api/tasks/{id}
```

Task listing supports pagination and the implemented search/filter parameters.

Example:

```http
GET /api/tasks?page=0&size=10&status=OPEN&assigneeId=1&fromDate=2026-09-01&toDate=2026-09-30
```

### Teams

```text
POST   /api/teams
GET    /api/teams
GET    /api/teams/{id}
POST   /api/teams/{id}/invite
POST   /api/teams/{id}/members
DELETE /api/teams/{id}/members/{userId}
```

### Comments

```text
POST /api/tasks/{taskId}/comments
GET  /api/tasks/{taskId}/comments
```

### Attachments

```text
POST   /api/tasks/{taskId}/attachments
GET    /api/tasks/{taskId}/attachments
DELETE /api/attachments/{id}
```

See `docs/API.md` for request/response examples.

## Example Authentication Flow

```text
Register
   |
   v
Login
   |
   v
JWT access token
   |
   v
Send token with protected API requests
   |
   v
Spring Security JWT filter
   |
   v
Authenticated request
```

Example:

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "ashkin",
  "password": "Password@123"
}
```

Then:

```http
GET /api/tasks
Authorization: Bearer <access-token>
```

## Error Handling

The API uses centralized exception handling.

Typical responses:

| Status | Meaning |
|---|---|
| 400 | Invalid request / validation failure |
| 401 | Missing or invalid authentication |
| 403 | Authenticated but not authorized |
| 404 | Resource not found |
| 500 | Unexpected server error |

Example:

```json
{
  "timestamp": "2026-08-30T17:30:00",
  "status": 404,
  "error": "RESOURCE_NOT_FOUND",
  "message": "Task not found",
  "path": "/api/tasks/999"
}
```

## Validation

Important request payloads use Bean Validation.

Examples include:

- Required task title
- Maximum title/description length
- Non-empty comments
- Valid email format
- Unique username/email constraints
- Valid task status
- Valid pagination parameters

## Attachment Storage

For the MVP, attachment metadata is stored in PostgreSQL and the physical file is stored on the local filesystem.

```text
PostgreSQL
    |
    +-- attachment metadata

Server filesystem
    |
    +-- uploads/
          |
          +-- <generated-file-name>
```

The application uses generated filenames rather than relying on user-provided filenames for physical storage.

## Project Structure

```text
task-master/
|
+-- src/
|   +-- main/
|       +-- java/com/airtribe/task_master/
|           +-- controller/
|           +-- dto/
|           +-- entity/
|           +-- repository/
|           +-- service/
|           +-- security/
|           +-- exception/
|
+-- uploads/
+-- pom.xml
+-- mvnw
+-- mvnw.cmd
+-- README.md
+-- docs/
    +-- API.md
    +-- ARCHITECTURE.md
    +-- DEVELOPMENT.md
```

## Development Notes

Use the Maven Wrapper rather than requiring a globally installed Maven installation.

Build:

```powershell
.\mvnw.cmd clean install
```

Run:

```powershell
.\mvnw.cmd spring-boot:run
```

Keep generated/local files such as uploaded attachments, local database configuration, secrets, and IDE files out of Git where appropriate.

## Scope

The MVP focuses on:

- Authentication
- Task management
- Team collaboration
- Comments
- Attachments
- Search/filtering/pagination
- Authorization
- Validation
- Consistent error handling

Real-time notifications and AI-based task features are outside the MVP scope and can be considered future enhancements.

## Future Enhancements

Potential future improvements include:

- WebSocket/SSE notifications
- Role-based access control
- Cloud object storage such as S3
- More advanced audit logging
- Docker/container deployment
- More extensive automated testing
- AI-assisted task summarization or generation

## Documentation

Additional documentation:

- `docs/API.md` — API endpoint reference and examples
- `docs/ARCHITECTURE.md` — architecture, security, data flow, and design decisions
- `docs/DEVELOPMENT.md` — local development and testing workflow

## Status

Core MVP implementation completed. Final integration testing and documentation should be completed before treating the project as production-ready.
