# Zlagoda Supermarket Management System

A desktop JavaFX application developed as a database course project. The focus of the project is relational database design, SQL query implementation, and data integrity enforcement across all layers of the application.

## Technical Stack

- **Java 23**
- **JavaFX** — UI framework
- **JDBI 3** — SQL to Java record mapping via `@SqlQuery` / `@SqlUpdate`
- **H2 Database** — embedded relational database
- **Google Guice** — dependency injection
- **Apache POI** — Excel `.xlsx` report export
- **jBCrypt** — password hashing

## System Architecture

The application strictly isolates concerns across the following packages:

- **`controller`** — handles UI interactions and view routing; contains no business logic
- **`service`** — core business logic and transaction management via `TransactionManager`
- **`validator`** — service-level validation of business rules and data integrity before DB writes
- **`dao`** — Data Access Objects; all SQL is written explicitly via JDBI annotations
- **`dto` & `entity`** — immutable Java records for data transfer and DB row mapping

## Database

The application uses an embedded **H2** relational database located at `./supermarket` relative to the execution directory. The schema consists of 6 entities:

- **Employee** — manager and cashier records with personal and employment data
- **Category** — product categories
- **Product** — product catalog with manufacturer and characteristics
- **Store_Product** — items available for sale; each product can have at most one regular and one promotional entry linked via a self-referencing FK (`UPC_prom`)
- **Customer_Card** — loyalty cards with a per-card discount percentage
- **Check_Table / Sale** — purchase history; `Sale` is a junction table storing quantity and price at the time of purchase

### Schema Design Decisions

**Constraints & data types:**
- All price, quantity, salary, and percentage columns enforce non-negative values via `CHECK`
- `date_of_birth` has a DB-level `CHECK` to enforce the 18-year minimum age rule
- Phone number fields are `VARCHAR(13)` to accommodate the `+380XXXXXXXXX` format
- `id_employee` uses `COLLATE BINARY` to make login case-sensitive

**Foreign keys:**
- `ON UPDATE CASCADE` is used throughout
- `ON DELETE RESTRICT` where referential integrity must be preserved
- `ON DELETE CASCADE` from `Check_Table` to `Sale` — deleting a check removes its line items
- `Store_Product.UPC_prom` is a nullable self-referencing FK with `ON DELETE SET NULL`, linking a promotional item to its regular counterpart

**Historical pricing:**
`Sale` stores `selling_price` independently from `Store_Product.selling_price` to preserve accurate check data after repricing events — when a new batch arrives, all stock is repriced, but past sales must reflect the price at the time of purchase.

### Integrity Enforcement

Data integrity is enforced at three levels:

1. **Database** — `CHECK` constraints, `NOT NULL`, and `FOREIGN KEY` rules
2. **Service layer** — business rule validation before every write (existence checks, role checks, and rules not expressible in SQL)
3. **UI layer** — immediate field-level validation via ValidatorFX before form submission

### SQL Files

Located in `src/main/resources/sql/`:

- `schema.sql` — full DDL with all tables, constraints, and indexes
- `seed.sql` — test data

## Setup and Execution

### Prerequisites

- JDK 23+
- Maven 3.8+

### Build & Run

```bash
mvn clean install
mvn javafx:run
```

On startup the schema is initialized automatically — **all tables start empty with no data preloaded**.

### Loading Test Data

**Option 1 — Launch flag:**
```bash
mvn javafx:run -Djavafx.args="--setup-db"
```

**Option 2 — Run the seeder class:**
Execute `DevDataSeeder.java` directly from your IDE (`src/main/java/com/vfrol/supermarket/DevDataSeeder.java`).

**Option 3 — Run SQL manually:**
Execute `seed.sql` from `src/main/resources/sql/` against the H2 database file using your IDE's database tool or [DBeaver](https://dbeaver.io/).

## User Roles

**Manager** — full CRUD access to all entities; can filter, sort, and export reports to Excel.

**Cashier** — can create checks, manage customer cards, browse products, and view their own transaction history.

## Business Rules

- Employees must be at least 18 years old at both the current date and the date of employment start
- Phone numbers must follow the format `+380XXXXXXXXX`
- Promotional price = regular price × 0.8 (fixed 20% discount)
- VAT (20%) is included in the selling price; check VAT = total × 0.2
- Repricing applies to the entire existing stock — old and new batches share the new price
- A manager account cannot be deleted if it is the last one in the system