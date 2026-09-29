# railway-reservation-system
Java-based Railway Reservation System with train search, seat booking, PNR generation, cancellation, waiting list, admin dashboard, and MySQL database design.
# 🚆 Railway Reservation System

A Java-based **Railway Reservation System** designed as a college project to demonstrate Object-Oriented Programming, Data Structures & Algorithms, train reservation management, route searching, booking, cancellation, and database design.

## 📌 Project Overview

The Railway Reservation System allows passengers to register, search for trains, check seat availability, calculate fares, book tickets, make simulated payments, view PNR details, cancel bookings, and manage waiting-list reservations.

The project also includes an **Admin module** for managing trains, passengers, bookings, and waiting-list information.

The application currently uses **in-memory storage** so that it can run without requiring an external database. A MySQL database schema is also provided in `database.sql` for future persistent deployment.

## ✨ Features

### 👤 Passenger Module

* Passenger registration
* OTP-style demo authentication
* Train and route search
* Segment-based seat availability
* Fare calculation
* Ticket booking
* PNR generation
* Booking history
* PNR search
* Ticket cancellation
* Waiting-list management
* Automatic waiting-list promotion

### 🛠️ Admin Module

* Admin login
* View passengers
* View trains
* View bookings
* View waiting-list information
* Add/create trains

### 🧠 Data Structures & Algorithms

* `ArrayList` for collections
* `HashMap` for fast entity lookup
* `Queue` / `ArrayDeque` for FIFO waiting lists
* `PriorityQueue` for shortest-path calculation
* Graph-based railway route representation
* **Dijkstra's Algorithm** for shortest-path calculation
* Segment-overlap logic for seat availability

### 💳 Payment Simulation

The system demonstrates simulated:

* UPI payment
* Card payment
* Net Banking payment

> Payment processing is simulated for educational purposes and does not process real transactions.

## 🛠️ Technology Stack

| Technology            | Usage                     |
| --------------------- | ------------------------- |
| Java 17+              | Core application          |
| OOP                   | Application architecture  |
| Collections Framework | Data management           |
| Graph & Dijkstra      | Route calculation         |
| MySQL 8+              | Database schema           |
| JDBC                  | Planned persistence layer |
| Batch Script          | Windows execution         |

## 📂 Project Structure

```text
RailwayReservationSystem/
│
├── src/
│   ├── Main.java
│   ├── Models.java
│   └── RailwaySystem.java
│
├── database.sql
├── PROJECT_REPORT.md
├── UML.md
├── demo-output.txt
├── README.md
└── run.bat
```

## ⚙️ Requirements

Before running the project, install:

* **JDK 17 or newer**
* Windows, Linux, or macOS
* MySQL 8+ only if you want to work with the provided database schema

Check your Java installation:

```bash
java -version
javac -version
```

## ▶️ How to Run

### Windows

The easiest way is to run:

```text
run.bat
```

### Using Terminal

Open a terminal inside the project folder.

Compile the source files:

```bash
javac -d out src/*.java
```

Run the application:

```bash
java -cp out Main
```

### Run Demo Mode

To execute the automated demonstration:

```bash
javac -d out src/*.java
java -cp out Main --demo
```

## 🔐 Demo Credentials

### Passenger

Use the **Demo User Login** option.

Demo OTP:

```text
123456
```

### Admin

```text
Username: admin
Password: admin123
```

> These credentials are for demonstration purposes only.

## 🗄️ Database

The project includes:

```text
database.sql
```

The SQL file contains a normalized database design for:

* Users
* Admins
* Stations
* Trains
* Train routes
* Seats
* Bookings
* Booking seats
* Payments
* Waiting list

The database includes relationships, foreign keys, and unique constraints.

You can open `database.sql` using **MySQL Workbench** and execute it to create the database structure.

> The current runnable application intentionally uses in-memory storage, so MySQL configuration is not required to run the demo.

## 🧪 Testing

The project includes a demo mode that verifies important system operations such as:

* Seat availability
* Overlapping seat restrictions
* Ticket booking
* PNR generation
* Route distance calculation
* Dijkstra shortest-path calculation
* Ticket cancellation

Run:

```bash
java -cp out Main --demo
```

## 📊 System Modules

```text
                    Railway Reservation System
                              │
             ┌────────────────┴────────────────┐
             │                                 │
        Passenger Module                  Admin Module
             │                                 │
      ┌──────┼───────┐                 ┌───────┼───────┐
      │      │       │                 │       │       │
   Search  Booking  PNR             Trains  Users  Bookings
      │      │       │
      │   Payment   History
      │      │       │
      └──────┴───────┘
             │
       Cancellation
             │
       Waiting List
```

## 🔐 Production Considerations

This project is intended for educational use.

For a production-ready railway reservation system, the following improvements would be required:

* Real MySQL persistence
* Password hashing
* Environment-based database credentials
* Real OTP/SMS service
* Secure payment gateway
* Role-based authorization
* Proper session management
* Input sanitization
* Transaction management
* Seat-map interface
* Real-time train status

## 🚀 Future Enhancements

* Web-based frontend
* Spring Boot backend
* REST APIs
* Real MySQL persistence
* Online payment gateway
* Real OTP verification
* Live train tracking
* Interactive seat selection
* Email/SMS ticket confirmation
* Cloud deployment
* Mobile application

## 📚 Documentation

Additional project documentation is available in:

* [`PROJECT_REPORT.md`](PROJECT_REPORT.md) — Project report
* [`UML.md`](UML.md) — UML/design documentation
* [`database.sql`](database.sql) — Database schema
* [`demo-output.txt`](demo-output.txt) — Demo execution output

## 👨‍💻 Author

**Kancharla Vinay**

### ⭐ If you find this project useful

Feel free to explore, modify, and improve the project for learning and academic purposes.
