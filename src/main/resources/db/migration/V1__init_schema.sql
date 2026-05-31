create table users (
    id bigint primary key auto_increment,
    full_name varchar(120) not null,
    email varchar(160) not null,
    phone varchar(30) not null,
    password_hash varchar(255) not null,
    role varchar(30) not null,
    enabled boolean not null,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    constraint uk_users_email unique (email),
    constraint uk_users_phone unique (phone),
    constraint ck_users_role check (role in ('ROLE_USER', 'ROLE_ADMIN'))
);

create table cars (
    id bigint primary key auto_increment,
    brand varchar(80) not null,
    model varchar(80) not null,
    manufacture_year integer not null,
    license_plate varchar(30) not null,
    city varchar(60) not null,
    transmission varchar(60) not null,
    fuel_type varchar(60) not null,
    seats integer not null,
    daily_rate numeric(12, 2) not null,
    status varchar(30) not null,
    description varchar(500),
    version bigint,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    constraint uk_cars_license_plate unique (license_plate),
    constraint ck_cars_status check (status in ('AVAILABLE', 'MAINTENANCE', 'INACTIVE')),
    constraint ck_cars_daily_rate check (daily_rate > 0),
    constraint ck_cars_seats check (seats > 0),
    constraint ck_cars_year check (manufacture_year >= 1980)
);

create table bookings (
    id bigint primary key auto_increment,
    user_id bigint not null,
    car_id bigint not null,
    start_at datetime(6) not null,
    end_at datetime(6) not null,
    total_amount decimal(12, 2) not null,
    status varchar(30) not null,
    cancel_reason varchar(300),
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    constraint fk_bookings_user foreign key (user_id) references users(id),
    constraint fk_bookings_car foreign key (car_id) references cars(id),
    constraint ck_bookings_status check (status in ('CONFIRMED', 'CANCELLED', 'COMPLETED')),
    constraint ck_bookings_interval check (end_at > start_at),
    constraint ck_bookings_total check (total_amount >= 0)
);

create table refresh_tokens (
    id bigint primary key auto_increment,
    user_id bigint not null,
    token_hash varchar(128) not null,
    expires_at datetime(6) not null,
    revoked boolean not null,
    created_at datetime(6) not null,
    constraint fk_refresh_tokens_user foreign key (user_id) references users(id),
    constraint uk_refresh_tokens_token_hash unique (token_hash)
);

create index idx_users_email on users(email);
create index idx_cars_search on cars(status, city, brand, daily_rate);
create index idx_cars_license_plate on cars(license_plate);
create index idx_bookings_user_start on bookings(user_id, start_at desc);
create index idx_bookings_car_status_interval on bookings(car_id, status, start_at, end_at);
create index idx_refresh_tokens_user on refresh_tokens(user_id);
create index idx_refresh_tokens_expiry on refresh_tokens(expires_at);
