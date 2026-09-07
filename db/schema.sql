-- ==========================================================
-- SMART BICYCLE RENTAL SYSTEM (EcoRide)
-- Database Creation & Schema Definition
-- Compatible with MySQL 8.0+
-- Prepared for NSP Minor Project Evaluation
-- Faculty Guide: Mr. Rohit Kumar (Dept. of AI & DS)
-- ==========================================================

DROP DATABASE IF EXISTS bicycle_rental_db;
CREATE DATABASE bicycle_rental_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bicycle_rental_db;

-- ----------------------------------------------------------
-- 1. USERS TABLE
-- ----------------------------------------------------------
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    role ENUM('CUSTOMER', 'OPERATOR', 'ADMIN') DEFAULT 'CUSTOMER',
    wallet_balance DECIMAL(10, 2) DEFAULT 200.00 CHECK (wallet_balance >= 0.0),
    eco_credits INT DEFAULT 0 CHECK (eco_credits >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ----------------------------------------------------------
-- 2. STATIONS TABLE
-- ----------------------------------------------------------
CREATE TABLE stations (
    station_id INT AUTO_INCREMENT PRIMARY KEY,
    station_name VARCHAR(100) NOT NULL UNIQUE,
    location_address VARCHAR(255) NOT NULL,
    total_docks INT NOT NULL CHECK (total_docks > 0),
    available_bikes INT NOT NULL DEFAULT 0,
    status ENUM('ACTIVE', 'UNDER_MAINTENANCE', 'CLOSED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_dock_capacity CHECK (available_bikes <= total_docks)
) ENGINE=InnoDB;

-- ----------------------------------------------------------
-- 3. BICYCLES TABLE
-- ----------------------------------------------------------
CREATE TABLE bicycles (
    bike_id INT AUTO_INCREMENT PRIMARY KEY,
    bike_code VARCHAR(20) NOT NULL UNIQUE,
    model_name VARCHAR(100) NOT NULL,
    bike_type ENUM('STANDARD', 'GEARED', 'ELECTRIC') DEFAULT 'STANDARD',
    current_station_id INT,
    status ENUM('AVAILABLE', 'RESERVED', 'IN_USE', 'MAINTENANCE', 'DECOMMISSIONED') DEFAULT 'AVAILABLE',
    hourly_rate DECIMAL(6, 2) NOT NULL DEFAULT 20.00,
    battery_level INT DEFAULT 100 CHECK (battery_level BETWEEN 0 AND 100),
    last_serviced_date DATE,
    FOREIGN KEY (current_station_id) REFERENCES stations(station_id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ----------------------------------------------------------
-- 4. RENTALS (TRIPS) TABLE
-- ----------------------------------------------------------
CREATE TABLE rentals (
    rental_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    bike_id INT NOT NULL,
    start_station_id INT NOT NULL,
    end_station_id INT NULL,
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_time TIMESTAMP NULL,
    duration_minutes INT DEFAULT 0,
    distance_km DECIMAL(5, 2) DEFAULT 0.00,
    base_fare DECIMAL(8, 2) NOT NULL DEFAULT 10.00,
    total_cost DECIMAL(8, 2) DEFAULT 0.00,
    eco_credits_earned INT DEFAULT 0,
    co2_saved_kg DECIMAL(5, 2) DEFAULT 0.00,
    rental_status ENUM('ACTIVE', 'COMPLETED', 'CANCELLED') DEFAULT 'ACTIVE',
    unlock_pin VARCHAR(6) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE RESTRICT,
    FOREIGN KEY (bike_id) REFERENCES bicycles(bike_id) ON DELETE RESTRICT,
    FOREIGN KEY (start_station_id) REFERENCES stations(station_id) ON DELETE RESTRICT,
    FOREIGN KEY (end_station_id) REFERENCES stations(station_id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ----------------------------------------------------------
-- 5. PAYMENTS TABLE
-- ----------------------------------------------------------
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    rental_id INT NOT NULL UNIQUE,
    user_id INT NOT NULL,
    amount DECIMAL(8, 2) NOT NULL CHECK (amount >= 0.0),
    eco_discount DECIMAL(8, 2) DEFAULT 0.00,
    net_paid DECIMAL(8, 2) NOT NULL,
    payment_method ENUM('WALLET', 'UPI', 'CREDIT_CARD', 'CASH') DEFAULT 'WALLET',
    payment_status ENUM('SUCCESS', 'FAILED', 'REFUNDED') DEFAULT 'SUCCESS',
    transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (rental_id) REFERENCES rentals(rental_id) ON DELETE RESTRICT,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ----------------------------------------------------------
-- 6. MAINTENANCE_LOGS TABLE
-- ----------------------------------------------------------
CREATE TABLE maintenance_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    bike_id INT NOT NULL,
    reported_by INT NOT NULL,
    issue_type ENUM('FLAT_TIRE', 'BRAKE_FAILURE', 'CHAIN_ISSUE', 'BATTERY_FAULT', 'OTHER') NOT NULL,
    description TEXT,
    reported_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP NULL,
    status ENUM('OPEN', 'IN_PROGRESS', 'RESOLVED') DEFAULT 'OPEN',
    repair_cost DECIMAL(8, 2) DEFAULT 0.00,
    FOREIGN KEY (bike_id) REFERENCES bicycles(bike_id) ON DELETE CASCADE,
    FOREIGN KEY (reported_by) REFERENCES users(user_id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ----------------------------------------------------------
-- 7. INITIAL SEED DATA (FOR TESTING & MID TERM-I EVALUATION)
-- ----------------------------------------------------------

-- Default Users (Password SHA-256 for 'admin123' and 'user123')
INSERT INTO users (username, password_hash, full_name, email, phone, role, wallet_balance, eco_credits) VALUES
('admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'System Administrator', 'admin@ecoride.com', '9876543210', 'ADMIN', 1000.00, 50),
('operator1', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'Rajesh Sharma', 'rajesh@ecoride.com', '9876543211', 'OPERATOR', 500.00, 20),
('student1', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'Aman Verma', 'aman@poornima.org', '9876543212', 'CUSTOMER', 350.00, 15),
('student2', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'Priya Singh', 'priya@poornima.org', '9876543213', 'CUSTOMER', 150.00, 8);

-- Stations
INSERT INTO stations (station_name, location_address, total_docks, available_bikes, status) VALUES
('Central Campus Dock', 'Academic Block A - Gate 1', 20, 6, 'ACTIVE'),
('Hostel Hub Station', 'Boys & Girls Hostel Plaza', 25, 8, 'ACTIVE'),
('Library Terminal', 'Central Library Front Courtyard', 15, 5, 'ACTIVE'),
('Sports Complex Station', 'Athletic Ground Pavilion', 15, 4, 'ACTIVE'),
('Metro Connector Hub', 'Sitapura Metro Station Exit 2', 30, 7, 'ACTIVE');

-- Bicycles
INSERT INTO bicycles (bike_code, model_name, bike_type, current_station_id, status, hourly_rate, battery_level, last_serviced_date) VALUES
('BK-101', 'Hero Sprint City', 'STANDARD', 1, 'AVAILABLE', 15.00, 100, '2026-08-15'),
('BK-102', 'Hero Sprint City', 'STANDARD', 1, 'AVAILABLE', 15.00, 100, '2026-08-15'),
('BK-201', 'Firefox Target 21S', 'GEARED', 1, 'AVAILABLE', 25.00, 100, '2026-08-20'),
('BK-202', 'Firefox Target 21S', 'GEARED', 1, 'AVAILABLE', 25.00, 100, '2026-08-20'),
('BK-301', 'Trek PowerFly E-Bike', 'ELECTRIC', 1, 'AVAILABLE', 40.00, 95, '2026-08-25'),
('BK-302', 'Trek PowerFly E-Bike', 'ELECTRIC', 1, 'AVAILABLE', 40.00, 88, '2026-08-25'),

('BK-103', 'Hero Sprint City', 'STANDARD', 2, 'AVAILABLE', 15.00, 100, '2026-08-12'),
('BK-104', 'Hero Sprint City', 'STANDARD', 2, 'AVAILABLE', 15.00, 100, '2026-08-12'),
('BK-203', 'Firefox Target 21S', 'GEARED', 2, 'AVAILABLE', 25.00, 100, '2026-08-18'),
('BK-204', 'Firefox Target 21S', 'GEARED', 2, 'AVAILABLE', 25.00, 100, '2026-08-18'),
('BK-303', 'Trek PowerFly E-Bike', 'ELECTRIC', 2, 'AVAILABLE', 40.00, 92, '2026-08-22'),
('BK-304', 'Trek PowerFly E-Bike', 'ELECTRIC', 2, 'MAINTENANCE', 40.00, 15, '2026-08-22'),

('BK-105', 'Hero Sprint City', 'STANDARD', 3, 'AVAILABLE', 15.00, 100, '2026-08-10'),
('BK-205', 'Firefox Target 21S', 'GEARED', 3, 'AVAILABLE', 25.00, 100, '2026-08-16'),
('BK-305', 'Trek PowerFly E-Bike', 'ELECTRIC', 3, 'AVAILABLE', 40.00, 85, '2026-08-21');
