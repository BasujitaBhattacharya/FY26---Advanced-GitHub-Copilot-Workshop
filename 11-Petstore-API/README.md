# Petstore API

## Project Title & Description

**Spring Boot Petstore API** is a microservices-based REST API application built with Spring Boot that manages a pet store's core operations. The system comprises three essential services: Pet Inventory Management, Shopping Cart, and Order Processing.

### Technology Stack

- **Framework:** Spring Boot 3.x
- **Language:** Java 17+
- **Build Tool:** Maven 3.8+
- **Database:** H2 in-memory database
- **ORM:** Spring Data JPA with Hibernate
- **API Documentation:** Swagger UI / Springfox
- **Architecture:** Layered Architecture with Maven Multi-Module Structure

## Features

- **Pet Inventory Service** – Browse, create, update, and manage pet inventory with detailed product information
- **Cart Management Service** – Add/remove pets from shopping cart, view cart contents, and manage item quantities
- **Order Processing Service** – Create orders from cart items, track order status, and manage order history
- **H2 In-Memory Database** – Embedded database with auto-initialization for quick development and testing
- **REST APIs** – Comprehensive REST endpoints for all operations with proper HTTP methods
- **Swagger UI** – Interactive API documentation and testing interface
- **Spring Data JPA** – Efficient data persistence with Hibernate ORM for database operations

## Architecture

The Petstore API follows a **layered architecture** pattern:

```
Request → Controller → Service → Repository → Entity ↔ Database
```

### Layer Responsibilities

- **Controller Layer** – Handles HTTP requests, route management, and response formatting
- **Service Layer** – Business logic, validation, and orchestration between repositories
- **Repository Layer** – Data access abstraction using Spring Data JPA
- **Entity Layer** – Domain models and database table representations

### Maven Multi-Module Structure

The project is organized as a Maven multi-module build for better modularity and code organization:

- **petstore-parent** – Root POM with common dependencies and plugin configurations
- **petstore-common** – Shared utilities, DTOs, and common classes used across modules
- **petstore-core** – Main Spring Boot application containing the three services

## Prerequisites

Before running the application, ensure you have the following installed:

- **Java Development Kit (JDK)** – Version 17 or higher
- **Apache Maven** – Version 3.8 or higher
- **Git** – (Optional) For cloning the repository

Verify installations:
```bash
java -version
mvn -version
```

## Installation & Setup

### Clone the Repository

```bash
git clone <repository-url>
cd 11-Petstore-API
```

### Build the Project

```bash
mvn clean install
```

This command:
- Cleans previous builds
- Resolves dependencies
- Compiles source code
- Runs unit tests
- Packages the application

## Running the Application

### Start the Spring Boot Application

Navigate to the core module and start the application:

```bash
cd petstore-core
mvn spring-boot:run
```

### Access the Application

Once running, the application will be available at:

- **API Base URL** – http://localhost:8080
- **Swagger UI** – http://localhost:8080/swagger-ui.html
- **H2 Console** – http://localhost:8080/h2-console

### H2 Console Credentials

- **JDBC URL:** `jdbc:h2:mem:petstoredb`
- **Username:** `sa`
- **Password:** (leave blank)

## Project Structure

```
11-Petstore-API/
├── petstore-parent/                 # Root POM and parent configuration
│   └── pom.xml                      # Parent POM with common dependencies
├── petstore-common/                 # Shared utilities and DTOs
│   ├── src/
│   │   └── main/java/
│   │       └── com/petstore/
│   │           ├── dto/            # Data Transfer Objects
│   │           ├── entity/         # Base entities
│   │           └── util/           # Utility classes
│   └── pom.xml
└── petstore-core/                   # Main Spring Boot application
    ├── src/main/
    │   ├── java/com/petstore/
    │   │   ├── config/             # Spring configuration classes
    │   │   ├── exception/          # Custom exceptions
    │   │   └── (modules below)
    │   └── resources/
    │       ├── application.yml      # Spring Boot configuration
    │       └── data.sql             # H2 initialization script
    ├── inventory/                   # Pet Inventory Service Module
    │   ├── controller/              # REST endpoints for pet management
    │   ├── service/                 # Business logic for inventory
    │   ├── repository/              # Data access for pets
    │   └── entity/                  # Pet domain model
    ├── cart/                        # Cart Management Service Module
    │   ├── controller/              # REST endpoints for cart operations
    │   ├── service/                 # Shopping cart business logic
    │   ├── repository/              # Cart data persistence
    │   └── entity/                  # Cart and CartItem models
    ├── order/                       # Order Processing Service Module
    │   ├── controller/              # REST endpoints for orders
    │   ├── service/                 # Order processing logic
    │   ├── repository/              # Order data access
    │   └── entity/                  # Order and OrderItem models
    └── pom.xml
```

## API Endpoints Overview

The Petstore API provides comprehensive REST endpoints for managing pets, shopping carts, and orders. Detailed API documentation is available in [API_ENDPOINTS.md](./API_ENDPOINTS.md).

### Pet Inventory Service

- `GET /api/pets` – Retrieve all available pets
- `GET /api/pets/{id}` – Retrieve a specific pet by ID
- `POST /api/pets` – Create a new pet
- `PUT /api/pets/{id}` – Update an existing pet
- `DELETE /api/pets/{id}` – Delete a pet from inventory

### Cart Management Service

- `GET /api/cart` – Retrieve current shopping cart
- `POST /api/cart/items` – Add pet(s) to cart
- `PUT /api/cart/items/{itemId}` – Update cart item quantity
- `DELETE /api/cart/items/{itemId}` – Remove item from cart
- `DELETE /api/cart` – Clear entire cart

### Order Processing Service

- `GET /api/orders` – Retrieve all orders
- `GET /api/orders/{id}` – Retrieve a specific order
- `POST /api/orders` – Create a new order from cart
- `GET /api/orders/{id}/status` – Check order status
- `PUT /api/orders/{id}/status` – Update order status

## Database

### H2 In-Memory Database

The application uses an H2 embedded, in-memory database for development and testing purposes.

### Auto-Initialization

On application startup, the database is automatically initialized with:
- Table schemas defined via Spring Data JPA entity annotations
- Initial sample data loaded from `data.sql` in the resources folder

### Connection Details

- **Type:** In-Memory Database
- **JDBC URL:** `jdbc:h2:mem:petstoredb`
- **Driver Class:** `org.h2.Driver`
- **Default Username:** `sa`
- **Default Password:** (empty)

### Accessing H2 Console

1. Start the application (as described above)
2. Navigate to http://localhost:8080/h2-console
3. Enter the JDBC URL: `jdbc:h2:mem:petstoredb`
4. Click "Connect"

**Note:** Data persists only during the application runtime. All changes are lost when the application stops.

## Build & Test

### Build the Project

```bash
mvn clean package
```

This creates a JAR file in the `target/` directory ready for deployment.

### Run Tests

```bash
mvn test
```

Runs all unit tests in the test suites and generates a coverage report.

### Skip Tests During Build

```bash
mvn clean package -DskipTests
```

## License

This project is licensed under the **MIT License** – see the [LICENSE](./LICENSE) file for details.

---

**For more information or to contribute**, please refer to the main repository's [CONTRIBUTING.md](../CONTRIBUTING.md) guidelines.
