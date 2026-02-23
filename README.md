# Department Service

Microservice for managing organizational departments, hierarchies, and department-related operations.

## Overview

The Department Service handles CRUD operations for department data, including department creation, hierarchy management, and employee-department associations.

## Architecture Overview

### System Architecture
```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │ HTTP/REST + JWT
       ▼
┌─────────────────────────────────┐
│   Department Service (8084)     │
│  ┌──────────────────────────┐   │
│  │  JWT Filter              │   │
│  └────────┬─────────────────┘   │
│           ▼                      │
│  ┌──────────────────────────┐   │
│  │  Department Controller   │   │
│  └────────┬─────────────────┘   │
│           ▼                      │
│  ┌──────────────────────────┐   │
│  │  Department Service      │   │
│  └────────┬─────────────────┘   │
│           ▼                      │
│  ┌──────────────────────────┐   │
│  │  Department Repository   │   │
│  └────────┬─────────────────┘   │
└───────────┼─────────────────────┘
            ▼
    ┌──────────────┐
    │  MySQL DB    │
    │(department_db)│
    └──────────────┘
            ▲
            │ WebClient
    ┌───────┴────────┐
    │                │
┌───▼────┐    ┌─────▼──────┐
│Auth    │    │Employee    │
│Service │    │Service     │
│(8081)  │    │(8082)      │
└────────┘    └────────────┘
```

### Component Responsibilities
- **JWT Filter**: Validates JWT tokens from Auth Service
- **Department Controller**: REST API endpoints
- **Department Service**: Business logic, hierarchy management, validation
- **Department Repository**: Database operations
- **WebClient**: Inter-service communication (Employee Service)

### Data Flow
1. Client sends authenticated request with JWT token
2. JWT Filter validates token
3. Controller receives request and delegates to service layer
4. Service layer processes business logic
5. Repository performs database operations
6. WebClient fetches employee data if needed
7. Response returned to client

## Assumptions

### Technical Assumptions
- MySQL database is accessible on localhost:3306
- JWT secret matches Auth Service configuration
- Employee Service is available for employee validation
- Database schema auto-created via Hibernate DDL
- WebFlux WebClient used for reactive HTTP calls

### Business Assumptions
- Department codes are unique and immutable
- Maximum hierarchy depth is 5 levels
- Manager must be an existing employee
- Department cannot be deleted if it has employees or sub-departments
- Budget is in base currency (no multi-currency support)
- One manager per department
- Parent department can be null (root department)

### Operational Assumptions
- Service runs on port 8084
- Pagination default: 10 items per page
- No caching implemented (can be added)
- Logging enabled for SQL queries
- No soft delete (hard delete only)

## Technology Stack

- **Java**: 21
- **Spring Boot**: 4.0.3
- **Spring Security**: JWT validation
- **Spring Data JPA**: Database operations
- **Spring WebFlux**: Reactive HTTP client
- **MySQL**: Database
- **Lombok**: Reduce boilerplate code (1.18.42)
- **JWT**: io.jsonwebtoken (0.11.5)

## Prerequisites

- JDK 21 or higher
- Maven 3.6+
- MySQL 8.0+
- Auth Service running (for JWT validation)

## Dependencies

```xml
- spring-boot-starter-data-jpa
- spring-boot-starter-validation
- spring-boot-starter-webmvc
- spring-boot-starter-security
- spring-boot-starter-webflux
- mysql-connector-j
- lombok (1.18.42)
- jjwt-api (0.11.5)
- jjwt-impl (0.11.5)
- jjwt-jackson (0.11.5)
```

## Environment Variables

Create `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8084

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/department_db
spring.datasource.username=<db_username>
spring.datasource.password=<db_password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# JWT Configuration
jwt.secret=<your_secret_key>

# Service URLs
auth.service.url=http://localhost:8081
employee.service.url=http://localhost:8082
```

## Setup Instructions

1. **Navigate to project directory**
   ```bash
   cd department-service
   ```

2. **Create MySQL database**
   ```sql
   CREATE DATABASE department_db;
   ```

3. **Configure application.properties**
   - Update database credentials
   - Set JWT secret key (must match auth-service)
   - Configure service URLs

4. **Build the project**
   ```bash
   mvn clean install
   ```

5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

## API Endpoints

All endpoints require JWT authentication via `Authorization: Bearer <token>` header.

### Department Management

#### Create Department
```http
POST /api/departments
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Engineering",
  "code": "ENG",
  "description": "Engineering Department",
  "managerId": 5,
  "parentDepartmentId": null,
  "location": "Building A, Floor 3",
  "budget": 1000000
}
```

#### Get All Departments
```http
GET /api/departments
Authorization: Bearer <token>

Query Parameters:
- page (default: 0)
- size (default: 10)
- sort (default: name)
```

#### Get Department by ID
```http
GET /api/departments/{id}
Authorization: Bearer <token>

Response:
{
  "id": 1,
  "name": "Engineering",
  "code": "ENG",
  "description": "Engineering Department",
  "managerId": 5,
  "managerName": "John Doe",
  "parentDepartmentId": null,
  "location": "Building A, Floor 3",
  "budget": 1000000,
  "employeeCount": 25,
  "createdAt": "2024-01-15T10:00:00",
  "updatedAt": "2024-02-20T15:30:00"
}
```




#### Get Department Employees
```http
GET /api/departments/{id}/employees
Authorization: Bearer <token>

Query Parameters:
- page (default: 0)
- size (default: 10)
```

## Project Structure

```
department-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/company/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── entity/
│   │   │       ├── exception/
│   │   │       ├── repository/
│   │   │       ├── security/
│   │   │       └── service/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

## Testing Instructions

### Unit Tests
Run all unit tests:
```bash
mvn test
```

Run specific test class:
```bash
mvn test -Dtest=DepartmentServiceTest
```

### Integration Tests
Run integration tests:
```bash
mvn verify
```

### Test Coverage
Generate coverage report:
```bash
mvn clean test jacoco:report
```
View at: `target/site/jacoco/index.html`

### Manual API Testing

#### 1. Get JWT token from Auth Service
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin@example.com","password":"admin123"}'
```

#### 2. Create department
```bash
curl -X POST http://localhost:8084/api/departments \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Engineering","code":"ENG","description":"Engineering Dept","managerId":1,"budget":1000000}'
```

#### 3. Get all departments
```bash
curl -X GET http://localhost:8084/api/departments \
  -H "Authorization: Bearer <token>"
```

#### 4. Get department hierarchy
```bash
curl -X GET http://localhost:8084/api/departments/1/hierarchy \
  -H "Authorization: Bearer <token>"
```

#### 5. Search departments
```bash
curl -X GET "http://localhost:8084/api/departments/search?query=eng" \
  -H "Authorization: Bearer <token>"
```

#### 6. Get department statistics
```bash
curl -X GET http://localhost:8084/api/departments/1/statistics \
  -H "Authorization: Bearer <token>"
```

#### 7. Update department
```bash
curl -X PUT http://localhost:8084/api/departments/1 \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Engineering & Tech","budget":1200000}'
```

### Test Data Setup
```sql
USE department_db;
INSERT INTO departments (name, code, description, manager_id, budget, location) VALUES 
('IT', 'IT', 'Information Technology', 1, 500000, 'Building A'),
('HR', 'HR', 'Human Resources', 2, 300000, 'Building B');
```

### Testing Checklist
- [ ] Create department with valid data
- [ ] Create department with duplicate code
- [ ] Update department information
- [ ] Delete empty department
- [ ] Prevent delete department with employees
- [ ] Get department hierarchy
- [ ] Search departments by name
- [ ] Validate manager exists
- [ ] Test circular hierarchy prevention
- [ ] Test pagination

## Docker Support

```dockerfile
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/department-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:
```bash
docker build -t department-service .
docker run -p 8084:8084 department-service
```

## Validation Rules

- Department name: Required, 2-100 characters, unique
- Department code: Required, 2-10 characters, unique, uppercase
- Manager ID: Must be valid employee
- Budget: Positive number
- Parent department: Cannot create circular hierarchy

## Business Rules

- Department cannot be deleted if it has employees
- Department cannot be deleted if it has sub-departments
- Manager must be an employee of the department or parent department
- Department code must be unique across organization
- Maximum hierarchy depth: 5 levels

## Error Handling

Standard error response format:
```json
{
  "timestamp": "2024-02-22T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Department code already exists",
  "path": "/api/departments"
}
```

## Common Error Codes

- 400: Invalid request data
- 401: Unauthorized (invalid/missing token)
- 403: Forbidden (insufficient permissions)
- 404: Department not found
- 409: Conflict (duplicate code/name)
- 500: Internal server error

## Integration with Other Services

- **Auth Service**: JWT token validation
- **Employee Service**: Employee information and validation

## Performance Considerations

- Implement caching for frequently accessed departments
- Use pagination for large result sets
- Optimize hierarchy queries with recursive CTEs
- Index department code and name fields

## Contributing

1. Create feature branch
2. Commit changes
3. Push to branch
4. Create Pull Request

## License

Proprietary
