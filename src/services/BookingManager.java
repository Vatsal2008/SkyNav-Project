package services;

import models.*;
import java.util.ArrayList;

public class BookingManager {
    private Flight[] flights;
    private ArrayList<Ticket> tickets;
    private int ticketCounter;

    public BookingManager() {
        // Load flights from CSV immediately
        this.flights = FileManager.loadFlights();
        this.tickets = new ArrayList<>();
        this.ticketCounter = 1;

        System.out.println("Loading System...");
        System.out.println("Loaded " + flights.length + " Flights.");
    }

    // Display all available flights
    public void displayFlights() {
        System.out.println("\n=== AVAILABLE FLIGHTS ===");
        System.out.println("Flight   Destination     Price      Availability");
        System.out.println("---------------------------------------------------");

        for (Flight flight : flights) {
            System.out.println(flight);
        }
    }

    // Find a flight by ID
    private Flight findFlight(String flightId) {
        for (Flight flight : flights) {
            if (flight.getFlightId().equalsIgnoreCase(flightId)) {
                return flight;
            }
        }
        return null;
    }

    // Book a ticket (Polymorphism in action!)
    public void bookTicket(String flightId, String passengerName, int ticketClass) {
        Flight flight = findFlight(flightId);

        if (flight == null) {
            System.out.println("Error: Flight not found!");
            return;
        }

        if (flight.getAvailableSeats() <= 0) {
            System.out.println("Error: No seats available on this flight!");
            return;
        }

        // Generate ticket ID
        String ticketId = "T" + String.format("%03d", ticketCounter++);

        // Create appropriate ticket type based on class (Polymorphism)
        Ticket ticket;
        double price = flight.getPrice();

        if (ticketClass == 1) {
            // Economy class
            ticket = new EconomyTicket(ticketId, passengerName, flight, price);
        } else if (ticketClass == 2) {
            // Business class (1.5x price)
            price = price * 1.5;
            ticket = new BusinessTicket(ticketId, passengerName, flight, price);
        } else {
            System.out.println("Error: Invalid ticket class!");
            return;
        }

        // Add to tickets list
        tickets.add(ticket);

        // Update flight seats
        flight.incrementBookedSeats();

        // Save to file
        FileManager.saveBooking(ticket);

        // Log transaction
        FileManager.logTransaction("BOOKING", price);

        // Confirmation
        System.out.println("\n✓ Booking Successful!");
        System.out.println(ticket);
        System.out.println("Amount Paid: $" + String.format("%.2f", price));
    }

    // Cancel a ticket (Polymorphic refund calculation)
    public void cancelTicket(String ticketId) {
        Ticket ticket = null;

        // Find the ticket
        for (Ticket t : tickets) {
            if (t.getTicketId().equalsIgnoreCase(ticketId)) {
                ticket = t;
                break;
            }
        }

        if (ticket == null) {
            System.out.println("Error: Ticket not found!");
            return;
        }

        // Calculate refund (Polymorphic call - different for Economy vs Business)
        double refundAmount = ticket.calculateRefund();

        // Update flight seats
        ticket.getFlight().decrementBookedSeats();

        // Remove from tickets list
        tickets.remove(ticket);

        // Log the refund transaction
        FileManager.logTransaction("REFUND", refundAmount);

        // Confirmation
        System.out.println("\n✓ Ticket Cancelled Successfully!");
        System.out.println("Ticket ID: " + ticketId);
        System.out.println("Refund Amount: $" + String.format("%.2f", refundAmount));
        System.out.println("(" + ticket.getTicketType() + " class: " +
                (int)(ticket.calculateRefund() / ticket.getBasePrice() * 100) + "% refund)");
    }

    // View all current bookings
    public void viewBookings() {
        if (tickets.isEmpty()) {
            System.out.println("\nNo active bookings.");
            return;
        }

        System.out.println("\n=== ACTIVE BOOKINGS ===");
        for (Ticket ticket : tickets) {
            System.out.println(ticket);
        }
    }

    // Generate financial report
    public void generateReport() {
        FileManager.generateDailyReport();
    }
}