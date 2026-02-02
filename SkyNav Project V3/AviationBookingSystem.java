import java.util.*;
import java.time.LocalDate;
import java.io.*;

public class AviationBookingSystem {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<User> users = new ArrayList<>();
    static ArrayList<Flight> flights = new ArrayList<>();
    static ArrayList<Booking> bookings = new ArrayList<>();
    static User currentUser = null;

    public static void main(String[] args) {
        loadData(); 
        seedFlights();
        while (true) {
            printMenu();
            int choice = getSafeInt("Choice (1-12): ");
            handleChoice(choice);
        }
    }

    static void seedFlights() {
        String today = LocalDate.now().toString();
        String tomorrow = LocalDate.now().plusDays(1).toString();
        flights.clear();
        flights.add(new Flight("AI101", "Delhi", "Mumbai", today, "22:00", 4500, 50));
        flights.add(new Flight("SG201", "Delhi", "Bangalore", tomorrow, "10:30", 5200, 45));
        flights.add(new Flight("6E301", "Mumbai", "Delhi", tomorrow, "06:00", 3800, 60));
        flights.add(new Flight("UK401", "Delhi", "Chennai", "2026-03-15", "14:00", 6500, 50));
    }

    static void printMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("          SKYNAV AVIATION BOOKING SYSTEM");
        System.out.println("=".repeat(60));
        if (currentUser != null) System.out.println("LOGGED IN: " + currentUser.name + " (ID: " + currentUser.userId + ")");
        System.out.println("1.Register  2.Login     3.View Flights  4.Search Flight");
        System.out.println("5.Book Now  6.History   7.Cancel Trip   8.Print Ticket");
        System.out.println("9.Status    10.Check-in 11.Logout       12.Exit");
        System.out.println("-".repeat(60));
    }

    static void handleChoice(int choice) {
        switch (choice) {
            case 1 -> register();
            case 2 -> login();
            case 3 -> displayFlights(flights);
            case 4 -> search();
            case 5 -> book();
            case 6 -> history();
            case 7 -> cancelTrip();
            case 8 -> printTicket();
            case 9 -> checkStatus();
            case 10 -> webCheckin();
            case 11 -> { currentUser = null; System.out.println("Logged out."); }
            case 12 -> { saveData(); System.out.println("Data Saved Successfully. Goodbye!"); System.exit(0); }
            default -> System.out.println("Invalid Option.");
        }
    }

    static void saveData() {
        try (ObjectOutputStream uos = new ObjectOutputStream(new FileOutputStream("users.txt"));
             ObjectOutputStream bos = new ObjectOutputStream(new FileOutputStream("bookings.txt"))) {
            uos.writeObject(users);
            bos.writeObject(bookings);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    static void loadData() {
        try {
            File uf = new File("users.txt");
            if (uf.exists()) {
                try (ObjectInputStream uis = new ObjectInputStream(new FileInputStream(uf))) {
                    users = (ArrayList<User>) uis.readObject();
                    if (!users.isEmpty()) User.userCounter = users.get(users.size() - 1).userId;
                }
            }
            File bf = new File("bookings.txt");
            if (bf.exists()) {
                try (ObjectInputStream bis = new ObjectInputStream(new FileInputStream(bf))) {
                    bookings = (ArrayList<Booking>) bis.readObject();
                }
            }
        } catch (Exception e) {
            System.out.println("System initialized...");
        }
    }

    static void book() {
        if (currentUser == null) { System.out.println("Login first."); return; }
        displayFlights(flights);
        System.out.print("\nEnter Flight ID: ");
        String id = scanner.nextLine().toUpperCase();
        Flight sel = null;
        for (Flight f : flights) if (f.flightId.equalsIgnoreCase(id) && f.isBookable()) sel = f;
        if (sel == null) { System.out.println("Flight not available."); return; }
        
        int count = getSafeInt("Passengers: ");
        ArrayList<Passenger> pList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            System.out.println("\n--- Passenger " + (i+1) + " ---");
            System.out.print("Full Name: "); String name = scanner.nextLine();
            int age = getSafeInt("Age: ");
            System.out.println("Gender: 1.Male  2.Female  3.Others");
            int gChoice = getSafeInt("Choice (1-3): ");
            String gender = (gChoice == 1) ? "Male" : (gChoice == 2) ? "Female" : "Others";
            System.out.println("Seat Type: 1.Window (Wall side)  2.Aisle (Path side)");
            int sChoice = getSafeInt("Choice (1-2): ");
            String pref = (sChoice == 1) ? "Window" : "Aisle";
            pList.add(new Passenger(name, age, gender, pref));
        }

        double total = sel.price * count;
        System.out.print("Enter Promo Code (Press Enter to skip): ");
        String promo = scanner.nextLine();
        if (promo.equalsIgnoreCase("SKY10")) {
            total *= 0.90;
            System.out.println("* Promo Applied! 10% Discount given.");
        }

        String pnr = "PNR" + (new Random().nextInt(900000) + 100000);
        bookings.add(new Booking(pnr, currentUser, sel, pList, total));
        sel.availableSeats -= count;
        System.out.println("* Booking Successful! PNR: " + pnr + " | Total: Rs." + total);
    }

    static void displayFlights(ArrayList<Flight> list) {
        System.out.printf("\n%-8s %-12s %-12s %-12s %-10s %-6s\n", "ID", "From", "To", "Date", "Price", "Seats");
        System.out.println("-".repeat(65));
        for (Flight f : list) {
            if (f.isBookable()) {
                System.out.printf("%-8s %-12s %-12s %-12s Rs.%-7d %-6d\n", 
                    f.flightId, f.from, f.to, f.date, f.price, f.availableSeats);
            }
        }
    }

    static void register() {
        System.out.print("Name: "); String n = scanner.nextLine();
        System.out.print("Email: "); String e = scanner.nextLine();
        System.out.print("Password: "); String p = scanner.nextLine();
        users.add(new User(n, e, "N/A", "N/A", p));
        System.out.println("* Registered Successfully! ID: " + users.get(users.size()-1).userId);
    }

    static void login() {
        System.out.print("Email: "); String e = scanner.nextLine();
        System.out.print("Password: "); String p = scanner.nextLine();
        for (User u : users) {
            if (u.email.equalsIgnoreCase(e) && u.password.equals(p)) {
                currentUser = u;
                System.out.println("* Welcome back, " + u.name);
                return;
            }
        }
        System.out.println("Login Failed.");
    }

    static void history() {
        if (currentUser == null) return;
        for (Booking b : bookings) {
            if (b.user.userId == currentUser.userId) {
                System.out.printf("PNR: %s | %s to %s | %s | Rs.%.2f\n", 
                    b.pnr, b.flight.from, b.flight.to, b.status, b.totalPaid);
            }
        }
    }

    static void cancelTrip() {
        if (currentUser == null) return;
        System.out.print("Enter PNR to cancel: ");
        String pnr = scanner.nextLine();
        for (Booking b : bookings) {
            if (b.pnr.equalsIgnoreCase(pnr) && b.user.userId == currentUser.userId) {
                b.status = "Cancelled";
                System.out.println("Trip Cancelled Successfully.");
                return;
            }
        }
        System.out.println("PNR not found.");
    }

    static void printTicket() {
        System.out.print("Enter PNR: "); String pnr = scanner.nextLine();
        for (Booking b : bookings) {
            if (b.pnr.equalsIgnoreCase(pnr)) {
                System.out.println("\n" + "=".repeat(60));
                System.out.println("PNR: " + b.pnr + " | Status: " + b.status);
                System.out.println("Flight: " + b.flight.flightId + " | " + b.flight.from + " to " + b.flight.to);
                for (Passenger p : b.passengers) System.out.println("- " + p.name + " (" + p.gender + ") | Seat: " + p.seatNumber);
                System.out.println("Total Amount Paid: Rs." + b.totalPaid);
                System.out.println("=".repeat(60));
                return;
            }
        }
        System.out.println("PNR not found.");
    }

    static void checkStatus() {
        System.out.print("Flight ID: "); String id = scanner.nextLine();
        System.out.println("Status: On Time | Gate: " + (new Random().nextInt(10)+1) + "B");
    }

    static void webCheckin() {
        System.out.print("Enter PNR for Check-in: "); String pnr = scanner.nextLine();
        for (Booking b : bookings) {
            if (b.pnr.equalsIgnoreCase(pnr)) { 
                b.checkedIn = true; 
                System.out.println("\n--- BOARDING PASS ---");
                System.out.println("PNR: " + b.pnr + " | Flight: " + b.flight.flightId);
                for (Passenger p : b.passengers) System.out.println("Passenger: " + p.name + " | SEAT: " + p.seatNumber);
                System.out.println("Gate: " + (new Random().nextInt(10)+1) + "B | Boarding starts 45m before departure");
                return; 
            }
        }
        System.out.println("PNR not found.");
    }

    static void search() {
        System.out.print("Source City: "); String from = scanner.nextLine();
        System.out.print("Destination City: "); String to = scanner.nextLine();
        ArrayList<Flight> results = new ArrayList<>();
        for (Flight f : flights) {
            if (f.from.equalsIgnoreCase(from) && f.to.equalsIgnoreCase(to)) results.add(f);
        }
        displayFlights(results);
    }

    static int getSafeInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int val = scanner.nextInt();
                scanner.nextLine();
                return val;
            } catch (Exception e) {
                System.out.println("Error: Please enter a numeric value.");
                scanner.nextLine();
            }
        }
    }
}