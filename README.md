# Zlagoda Supermarket Management System

A desktop application for supermarket management. The system implements a layered architecture with dependency injection, role-based access control, and an embedded SQL database.

## Technical Stack

- **Java 23**
- **JavaFX** — UI framework
- **JDBI 3** — Data Access layer mapping SQL to Java records
- **H2 Database** — Embedded local DB
- **Google Guice** — Dependency Injection container
- **Apache POI** — Excel `.xlsx` report generation
- **jBCrypt** — Password hashing

## System Architecture

The application strictly isolates concerns across the following packages:

- **`controller`** — Handles UI interactions and view routing. Controllers do not execute business logic directly.
- **`service`** — Contains core business logic, transaction boundaries (`TransactionManager`), and orchestrates DAOs.
- **`validator`** — Service-level validation ensuring data integrity and business rule compliance before DB transactions.
- **`dao`** — Data Access Objects utilizing JDBI `@SqlQuery` and `@SqlUpdate` annotations.
- **`dto` & `entity`** — Immutable Java records used for data transfer and database mapping.

## Setup and Execution

### Prerequisites

- JDK 23 or higher
- Maven 3.8+

### Build & Run

```bash
mvn clean install
mvn javafx:run
```

## Database Initialization & Seeding

The application uses an embedded H2 database located at `./supermarket` relative to the execution directory.

On startup, `SupermarketApp.java` automatically initializes the schema — **all tables start empty with no data preloaded**.

To populate the database with test data, choose one of the following options:

**Option 1 — Launch flag:**
```bash
mvn javafx:run -Djavafx.args="--setup-db"
```

**Option 2 — Run the seeder class:**
Execute `DevDataSeeder.java` directly from your IDE (`src/main/java/com/vfrol/supermarket/DevDataSeeder.java`).

**Option 3 — Run SQL manually:**
Execute the SQL files in `src/main/resources/sql/` directly against the H2 database file using your IDE's database tool or [DBeaver](https://dbeaver.io/).

## User Roles

The system supports two roles with different access levels:

**Manager** — full control over employees, customers, categories, products, store products, and checks. Can generate and export reports.

**Cashier** — can create sales (checks), manage customer cards, browse products, and view their own transaction history.

## Business Rules

- Employees must be at least 18 years old
- Phone numbers must follow the format `+380XXXXXXXXX`
- Promotional product price = base price × 0.8 (20% discount)
- VAT is included in the selling price at 20%; check VAT = total × 0.2
- Each product can have at most one regular and one promotional store entry
- Check history is retained for 3 years
