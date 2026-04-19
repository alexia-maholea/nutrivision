# NutriVision Spring Backend

Backend API for NutriVision, built with Spring Boot, PostgreSQL, Flyway, and Spring Security (JWT).

## Prerequisites

- Java 17+
- Maven (or use included wrapper `mvnw` / `mvnw.cmd`)
- PostgreSQL running on `localhost:5432`
  - default DB: `postgres`
  - default user/password: `postgres` / `postgres`

## Project Setup

1. Clone the repository and open the `springbackend` folder.
2. Ensure PostgreSQL is running.
3. Create a `.env` file in project root for mail settings.

Notes:

- `.env` is loaded via `spring.config.import: optional:file:.env[.properties]`.
- If `.env` is missing, app starts with defaults from `application.yml`.

## Database (Docker Option)

If you want to start PostgreSQL with Docker Compose:

```powershell
docker compose up -d
```

This starts `postgres:13.2-alpine` mapped to `localhost:5432`.

## Run the Backend

Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

App default URL:

- `http://localhost:8090`

Flyway migrations run automatically at startup.

## Swagger / API Docs

After the app starts, Swagger is available at:

- `http://localhost:8090/swagger-ui/index.html`

OpenAPI JSON:

- `http://localhost:8090/v3/api-docs`

## Authentication Quick Start

Public endpoints:

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`

Most other endpoints require `Authorization: Bearer <token>`.

Default seeded admin credentials (from config):

- email: `admin@admin.com`
- password: `admin`


