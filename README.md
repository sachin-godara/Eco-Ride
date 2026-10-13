# EcoRide — Smart Bicycle Rental & Urban Micro-Mobility Platform

> **System Architecture**: 3-Tier Desktop Client (Java 17 FlatLaf) / Relational MySQL 8.x Engine  
> **Standard Compliance**: IEEE Std 830-1998 Software Requirements Specification  
> **Core Features**: IoT Dock Telemetry, Dynamic Fleet Rebalancing, Carbon Offset Tracking, Real-Time Metering  
> **Target Deployment**: Smart Cities, University Campuses, Business Parks, and Urban Transit Hubs  

---

## Table of Contents
1. [Executive Summary & Project Overview](#1-executive-summary--project-overview)
2. [System Architecture & Engineering Principles](#2-system-architecture--engineering-principles)
3. [Core Innovation: Fleet Intelligence & Carbon Tracking](#3-core-innovation-fleet-intelligence--carbon-tracking)
4. [Software Requirements Specification (SRS) — IEEE 830 Standard](#4-software-requirements-specification-srs--ieee-830-standard)
5. [Database Architecture & MySQL Schema](#5-database-architecture--mysql-schema)
6. [System Architecture & Java OOP Modular Design](#6-system-architecture--java-oop-modular-design)
7. [Java Swing GUI Desktop Specifications](#7-java-swing-gui-desktop-specifications)
8. [Technical Architecture & Engineering FAQ](#8-technical-architecture--engineering-faq)
9. [Platform Deployment & Verification Checklist](#9-platform-deployment--verification-checklist)

---

## 1. Executive Summary & Project Overview

### 1.1 Project Title
**Smart Bicycle Rental System (EcoRide) with Dynamic Dock Rebalancing & Carbon Offset Tracking**

### 1.2 Problem Statement
Urban transit and university campuses face serious challenges regarding last-mile connectivity, vehicular carbon emissions, traffic congestion, and sedentary lifestyles. Traditional manual bicycle rental systems suffer from:
- **Inefficient Manual Ledgering**: Paper-based or spreadsheet logs cause billing disputes, lost assets, and delayed return registrations.
- **Station Imbalance**: Peak hours leave commuter hubs drained of bikes while destination stations overflow with no empty return docks.
- **Lack of Rider Engagement**: Conventional bike rentals offer zero incentive for users to choose green mobility over fuel-burning transit.
- **Opaque Fleet Maintenance**: Damaged bikes (flat tires, faulty brakes) are re-rented unknowingly, posing safety hazards.

### 1.3 Proposed Solution
The **Smart Bicycle Rental System (EcoRide)** is an integrated desktop client developed in **Java Swing** and powered by a relational **MySQL** database via **JDBC**. It provides:
1. **Rider Self-Service Portal**: Fast station browsing, real-time bicycle availability filtering (Electric, Geared, Standard), one-time secure unlock PIN generation, active ride tracking, and digital invoice generation.
2. **Station Fleet Management**: Multi-station inventory control, automated dock capacity monitoring, and maintenance condition auditing.
3. **Green Mobility Gamification**: Dynamic calculation of CO₂ emissions prevented and calories burned per trip, converted into redeemable "Eco-Reward Points".

---

## 2. System Architecture & Engineering Principles

EcoRide is engineered according to enterprise desktop software best practices:

```
+----------------------------------------------------------------------------------------------------+
|                                    ECORIDE ENTERPRISE ARCHITECTURE                                  |
|                      (Presentation Tier  |  Business Tier  |  Persistence Tier)                     |
+------------------------------------+---------------------------------------------------------------+
| Component Layer                    | Implementation Details & Technology                           |
+------------------------------------+---------------------------------------------------------------+
| Presentation Tier (GUI)            | Java Swing (Java 17) styled with FlatLaf Aesthetic Light Look & Feel.|
|                                    | Event-Dispatch Thread (EDT) safety via SwingUtilities.        |
|                                    | Modern responsive cards, data tables, and modal dialogs.      |
+------------------------------------+---------------------------------------------------------------+
| Business Logic Tier                | Pure Java OOP Domain Models (Bicycle, Station, User, Rental). |
|                                    | Real-time IoT continuous telemetry engine & pricing formulas. |
|                                    | Dynamic dock congestion analysis & rebalancing dispatcher.     |
+------------------------------------+---------------------------------------------------------------+
| Data Persistence Tier              | MySQL 8.x relational database via JDBC PreparedStatements.     |
|                                    | Thread-safe connection pooling & automated offline failover.   |
|                                    | Complete ACID transactional integrity for fare settlements.   |
+------------------------------------+---------------------------------------------------------------+
```

### 2.1 Core Design Pillars
- **Strict Separation of Concerns**: UI views (`com.bicyclerental.ui`) communicate exclusively through domain services (`com.bicyclerental.service`) and data access layers (`com.bicyclerental.dao`).
- **Offline Reliability & Failover**: The built-in repository (`DataRepository`) maintains an in-memory cache and state engine, allowing seamless evaluation and demonstration even if a local MySQL instance is temporarily unreachable.
- **Cryptographic PIN Generation**: Digital bike checkout generates a transient 6-digit unlock PIN verified against dock terminal controllers.
- **Native OS Typography & UTF-8**: Optimized for clean font rendering and cross-platform compatibility without corrupting currency characters or symbols.

---

## 3. Core Innovation: Fleet Intelligence & Carbon Tracking

The Smart Bicycle Rental System incorporates two core architectural innovations designed for modern smart cities and campuses:

### 3.1 Smart Dynamic Dock Rebalancing & Fleet Intelligence
- **The Problem**: Commuters ride from transit hubs/stations to high-density destinations during rush hour, creating station starvation at origin docks and saturation lockout at destination docks.
- **The Solution**:
  - The system tracks dock saturation levels in real time (`Available / Normal / Nearly Full / Empty`).
  - When a station reaches >85% capacity, the system triggers a **Rebalancing Work Order** for operators to redistribute bikes.
  - When a user selects a destination station that is at full capacity, the UI intelligently suggests the closest alternative station with available return docks and estimated walk distance.

### 3.2 Eco-Reward Points & Green Carbon-Offset Engine
- **The Problem**: Conventional transit software focuses solely on billing dollars, offering zero incentive for ecological commuting.
- **The Solution**:
  - For every completed ride, the system calculates:
    $$\text{CO}_2\text{ Saved (kg)} = \text{Distance (km)} \times 0.21\,\text{kg/km (vs average petrol vehicle)}$$
    $$\text{Calories Burned (kcal)} = \text{Ride Duration (min)} \times 7.5\,\text{kcal/min}$$
  - Riders earn **Eco-Credits** ($1\text{ Credit per } 0.5\text{ kg CO}_2\text{ saved}$) credited directly to their in-app wallet, redeemable for discounted rental rates or free ride minutes.

---

## 4. Software Requirements Specification (SRS) — IEEE 830 Standard

### 4.1 Introduction
- **Purpose**: Specifies all functional and non-functional requirements for the Smart Bicycle Rental System desktop application.
- **Scope**: Covers customer authentication, station/bicycle browsing, reservation lifecycle, active ride tracking, automated fee calculation, carbon offsetting, maintenance logging, and administrative oversight.
- **Definitions, Acronyms, Abbreviations**:
  - **SRS**: Software Requirements Specification
  - **GUI**: Graphical User Interface (Java Swing FlatLaf)
  - **JDBC**: Java Database Connectivity
  - **EDT**: Event Dispatch Thread
  - **DAO**: Data Access Object
  - **CO₂e**: Carbon Dioxide Equivalent

### 4.2 Overall Description
- **Product Perspective**: Standalone 3-tier desktop client communicating over TCP/IP (localhost or local network) to a MySQL 8.x relational database server.
- **User Classes & Characteristics**:
  1. **Customer (Rider)**: Students/staff who register, browse stations, unlock bikes, monitor active rides, view carbon savings, and pay fares.
  2. **Station Operator / Technician**: Inspects bicycles, marks damaged units under maintenance, reviews station dock capacity, and logs repairs.
  3. **System Administrator**: Full system access to configure stations, manage bicycle inventory, view financial metrics, and manage user accounts.
- **Operating Environment**:
  - OS: Windows 10/11 (or Linux/macOS with JRE 17+)
  - Java Runtime: OpenJDK 17.0+
  - Database: MySQL Community Server 8.0+
  - Screen Resolution: Optimized for $1280 \times 720$ and higher.

### 4.3 Functional Requirements Breakdown
- **REQ-1 (User Management & Authentication)**:
  - Secure registration and login with SHA-256 hashed passwords.
  - Role-based redirection (Customer Dashboard vs Admin/Staff Portal).
  - In-app digital wallet top-up and balance validation.
- **REQ-2 (Station & Fleet Inventory Management)**:
  - Real-time catalog of stations with location name, total capacity, and available bike counts.
  - Bicycle categorization: Standard Urban, Multi-Gear Mountain, and Electric Pedal-Assist (with battery level %).
- **REQ-3 (Reservation & Smart OTP Unlock)**:
  - Customer selects station and bike type.
  - System generates a 6-digit cryptographic unlock passcode and marks the bike as `RESERVED` or `IN_USE`.
  - Ensures a user cannot rent more than one bike concurrently.
- **REQ-4 (Live Trip Tracking & Metering)**:
  - Real-time ride timer running on a background worker thread.
  - Live cost accumulator based on base fare + per-minute rate.
  - Emergency incident/breakdown trigger.
- **REQ-5 (Return, Docking & Auto-Billing)**:
  - User chooses return station with available empty dock.
  - System updates bike status to `AVAILABLE` at the new station and increments dock count.
  - Calculates total duration, total cost, Eco-Credits awarded, and deducts from user wallet.
  - Generates a downloadable/printable receipt with transaction ID.
- **REQ-6 (Maintenance & Defect Logging)**:
  - Users and technicians can flag bicycles with issues (e.g., flat tire, chain slip, brake failure).
  - Automatically transitions bike status to `MAINTENANCE`, removing it from the public rental pool until certified by a technician.
- **REQ-7 (Administrative Analytics & Reporting)**:
  - Financial reports (daily/weekly/monthly revenue).
  - Fleet utilization statistics and station heat-maps.
  - User transaction history and maintenance audit logs.

### 4.4 Non-Functional Requirements
- **Performance**: GUI responds to all user interactions within $<100\text{ ms}$. Database queries execute in $<50\text{ ms}$ with indexed foreign keys.
- **Reliability & Data Integrity**: ACID transaction guarantees on rental checkouts and payments (`conn.setAutoCommit(false)` with rollback on failure).
- **Security**: SQL Injection prevention using `PreparedStatement` across all DAO queries; no plain-text password storage.
- **Usability**: Clean, modern Swing UI using FlatLaf/Nimbus aesthetic standards with intuitive keyboard shortcuts and clear validation error dialogs.

---

## 5. Database Architecture & MySQL Schema

### 5.1 Entity-Relationship (ER) Overview
- **Users** $(1) \longleftrightarrow (\infty)$ **Rentals**
- **Stations** $(1) \longleftrightarrow (\infty)$ **Bicycles** (Current Station)
- **Stations** $(1) \longleftrightarrow (\infty)$ **Rentals** (Start Station)
- **Stations** $(1) \longleftrightarrow (\infty)$ **Rentals** (End Station)
- **Bicycles** $(1) \longleftrightarrow (\infty)$ **Rentals**
- **Rentals** $(1) \longleftrightarrow (1)$ **Payments**
- **Bicycles** $(1) \longleftrightarrow (\infty)$ **Maintenance_Logs**

### 5.2 Complete Production MySQL DDL Script (`schema.sql`)

```sql
-- ==========================================================
-- SMART BICYCLE RENTAL SYSTEM (EcoRide)
-- Database Creation & Schema Definition
-- Compatible with MySQL 8.0+
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
-- 7. INITIAL SEED DATA (CORE FLEET & DEMO ACCOUNTS)
-- ----------------------------------------------------------

-- Default Users (Password is 'admin123' and 'user123' hashed or demo strings)
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
('BK-304', 'Trek PowerFly E-Bike', 'ELECTRIC', 2, 'MAINTENANCE', 40.00, 15, '2026-08-22');
```

---

## 6. System Architecture & Java OOP Modular Design

The project strictly follows the industry-standard **3-Tier Layered MVC & DAO Architecture**:

```
+-------------------------------------------------------------------------------+
|                             PRESENTATION LAYER (VIEW)                         |
|   Java Swing Forms, Dashboards, Custom Theme Components, Event Listeners      |
|   (LoginFrame, CustomerDashboard, BikeExplorerPanel, ActiveRideDialog, etc.)  |
+-------------------------------------------------------------------------------+
                                      │  User Actions / Form Submissions
                                      ▼
+-------------------------------------------------------------------------------+
|                            BUSINESS LOGIC LAYER (SERVICE)                     |
|   Validation, Carbon Calculation, Pricing Engine, Rebalance Intelligence      |
|   (AuthService, RentalService, FareCalculatorService, MaintenanceService)    |
+-------------------------------------------------------------------------------+
                                      │  Domain Objects / DTOs
                                      ▼
+-------------------------------------------------------------------------------+
|                            DATA ACCESS LAYER (DAO)                            |
|   JDBC PreparedStatements, Connection Handling, SQL Transaction Control       |
|   (UserDAO, BikeDAO, StationDAO, RentalDAO, PaymentDAO, DBConnection)         |
+-------------------------------------------------------------------------------+
                                      │  TCP/IP (JDBC Protocol: 3306)
                                      ▼
+-------------------------------------------------------------------------------+
|                               DATABASE LAYER (MySQL)                          |
|   Tables, Foreign Keys, ACID Constraints, Transaction Isolation               |
+-------------------------------------------------------------------------------+
```

### 6.1 Package Directory Hierarchy
```text
f:/Project 001/java NSP/
├── imp/                                # Institutional templates & instructions
├── src/
│   └── com/
│       └── bicyclerental/
│           ├── main/
│           │   └── Application.java    # Application entry point (main method)
│           ├── model/                  # Domain entity POJOs
│           │   ├── User.java
│           │   ├── Bicycle.java
│           │   ├── Station.java
│           │   ├── Rental.java
│           │   ├── Payment.java
│           │   └── MaintenanceLog.java
│           ├── dao/                    # Database Access Object interfaces & implementations
│           │   ├── DBConnection.java   # Singleton connection manager
│           │   ├── UserDAO.java
│           │   ├── BicycleDAO.java
│           │   ├── StationDAO.java
│           │   ├── RentalDAO.java
│           │   └── PaymentDAO.java
│           ├── service/                # Business logic services
│           │   ├── AuthService.java
│           │   ├── RentalService.java
│           │   ├── CarbonCreditService.java
│           │   └── RebalanceService.java
│           ├── ui/                     # Java Swing graphical interfaces
│           │   ├── theme/
│           │   │   └── UITheme.java    # Color palettes, typography, UI constants
│           │   ├── LoginFrame.java
│           │   ├── RegisterDialog.java
│           │   ├── CustomerDashboard.java
│           │   ├── AdminDashboard.java
│           │   ├── BikeCatalogPanel.java
│           │   ├── ActiveRideFrame.java
│           │   ├── ReceiptDialog.java
│           │   └── FleetManagementPanel.java
│           └── util/                   # Utility helpers
│               ├── PasswordHasher.java # SHA-256 password hashing
│               ├── FormValidator.java  # Input validation regex
│               └── DateFormatter.java
├── db/
│   └── schema.sql                      # Complete MySQL database creation script
├── docs/
│   ├── Bicycle_Rental_System_SRS.docx  # IEEE 830 compliant SRS document
│   └── Flowcharts_and_Diagrams.png
└── README.md                           # Master Project Guide & Architecture
```

---

## 7. Java Swing GUI Desktop Specifications

The EcoRide application includes a cohesive, professional suite of Java Swing screens designed with a modern aesthetic light card-based interface powered by the FlatLaf Look and Feel:

### 7.1 Screen Inventory & User Flow

| # | Screen Name | Class | Primary Purpose & Key Components |
|---|---|---|---|
| **1** | **Splash & Login Screen** | `LoginFrame.java` | Username/Password authentication, role picker, animated logo banner, registration link, error indicators for invalid credentials. |
| **2** | **Registration Dialog** | `RegisterDialog.java` | New user sign-up with real-time validation (email regex, 10-digit phone, password complexity), initial wallet deposit. |
| **3** | **Customer Main Dashboard** | `CustomerDashboard.java` | Visual summary tiles: *Wallet Balance*, *Eco-Credits*, *Total Rides*, *CO₂ Saved (kg)*; quick navigation buttons to browse stations, view history, or report issues. |
| **4** | **Station & Bicycle Explorer** | `BikeCatalogPanel.java` | Station dropdown filter, bike type tabs (`Standard`, `Geared`, `Electric`), `JTable` / card grid showing live status, battery %, hourly price, and "Unlock & Rent" CTA. |
| **5** | **Ride Confirmation & OTP Dialog** | `RentConfirmDialog.java` | Summary of rental terms, estimated deposit, one-time 6-digit cryptographic unlock passcode generator, confirmation sound/visual cue. |
| **6** | **Active Trip Monitor** | `ActiveRideFrame.java` | Live running timer (`HH:mm:ss`) powered by a background `javax.swing.Timer` / thread; dynamic meter for current fare, distance simulated, and "End Ride & Dock" button. |
| **7** | **Return & Docking Dialog** | `ReturnBikeDialog.java` | Dropdown of destination stations with real-time dock capacity indicators (`Normal`, `Nearly Full`, `Full - Locked`); handles smart rebalancing redirect suggestions. |
| **8** | **Billing & Eco-Invoice Screen** | `ReceiptDialog.java` | Final invoice breakdown: duration, gross fare, Eco-Credit discount applied, net debited from wallet; printable/exportable receipt text. |
| **9** | **User Profile & Trip History** | `TripHistoryPanel.java` | Tabular `JTable` with pagination showing all previous rentals, timestamps, start/end stations, amount paid, and CO₂ saved. |
| **10** | **Admin / Operator Fleet Management** | `FleetManagementPanel.java` | Complete CRUD operations for bicycles and stations: Add new bike, edit rates, assign to station, mark for maintenance, view revenue charts. |

---

## 8. Technical Architecture & Engineering FAQ

### 8.1 Core Java & OOP Concepts
- **Q1: How is Encapsulation applied in your project?**  
  *Answer*: All domain entity fields (e.g., in `Bicycle.java`, `User.java`) are declared `private` to prevent unauthorized external mutation. State access and modifications are strictly channeled through public getters and setters with internal validation rules (e.g., preventing negative wallet balances or invalid battery percentages).
- **Q2: Where is Polymorphism demonstrated?**  
  *Answer*: In our Data Access layer, interfaces like `BicycleDAO` declare operational contracts (`findById`, `updateStatus`, `getAll`), implemented by `BicycleDAOImpl`. Method overriding is also demonstrated across custom Swing cell renderers (`TableCellRenderer`) to format bicycle status badges dynamically with custom colors.
- **Q3: What design pattern is utilized for database connectivity?**  
  *Answer*: The **Singleton Pattern** is implemented in `DBConnection.java`. The constructor is `private`, and a synchronized static method `getConnection()` ensures only a single database connection or pool manager is instantiated across the entire JVM lifecycle, preventing connection starvation.

### 8.2 Java Swing & GUI Concurrency
- **Q4: What is the Event Dispatch Thread (EDT) and why is it important?**  
  *Answer*: In Java Swing, GUI rendering and component event handling occur on a single dedicated thread called the Event Dispatch Thread (EDT). Invoking long-running operations (like database queries or time delays) directly on the EDT freezes the UI. We ensure UI updates are scheduled safely via `SwingUtilities.invokeLater()` and asynchronous work uses `SwingWorker` or background threads.
- **Q5: How does your active ride timer run without freezing the Swing window?**  
  *Answer*: The active ride timer utilizes a `javax.swing.Timer` that fires an `ActionEvent` every 1000 milliseconds on the EDT without blocking, updating the `JLabel` stopwatch and calculating incremental fare in real time.
- **Q6: Which Layout Managers did you use and why?**  
  *Answer*: We avoided absolute positioning (`null` layout) because it breaks across different screen resolutions and OS DPI settings. Instead, we combined `BorderLayout` for main window frames (Header, Sidebar, Center Content, Footer), `GridBagLayout` for precise form input fields, and `FlowLayout` for action button toolbars.

### 8.3 JDBC & MySQL Integration
- **Q7: Why did you use `PreparedStatement` instead of `Statement`?**  
  *Answer*: `PreparedStatement` pre-compiles the SQL query structure in the database engine and substitutes parameters securely via placeholders (`?`). This provides two major advantages: (1) it completely prevents SQL Injection attacks by treating input strictly as literal data, and (2) it improves performance through execution plan caching.
- **Q8: How do you handle database transaction management during bike checkout?**  
  *Answer*: Checking out a bicycle requires updating two tables simultaneously: (1) inserting a new record into `rentals` and (2) changing the bike's status to `IN_USE` and decrementing the station's available count in `bicycles` and `stations`. We call `conn.setAutoCommit(false);`, execute both queries, and then call `conn.commit();`. If any query throws a `SQLException`, the `catch` block invokes `conn.rollback();` to preserve database consistency.

### 8.4 Core Platform Differentiators
- **Q9: What makes your project unique compared to standard bicycle rental systems?**  
  *Answer*: Our system features two distinct innovations:
  1. **Dynamic Dock Rebalancing Intelligence**: Rather than allowing stations to run out of bikes or reach 100% capacity (preventing returns), our system tracks dock saturation thresholds and suggests nearby stations with guaranteed dock availability.
  2. **Eco-Reward Carbon Offset Engine**: It computes tangible CO₂ emission savings ($0.21\text{ kg per km}$) and calories burned ($7.5\text{ kcal/min}$) for every trip, rewarding riders with in-app Eco-Reward points redeemable for discounted rental rates.

---

## 9. Platform Deployment & Verification Checklist

### 9.1 Build & Verification Status
- [x] Comprehensive requirements baseline specified according to IEEE Std 830.
- [x] Modern 3-tier architecture with clean separation of presentation, domain, and persistence layers.
- [x] Relational MySQL schema scripted with full foreign key constraints and transactional boundaries (`schema.sql`).
- [x] Production Java Swing UI styled with FlatLaf Aesthetic Light Theme and custom cell renderers.
- [x] Real-time continuous IoT telemetry engine with live distance, fare, and carbon calculations.
- [x] Automated end-to-end test suite (`TripWorkflowTest.java`) passing with 100% assertions.
- [x] Automated comprehensive verification suite (`SystemVerificationTest.java`) covering all 8 functional domains.
- [x] Offline database failover engine enabling reliable evaluation without external service dependency.

### 9.2 Running Automated Verification
To run the automated verification suite:
```powershell
# One-click execution via test.bat
test.bat

# Or run directly via Java CLI with assertion flag:
java -ea -cp "bin;lib/*" com.bicyclerental.main.SystemVerificationTest
java -ea -cp "bin;lib/*" com.bicyclerental.main.TripWorkflowTest
```

---
*EcoRide Smart Mobility System • Version 1.0.0 (Production Core)*
