# 🌍 Travel Planner Backend

[![Java](https://img.shields.io/badge/Java-25-orange?logo=java&logoColor=white)](https://adoptium.net)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?logo=docker&logoColor=white)](https://www.docker.com)

Backend application for the Travel Planner platform.

REST API for managing trips, accommodations, activities, and transports. Built with Spring Boot, JWT authentication, and a clean layered architecture.

---

## 🚀 Quick Highlights

* 🔐 JWT Authentication (Spring Security)
* 🧭 Full Trip Management System
* 🏨 Accommodations / Activities / Transports
* 👤 User profile management (`/users/me`)
* 🧪 Unit + Controller Tests (JUnit + Mockito + MockMvc)
* 📦 PostgreSQL + Docker setup
* 📄 Swagger/OpenAPI documentation
* 🧱 Clean layered architecture (Controller → Service → Repository)

---

## 🛠 Tech Stack

* Java 25
* Spring Boot 3
* Spring Security (JWT)
* Spring Data JPA / Hibernate
* PostgreSQL
* Maven
* Docker
* JUnit 5
* Mockito
* MockMvc
* OpenAPI (Swagger)

---

## 🧱 Architecture

The project follows a clean layered architecture:

```
Controller → Service → Repository → Database
```

### Principles applied:

* DTO pattern for request/response separation
* Centralized global exception handling (`@RestControllerAdvice`)
* Input validation (`@Valid` / Jakarta Validation)
* Stateless authentication (JWT via OAuth2 Resource Server)
* Ownership-based access control (`/users/me`)
* Proper explicit entity relationships with JPA (`FetchType.LAZY`)

---

## 🔐 Authentication & Security

Authentication is handled using JWT (Bearer Token) via asymmetric RSA keys.

### Example Header:

```http
Authorization: Bearer <token>
```

### Security model:

* Stateless authentication context
* Protected endpoints require a valid JWT
* User context extracted directly from the token subject
* No direct user ID exposure in sensitive routes to prevent IDOR/BOLA vulnerabilities

---

## 📖 API Documentation

Swagger UI is available dynamically at:

```text
http://localhost:8080/swagger-ui/index.html
```

To test secured endpoints:
1. Trigger a successful login at `/auth/login`.
2. Copy the returned JWT token.
3. Click the **Authorize** lock button in Swagger UI.
4. Paste the token and apply.

---

## 📐 Primary API Endpoints Mapping

### 👤 Users & Authentication
* `POST   /users` - Registers a new user account
* `POST   /auth/login` - Authenticates user and issues a JWT token
* `GET    /users/me` - Retrieves the authenticated user profile
* `PUT    /users/me` - Updates data for the currently authenticated user
* `DELETE /users/me` - Deletes user account and cascade wipes all data

### 🗺 Trips
* `POST   /trips` - Creates a new trip itinerary
* `GET    /trips` - Lists all trips belonging to the authenticated user
* `GET    /trips/{id}` - Retrieves a specific trip (ownership validated)
* `PUT    /trips/{id}` - Updates a specific trip's details
* `DELETE /trips/{id}` - Deletes a trip and cascades all its child resources

### 🏨 Accommodations
* `POST   /trips/{tripId}/accommodations` - Links a new accommodation to an owned trip
* `GET    /trips/{tripId}/accommodations` - Lists all accommodations for a specific trip
* `GET    /trips/{tripId}/accommodations/{id}` - Retrieves a specific accommodation detail
* `PUT    /trips/{tripId}/accommodations/{id}` - Updates an existing accommodation entry
* `DELETE /trips/{tripId}/accommodations/{id}` - Deletes an accommodation from the trip

### 🎯 Activities
* `POST   /trips/{tripId}/activities` - Schedules a new activity inside an owned trip
* `GET    /trips/{tripId}/activities` - Lists all scheduled activities for a specific trip
* `GET    /trips/{tripId}/activities/{id}` - Retrieves a specific activity detail
* `PUT    /trips/{tripId}/activities/{id}` - Updates an existing activity entry
* `DELETE /trips/{tripId}/activities/{id}` - Deletes an activity from the itinerary

### ✈ Transports
* `POST   /trips/{tripId}/transports` - Links a transport log to a trip
* `GET    /trips/{tripId}/transports` - Lists all transportation logs for a specific trip
* `GET    /trips/{tripId}/transports/{id}` - Retrieves a specific transport detail
* `PUT    /trips/{tripId}/transports/{id}` - Updates an existing transport log
* `DELETE /trips/{tripId}/transports/{id}` - Deletes a transport log from the trip

---

## 📦 Running Locally

### 1. Clone repository
```bash
git clone https://github.com/viesant/travel-planner
cd travel-planner/backend
```

### 2. Start database
The database infrastructure is isolated via Docker Compose. The required RSA asymmetric keys for cryptographic token signing are already pre-packaged inside the `src/main/resources` folder.
```bash
docker-compose up -d
```

### 3. Run application
```bash
./mvnw spring-boot:run
```
The application will bootstrap successfully at `http://localhost:8080`.

---

## 🧪 Running Tests

You can execute the automated test pipelines directly via Maven:
```bash
./mvnw clean test
```

### Test coverage includes:
* **Service layer unit tests** (business logic validation and date constraint errors via Mockito)
* **Controller layer tests** (HTTP slice testing, status code returns, and JSON contract mapping via MockMvc)
* **Validation tests** (`@NotBlank` and `@Email` constraints check)
* **Centralized exception handling tests** (ProblemDetail output mapping)

---

## 🧭 Design Decisions

* **Stateless JWT-based Architecture**: Ensures horizontal scaling capabilities and decoupled resource serving.
* **IDOR/BOLA Protection**: Utilizes the `/users/me` pattern and token extraction to isolate entity actions without relying on path-based database sequence exposure.
* **Standardized Error Responses**: Adopts the **RFC 9457 (Problem Details)** specification to pass readable, decoupled validation maps (`invalid_fields`) to consumer apps.
* **Cascade Deletes via Hibernate Lifecycle**: Enforces referential integrity at the application layer (`CascadeType.REMOVE` + `orphanRemoval = true`) to prevent database orphan leaks.

---

## 📌 Roadmap

* Integration tests environment setup using Testcontainers
* Production CI/CD automated workflow configuration using GitHub Actions
* Secure authentication expansion with Refresh Token mechanisms
* Global API sorting, pagination, and filter parameters
* Rate limiting interceptor guards
* API design versioning headers

---

## 👤 Author

**Ricardo Vieira dos Santos**

* GitHub: [https://github.com/viesant](https://github.com/viesant)
