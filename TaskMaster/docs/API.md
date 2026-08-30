# Task Master API Reference

Base URL:

```text
http://localhost:8080
```

Most endpoints require:

```http
Authorization: Bearer <access-token>
```

## 1. Authentication

### Register

```http
POST /api/auth/register
Content-Type: application/json
```

Example:

```json
{
  "username": "ashkin",
  "email": "ashkin@example.com",
  "password": "Password@123",
  "firstName": "Ashkin",
  "lastName": "Fino"
}
```

### Login

```http
POST /api/auth/login
Content-Type: application/json
```

Example:

```json
{
  "username": "ashkin",
  "password": "Password@123"
}
```

Use the returned access token in subsequent protected requests.

## 2. Tasks

### Create task

```http
POST /api/tasks
Authorization: Bearer <token>
Content-Type: application/json
```

The request must follow the fields implemented by the current `TaskRequest` DTO.

### List tasks

```http
GET /api/tasks?page=0&size=10
Authorization: Bearer <token>
```

Supported filters include the implemented task search/filter parameters such as:

```text
status
assigneeId
fromDate
toDate
title
description
```

Example:

```http
GET /api/tasks?page=0&size=10&status=OPEN&assigneeId=1
```

### Get task

```http
GET /api/tasks/{id}
Authorization: Bearer <token>
```

### Update task

```http
PUT /api/tasks/{id}
Authorization: Bearer <token>
Content-Type: application/json
```

### Delete task

```http
DELETE /api/tasks/{id}
Authorization: Bearer <token>
```

## 3. Teams

### Create team

```http
POST /api/teams
Authorization: Bearer <token>
Content-Type: application/json
```

### Get my teams

```http
GET /api/teams
Authorization: Bearer <token>
```

### Get team

```http
GET /api/teams/{id}
Authorization: Bearer <token>
```

### Invite member

```http
POST /api/teams/{id}/invite
Authorization: Bearer <token>
Content-Type: application/json
```

### Add/accept member

```http
POST /api/teams/{id}/members
Authorization: Bearer <token>
Content-Type: application/json
```

### Remove member

```http
DELETE /api/teams/{id}/members/{userId}
Authorization: Bearer <token>
```

## 4. Comments

### Add comment

```http
POST /api/tasks/{taskId}/comments
Authorization: Bearer <token>
Content-Type: application/json
```

Example:

```json
{
  "content": "Implementation is ready for review."
}
```

### Get comments

```http
GET /api/tasks/{taskId}/comments
Authorization: Bearer <token>
```

## 5. Attachments

### Upload attachment

```http
POST /api/tasks/{taskId}/attachments
Authorization: Bearer <token>
Content-Type: multipart/form-data
```

The multipart field is:

```text
file
```

cURL example:

```powershell
curl.exe -X POST "http://localhost:8080/api/tasks/1/attachments" `
  -H "Authorization: Bearer <TOKEN>" `
  -F "file=@C:\path\to\test.pdf"
```

### List attachments

```http
GET /api/tasks/{taskId}/attachments
Authorization: Bearer <token>
```

### Delete attachment

```http
DELETE /api/attachments/{id}
Authorization: Bearer <token>
```

## 6. Authorization

Task-related access is based on the current user's relationship to the task.

A user can access a task when they are:

```text
Task creator
     OR
Task assignee
     OR
Member of task's team
```

This logic is centralized in `TaskAccessService`.

## 7. HTTP Status Codes

```text
200 OK
201 Created
204 No Content
400 Bad Request
401 Unauthorized
403 Forbidden
404 Not Found
500 Internal Server Error
```

## 8. Example Error

```json
{
  "timestamp": "2026-08-30T17:30:00",
  "status": 403,
  "error": "FORBIDDEN",
  "message": "You do not have access to this task",
  "path": "/api/tasks/1"
}
```

## 9. Testing With cURL

Set the token in PowerShell:

```powershell
$TOKEN="<your-jwt-token>"
```

Then:

```powershell
curl.exe -X GET "http://localhost:8080/api/tasks" `
  -H "Authorization: Bearer $TOKEN"
```

Get a specific task:

```powershell
curl.exe -X GET "http://localhost:8080/api/tasks/1" `
  -H "Authorization: Bearer $TOKEN"
```

Get comments:

```powershell
curl.exe -X GET "http://localhost:8080/api/tasks/1/comments" `
  -H "Authorization: Bearer $TOKEN"
```

Get attachments:

```powershell
curl.exe -X GET "http://localhost:8080/api/tasks/1/attachments" `
  -H "Authorization: Bearer $TOKEN"
```

> Note: This document intentionally describes the implemented API at a high level. Before publishing it as a contract, compare it with the final controller and DTO signatures so optional fields and exact request/response schemas match the code.
