# Internal Transfers Service

Internal Transfers - Spring Boot 3.2 (Java 21), PostgreSQL, Liquibase. Use Docker Compose for quickest run.

## Quickstart
1. Build and run with Docker:
   ```bash
   docker compose up --build -d
   ```
2. App available at: http://localhost:8080

## API (curl)
- Create account
  ```bash
  curl -sS -X POST http://localhost:8080/accounts \
    -H 'Content-Type: application/json' \
    -d '{
      "account_id": 1,
      "initial_balance": 100.00
    }'
  ```

- Get account
  ```bash
  curl -sS http://localhost:8080/accounts/1
  ```

- Submit transaction
  ```bash
  curl -sS -X POST http://localhost:8080/transactions \
    -H 'Content-Type: application/json' \
    -d '{
      "source_account_id": 1,
      "destination_account_id": 2,
      "amount": 25.50
    }'
  ```

## API Assumptions
- **Idempotency**
  - Create Account is not upsert; reusing an existing `account_id` will give an error.
  - Transaction submission is not idempotent by default; replays re-apply.
- **Amounts and currency**
  - `amount` is a decimal (2 decimal places). 
  - Single currency only for all the accounts.
- **Validation**
  - Non-negative `balances` and `transfer amount`.
  - `source_account_id` and `destination_account_id` must exist and differ.
  - Insufficient balance returns a validation error (4xx).
- **Responses**
  - Create Account and Submit Transaction return `200 OK` with empty body.
  - Get Account returns `account_id` and `balance`.
- **Consistency**
  - Transfers are atomic with row-level locking.
- **Error model**
  - Validation errors return `4xx` with a simple message.
  Example:
  ```json
  {
    "error": "Account not found for accountId 125"
  }
  ```

## Project Structure
- app: REST application (Spring Web, JPA, Validation)
- database: Liquibase migrator module
- docker-compose.yml: Postgres, migrator, and app services
- Dockerfile: Multi-stage build producing a single image containing both jars

## Configuration
The application reads database settings from environment variables:
- DB_URL (default in Compose: `jdbc:postgresql://postgres:5432/internal_transfers`)
- DB_USERNAME (default: `postgres`)
- DB_PASSWORD (default: `postgres`)

For local (without Docker), typical values are:
- DB_URL=`jdbc:postgresql://localhost:5432/internal_transfers`
- DB_USERNAME=`postgres`
- DB_PASSWORD=`postgres`

## Build
- Build all modules (skip tests):
  ```bash
  mvn -DskipTests package
  ```
- Run tests:
  ```bash
  mvn test
  ```

## Run with Docker (recommended)
1. Build image and start services:
   ```bash
   docker compose up --build -d
   ```
   This will:
   - Start Postgres with a persistent volume.
   - Run the `migrator` (Liquibase) once.
   - Start the `app` after migrations have completed.

2. Access the app:
   - App: http://localhost:8080
   - Postgres: localhost:5432 (db `internal_transfers`, user `postgres`, pass `postgres`)

3. View logs:
   ```bash
   docker compose logs -f app
   ```

4. Stop services:
   ```bash
   docker compose down
   ```
   If you also want to remove the database volume (data loss):
   ```bash
   docker compose down -v
   ```

## Run locally (without Docker)
1. Start a local Postgres 16 with a database named `internal_transfers` and user/password `postgres/postgres` (or adjust env vars accordingly).

2. Run Liquibase migrations (choose one):
   - Using packaged jar (after `mvn package`):
     ```bash
     DB_URL=jdbc:postgresql://localhost:5432/internal_transfers \
     DB_USERNAME=postgres \
     DB_PASSWORD=postgres \
     java -jar database/target/database-0.0.1-SNAPSHOT.jar
     ```
   - Or via Maven:
     ```bash
     mvn -pl database spring-boot:run -Dspring-boot.run.jvmArguments="-DDB_URL=jdbc:postgresql://localhost:5432/internal_transfers -DDB_USERNAME=postgres -DDB_PASSWORD=postgres"
     ```

3. Run the application:
   - Using packaged jar (after `mvn package`):
     ```bash
     DB_URL=jdbc:postgresql://localhost:5432/internal_transfers \
     DB_USERNAME=postgres \
     DB_PASSWORD=postgres \
     java -jar app/target/app-0.0.1-SNAPSHOT.jar
     ```
   - Or via Maven:
     ```bash
     mvn -pl app spring-boot:run -Dspring-boot.run.jvmArguments="-DDB_URL=jdbc:postgresql://localhost:5432/internal_transfers -DDB_USERNAME=postgres -DDB_PASSWORD=postgres"
     ```

## Troubleshooting
- If the app fails to start due to missing tables, ensure migrations have run successfully (check `migrator` logs in Docker or rerun the database module locally).
- Ensure ports 8080 (app) and 5432 (Postgres) are free.
- Delete and recreate the Docker volume if schema changes cause drift:
  ```bash
  docker compose down -v && docker compose up --build
  ```
