# Task Master Development Guide

## 1. Local Environment

Required:

- Java
- PostgreSQL
- Git

A globally installed Maven installation is not required.

Use:

```powershell
.\mvnw.cmd
```

on Windows.

## 2. PostgreSQL

Local application database:

```text
Database: task_master_db
User: task_master_user
```

Verify the PostgreSQL client:

```powershell
psql --version
```

Connect:

```powershell
psql -U task_master_user -d task_master_db
```

## 3. Build

```powershell
.\mvnw.cmd clean install
```

This should compile the project and execute the configured Maven test lifecycle.

## 4. Run

```powershell
.\mvnw.cmd spring-boot:run
```

## 5. Configuration

Keep environment-specific values outside committed source where possible.

Typical values include:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/task_master_db
spring.datasource.username=task_master_user
spring.datasource.password=<password>

jwt.secret=<secret>

file.upload-dir=uploads
```

## 6. Git Hygiene

Do not commit:

```text
.env
database passwords
JWT secrets
uploads/
IDE metadata
target/
```

A suitable `.gitignore` should include at least:

```gitignore
target/
uploads/
.idea/
.vscode/
*.iml
.env
```

## 7. Recommended Development Workflow

```text
Make change
   |
   v
.\mvnw.cmd clean install
   |
   v
Start application
   |
   v
Test endpoint
   |
   v
Check PostgreSQL state
   |
   v
Commit focused change
```

## 8. Manual API Testing

Authenticate first and save the returned JWT.

PowerShell:

```powershell
$TOKEN="<jwt-token>"
```

Then:

```powershell
curl.exe -X GET "http://localhost:8080/api/tasks" `
  -H "Authorization: Bearer $TOKEN"
```

## 9. Security Testing Checklist

Verify:

- No-token request returns 401 where authentication is required.
- Expired token returns 401.
- Malformed token returns 401.
- Invalid signature returns 401.
- Authenticated but unauthorized task access returns 403.
- Team members can access team tasks.
- Non-members cannot access team tasks.
- Users cannot delete another user's attachment where ownership restrictions apply.

## 10. Collaboration Testing

Verify:

```text
Create task
   |
   +--> Add comment
   |
   +--> Get comments
   |
   +--> Upload attachment
   |
   +--> Get attachments
   |
   +--> Delete attachment
```

## 11. Before Submission

Run:

```powershell
.\mvnw.cmd clean install
```

Then manually verify:

- registration
- login
- JWT-protected endpoint
- expired JWT
- task CRUD
- task search/filter/pagination
- team creation
- invitation/membership
- team-aware task visibility
- comments
- attachments
- validation errors
- 401/403/404 responses

After that, complete the integration-test suite and update the README with any endpoint details that differ from the current implementation.
