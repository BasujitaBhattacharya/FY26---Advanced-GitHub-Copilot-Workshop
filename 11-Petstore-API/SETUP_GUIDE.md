# Petstore API Setup Guide

This guide provides detailed instructions for setting up and running the Petstore API project locally.

## H2 Database Setup

The Petstore API uses **H2 Database**, an embedded relational database ideal for development and testing.

### Connection Details

When the application is running, you can connect to the database using the following credentials:

| Property | Value |
|----------|-------|
| **URL** | `jdbc:h2:mem:petstoredb` |
| **Driver** | `org.h2.Driver` |
| **Username** | `sa` |
| **Password** | (empty) |

These settings are configured in `petstore-core/src/main/resources/application.yml`.

### H2 Web Console Access

The H2 Database Console provides a web-based interface for executing SQL queries and managing the database.

**To access the console:**

1. Ensure the application is running (`mvn spring-boot:run` in the `petstore-core` directory)
2. Open your browser and navigate to: **http://localhost:8080/h2-console**
3. Enter the connection details from the table above
4. Click **Connect** to access the database

**Useful operations in the console:**
- Execute SQL queries directly
- Browse database schema and tables
- Insert, update, or delete data
- Test complex queries before adding them to the application

## Initial Data

### Data Seed File

The `petstore-core/src/main/resources/data.sql` file contains seed data that is automatically loaded when the application starts. This file is executed after the database schema is created (due to `spring.jpa.hibernate.ddl-auto=create-drop`).

### Sample Pets (10 Seed Records)

The following 10 pets are loaded automatically on startup:

| Name | Species | Price | Stock |
|------|---------|-------|-------|
| Buddy | DOG | $299.99 | 5 |
| Whiskers | CAT | $199.99 | 8 |
| Tweety | BIRD | $79.99 | 12 |
| Fluffy | RABBIT | $89.99 | 6 |
| Nibbles | HAMSTER | $29.99 | 20 |
| Nemo | FISH | $39.99 | 15 |
| Rex | DOG | $349.99 | 3 |
| Mittens | CAT | $249.99 | 4 |
| Polly | BIRD | $149.99 | 7 |
| Hoppy | RABBIT | $129.99 | 5 |

### Sample Cart Items

Two sample cart items are pre-loaded for testing:

- **Cart Item 1:** 2x Buddy (DOG)
- **Cart Item 2:** 1x Tweety (BIRD)

### Adding More Seed Data

To add additional seed data:

1. Open `petstore-core/src/main/resources/data.sql`
2. Add new `INSERT` statements following the existing format:
   ```sql
   INSERT INTO pets (name, species, price, inventory_count, created_at, updated_at) 
   VALUES ('PetName', 'SPECIES', price, stock_count, NOW(), NOW());
   ```
3. Supported species: `DOG`, `CAT`, `BIRD`, `RABBIT`, `HAMSTER`, `FISH`
4. Save the file and restart the application

## Build Profiles

The project uses Maven's default profile system to support different environments:

### Available Profiles

- **dev (default):** Uses H2 in-memory database for rapid development and testing
- **test:** Uses H2 with isolated test data, suitable for running integration tests

### Activating Profiles

Build with a specific profile:

```bash
# Development (default, no flag needed)
mvn clean package

# Explicit development
mvn clean package -P dev

# Test profile
mvn clean package -P test
```

The profile configuration is managed in the parent `pom.xml`. H2 is currently configured for all profiles.

## Maven Commands

This multi-module Maven project provides the following build and execution commands. Run them from the `petstore-parent` or `petstore-core` directory:

### Build and Package

```bash
# Full build with all modules
mvn clean install

# Package for deployment (creates JAR)
mvn clean package

# View dependency tree
mvn dependency:tree
```

### Running the Application

```bash
# Run the Spring Boot application locally
mvn spring-boot:run
```

The application will start on **http://localhost:8080**.

### Testing

```bash
# Run all unit and integration tests
mvn test

# Run tests for a specific module
mvn test -pl petstore-core

# Skip tests during build
mvn clean package -DskipTests
```

## Troubleshooting

### Port 8080 Already in Use

**Error:** `Address already in use: bind`

**Solution:** Change the server port in `petstore-core/src/main/resources/application.yml`:

```yaml
server:
  port: 8081  # Change to an available port
```

Then restart the application.

### H2 Console Not Accessible

**Error:** Cannot reach http://localhost:8080/h2-console or returns 404

**Solution:** Ensure H2 console is enabled in `application.yml`:

```yaml
spring:
  h2:
    console:
      enabled: true
```

Restart the application after making changes.

### Pets Table Not Found

**Error:** `SQLException: Table "PETS" not found` when querying

**Solution:** Verify the Hibernate DDL setting in `application.yml`:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: create-drop  # Must be set to auto-create tables on startup
```

The `create-drop` setting creates tables on startup and drops them on shutdown. After restarting the application, the schema and seed data should be automatically created.

## Development Tips

### Swagger UI for API Testing

The project includes SpringDoc OpenAPI (Swagger), providing a web-based UI for testing API endpoints:

- **URL:** http://localhost:8080/swagger-ui.html
- **OpenAPI Spec:** http://localhost:8080/v3/api-docs

Use the Swagger UI to:
- Explore all available endpoints
- Test API calls without external tools
- Review request/response schemas

### H2 Console for Database Queries

Use the H2 Web Console (http://localhost:8080/h2-console) to:
- Execute SQL queries directly against the database
- Verify data was inserted correctly
- Test complex queries
- Troubleshoot data issues

### Checking Application Logs

Monitor the console output when running `mvn spring-boot:run` for:
- SQL execution (if `show-sql: true` in application.yml)
- Startup messages and errors
- Request/response logs (if configured)

### Using RequestId for Debugging

Include a Request ID header in API calls for tracing requests through logs. The application can be configured to log Request IDs across the request lifecycle.

## Next Steps

After completing the setup, consider these enhancements:

### 1. Modify Seed Data
- Customize the list of sample pets in `data.sql`
- Add your own business logic or test data

### 2. Add Custom Repository Queries
- Extend `PetRepository` with custom query methods using `@Query`
- Example: Find pets by species, price range, or stock level

### 3. Add Validation Annotations
- Add validation constraints to entity classes (e.g., `@NotNull`, `@Min`, `@Max`)
- Example:
  ```java
  @NotBlank(message = "Pet name is required")
  private String name;
  
  @Positive(message = "Price must be greater than zero")
  private BigDecimal price;
  ```

### 4. Add Unit and Integration Tests
- Create test classes using **xUnit** and **Moq** patterns
- Test repository queries, service logic, and API endpoints
- Use H2 test profile for isolated test data

### 5. Configure Logging
- Set log levels for different packages in `application.yml`
- Enable SQL logging for debugging: `spring.jpa.show-sql: true`
- Use structured logging frameworks for production readiness

## Quick Start Checklist

- [ ] Java 17+ is installed (`java -version`)
- [ ] Maven 3.8+ is installed (`mvn -version`)
- [ ] Clone or navigate to the project root (`11-Petstore-API`)
- [ ] Run `mvn clean install` to build all modules
- [ ] Run `mvn spring-boot:run -pl petstore-core` to start the application
- [ ] Open http://localhost:8080/swagger-ui.html to test API endpoints
- [ ] Open http://localhost:8080/h2-console to view the database
- [ ] Start developing! 🚀

---

For additional information, refer to:
- **Spring Boot Documentation:** https://spring.io/projects/spring-boot
- **H2 Database Guide:** https://www.h2database.com
- **SpringDoc OpenAPI:** https://springdoc.org
