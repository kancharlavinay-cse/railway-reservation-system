# UML Guide

## Use Case Diagram
Actors: Passenger, Admin.

Passenger use cases:
- Register
- Login/OTP
- Search Train
- Book Ticket
- Make Payment
- View PNR/Ticket
- View Bookings
- Cancel Ticket
- View Waiting List

Admin use cases:
- Login
- View Trains
- View Passengers
- View Bookings
- View Waiting List
- Add Train

## Class Diagram
```text
Passenger 1 ---- * Booking
Train 1 -------- * Booking
Train 1 -------- * Stop
Train 1 -------- * Seat
Booking 1 ------ 1 Seat
Booking 1 ------ 1 Payment (logical)
Train 1 -------- * WaitingRequest
Station <------- Stop
Graph ---------- Edge
Graph ---------- Node (Dijkstra priority queue)
```

## ER Diagram
```text
USERS 1---N BOOKINGS N---1 TRAINS
BOOKINGS 1---N BOOKING_SEATS N---1 SEATS
BOOKINGS 1---N PAYMENTS
TRAINS 1---N TRAIN_ROUTES N---1 STATIONS
USERS 1---N WAITING_LIST
```
