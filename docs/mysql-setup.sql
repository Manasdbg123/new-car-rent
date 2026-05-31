CREATE DATABASE IF NOT EXISTS car_rental;
CREATE USER IF NOT EXISTS 'car_rental'@'localhost' IDENTIFIED BY 'car_rental';
GRANT ALL PRIVILEGES ON car_rental.* TO 'car_rental'@'localhost';
FLUSH PRIVILEGES;
