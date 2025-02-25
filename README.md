**A Collaborative Task Tracking System**
==============================================

This document provides details about the API endpoints for the Task Tracking and Management Application. The API is built using **Spring Boot** and supports features like user authentication, task management, team collaboration, and real-time notifications.

* * * * *

Base URL
--------

All endpoints are relative to the base URL:

Copy

http://localhost:8080

* * * * *

Authentication Endpoints
------------------------

### **1\. Register a New User**

-   **Endpoint**: `POST /auth/register`

-   **Description**: Register a new user.

-   **Request Body**:
```
    {
      "username": "john",
      "password": "password",
      "email": "john@example.com",
      "firstName": "John",
      "lastName": "Doe"
    }
```
-   **Response**: Registered user details.

* * * * *

### **2\. Log In**

-   **Endpoint**: `POST /auth/login`

-   **Description**: Log in and receive a JWT token.

-   **Request Body**:

```

    {
      "username": "john",
      "password": "password"
    }
```
-   **Response**: JWT token for authenticated requests.

* * * * *

### **3\. Log Out**

-   **Endpoint**: `POST /auth/logout`

-   **Description**: Log out and invalidate the JWT token.

-   **Headers**:

    -   `Authorization: Bearer <your-jwt-token>`

-   **Response**: `"Logged out successfully"`

* * * * *

User Profile Endpoints
----------------------

### **4\. Get Current User Profile**

-   **Endpoint**: `GET /users/me`

-   **Description**: Retrieve the profile of the currently authenticated user.

-   **Headers**:

    -   `Authorization: Bearer <your-jwt-token>`

-   **Response**: Current user's profile.

* * * * *

### **5\. Update Current User Profile**

-   **Endpoint**: `PUT /users/me`

-   **Description**: Update the profile of the currently authenticated user.

-   **Headers**:

    -   `Authorization: Bearer <your-jwt-token>`

-   **Request Body**:

 ```
    {
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com"
    }
```
-   **Response**: Updated user profile.

* * * * *

Task Endpoints
--------------

### **6\. Create a Task**

-   **Endpoint**: `POST /tasks`

-   **Description**: Create a new task.

-   **Headers**:

    -   `Authorization: Bearer <your-jwt-token>`

-   **Request Body**:

```
    {
      "title": "Complete project",
      "description": "Finish the task tracker project",
      "dueDate": "2023-12-31",
      "completed": false
    }
```
-   **Response**: Created task details.

* * * * *

### **7\. Get All Tasks**

-   **Endpoint**: `GET /tasks`

-   **Description**: Retrieve all tasks for the current user.

-   **Headers**:

    -   `Authorization: Bearer <your-jwt-token>`

-   **Response**: List of tasks.

* * * * *

### **8\. Get Task by ID**

-   **Endpoint**: `GET /tasks/{id}`

-   **Description**: Retrieve a task by its ID.

-   **Headers**:

    -   `Authorization: Bearer <your-jwt-token>`

-   **Response**: Task details.

* * * * *

### **9\. Update a Task**

-   **Endpoint**: `POST /tasks/{id}`

-   **Description**: Update a task by its ID.

-   **Headers**:

    -   `Authorization: Bearer <your-jwt-token>`

-   **Request Body**:

 ```
    {
      "title": "Updated task title",
      "description": "Updated task description",
      "dueDate": "2023-12-31",
      "completed": true
    }
```
-   **Response**: Updated task details.

* * * * *

### **10\. Mark Task as Completed**

-   **Endpoint**: `PUT /tasks/{id}/complete`

-   **Description**: Mark a task as completed.

-   **Headers**:

    -   `Authorization: Bearer <your-jwt-token>`

-   **Response**: Updated task details.

* * * * *

### **11\. Assign Task to User**

-   **Endpoint**: `PUT /tasks/{id}/assign`

-   **Description**: Assign a task to another user.

-   **Headers**:

    -   `Authorization: Bearer <your-jwt-token>`

-   **Query Parameter**:

    -   `userName`: Username of the user to assign the task to.

-   **Response**: Updated task details.

* * * * *

### **12\. Delete a Task**

-   **Endpoint**: `DELETE /tasks/{id}`

-   **Description**: Delete a task by its ID.

-   **Headers**:

    -   `Authorization: Bearer <your-jwt-token>`

-   **Response**: No content.

* * * * *

### **13\. Filter Tasks by Status**

-   **Endpoint**: `GET /tasks/filter`

-   **Description**: Filter tasks by completion status.

-   **Headers**:

    -   `Authorization: Bearer <your-jwt-token>`

-   **Query Parameter**:

    -   `completed`: `true` for completed tasks, `false` for open tasks.

-   **Response**: List of filtered tasks.

* * * * *

### **14\. Search Tasks**

-   **Endpoint**: `GET /tasks/search`

-   **Description**: Search tasks by title or description.

-   **Headers**:

    -   `Authorization: Bearer <your-jwt-token>`

-   **Query Parameter**:

    -   `query`: Search keyword.

-   **Response**: List of matching tasks.

* * * * *

### **15\. Add Comment to Task**

-   **Endpoint**: `POST /tasks/{id}/comments`

-   **Description**: Add a comment to a task.

-   **Headers**:

    -   `Authorization: Bearer <your-jwt-token>`

-   **Request Body**:

 ```
    "This is a comment on the task."
```
-   **Response**: Added comment details.

* * * * *

### **16\. Add Attachment to Task**

-   **Endpoint**: `POST /tasks/{id}/attachments`

-   **Description**: Add an attachment to a task.

-   **Headers**:

    -   `Authorization: Bearer <your-jwt-token>`

-   **Request Body**:

    -   `file`: Multipart file to upload.

-   **Response**: Added attachment details.

* * * * *

### **17\. Generate Task Description Using AI**

-   **Endpoint**: `POST /tasks/generate-description`

-   **Description**: Generate a task description using OpenAI's GPT-3.5 Turbo.

-   **Headers**:

    -   `Authorization: Bearer <your-jwt-token>`

-   **Query Parameter**:

    -   `prompt`: Prompt for generating the description.

-   **Response**: Generated task description.

* * * * *

Project Endpoints
-----------------

### **18\. Create a Project**

-   **Endpoint**: `POST /projects`

-   **Description**: Create a new project.

-   **Headers**:

    -   `Authorization: Bearer <your-jwt-token>`

-   **Request Body**:

 ```
    {
      "name": "Project Alpha",
      "description": "A new project for task management"
    }
```
-   **Response**: Created project details.

* * * * *

### **19\. Invite User to Project**

-   **Endpoint**: `POST /projects/{projectName}/invite`

-   **Description**: Invite a user to a project.

-   **Headers**:

    -   `Authorization: Bearer <your-jwt-token>`

-   **Query Parameter**:

    -   `userName`: Username of the user to invite.

-   **Response**: Updated project details.

* * * * *

Error Responses
---------------

### Common Error Responses

-   **400 Bad Request**: Invalid request body or parameters.

-   **401 Unauthorized**: Missing or invalid JWT token.

-   **403 Forbidden**: User does not have permission to perform the action.

-   **404 Not Found**: Resource not found (e.g., task, project, or user).

-   **500 Internal Server Error**: Server-side error.

* * * * *

Example Requests
----------------

### Register a User

```

POST /auth/register
Content-Type: application/json
 {
  "username": "john",
  "password": "password",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe"
}
```
### Create a Task

```
POST /tasks
Authorization: Bearer <your-jwt-token>
Content-Type: application/json
 {
  "title": "Complete project",
  "description": "Finish the task tracker project",
  "dueDate": "2023-12-31",
  "completed": false
}
```
### Generate Task Description

```

POST /tasks/generate-description?prompt=Write%20a%20task%20description%20for%20completing%20a%20project
Authorization: Bearer <your-jwt-token>
```
* * * * *

License
-------

This project is licensed under the MIT License. See the [LICENSE](https://chat.deepseek.com/a/chat/s/LICENSE) file for details.

* * * * *
