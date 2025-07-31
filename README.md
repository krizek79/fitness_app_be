[![codecov](https://codecov.io/gh/krizek79/fitness_app_be/branch/master/graph/badge.svg)](https://codecov.io/gh/krizek79/fitness_app_be)

# Fitness App Backend

This is the backend for a fitness management application. It provides a secure and extensible REST API for managing users, coach-client relationships, structured training cycles, weekly plans, workouts, exercises, and fitness goals. The backend is designed to power both personal fitness tracking and collaborative coaching platforms.

## Key Features

* **User Management & Authentication**: Secure JWT-based authentication, role-based access, and strong password hashing.
* **Coach-Client Relationships**: Manage connections between coaches and clients for collaborative training.
* **Training Structure**: CRUD and filtering for cycles, weeks, workouts, and exercises to support long-term planning.
* **Goal Tracking**: Track, update, and filter fitness goals.
* **Profile Management**: User profiles with avatars and bios.
* **Tags & Organization**: Tagging system for categorizing workouts and exercises.
* **OpenAPI Documentation**: Interactive API docs via Swagger UI.
* **Robust Testing & Reporting**: Automated testing with JUnit, Testcontainers, and code coverage reports with JaCoCo & Codecov.
* **CI/CD Pipeline**: GitHub Actions workflows for build, test, and deployment with secret management.
* **Dockerized Setup**: Docker and Docker Compose support for local development and containerized deployment.

## Technologies Used

* Java 17
* Spring Boot (Web, Data JPA, Validation, Security, OAuth2 Resource Server)
* PostgreSQL
* Flyway
* Maven
* Lombok
* JUnit, Testcontainers, JaCoCo, Codecov
* springdoc-openapi (Swagger UI)
* Cloudinary (media hosting)
* Docker, Docker Compose
* Render (application and database hosting)

## Local Development

Copy `.env.template` to `.env` in the project root:

```bash
cp .env.template .env
```

* Edit `.env` to provide your local development values (e.g., database credentials, JWT keystore paths).
* The `.env` file is excluded from version control (via `.gitignore`) to keep sensitive data safe.

## Running with Docker Compose

Use the following commands to start the application locally with Docker Compose:

```bash
cp .env.template .env
# Edit .env if needed
docker-compose up --build
```

## Build & Run

```bash
./mvnw clean install
./mvnw spring-boot:run
```

## API Documentation

Interactive OpenAPI docs available at:
[https://fitness-app-qfb0.onrender.com/swagger-ui/index.html](https://fitness-app-qfb0.onrender.com/swagger-ui/index.html)
(Please be patient, the hosting service is currently quite slow.)

## Testing & Code Coverage

* Tests are executed using **JUnit** and **Testcontainers**.
* Code coverage is measured using **JaCoCo**, and uploaded to [Codecov](https://codecov.io/gh/krizek79/fitness_app_be).
* The Codecov badge at the top of this README shows the latest results.
* Coverage and test reports are uploaded as GitHub Actions artifacts for each run.

## Continuous Integration & Deployment

* CI workflow: `.github/workflows/ci.yml`

    * Triggers on push/PR to `master` and `dev`
    * Runs tests, coverage, and stores artifacts

* CD workflow: `.github/workflows/cd.yml`

    * Triggers only when CI passes on `master` (via `workflow_run`)
    * Deploys backend via Render webhook
    * Ensures clean separation between CI and CD steps