import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class RailwaySystem {
    private final Scanner sc = new Scanner(System.in);
    private final Map<Integer, Train> trains = new HashMap<>();
    private final Map<Integer, Station> stations = new HashMap<>();
    private final Map<Integer, Passenger> passengers = new HashMap<>();
    private final Map<String, Booking> bookingsByPnr = new HashMap<>();
    private final Map<Integer, List<Booking>> bookingsByPassenger = new HashMap<>();
    private final Map<Integer, Queue<WaitingRequest>> waiting = new HashMap<>();
    private int passengerSeq = 1, bookingSeq = 1, waitingSeq = 1;
    private final Graph graph = new Graph();
    private static final BigDecimal RATE_PER_KM = new BigDecimal("2.5");
    private static final BigDecimal AC_MULTIPLIER = new BigDecimal("1.4");
    private static final BigDecimal MIN_FARE = new BigDecimal("100.00");

    public RailwaySystem() { seed(); }

    private void seed() {
        addStationInternal(1, "BZA", "Vijayawada");
        addStationInternal(2, "ELR", "Eluru");
        addStationInternal(3, "RJY", "Rajahmundry");
        addStationInternal(4, "VSKP", "Visakhapatnam");
        addStationInternal(5, "HYB", "Hyderabad");
        graph.addEdge(1,2,60); graph.addEdge(2,3,90); graph.addEdge(3,4,130); graph.addEdge(1,5,350);
        Train t = new Train(12727, "Godavari Express");
        t.addStop(new Stop(1, "11:10", "11:10", 0));
        t.addStop(new Stop(2, "12:00", "12:05", 60));
        t.addStop(new Stop(3, "13:30", "13:35", 150));
        t.addStop(new Stop(4, "15:30", "15:35", 280));
        addStandardSeats(t);
        trains.put(t.number, t);
        Passenger p = new Passenger(passengerSeq++, "Demo User", "9999999999", "demo@example.com");
        passengers.put(p.id,p);
    }

    private void addStandardSeats(Train t) {
        if (!t.seats.isEmpty()) return;
        for (int i=1;i<=12;i++) t.seats.add(new Seat(i, "A1", i, "2A"));
        for (int i=1;i<=20;i++) t.seats.add(new Seat(12+i, "S1", i, "SL"));
    }

    private void addStandardRoute(Train t) {
        if (!t.stops.isEmpty()) return;
        t.addStop(new Stop(1, "11:10", "11:10", 0));
        t.addStop(new Stop(2, "12:00", "12:05", 60));
        t.addStop(new Stop(3, "13:30", "13:35", 150));
        t.addStop(new Stop(4, "15:30", "15:35", 280));
    }

    private void addStationInternal(int id,String code,String name){ stations.put(id,new Station(id,code,name)); }

    public void run() {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("     RAILWAY RESERVATION SYSTEM");
            System.out.println("========================================");
            System.out.println("1. Passenger");
            System.out.println("2. Admin");
            System.out.println("3. Dijkstra Route Demo");
            System.out.println("4. Exit");
            int c = readInt("Enter choice: ");
            if(c==1) passengerMenu(); else if(c==2) adminMenu(); else if(c==3) routeDemo(); else if(c==4) return; else System.out.println("Invalid choice.");
        }
    }

    private void passengerMenu() {
        System.out.println("\n1. Register  2. Login  3. Search Train  4. Demo User Login  5. Back");
        int c=readInt("Choice: ");
        if(c==1) register(); else if(c==2) login(); else if(c==3) search(); else if(c==4) passengerDashboard(passengers.get(1));
    }

    private void register() {
        String name=read("Name: "), phone=read("10-digit phone: "), email=read("Email: ");
        if(!name.matches("[a-zA-Z ]+") || !phone.matches("\\d{10}") || !email.contains("@")) { System.out.println("Invalid details."); return; }
        for(Passenger p:passengers.values()) if(p.phone.equals(phone)){System.out.println("Phone already registered.");return;}
        Passenger p=new Passenger(passengerSeq++,name,phone,email); passengers.put(p.id,p); System.out.println("Registered successfully. OTP: 123456 (demo mode)"); passengerDashboard(p);
    }

    private void login() {
        String phone=read("Phone: "); Passenger found=null; for(Passenger p:passengers.values()) if(p.phone.equals(phone)) found=p;
        if(found==null){System.out.println("Passenger not found.");return;}
        String otp=read("OTP (demo: 123456): "); if(!otp.equals("123456")){System.out.println("Invalid OTP.");return;} passengerDashboard(found);
    }

    private void passengerDashboard(Passenger p) {
        while(true){
            System.out.println("\nWelcome, "+p.name);
            System.out.println("1.Search Train 2.Book Ticket 3.My Bookings 4.Search PNR 5.Cancel 6.Waiting List 7.Logout");
            int c=readInt("Choice: ");
            if(c==1) search(); else if(c==2) book(p); else if(c==3) myBookings(p); else if(c==4) pnrSearch(); else if(c==5) cancel(p); else if(c==6) waitingList(p); else if(c==7) return; else System.out.println("Invalid choice.");
        }
    }

    private void search(){
        String from=read("From station code: ").toUpperCase(), to=read("To station code: ").toUpperCase();
        int s=findStation(from), d=findStation(to); if(s<0||d<0){System.out.println("Unknown station.");return;}
        for(Train t:trains.values()){if(t.hasRoute(s,d)) System.out.println(t.number+" - "+t.name+" | "+stations.get(s).name+" -> "+stations.get(d).name+" | Distance "+t.distance(s,d)+" km");}
    }

    private void book(Passenger p){
        Train t=chooseTrain(); if(t==null)return;
        int s=readStation("Source station code: "), d=readStation("Destination station code: ");
        if(s<0||d<0){System.out.println("Unknown station.");return;}
        if(s==d){System.out.println("Source and destination must be different.");return;}
        if(!t.hasRoute(s,d)){System.out.println("Invalid route for this train.");return;}
        List<Seat> avail=availableSeats(t,s,d); if(avail.isEmpty()){System.out.println("No seats available. Add to waiting list? 1.Yes 2.No"); if(readInt("Choice: ")==1) addWaiting(p,t,s,d); return;}
        System.out.println("Available seats:"); for(Seat seat:avail) System.out.print(seat.id+"("+seat.coach+"-"+seat.number+") "); System.out.println();
        int seatId=readInt("Seat ID: "); Seat seat=seatById(avail,seatId); if(seat==null){System.out.println("Invalid/unavailable seat.");return;}
        BigDecimal fare=calculateFare(t,s,d,seat);
        System.out.printf("Fare: ₹%s%n",formatMoney(fare)); String method=read("Payment method (UPI/Card/NetBanking): ");
        System.out.println("Processing payment...");
        String pnr=generatePnr();
        Booking b=new Booking(bookingSeq++,p.id,t.number,s,d,seat.id,pnr,fare,method,"CONFIRMED"); bookingsByPnr.put(pnr,b); bookingsByPassenger.computeIfAbsent(p.id,k->new ArrayList<>()).add(b);
        System.out.println("\nBOOKING CONFIRMED!\nPNR: "+pnr+"\nSeat: "+seat.coach+"-"+seat.number+"\nAmount: ₹"+formatMoney(fare));
    }

    private BigDecimal calculateFare(Train t,int s,int d,Seat seat){
        BigDecimal distanceFare=BigDecimal.valueOf(t.distance(s,d)).multiply(RATE_PER_KM);
        if(seat.cls.equals("2A")) distanceFare=distanceFare.multiply(AC_MULTIPLIER);
        return distanceFare.max(MIN_FARE).setScale(2,RoundingMode.HALF_UP);
    }

    private String formatMoney(BigDecimal amount){return amount.setScale(2,RoundingMode.HALF_UP).toPlainString();}
    private String generatePnr(){
        String pnr;
        do { pnr=String.format("%010d",Math.abs(new Random().nextLong())%10_000_000_000L); } while(bookingsByPnr.containsKey(pnr));
        return pnr;
    }

    private List<Seat> availableSeats(Train t,int s,int d){List<Seat> out=new ArrayList<>(); for(Seat seat:t.seats){boolean blocked=false; for(Booking b:bookingsByPnr.values()) if(b.trainNumber==t.number&&b.status.equals("CONFIRMED")&&b.seatId==seat.id&&overlap(t,s,d,b.source,b.destination)){blocked=true;break;} if(!blocked)out.add(seat);}return out;}
    private boolean overlap(Train t,int s1,int d1,int s2,int d2){int a=t.stopIndex(s1),b=t.stopIndex(d1),c=t.stopIndex(s2),d=t.stopIndex(d2);return a>=0&&b>=0&&c>=0&&d>=0&&a<d&&c<b;}
    private Seat seatById(List<Seat> list,int id){for(Seat s:list)if(s.id==id)return s;return null;}

    private void myBookings(Passenger p){List<Booking> list=bookingsByPassenger.getOrDefault(p.id,List.of()); if(list.isEmpty()){System.out.println("No bookings.");return;} for(Booking b:list) printBooking(b);}
    private void pnrSearch(){String pnr=read("PNR: "); Booking b=bookingsByPnr.get(pnr); if(b==null)System.out.println("PNR not found.");else printBooking(b);}
    private void printBooking(Booking b){System.out.println("PNR "+b.pnr+" | Train "+b.trainNumber+" | "+stations.get(b.source).code+" -> "+stations.get(b.destination).code+" | Seat "+b.seatId+" | ₹"+formatMoney(b.amount)+" | "+b.status);}

    private void cancel(Passenger p){String pnr=read("PNR: ");Booking b=bookingsByPnr.get(pnr);if(b==null||b.passengerId!=p.id){System.out.println("Booking not found.");return;}if(!b.status.equals("CONFIRMED")){System.out.println("Already cancelled.");return;}b.status="CANCELLED";System.out.println("Cancelled successfully. Refund initiated (demo mode).");promote(b.trainNumber,b.source,b.destination);}
    private void addWaiting(Passenger p,Train t,int s,int d){WaitingRequest w=new WaitingRequest(waitingSeq++,p.id,t.number,s,d);waiting.computeIfAbsent(t.number,k->new ArrayDeque<>()).offer(w);System.out.println("Added to waiting list at position "+waiting.get(t.number).size()+".");}
    private void waitingList(Passenger p){
        boolean found=false;
        for(Queue<WaitingRequest> q:waiting.values()){
            int position=0;
            for(WaitingRequest w:q){
                position++;
                if(w.passengerId==p.id){
                    found=true;
                    String from=stations.get(w.source).code, to=stations.get(w.destination).code;
                    System.out.println("Train "+w.trainNumber+" | "+from+" -> "+to+" | Position "+position+" | WAITING");
                }
            }
        }
        if(!found) System.out.println("No waiting-list entries.");
    }
    private void promote(int train,int s,int d){
        Queue<WaitingRequest> q=waiting.get(train);if(q==null)return;
        Iterator<WaitingRequest> it=q.iterator();
        while(it.hasNext()){
            WaitingRequest w=it.next();Train t=trains.get(train);List<Seat> available=availableSeats(t,w.source,w.destination);
            if(!available.isEmpty()){
                Seat seat=available.get(0);String pnr=generatePnr();
                BigDecimal fare=calculateFare(t,w.source,w.destination,seat);
                Booking b=new Booking(bookingSeq++,w.passengerId,train,w.source,w.destination,seat.id,pnr,fare,"WAITLIST_PROMOTION","CONFIRMED");
                bookingsByPnr.put(pnr,b);bookingsByPassenger.computeIfAbsent(w.passengerId,k->new ArrayList<>()).add(b);it.remove();
                System.out.println("Waiting passenger promoted. New PNR: "+pnr);break;
            }
        }
        if(q.isEmpty()) waiting.remove(train);
    }

    private void adminMenu(){String u=read("Admin username: "),pw=read("Password: ");if(!u.equals("admin")||!pw.equals("admin123")){System.out.println("Invalid admin credentials.");return;}while(true){System.out.println("\nADMIN: 1.View Trains 2.View Users 3.View Bookings 4.Add Train 5.Logout");int c=readInt("Choice: ");if(c==1)for(Train t:trains.values())System.out.println(t.number+" - "+t.name+" seats="+t.seats.size()+" routes="+t.stops.size());else if(c==2)for(Passenger p:passengers.values())System.out.println(p.id+" "+p.name+" "+p.phone);else if(c==3)for(Booking b:bookingsByPnr.values())printBooking(b);else if(c==4)addTrain();else if(c==5)return;}}

    private void addTrain(){
        int n=readInt("Train number: ");String name=read("Train name: ");
        if(trains.containsKey(n)){System.out.println("Already exists.");return;}
        Train t=new Train(n,name);
        addStandardRoute(t);
        addStandardSeats(t);
        trains.put(n,t);
        System.out.println("Train added successfully with "+t.seats.size()+" seats and "+t.stops.size()+" stations (BZA -> ELR -> RJY -> VSKP).");
    }

    private void routeDemo(){
        int a=readStation("Source station code: "),b=readStation("Destination station code: ");
        if(a<0||b<0){System.out.println("Unknown station code.");return;}
        if(a==b){System.out.println("Source and destination must be different.");return;}
        Map<Integer,Integer> d=graph.dijkstra(a);int distance=d.getOrDefault(b,Integer.MAX_VALUE);
        if(distance==Integer.MAX_VALUE) System.out.println("No route found."); else System.out.println("Shortest distance: "+distance+" km");
    }

    private Train chooseTrain(){if(trains.isEmpty())return null;for(Train t:trains.values())System.out.println(t.number+" - "+t.name);int n=readInt("Train number: ");Train t=trains.get(n);if(t==null)System.out.println("Train not found.");return t;}
    private int findStation(String code){for(Station s:stations.values())if(s.code.equalsIgnoreCase(code))return s.id;return -1;}
    private int readStation(String prompt){
        String value=read(prompt).toUpperCase();
        int id=findStation(value);
        if(id>=0)return id;
        try { int numeric=Integer.parseInt(value); if(stations.containsKey(numeric)) return numeric; } catch(NumberFormatException ignored) {}
        return -1;
    }
    private String read(String x){System.out.print(x);return sc.nextLine().trim();}
    private int readInt(String x){while(true){try{return Integer.parseInt(read(x));}catch(Exception e){System.out.println("Enter a number.");}}}

    public void demo(){
        System.out.println("=== AUTOMATED APPLICATION TEST ===");
        Passenger p=passengers.get(1);Train t=trains.get(12727);List<Seat> a=availableSeats(t,1,4);System.out.println("Available seats BZA->VSKP: "+a.size());
        Seat s=a.get(0);String pnr="2465127853";Booking b=new Booking(bookingSeq++,p.id,t.number,1,4,s.id,pnr,new BigDecimal("980.00"),"UPI","CONFIRMED");bookingsByPnr.put(pnr,b);bookingsByPassenger.computeIfAbsent(p.id,k->new ArrayList<>()).add(b);
        System.out.println("Booking created: ");printBooking(b);
        System.out.println("Same seat overlapping BZA->RJY available? "+(availableSeats(t,1,3).stream().anyMatch(x->x.id==s.id)));
        System.out.println("Same seat non-overlapping VSKP->HYB (not on train) route check: "+t.hasRoute(4,5));
        System.out.println("Dijkstra BZA->VSKP: "+graph.dijkstra(1).get(4)+" km");
        b.status="CANCELLED";promote(t.number,1,4);System.out.println("Cancellation: "+b.status);System.out.println("=== TEST COMPLETE: APPLICATION CORE RUNS ===");
    }
}
