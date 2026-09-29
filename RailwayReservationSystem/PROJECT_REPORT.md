# Railway Reservation System

## 1. Abstract
A Java-based Railway Reservation System demonstrating object-oriented programming, data structures and algorithms, train/route search, segment-aware seat availability, booking, PNR generation, cancellation, FIFO waiting list and a normalized MySQL database design.

## 2. Technology Stack
- Java 17+
- OOP
- Collections / Queue / PriorityQueue
- Graph + Dijkstra shortest path
- MySQL 8+
- JDBC schema prepared in `database.sql`

## 3. Modules
### Passenger
Registration, OTP-style authentication (demo OTP 123456), train search, booking, fare calculation, simulated payment, PNR lookup, booking history, cancellation and waiting list.

### Admin
Admin login (`admin` / `admin123` in demo mode), train/passenger/booking/waiting-list viewing and basic train creation.

## 4. DSA
- ArrayList/HashMap for entities and indexes
- ArrayDeque Queue for FIFO waiting list
- Graph adjacency list
- PriorityQueue-based Dijkstra
- Segment-overlap logic for seat availability

## 5. Database Design
The SQL schema contains users, admins, stations, trains, train_routes, seats, bookings, booking_seats, payments and waiting_list. Foreign keys and unique constraints protect relationships and identifiers.

## 6. Security / Production Notes
The supplied runnable demo uses in-memory storage so it runs without external services. OTP and payment are simulated. For production, replace these with a real OTP provider and PCI-compliant payment gateway, hash passwords, store DB credentials in environment variables, and use JDBC repositories for persistence.

## 7. Test Results
`java -cp out Main --demo` was compiled and executed successfully. It verifies seat availability, overlapping-seat blocking, PNR booking, Dijkstra distance and cancellation.

## 8. Future Enhancements
Real MySQL persistence, real OTP/SMS, payment gateway, seat-map GUI, live train status, role-based authorization and cloud deployment.
