# Design Specification - TaskTrackerSystem

## 1. Document Purpose
This document translates the approved functional and non-functional requirements into a design-level blueprint for the backend-only TaskTrackerSystem. It includes both a High-Level Design (HLD) and a Low-Level Design (LLD) for a Spring Boot + PostgreSQL based application.

This design is aligned with the requirements defined in [requirements.md](requirements.md).

---

## 2. Design Goals
- Build a backend-only task tracking system for teams and projects.
- Support secure user authentication and task lifecycle management.
- Enable team collaboration through comment threads and task attachments.
- Use a PostgreSQL database for persistent storage.
- Expose RESTful APIs for all core functionality.
- Keep the first release limited to MVP scope without real-time notifications or AI features.

---

## 3. High-Level Design (HLD)

### 3.1 System Overview
The application will be a layered backend service built with Java and Spring Boot. It will expose REST APIs for authentication, user management, task management, team/project collaboration, and attachments/comments.

The core architectural layers are:
- Presentation Layer: REST controllers
- Application Layer: service classes and business logic
- Domain Layer: entities and business rules
- Persistence Layer: repositories and PostgreSQL database
- Security Layer: authentication, authorization, JWT handling
- Storage Layer: local file system or cloud object storage for file attachments

### 3.2 High-Level Architecture

Client
  |
  v
REST API Layer (Controllers)
  |
  v
Service Layer (Business Logic)
  |
  v
Repository Layer (JPA / PostgreSQL)
  |
  v
PostgreSQL Database

Supporting components:
- Authentication and Authorization Module
- Task Management Module
- Team/Project Module
- Comment Module
- Attachment Module
- Validation and Error Handling Module
- Logging and Monitoring

### 3.3 Core Modules

#### 3.3.1 Authentication and User Management
Responsibilities:
- Register a new user
- Log in using credentials
- Issue and validate JWT access and refresh tokens
- Refresh expired access tokens using valid refresh tokens
- Get/update user profile
- Log out securely (token invalidation or expiry-based logout)

Key components:
- UserController
- AuthController
- UserService
- AuthService
- UserRepository
- SecurityConfig
- JwtService
- RefreshTokenService
- RefreshToken entity/repository

#### 3.3.2 Task Management
Responsibilities:
- Create, read, update, delete task
- Mark tasks complete or in-progress
- Filter tasks by status
- Search by title, description, assignee, date range, and other supported filters
- View tasks assigned to a user
- Return paginated task lists

Key components:
- TaskController
- TaskService
- TaskRepository
- Task entity/model
- TaskStatus enum
- TaskFilter DTOs

#### 3.3.3 Team/Project Collaboration
Responsibilities:
- Create project/team
- Invite members to join a team/project
- Manage membership visibility
- Scope work to relevant team members
- Restrict access to invited members only

Key components:
- TeamController
- TeamService
- TeamRepository
- TeamMemberRepository
- TeamInvitationRepository
- Team entity

#### 3.3.4 Commenting and Attachments
Responsibilities:
- Add comments to tasks
- Retrieve task history/comments
- Upload and save attachments
- Link attachments to task records

Key components:
- CommentController
- CommentService
- AttachmentController
- AttachmentService
- CommentRepository
- AttachmentRepository

### 3.4 Technology Stack
- Java 17+
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- Spring Security
- PostgreSQL
- Maven or Gradle
- JWT for authentication
- Validation framework for request validation
- File storage: local filesystem for MVP, with upgrade path to cloud storage
- Authentication: JWT access tokens + refresh tokens

### 3.5 Deployment View
For the MVP, the system will run as a single backend service with PostgreSQL as the persistent database. The application can be deployed in a standalone environment or a containerized setup later.

Suggested deployment model:
- Application server: Spring Boot app
- Database: PostgreSQL instance
- File storage: local disk or object storage
- Environment config: application.properties / application.yml

### 3.6 Security Architecture
The application must enforce:
- password hashing using BCrypt or similar
- JWT-based authentication for protected endpoints using access tokens and refresh tokens
- refresh token flow for renewing access tokens securely
- authorization checks before task access or modification
- input validation for all API payloads
- HTTPS in production

### 3.7 Error Handling Strategy
- Validation errors return 400 Bad Request
- Unauthorized access returns 401 Unauthorized
- Forbidden operations return 403 Forbidden
- Resource not found returns 404 Not Found
- Unhandled failures return 500 Internal Server Error

### 3.8 Non-Functional Design Considerations
- Scalability: modular services and repository-based data access allow future extension
- Performance: optimized query patterns for task listing, search, and assignment retrieval with pagination
- Maintainability: layered architecture and separation of concerns
- Reliability: transactional service methods for task, comment, and attachment operations
- Security: JWT access/refresh tokens + secure password handling + validation

---

## 4. Low-Level Design (LLD)

### 4.1 Package Structure
A likely package layout:

com.tasktrackersystem
  ├── config
  │   ├── SecurityConfig.java
  │   ├── JwtAuthenticationFilter.java
  │   └── OpenApiConfig.java
  ├── controller
  │   ├── AuthController.java
  │   ├── UserController.java
  │   ├── TaskController.java
  │   ├── TeamController.java
  │   ├── CommentController.java
  │   └── AttachmentController.java
  ├── dto
  │   ├── AuthRequest.java
  │   ├── AuthResponse.java
  │   ├── UserDto.java
  │   ├── TaskRequest.java
  │   ├── TaskResponse.java
  │   ├── CommentRequest.java
  │   └── AttachmentDto.java
  ├── entity
  │   ├── User.java
  │   ├── Task.java
  │   ├── Team.java
  │   ├── Comment.java
  │   └── Attachment.java
  ├── enums
  │   └── TaskStatus.java
  ├── repository
  │   ├── UserRepository.java
  │   ├── TaskRepository.java
  │   ├── TeamRepository.java
  │   ├── CommentRepository.java
  │   └── AttachmentRepository.java
  ├── service
  │   ├── AuthService.java
  │   ├── UserService.java
  │   ├── TaskService.java
  │   ├── TeamService.java
  │   ├── CommentService.java
  │   └── AttachmentService.java
  ├── security
  │   ├── JwtService.java
  │   └── CustomUserDetailsService.java
  ├── exception
  │   ├── GlobalExceptionHandler.java
  │   └── ResourceNotFoundException.java
  └── utils
      └── ValidationUtil.java

### 4.2 Database Design
PostgreSQL will be used for persistent data storage.

#### 4.2.1 User Entity
Fields:
- id
- username
- email
- password
- firstName
- lastName
- createdAt
- updatedAt

Additional support entity:
- RefreshToken
  - id
  - userId
  - token
  - expiryDate
  - revoked
  - createdAt

Relationships:
- One user can create many tasks
- One user can be a member of many teams
- One user can create many comments
- One user can upload many attachments

#### 4.2.2 Task Entity
Fields:
- id
- title
- description
- status
- dueDate
- createdAt
- updatedAt
- createdBy
- assignedTo
- teamId (optional)

Relationships:
- Many tasks belong to one creator
- Many tasks belong to one assignee
- One task may have many comments
- One task may have many attachments

#### 4.2.3 Team Entity
Fields:
- id
- name
- description
- createdBy
- createdAt

Relationships:
- One team has many members
- One team has many tasks

#### 4.2.4 Comment Entity
Fields:
- id
- taskId
- userId
- content
- createdAt

#### 4.2.5 Attachment Entity
Fields:
- id
- taskId
- uploadedBy
- fileName
- storagePath
- fileType
- createdAt

### 4.3 Data Model Notes
- Use UUID or numeric primary keys
- Use timestamps for audit tracking
- Keep foreign key constraints for integrity
- Store file metadata in database and file bytes in a file store or local path

### 4.4 API Design

#### Authentication APIs
- POST /api/auth/register
- POST /api/auth/login
- POST /api/auth/refresh
- POST /api/auth/logout
- GET /api/users/profile
- PUT /api/users/profile

#### Task APIs
- POST /api/tasks
- GET /api/tasks/{id}
- GET /api/tasks
- GET /api/tasks/my-tasks
- PUT /api/tasks/{id}
- PATCH /api/tasks/{id}/status
- DELETE /api/tasks/{id}
- GET /api/tasks/search?query=...
- GET /api/tasks?page=0&size=10&status=OPEN&assigneeId=...&fromDate=...&toDate=...

#### Team APIs
- POST /api/teams
- GET /api/teams
- GET /api/teams/{id}
- POST /api/teams/{id}/invite
- POST /api/teams/{id}/members
- DELETE /api/teams/{id}/members/{userId}

#### Comment APIs
- POST /api/tasks/{taskId}/comments
- GET /api/tasks/{taskId}/comments

#### Attachment APIs
- POST /api/tasks/{taskId}/attachments
- GET /api/tasks/{taskId}/attachments
- DELETE /api/attachments/{id}

### 4.5 Service Layer Responsibilities

#### AuthService
- validate user credentials
- encode password
- generate JWT access token and refresh token
- validate refresh token and issue new access token
- return auth response

#### UserService
- create user
- update profile
- fetch user details
- enforce access restrictions

#### TaskService
- create task with validation
- fetch all relevant tasks
- search and filter tasks
- update task status
- delete task if authorized

#### TeamService
- create teams
- add members
- restrict team data access to members

#### CommentService
- add comment to task
- fetch comments by task
- validate user access to the task

#### AttachmentService
- store file metadata and physical file
- validate file type and upload size
- fetch attachments for a task

### 4.6 Sequence Flow Examples

#### User Registration Flow
1. Client sends registration request
2. AuthController receives request
3. AuthService validates input
4. UserService creates user with hashed password
5. UserRepository saves user in PostgreSQL
6. API returns success response

#### Task Creation Flow
1. Client sends create task request
2. TaskController checks authentication
3. TaskService validates fields and assignee
4. TaskRepository persists the task
5. Response returns created task details

#### Comment Creation Flow
1. Client POSTs comment to task endpoint
2. CommentController validates authenticated user
3. CommentService checks that task exists and user can access it
4. CommentRepository saves comment
5. Response confirms comment creation

### 4.7 Validation Rules
- Required fields: title, description, dueDate for task creation
- Email must be unique and valid
- Username must be unique
- Task assignee must exist
- Status must be from a valid enum list
- File upload must follow allowed type/size rules
- Task filtering and search must support title, description, status, assignee, and date-range inputs
- Pagination parameters must be validated: page >= 0 and size > 0
- Refresh tokens must be validated for expiry and revocation

### 4.8 Exception Handling Design
Create custom exceptions:
- UserNotFoundException
- TaskNotFoundException
- TeamNotFoundException
- DuplicateResourceException
- InvalidOperationException
- InvalidFileException

GlobalExceptionHandler converts them into structured API responses.

### 4.9 Testing Strategy
- Unit tests for services
- Integration tests for controllers and repositories
- Validation tests for invalid payloads
- Security tests for unauthorized access
- Database tests for task/comment/attachment relationships

---

## 5. Design Decisions and Assumptions
- The project is backend-only and does not include a frontend UI.
- Standard user-based access is sufficient for this MVP; no admin/member roles are required.
- PostgreSQL is the chosen database for persistence.
- JWT authentication is the preferred secure authentication mechanism, using access + refresh tokens.
- Comments and attachments are mandatory features for the first release.
- Local filesystem storage is used for attachments in the MVP.
- Task search supports title, description, assignee, date range, and status filters.
- Task list APIs return paginated results.
- Team membership uses invite-based access control.
- Real-time notifications and AI integration are out of scope for the MVP and should be treated as future enhancements.

---

## 6. Risks and Considerations
- File uploads can increase storage and maintenance complexity
- Large task collections may require pagination in the future
- Search and sorting performance may need indexes as data grows
- Security review is required before production deployment

---

## 7. Future Extension Roadmap
- Add WebSocket/SSE notifications
- Add admin or role-based access
- Add pagination and advanced filters
- Add document storage using S3 or Azure Blob Storage
- Add AI-based task summarization or description generation

---

## 8. Finalized Design Decisions
The following decisions are now confirmed for implementation:
1. Attachments will be stored locally on the server filesystem.
2. Task search will support title, description, status, assignee, and date-range filters.
3. Task list APIs will return paginated results.
4. Authentication will use JWT access tokens and refresh tokens.
5. Team membership will be invite-based.

This design is ready for implementation planning and development.