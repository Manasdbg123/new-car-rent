# Database Design

## ER Diagram

```mermaid
erDiagram
    USERS ||--o{ BOOKINGS : creates
    CARS ||--o{ BOOKINGS : reserved_for
    USERS ||--o{ REFRESH_TOKENS : owns

    USERS {
        bigint id PK
        varchar full_name
        varchar email UK
        varchar phone UK
        varchar password_hash
        varchar role
        boolean enabled
        datetime created_at
        datetime updated_at
    }

    CARS {
        bigint id PK
        varchar brand
        varchar model
        integer year
        varchar license_plate UK
        varchar city
        varchar transmission
        varchar fuel_type
        integer seats
        numeric daily_rate
        varchar status
        bigint version
        datetime created_at
        datetime updated_at
    }

    BOOKINGS {
        bigint id PK
        bigint user_id FK
        bigint car_id FK
        timestamp start_at
        timestamp end_at
        numeric total_amount
        varchar status
        varchar cancel_reason
        datetime created_at
        datetime updated_at
    }

    REFRESH_TOKENS {
        bigint id PK
        bigint user_id FK
        varchar token_hash UK
        datetime expires_at
        boolean revoked
        datetime created_at
    }
```

## Schema

The executable SQL schema is maintained in `src/main/resources/db/migration/V1__init_schema.sql`.

## Booking Concurrency

Bookings lock the selected car row with `PESSIMISTIC_WRITE` inside a transaction, then check active interval overlap using:

`existing.start_at < requested.end_at AND existing.end_at > requested.start_at`

The composite index `idx_bookings_car_status_interval` supports overlap checks for confirmed bookings on MySQL.
