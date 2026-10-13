# Data Flow Diagram (DFD) Architectural Analysis
## Smart Bicycle Rental System (EcoRide)
### With Dynamic Dock Rebalancing & Carbon Offset Tracking

---

**Document Control:**
- **System**: Smart Bicycle Rental System (EcoRide)
- **Document**: Data Flow Analysis & Formal DFD Specification
- **Version**: 1.0.0 (Production Core)
- **Author**: System Architecture Team
- **Date**: September 2026
- **Target Audience**: Software Engineers, System Evaluators, Academic Viva Examiners

---

## Table of Contents
1. [Introduction to Data Flow Diagrams (DFD)](#1-introduction-to-data-flow-diagrams-dfd)
   - 1.1 What is a DFD?
   - 1.2 DFD Notation Standards (Gane-Sarson vs. Yourdon-DeMarco)
   - 1.3 Fundamental DFD Rules & Conservation of Data
2. [Level 0 DFD (Context Diagram) - "The Simple DFD"](#2-level-0-dfd-context-diagram---the-simple-dfd)
   - 2.1 Context Diagram Overview
   - 2.2 External Entities (Terminators)
   - 2.3 System Boundary & Central Process 0.0
   - 2.4 Level 0 Mermaid Visual Model
   - 2.5 Level 0 ASCII Architectural Model
   - 2.6 Input & Output Data Flow Specifications
3. [Level 1 DFD (Decomposed Functional Architecture)](#3-level-1-dfd-decomposed-functional-architecture)
   - 3.1 Functional Decomposition of Process 0.0
   - 3.2 Identification of Data Stores (Relational Mapping)
   - 3.3 Level 1 Mermaid Visual Model
   - 3.4 Process-by-Process Deep Dive (Processes 1.0 to 9.0)
4. [Tracing Core Business Workflows](#4-tracing-core-business-workflows)
   - 4.1 Workflow 1: Customer Onboarding & Wallet Recharge
   - 4.2 Workflow 2: Station Browsing & Bike Reservation (OTP Unlock)
   - 4.3 Workflow 3: Live Trip Metering (EDT Concurrency)
   - 4.4 Workflow 4: Dock Return & Dynamic Rebalancing (USP #1)
   - 4.5 Workflow 5: Fare Settlement & Eco-Credits Engine (USP #2)
   - 4.6 Workflow 6: Maintenance & Technician Dispatch
   - 4.7 Workflow 7: Administrative Oversight & Fleet Analytics
5. [Complete Data Dictionary (Data Stores & Flows)](#5-complete-data-dictionary-data-stores--flows)
   - 5.1 Data Stores Specification
   - 5.2 Composite Data Flows & Data Elements
6. [Academic & Viva Voce Preparation Guide](#6-academic--viva-voce-preparation-guide)
   - 6.1 Core DFD Concepts Viva Q&A
   - 6.2 Project-Specific Implementation Viva Q&A
   - 6.3 USP-Specific Viva Q&A

---

## 1. Introduction to Data Flow Diagrams (DFD)

### 1.1 What is a DFD?
A **Data Flow Diagram (DFD)** is a graphical representation of the flow of data through an information system. It models:
1. Where data originates (**External Entities** or **Sources**).
2. How data is transformed through logical computation and business rules (**Processes**).
3. Where data is stored for persistence (**Data Stores**).
4. Where final results are delivered (**Sinks**).

Unlike a **Flowchart**, which illustrates procedural logic, control flows, loops, and conditional branching (`if-then-else`), a **DFD focuses strictly on the movement, inputs, outputs, and storage of data** without assuming sequential control flow.

### 1.2 DFD Notation Standards
Two primary visual notation conventions exist in software engineering:

| Component | Yourdon & DeMarco Notation | Gane & Sarson Notation | EcoRide Implementation |
| :--- | :--- | :--- | :--- |
| **Process** | Circle / Bubble | Rounded Rectangle | Rounded Node `(Process Name)` |
| **External Entity** | Square / Rectangle | 3D Square / Box | Sharp Rectangle `[Entity Name]` |
| **Data Store** | Two parallel horizontal lines | Open-ended Rectangle with ID box | Cylindrical Storage `[(Data Store)]` |
| **Data Flow** | Directed Arrow with flow label | Directed Arrow with flow label | Directed Arrow `-->|Data Name|` |

### 1.3 Fundamental DFD Rules & Conservation of Data
To maintain structural integrity and prevent analysis errors, the EcoRide DFD strictly satisfies the following software engineering rules:

1. **Rule of Data Conservation (No Black Holes)**: Every process must produce at least one output data flow. A process that consumes inputs but produces nothing is an invalid "Black Hole".
2. **Rule of Data Creation (No Miracles)**: Every process must receive at least one input data flow. A process that generates outputs with no inputs is an invalid "Miracle".
3. **Rule of Data Transformation (No Grey Holes)**: The outputs of a process must be logically derivable from its inputs. If an output requires fields not present in any input or data store, it is a "Grey Hole".
4. **Entity Separation Rule**: External Entities cannot communicate directly with other External Entities within the system DFD. Any exchange must pass through a system process.
5. **Direct Storage Access Rule**: External Entities cannot read from or write directly to a Data Store. All database interactions must be mediated by a validated process.
6. **Data Store Isolation Rule**: A Data Store cannot directly transfer data to another Data Store. A process must execute the read, transform, and write steps.
7. **Balancing Rule (Level 0 to Level 1 Consistency)**: All data inflows entering and outflows leaving the single Process 0.0 at Level 0 must be accounted for across the decomposed sub-processes at Level 1.

---

## 2. Level 0 DFD (Context Diagram) - "The Simple DFD"

### 2.1 Context Diagram Overview
The **Level 0 DFD**, commonly referred to as the **Context Diagram**, is the most concise, simple, and high-level DFD representation of the system. 

It views the entire **Smart Bicycle Rental System (EcoRide)** as a single, central operational bubble (`Process 0.0`). It highlights the system boundary and captures all communication between the software application and the external human actors or external services.

> **Key Rule of Level 0 DFD:**  
> In a Context Diagram, **no internal data stores (database tables) are shown**. All databases, cache structures, and tables are encapsulated *inside* Process 0.0.

### 2.2 External Entities (Terminators)
Process 0.0 interacts with four key external entities:

1. **Customer (Rider)**:
   - Primary consumer who signs up, logs in, checks wallet balance, browses docking stations, selects bicycles, requests unlock PINs, monitors live trip metrics, docks the bike at destination, and pays the metered fare while earning Eco-Credits.
2. **Station Operator (Field Technician)**:
   - Field operations staff responsible for monitoring station saturation levels, acting on dynamic dock rebalancing alerts (transporting bikes from congested to starved stations), inspecting reported faults, and logging maintenance repairs.
3. **System Administrator**:
   - Platform manager responsible for administrative control: adding/updating docking stations, registering new bicycles in the fleet, configuring base fares and hourly tariffs, and auditing financial revenue and campus carbon offset metrics.
4. **Payment Gateway / External Simulator**:
   - Financial interface entity that handles simulated or live payment authorizations, card/UPI wallet top-ups, and balance settlement transactions.

### 2.3 System Boundary & Central Process 0.0
- **Process ID**: `0.0`
- **Process Name**: `Smart Bicycle Rental System (EcoRide)`
- **Core Responsibility**: Manages the complete end-to-end lifecycle of bicycle discovery, authentication, OTP unlock generation, concurrent ride timing, dynamic dock rebalancing, carbon emission offset calculation, wallet debiting, and fleet maintenance.

### 2.4 Level 0 Mermaid Visual Model

```mermaid
flowchart TD
    %% Entity Styling
    classDef entity fill:#1e293b,stroke:#0ea5e9,stroke-width:2px,color:#f8fafc;
    classDef process fill:#065f46,stroke:#10b981,stroke-width:3px,color:#ecfdf5,font-weight:bold;
    classDef gateway fill:#3b0764,stroke:#a855f7,stroke-width:2px,color:#faf5ff;

    %% External Entities
    E1["👤 Customer (Rider)"]:::entity
    E2["🛠️ Station Operator / Technician"]:::entity
    E3["⚙️ System Administrator"]:::entity
    E4["💳 Payment Simulator / Gateway"]:::gateway

    %% Central Process (Level 0 Bubble)
    P0(("0.0<br/>Smart Bicycle Rental System<br/>(EcoRide Core)")):::process

    %% Customer Data Flows
    E1 -->|1. Reg Details & Credentials| P0
    E1 -->|2. Wallet Top-up Request| P0
    E1 -->|3. Station / Bike Filter Query| P0
    E1 -->|4. Bike Reservation Request| P0
    E1 -->|5. Return Dock & Discount Choice| P0
    E1 -->|6. Fault / Damage Report| P0

    P0 -->|A. Auth Status & User Profile| E1
    P0 -->|B. Station & Bike Availability + Battery %| E1
    P0 -->|C. 6-Digit Cryptographic Unlock PIN| E1
    P0 -->|D. Live Ride HUD (Duration, Fare, Distance)| E1
    P0 -->|E. Itemized Invoice Receipt & Wallet Balance| E1
    P0 -->|F. Eco-Credits Earned & CO2 Saved Summary| E1
    P0 -->|G. Congestion Alert & Alt-Station Suggestion| E1

    %% Operator Data Flows
    P0 -->|H. Dock Saturation Alerts (Starved / Congested)| E2
    P0 -->|I. Open Maintenance Tickets Queue| E2
    E2 -->|7. Rebalance Transfer Log| P0
    E2 -->|8. Maintenance Resolution & Repair Cost| P0

    %% Administrator Data Flows
    E3 -->|9. Admin Credentials & Auth| P0
    E3 -->|10. Fleet & Station CRUD Commands| P0
    E3 -->|11. Tariff & Base Fare Config| P0
    P0 -->|J. Fleet Distribution & Health Metrics| E3
    P0 -->|K. Revenue & Campus Green Analytics| E3

    %% Payment Gateway Flows
    P0 -->|L. Payment Deduction Request| E4
    E4 -->|12. Transaction Auth Status & Ref ID| P0
```

### 2.5 Level 0 ASCII Architectural Model

```
+-----------------------------------------------------------------------------------------------+
|                                    LEVEL 0 DFD (CONTEXT DIAGRAM)                              |
+-----------------------------------------------------------------------------------------------+

                             +-----------------------------------+
                             |     E3: System Administrator      |
                             +-----------------------------------+
                               |  ^                         ^  |
      [10. Fleet/Station CRUD] |  | [J. Fleet Health]       |  | [11. Tariff Config]
      [9. Admin Credentials]   |  | [K. Revenue Analytics]  |  |
                               v  |                         |  v
+-----------------------------------------------------------------------------------------------+
|                                                                                               |
|                       +-----------------------------------------------+                       |
|                       |                                               |                       |
|  +----------------+   |                     0.0                       |   +----------------+  |
|  |                |==>|                                               |==>|                |  |
|  |  E1: Customer  |   |          Smart Bicycle Rental System          |   |  E2: Operator  |  |
|  |    (Rider)     |<==|                    (EcoRide)                  |<==|  (Technician)  |  |
|  |                |   |                                               |   |                |  |
|  +----------------+   |                                               |   +----------------+  |
|     |  ^              +-----------------------------------------------+             |  ^      |
|     |  |                                  ^       |                                 |  |      |
|     |  | [A. Auth & Profile]              |       | [L. Payment Debit Req]          |  |      |
|     |  | [B. Catalog & Battery %]         |       |                                 |  |      |
|     |  | [C. 6-Digit Unlock PIN]          |       v                                 |  |      |
|     |  | [D. Live HUD Metrics]        +-----------------------+                     |  |      |
|     |  | [E. Itemized Invoice]        | E4: Payment Gateway   |                     |  |      |
|     |  | [F. Eco-Credit/CO2 Summary]  |     (Simulator)       |                     |  |      |
|     |  | [G. Alt-Station Guidance]    +-----------------------+                     |  |      |
|     |  |                                  |                                         |  |      |
|     |  |                                  | [12. Txn Status & Auth]                 |  |      |
|     |  +----------------------------------+-----------------------------------------+  |      |
|     |                                                                                  |      |
|     +==[1. Reg/Login Credentials]======================================================+      |
|     +==[2. Wallet Top-up]==============================================================+      |
|     +==[3. Catalog Filter Query]===========> [H. Saturation Alerts (Starved/Congested)]+      |
|     +==[4. Bike Reservation Request]=======> [I. Maintenance Ticket Queue]             |      |
|     +==[5. Return Dock & Discount Preference]                                          |      |
|     +==[6. Fault Report] <================== [7. Rebalance Transfer Log]===============+      |
|                          <================== [8. Repair Cost & Status Resolution]======+      |
+-----------------------------------------------------------------------------------------------+
```

### 2.6 Input & Output Data Flow Specifications

#### Inputs to System (Process 0.0):
| Ref # | Flow Name | Source Entity | Description & Key Attributes |
| :---: | :--- | :--- | :--- |
| **1** | `User_Registration_&_Login` | Customer | `username`, `password_hash`, `full_name`, `email`, `phone` |
| **2** | `Wallet_Topup_Request` | Customer | `user_id`, `amount`, `payment_method` (UPI, Card, Cash) |
| **3** | `Catalog_Filter_Query` | Customer | `station_id`, `bike_type` (STANDARD, GEARED, ELECTRIC) |
| **4** | `Bike_Reservation_Request` | Customer | `user_id`, `bike_id`, `start_station_id` |
| **5** | `Return_Dock_&_Discount` | Customer | `rental_id`, `end_station_id`, `apply_eco_credits_flag` |
| **6** | `Fault_Report` | Customer | `bike_id`, `issue_type` (FLAT_TIRE, BRAKES, etc.), `notes` |
| **7** | `Rebalance_Transfer_Log` | Operator | `bike_id`, `source_station_id`, `target_station_id`, `timestamp` |
| **8** | `Maintenance_Resolution` | Operator | `log_id`, `bike_id`, `repair_cost`, `resolution_status` |
| **9** | `Admin_Credentials` | Administrator | `admin_username`, `password_hash` |
| **10**| `Fleet_&_Station_CRUD` | Administrator | Entity attributes for adding/editing stations & bicycles |
| **11**| `Tariff_&_Base_Fare_Config`| Administrator | `hourly_rate`, `base_fare`, `per_minute_rate`, `penalty_rate`|
| **12**| `Payment_Auth_Response` | Payment Gateway | `transaction_id`, `status` (SUCCESS/FAILED), `gateway_ref` |

#### Outputs from System (Process 0.0):
| Ref # | Flow Name | Target Entity | Description & Key Attributes |
| :---: | :--- | :--- | :--- |
| **A** | `Auth_Status_&_Profile` | Customer | Session status, `user_id`, `role`, `wallet_balance`, `eco_credits`|
| **B** | `Catalog_&_Battery_Feed` | Customer | Station docks list, `available_bikes`, `model`, `battery_level` |
| **C** | `6-Digit_Unlock_PIN` | Customer | Cryptographic 6-digit numeric PIN token (e.g., `482910`) |
| **D** | `Live_Ride_HUD_Metrics` | Customer | Running `elapsed_seconds`, `distance_km`, `estimated_fare` |
| **E** | `Itemized_Invoice_Receipt` | Customer | `rental_id`, `duration_min`, `gross_fare`, `discount`, `net_paid` |
| **F** | `Eco_Credit_&_CO2_Summary` | Customer | `co2_saved_kg`, `calories_burned`, `credits_earned` |
| **G** | `Alt_Station_Guidance` | Customer | Alternative station recommendation when destination dock is full |
| **H** | `Dock_Saturation_Alerts` | Operator | `station_id`, `saturation_%`, Status (`STARVED < 15%`, `CONGESTED ≥ 90%`)|
| **I** | `Maintenance_Ticket_Queue` | Operator | List of open maintenance logs with bike ID and reported issue |
| **J** | `Fleet_Health_&_Metrics` | Administrator | Total fleet count, active rides count, bikes under repair |
| **K** | `Revenue_&_Green_Analytics`| Administrator | Aggregate revenue generated, total campus CO2 emissions saved |
| **L** | `Payment_Debit_Request` | Payment Gateway | Customer ID, amount, payment method token |

---

## 3. Level 1 DFD (Decomposed Functional Architecture)

### 3.1 Functional Decomposition of Process 0.0
To understand the inner workings of the simple Context Diagram, Process 0.0 is decomposed into **9 distinct functional sub-processes**:

1. **Process 1.0: User Authentication & Wallet Management**: Handles customer/admin signup, SHA-256 password validation, session initialization, and wallet top-ups.
2. **Process 2.0: Station & Fleet Catalog Discovery**: Manages real-time querying of docking stations, bike counts, and battery status filtering.
3. **Process 3.0: Bicycle Reservation & OTP Unlock**: Validates eligibility (wallet >= ₹50, no active rides), updates bike status to `IN_USE`, decrements dock count, and generates the 6-digit unlock PIN.
4. **Process 4.0: Live Trip Metering & HUD Tracking**: Executes an EDT-safe background timer tracking duration, estimating distance, and computing dynamic accrued fares.
5. **Process 5.0: Dock Return & Dynamic Rebalancing (USP #1)**: Validates dock availability at destination, triggers rebalancing alerts, blocks full stations, and suggests alternative docks.
6. **Process 6.0: Automated Billing & Payment Settlement**: Debits the net fare from the user's wallet, records payment transactions, and prints the itemized receipt.
7. **Process 7.0: Eco-Credit & Carbon Offset Engine (USP #2)**: Calculates environmental impact ($0.21\text{ kg CO}_2/\text{km}$), calories burned, and deposits loyalty Eco-Credits.
8. **Process 8.0: Maintenance & Fault Reporting**: Records mechanical breakdowns during return or inspection, immediately locks bike from rental, and facilitates repair logging.
9. **Process 9.0: Administrative Fleet Control & Analytics**: Empowers administrators with full CRUD capabilities and real-time revenue and sustainability reporting.

### 3.2 Identification of Data Stores (Relational Mapping)
Level 1 introduces the system's persistent data stores, mapping 1-to-1 to the MySQL database tables:

- **`D1: Users Store`** (`users` table): User credentials, wallet balances, roles, and earned Eco-Credits.
- **`D2: Stations Store`** (`stations` table): Station geographical info, total dock capacities, and available bike counts.
- **`D3: Bicycles Store`** (`bicycles` table): Bike codes, models, types (STANDARD/GEARED/ELECTRIC), statuses, battery levels, and tariffs.
- **`D4: Rentals Store`** (`rentals` table): Active and historical trip records, start/end timestamps, unlock PINs, and calculated metrics.
- **`D5: Payments Store`** (`payments` table): Financial settlement logs, payment methods, discounts, and transaction statuses.
- **`D6: Maintenance Logs Store`** (`maintenance_logs` table): Fault tickets, issue descriptions, technician assignments, and repair costs.

### 3.3 Level 1 Mermaid Visual Model

```mermaid
flowchart TD
    classDef entity fill:#0f172a,stroke:#38bdf8,stroke-width:2px,color:#f8fafc;
    classDef process fill:#1e3a8a,stroke:#60a5fa,stroke-width:2px,color:#eff6ff;
    classDef usp fill:#064e3b,stroke:#34d399,stroke-width:2px,color:#ecfdf5,font-weight:bold;
    classDef datastore fill:#312e81,stroke:#818cf8,stroke-width:2px,color:#e0e7ff;

    E1["👤 Customer (Rider)"]:::entity
    E2["🛠️ Station Operator"]:::entity
    E3["⚙️ Administrator"]:::entity

    D1[("D1: Users Store")]:::datastore
    D2[("D2: Stations Store")]:::datastore
    D3[("D3: Bicycles Store")]:::datastore
    D4[("D4: Rentals Store")]:::datastore
    D5[("D5: Payments Store")]:::datastore
    D6[("D6: Maintenance Logs")]:::datastore

    P1("1.0<br/>User Authentication &<br/>Wallet Management"):::process
    P2("2.0<br/>Station & Fleet<br/>Catalog Discovery"):::process
    P3("3.0<br/>Bicycle Reservation &<br/>OTP Unlock PIN"):::process
    P4("4.0<br/>Live Trip Metering &<br/>HUD Concurrency"):::process
    P5("5.0<br/>Dock Return & Dynamic<br/>Rebalancing (USP)"):::usp
    P6("6.0<br/>Fare Calculation &<br/>Payment Settlement"):::process
    P7("7.0<br/>Eco-Credit & Carbon<br/>Offset Engine (USP)"):::usp
    P8("8.0<br/>Maintenance & Fault<br/>Lifecycle Management"):::process
    P9("9.0<br/>Admin Fleet Control<br/>& Sustainability Analytics"):::process

    E1 -->|Credentials / Top-up| P1
    P1 -->|Auth Token & Wallet Balance| E1
    P1 <-->|Read/Write User & Wallet| D1

    E1 -->|Filter By Station & Type| P2
    P2 -->|Station List & Battery %| E1
    D2 -.->|Read Station Capacity| P2
    D3 -.->|Read Bike Availability| P2

    E1 -->|Select Bike & Checkout| P3
    P3 -->|6-Digit Unlock PIN| E1
    P3 -.->|Check Balance >= ₹50| D1
    P3 -->|Set Bike Status=IN_USE| D3
    P3 -->|Decrement Available Bikes| D2
    P3 -->|Create Rental (ACTIVE)| D4

    P3 -->|Trigger Active Trip Timer| P4
    P4 -->|Real-time HUD Stream| E1
    P4 <-->|Track Elapsed Time & Metering| D4

    E1 -->|Dock at End Station| P5
    P5 <-->|Check Dock Saturation| D2
    P5 -->|Saturation Alerts (Starved/Congested)| E2
    P5 -->|Alternative Station Guidance| E1

    P5 -->|Proceed with Valid Return| P6
    E1 -->|Redeem Eco-Credits Choice| P6
    P6 <-->|Update Rental Completed| D4
    P6 -->|Deduct Net Fare| D1
    P6 -->|Log Payment Record| D5
    P6 -->|Set Bike AVAILABLE & Docked| D3
    P6 -->|Increment Available Bikes| D2
    P6 -->|Itemized Receipt Invoice| E1

    P6 -->|Send Distance & Duration| P7
    P7 -->|Credit User Eco-Points| D1
    P7 -->|Save CO2 & Eco-Credits| D4
    P7 -->|Eco-Impact & Saved CO2| E1

    E1 -->|Report Fault / Damage| P8
    E2 -->|Repair Status & Costs| P8
    P8 -->|Open Fault Tickets| E2
    P8 -->|Set Bike=MAINTENANCE| D3
    P8 <-->|Read/Write Tickets| D6

    E3 -->|CRUD Stations, Bikes & Tariffs| P9
    P9 <-->|Fleet & Config Sync| D2
    P9 <-->|Fleet & Config Sync| D3
    P9 <-->|Aggregate Financials & Green Impact| D4
    P9 <-->|Aggregate Financials & Green Impact| D5
    P9 -->|Fleet Dashboard & Revenue Reports| E3
```

---

## 4. Academic & Viva Voce Preparation Guide

### 4.1 Core DFD Concepts Viva Q&A
**Q1: What is a Data Flow Diagram (DFD) and how is it different from a Flowchart?**  
*Answer*: A DFD models the flow, storage, and transformation of data through a system without implying any sequential timing, loops, or decision branches. A Flowchart, on the other hand, models procedural control flow, conditional execution (`if/else`), and execution sequence.

**Q2: What is a Level 0 DFD (Context Diagram)? Why are there no database tables in it?**  
*Answer*: A Level 0 DFD represents the entire system as a single central process bubble (Process 0.0) bounded by external entities. Under DFD modeling principles, database tables (data stores) are internal to the system boundary at Level 0 and are therefore strictly encapsulated inside Process 0.0. They are only exposed when the system is decomposed at Level 1.

**Q3: What are the primary rules that must be followed when drawing a DFD?**  
*Answer*:  
1. No Entity can connect directly to another Entity.
2. No Entity can connect directly to a Data Store (all access must be mediated through a process).
3. No Data Store can connect directly to another Data Store.
4. No "Black Holes" (processes with inputs but no outputs).
5. No "Miracles" (processes with outputs but no inputs).
6. No "Grey Holes" (processes where outputs cannot be generated from inputs).

### 4.2 Project USP Viva Defense
**Q4: How does your DFD reflect the two USPs of your project?**  
*Answer*:  
1. **Dynamic Dock Rebalancing (Process 5.0)**: Continually monitors the saturation ratio $\frac{\text{available\_bikes}}{\text{total\_docks}}$ in `D2: Stations`. If capacity drops below 15%, it outputs an emergency `STARVED` alert to the Station Operator. If capacity hits $\ge 90\%$, it flags `CONGESTED`, redirecting riders to nearby docks and dispatching technicians.
2. **Eco-Credits & Carbon Offset Engine (Process 7.0)**: Automatically computes emissions prevented ($\text{Distance} \times 0.21\text{ kg CO}_2/\text{km}$) and calories burned ($\text{Duration} \times 7.5\text{ kcal/min}$). For every $0.5\text{ kg}$ of CO2 saved, Process 7.0 awards 1 Eco-Credit into `D1: Users`, which can be redeemed for instant financial discounts on future bike rides during **Process 6.0 (Fare Settlement)**.

---
*End of Data Flow Diagram Analysis Document*
