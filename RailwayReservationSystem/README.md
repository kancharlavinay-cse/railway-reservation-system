# Railway Reservation System — Final Runnable College Project

## Status
The Java application core has been compiled and executed successfully. The current runnable build uses in-memory data so it works immediately without a MySQL server. `database.sql` provides the MySQL schema for persistent deployment.

## Requirements
- JDK 17 or newer
- Windows: use `run.bat`, or run commands below
- MySQL 8+ only when enabling the persistence layer

## Run
```text
javac -d out src\*.java
java -cp out Main
```

## Automated test
```text
javac -d out src\*.java
java -cp out Main --demo
```

## Demo credentials
Passenger demo login: use Passenger -> Demo User Login
OTP: `123456`
Admin: `admin`
Password: `admin123`

## Database
Run `database.sql` in MySQL Workbench to create the normalized schema. The runnable demo intentionally does not require external DB credentials. For a production deployment, replace the in-memory repositories with JDBC repositories and configure credentials through environment variables.

## Main features
- Passenger registration and OTP-style login
- Train and route search
- Segment-aware seat availability
- Fare calculation
- Booking and PNR
- Simulated UPI/Card/NetBanking payment
- Booking history and PNR search
- Cancellation and waiting-list promotion
- Admin dashboard
- Graph and Dijkstra shortest path
- Input validation and error handling

## Documentation
- `PROJECT_REPORT.md`
- `UML.md`
- `database.sql`
- `demo-output.txt`
