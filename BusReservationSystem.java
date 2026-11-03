import java.util.*;
import java.util.Queue;

class Bus {
    String busNo, start, end, startTime;
    int totalSeats, fare;
    Bus next;
}

class Customer {
    String id, name, mobile, email, city;
    int age;
    Customer next;
}

class Reservation {
    String custId, busNo;
    int seatNo;
    Reservation next;
}

public class BusReservationSystem {
    // === Linked List head and tail for each module ===
    LinkedList<Customer> customers = new LinkedList<>();
    LinkedList<Bus> buses = new LinkedList<>();
    LinkedList<Reservation> reservations = new LinkedList<>();
    Queue<String> seatChangeQueue = new LinkedList<>();

    Scanner sc = new Scanner(System.in);

    // === INITIALIZATION ===
    public void init() {
        customers.clear();
        buses.clear();
        reservations.clear();
        seatChangeQueue.clear();
        System.out.println("System has been initialized. All data has been cleared.");
    }

    // === REGISTER CUSTOMER ===
    public void addCustomer() {
        System.out.println("REGISTER CUSTOMER");
        System.out.print("Customer ID     : ");
        String id = sc.next();
        System.out.print("Name            : ");
        String name = sc.next();
        System.out.print("Contact No      : ");
        String tel = sc.next();
        System.out.print("Email           : ");
        String email = sc.next();
        System.out.print("City            : ");
        String city = sc.next();
        System.out.print("Age             : ");
        int age = sc.nextInt();

        Customer c = new Customer();
        c.id = id;
        c.name = name;
        c.mobile = tel;
        c.email = email;
        c.city = city;
        c.age = age;
        customers.add(c);

        System.out.println("Customer registered successfully.");
    }

    
    public void displayCustomers() {
        System.out.println("REGISTERED CUSTOMERS");
        for (Customer c : customers) {
            System.out.println(c.id + "   " + c.name + "   " + c.mobile + "   " + c.city);
        }
    }

    
    public void addBus() {
        System.out.println("REGISTER BUS");
        System.out.print("Bus Number      : ");
        String bno = sc.next();
        System.out.print("Start Point     : ");
        String start = sc.next();
        System.out.print("End Point       : ");
        String end = sc.next();
        System.out.print("Total Seats     : ");
        int seats = sc.nextInt();
        System.out.print("Start Time  : ");
        String time = sc.next();
        System.out.print("Fare      : ");
        int fare = sc.nextInt();

        Bus b = new Bus();
        b.busNo = bno;
        b.start = start;
        b.end = end;
        b.totalSeats = seats;
        b.startTime = time;
        b.fare = fare;
        buses.add(b);

        System.out.println("Bus registered successfully.");
    }

    
    public void searchBus() {
        System.out.print("Enter Start Point   : ");
        String start = sc.next();
        System.out.print("Enter End Point     : ");
        String end = sc.next();

        boolean found = false;
        for (Bus b : buses) {
            if (b.start.equalsIgnoreCase(start) && b.end.equalsIgnoreCase(end)) {
                System.out.println("Bus No      : " + b.busNo);
                System.out.println("Time        : " + b.startTime);
                System.out.println("Seats       : " + b.totalSeats);
                System.out.println("Fare        : " + b.fare);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No buses found on this route.");
        }
    }

    
    public void reserveSeat() {
        System.out.print("Enter Customer ID    : ");
        String cid = sc.next();
        boolean foundCustomer = customers.stream().anyMatch(c -> c.id.equals(cid));
        if (!foundCustomer) {
            System.out.println("Customer not found!");
            return;
        }

        System.out.print("Enter Bus Number  : ");
        String bno = sc.next();
        Bus bus = buses.stream().filter(b -> b.busNo.equals(bno)).findFirst().orElse(null);
        if (bus == null) {
            System.out.println("Bus not found!");
            return;
        }

        long count = reservations.stream().filter(r -> r.busNo.equals(bno)).count();
        if (count >= bus.totalSeats) {
            System.out.println("No seats available! Added to waiting list.");
            seatChangeQueue.add(cid);
            return;
        }

        Reservation r = new Reservation();
        r.custId = cid;
        r.busNo = bno;
        r.seatNo = (int) count + 1;
        reservations.add(r);

        
        System.out.println("Seat Reserved. " + r.seatNo);
       
    }

   
    public void displayReservations() {
        System.out.println("All  Reservations");
        for (Reservation r : reservations) {
            System.out.println(r.custId + "   " + r.busNo + "   " + r.seatNo);
        }
    }

   
    public void cancelReservation() {
        System.out.print("Enter Customer ID: ");
        String cid = sc.next();

        Reservation r = reservations.stream().filter(res -> res.custId.equals(cid)).findFirst().orElse(null);
        if (r != null) {
            reservations.remove(r);
            System.out.println("Reservation cancelled " + cid);

            if (!seatChangeQueue.isEmpty()) {
                String next = seatChangeQueue.poll();
                Reservation newRes = new Reservation();
                newRes.custId = next;
                newRes.busNo = r.busNo;
                newRes.seatNo = r.seatNo;
                reservations.add(newRes);
                System.out.println("Seat reassigned to: " + next);
            }
        } else {
            System.out.println(" No reservation found for this customer.");
        }
    }

    
    public void requestSeatChange() {
        System.out.print("Enter Customer ID for Seat Change Request : ");
        String cid = sc.next();
        boolean hasRes = reservations.stream().anyMatch(r -> r.custId.equals(cid));
        if (hasRes) {
            seatChangeQueue.add(cid);
            System.out.println("Seat change request added to queue.");
        } else {
            System.out.println("oops! You haven't reserved a seat to request a seat change.");
        }
    }

    public static void main(String[] args) {
        BusReservationSystem sys = new BusReservationSystem();
        Scanner sc = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.println("\n===== BUS RESERVATION MENU =====");
            System.out.println("1 - Initialize System");
            System.out.println("2 - Register Customer");
            System.out.println("3 - Display Customers");
            System.out.println("4 - Register Bus");
            System.out.println("5 - Search Buses");
            System.out.println("6 - Reserve Seat");
            System.out.println("7 - Display Reservations");
            System.out.println("8 - Cancel Reservation");
            System.out.println("9 - Request Seat Change");
            System.out.println("10 - Exit");
            System.out.print("Enter your choice [1-10] : ");
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> sys.init();
                case 2 -> sys.addCustomer();
                case 3 -> sys.displayCustomers();
                case 4 -> sys.addBus();
                case 5 -> sys.searchBus();
                case 6 -> sys.reserveSeat();
                case 7 -> sys.displayReservations();
                case 8 -> sys.cancelReservation();
                case 9 -> sys.requestSeatChange();
                case 10 -> {
                    System.out.println("Existing the system. Thank you");
                    return;
                }
                default -> System.out.println("Invalid choice! Please enter a choise between 1 and 10");
            }
        }
    }
}
