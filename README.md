# Book Store API

A RESTful API for a Book Store, developed with Java and Spring Boot.
The project provides API endpoints for managing books and categories, user authentication, and access control.

## Technologies

- Java 17
- Spring Boot 3.4.4
- Spring Security
- JWT Authentication
- Spring Data JPA / Hibernate
- MySQL 8.4
- Maven
- Liquibase
- MapStruct
- Docker & Docker Compose
- Swagger / OpenAPI

## Features

- User authentication with JWT
- Role-based access control (USER and ADMIN)
- Book management
- Category management
- Book and category relationships
- Request validation
- Database migrations with Liquibase
- Shopping cart management
- Order creation from the shopping cart
- Order items management
- Order history
- Order status management
- Pagination for retrieving data
- API documentation with Swagger

## Getting Started

### Prerequisites

Make sure you have installed:

- Java 17
- Maven
- Docker Desktop
- Docker Compose

### 1. Clone the repository

```bash
git clone <repository-url>
cd <project-directory>
```

### 2. Configure environment variables

Create your local `.env` file using the provided template:

```bash
cp .env.template .env
```

Open `.env` and configure the required variables:

```dotenv
MYSQLDB_DATABASE=book_store
MYSQLDB_USER=bookstore
MYSQLDB_PASSWORD=your_database_password
MYSQLDB_ROOT_PASSWORD=your_root_password

JWT_SECRET=your_secure_jwt_secret
JWT_EXPIRATION=your_jwt_expiration
```

Use your own secure passwords and JWT secret. Do not commit the `.env` file to the repository.

### 3. Build the application

Build the executable JAR file:

```bash
mvn clean package
```

### 4. Run the application with Docker

Build the Docker image and start the application together with the MySQL database:

```bash
docker compose up --build
```

The application will be available at:

```text
http://localhost:8080/api
```

MySQL is exposed on port `3307` on the host machine.

### 5. Access API documentation

Swagger UI:

```text
http://localhost:8080/api/swagger-ui/index.html
```

## Docker and Database Persistence

The application and MySQL database run in separate Docker containers.

MySQL data is stored in a named Docker volume,
so database records are preserved when containers are stopped or recreated.

To stop the application:

```bash
docker compose down
```

To start it again:

```bash
docker compose up
```

**Note:** Avoid using `docker compose down -v` 
unless you intentionally want to delete the database volume and all its stored data.

## Environment Variables

The following environment variables must be configured in the `.env` file:

| Variable | Description |
|---|---|
| `MYSQLDB_DATABASE` | MySQL database name |
| `MYSQLDB_USER` | MySQL application username |
| `MYSQLDB_PASSWORD` | MySQL application password |
| `MYSQLDB_ROOT_PASSWORD` | MySQL root password |
| `JWT_SECRET` | Secret key used to sign JWT tokens |
| `JWT_EXPIRATION` | JWT token expiration time |

## Security

- Passwords are securely hashed using BCrypt.
- Access to protected endpoints is controlled by user roles.
- Sensitive configuration values are stored in environment variables.
