# Task Management Microservice

The repository contains a simple task management microservice to manage tasks efficiently. It provides functionality to
perform Create, Read, Update, and Delete (CRUD) operations on tasks and users. Tasks are separeted by categories into BUG and FEATURE, and can be assigned to users. 
The service is designed to be lightweight and easy to use, making it suitable for small to medium-sized applications.

## Features

- Create a new user with specific details.
- Retrieve user information by ID or view all users.
- Update user details.
- Delete users.
- Create a new task with specific details.
- Retrieve task information by ID or view all tasks.
- Update task details.
- Delete tasks.
- Search tasks by status and user.
- Lightweight microservice built using **Spring Boot** and **Spring Data JPA**.
- Integrated with a relational database for persistence.

## Prerequisites

To run this application, ensure that you have the following installed:

1. **Java Development Kit (JDK)** - Version 17 or higher.
2. **Maven** - To build and manage dependencies.
3. **Database** - A relational database like PostgreSQL (configured in compose.yml and .env).
4. **Docker** (optional) - To run the application using Docker.

## How to Run the Application

### Using Maven and Docker

1. Ensure Docker is installed and running.

2. Clone the repository:
   ```bash
   git clone https://github.com/pkukucka/task-management.git
   cd task-management
   ```

3. Build the project:
   ```bash
   vn clean install
   ```

4. Build and run the application using Docker Compose:
   ```bash
   docker-compose up 
   ```

5. Access the application:
    - API Endpoints: `http://localhost:8080/api/`
    - Swagger UI: `http://localhost:8080/swagger-ui/`

## API Endpoints

- `POST /api/v1/users` - Create a new user.
- `GET /api/v1/users` - Get all users.
- `GET /api/v1/users/{id}` - Get user by ID.
- `PUT /api/v1/users/{id}` - Update user by ID.
- `DELETE /api/v1/users/{id}` - Delete user by ID.

- `GET /api/v1/tasks/search` - Search tasks by status and user.
- `POST /api/v1/tasks` - Create a new task.
- `GET /api/v1/tasks` - Get all tasks.
- `GET /api/v1/tasks/{id}` - Get task by ID.
- `PUT /api/v1/tasks/{id}` - Update task by ID.
- `DELETE /api/v1/tasks/{id}` - Delete task by ID.

## Design explanation
The application is designed using a microservice architecture, with the following key components:
- **User Service**: Manages user-related operations such as creating, retrieving, updating, and deleting users.
- **Task Service**: Manages task-related operations, including creating, retrieving, updating, and deleting tasks.
- **Database**: A relational database (PostgreSQL) is used for persistent storage of user and task data.
- **API Layer**: Exposes RESTful endpoints for interaction with the user and task services.
- **Swagger UI**: Provides an interactive interface for testing the API endpoints.
- **Docker Compose**: Simplifies the deployment process by managing the application and database containers.
- **Spring Boot**: The application is built using Spring Boot, which provides a robust framework for developing microservices with minimal configuration.
- **Spring Data JPA**: Used for data access and manipulation, providing an easy way to interact with the database using JPA repositories.
- **Logging**: Integrated logging for monitoring and debugging purposes.
- **Exception Handling**: Global exception handling to manage errors and provide meaningful responses to API consumers.
- **Validation**: Input validation to ensure data integrity and consistency.
- **Flyway**: Database migrations are managed using Flyway, allowing for version control of the database schema.

## Unit and Integration Tests
- for demo purposes, the application includes unit test of the User Service and integration test of the UserController.