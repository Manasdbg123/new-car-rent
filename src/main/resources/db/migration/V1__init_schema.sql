create table users (
                       id bigint primary key auto_increment,
                       full_name varchar(120) not null,
                       email varchar(160) not null,
                       phone varchar(30) not null,
                       password varchar(255) not null,
                       role varchar(30) not null,
                       enabled boolean not null,
                       account_non_locked boolean not null,
                       created_at datetime(6),
                       constraint uk_users_email unique (email),
                       constraint uk_users_phone unique (phone)
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
                      image_url varchar(1000),
                      description varchar(500),
                      version bigint,
                      created_at datetime(6),
                      updated_at datetime(6),
                      constraint uk_cars_license_plate unique (license_plate)
);

create table bookings (
                          id bigint primary key auto_increment,
                          user_id bigint not null,
                          car_id bigint not null,
                          start_at datetime(6) not null,
                          end_at datetime(6) not null,
                          total_amount decimal(12, 2) not null,
                          status varchar(30) not null,
                          cancellation_reason varchar(500),
                          cancelled_at datetime(6),
                          created_at datetime(6),
                          updated_at datetime(6),
                          constraint fk_bookings_user foreign key (user_id) references users(id),
                          constraint fk_bookings_car foreign key (car_id) references cars(id)
);

create table refresh_tokens (
                                id bigint primary key auto_increment,
                                user_id bigint not null,
                                token varchar(512) not null,
                                expiry_date datetime(6) not null,
                                revoked boolean not null,
                                constraint fk_refresh_tokens_user foreign key (user_id) references users(id),
                                constraint uk_refresh_tokens_token unique (token)
);

create index idx_cars_search on cars(status, city, brand, daily_rate);
create index idx_bookings_user_start on bookings(user_id, start_at desc);
create index idx_bookings_car_status_interval on bookings(car_id, status, start_at, end_at);