CREATE TABLE IF NOT EXISTS driver (
    driver_id SERIAL PRIMARY KEY,
    driver_name VARCHAR(60) NOT NULL,
    driver_age INT CONSTRAINT chk_driver_age CHECK (driver_age >= 18 AND driver_age <= 100),
    driver_has_licence BOOLEAN NOT NULL DEFAULT FALSE);

CREATE TABLE IF NOT EXISTS car (
    car_id SERIAL PRIMARY KEY,
    car_brand VARCHAR(60) NOT NULL,
    car_model VARCHAR(60) NOT NULL,
    car_price INTEGER NOT NULL CHECK (car_price > 0),
    car_driver_id INT REFERENCES driver(driver_id));
