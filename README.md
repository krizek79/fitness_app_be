[![codecov](https://codecov.io/gh/krizek79/fitness_app_be/branch/master/graph/badge.svg)](https://codecov.io/gh/krizek79/fitness_app_be)

# Fitness App Backend

This is the backend for a fitness management application. It provides a secure and extensible REST API for managing users, coach-client relationships, structured training cycles, weekly plans, workouts, exercises, and fitness goals. The backend is designed to power both personal fitness tracking and collaborative coaching platforms.

## Key Features

- **User Management & Authentication**: Secure JWT-based authentication, role-based access, and strong password hashing.
- **Coach-Client Relationships**: Manage connections between coaches and clients for collaborative training.
- **Training Structure**: CRUD and filtering for cycles, weeks, workouts, and exercises to support long-term planning.
- **Goal Tracking**: Track, update, and filter fitness goals.
- **Profile Management**: User profiles with avatars and bios.
- **Tags & Organization**: Tagging system for categorizing workouts and exercises.
- **OpenAPI Documentation**: Interactive API docs via Swagger UI.
- **Robust Testing & Reporting**: Automated testing with JUnit, Testcontainers, and code coverage reports with JaCoCo & Codecov.

## Technologies Used

- Java 17
- Spring Boot (Web, Data JPA, Validation, Security, OAuth2 Resource Server)
- PostgreSQL
- Flyway
- Maven
- Lombok
- JUnit, Testcontainers, JaCoCo, Codecov
- springdoc-openapi (Swagger UI)
- Cloudinary (media hosting)
- Docker (mandatory for running tests)

## Local Development
Copy `.env.template` to `.env` in the project root:<br>
```cp .env.template .env```
- Edit `.env` to provide your local development values (e.g., local database credentials).
- The `.env` file is excluded from version control (via .gitignore) to keep sensitive data safe.
- When running locally (e.g., via mvn spring-boot:run or docker-compose), Spring Boot picks up these variables.

## Running with Docker Compose
Use the following commands to start the application locally with Docker Compose, which automatically loads variables from `.env`:
```bash
cp .env.template .env
# Edit .env to fill in your local credentials if needed
docker-compose up --build
```

## Build & Run

```bash
./mvnw clean install
./mvnw spring-boot:run
```

## API Docs

Interactive OpenAPI docs available at:  
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Testing & Reports

- Tests are executed using **JUnit** and **Testcontainers** during CI runs.
- **Code coverage** is measured using **JaCoCo**, and the results are uploaded to [Codecov](https://codecov.io/gh/krizek79/fitness_app_be).
- The Codecov badge at the top of this README displays the current coverage percentage.
- CI pipeline is defined in `.github/workflows/ci.yml`.
- HTML coverage and test results are stored as GitHub Actions artifacts per run.