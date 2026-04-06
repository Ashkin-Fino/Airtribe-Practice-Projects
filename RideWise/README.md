# 🚗 Modular Ride Sharing System (LLD)

## 📌 Overview

This project is a **Low-Level Design (LLD)** implementation of a modular ride-sharing system (like Uber/Ola) built using **Java**.

The system is designed with **clean architecture principles**, focusing on:

* Separation of concerns
* Extensibility
* Design patterns (Strategy Pattern)

---

## 🏗️ System Design

### 🔹 Core Entities

* **Person (Abstract)**

  * Base class for `Rider` and `Driver`

* **Rider**

  * Can request rides

* **Driver**

  * Has availability and location
  * Gets assigned rides

* **Ride**

  * Represents a ride request
  * Contains rider, driver, locations, distance, and status

* **FareReceipt**

  * Generated after ride completion

---

### 🔹 Enums

* `RideStatus`

  * `YETTOBOARD`
  * `INPROGRESS`
  * `COMPLETED`

---

## 🧠 Design Patterns Used

### 1. Strategy Pattern

#### 🚕 Ride Matching Strategy

* `NearestDriverStrategy`
* `LeastActiveDriverStrategy`

#### 💰 Fare Calculation Strategy

* `DefaultFareStrategy`
* `PeakHourFareStrategy`

This makes the system easily extensible:

* Add surge pricing
* Add better driver matching algorithms

---

## 🧱 Architecture

```
Entities → Repositories → Services → Strategies
```

### 🔹 Repositories (In-Memory)

* RiderRepository
* DriverRepository
* RideRepository
* FareReceiptRepository

### 🔹 Services

* RiderService
* DriverService
* RideService
* FareReceiptService

---

## 🔄 Flow of Execution

### 1️⃣ Register Users

* Rider registers
* Driver registers

### 2️⃣ Request Ride

* Rider requests ride
* System:

  * Finds available drivers
  * Applies matching strategy
  * Assigns driver
  * Marks driver unavailable

### 3️⃣ Ride In Progress

* Ride status → `INPROGRESS`

### 4️⃣ Complete Ride

* Distance is provided
* Fare is calculated using strategy
* Receipt is generated
* Driver becomes available again

---

## ⚙️ Key Methods

### RideService

* `requestRide()`
* `assignDriver()`
* `completeRide()`
* `viewAllRides()`

### DriverService

* `registerDriver()`
* `listAvailableDrivers()`
* `updateAvailability()`

### RiderService

* `registerRider()`
* `getRiderById()`

---

## 🧪 Sample Usage (Pseudo Flow)

```java
// Register rider
Rider rider = riderService.registerRider("John");

// Register driver
Driver driver = driverService.registerDriver("Alex", new String[]{"10", "20"});

// Request ride
Ride ride = rideService.requestRide(rider.getId(),
        new String[]{"10", "20"},
        new String[]{"30", "40"});

// Complete ride
rideService.completeRide(ride, 15);
```

---

## 🚀 Extensibility

You can easily extend this system by adding:

* ✅ Surge pricing strategy
* ✅ Real-time location tracking
* ✅ Payment integration
* ✅ Ride cancellation
* ✅ Driver rating system
* ✅ Multiple vehicle types

---

## ⚠️ Limitations

* In-memory storage (no database)
* Simplified location model (`String[]`)
* No concurrency handling
* Basic driver matching logic

---

## 🛠️ Future Improvements

* Replace `String[]` with a proper `Location` class
* Add thread-safe repositories
* Implement real distance calculation (Haversine formula)
* Add REST APIs (Spring Boot)
* Integrate database (MySQL / PostgreSQL)

---

## 📚 Concepts Covered

* Object-Oriented Design
* SOLID Principles
* Strategy Pattern
* Service-Repository Pattern
* Modular Design

---

## 👨‍💻 Author

Ashkin Fino. R

---

