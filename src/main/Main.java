package main;

import services.BookingManager;
import services.FileManager;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialize directories
        FileManager.initializeDirectories();

        // Create booking manager (loads flights automatically)
        BookingManager manager = new BookingManager();

        // Scanner for user input
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("\nWelcome to SkyNav Flight Management System!");

        while (running) {
            displayMenu();

            System.out.print("Enter Choice: ");
            int choice = getIntInput(scanner);

            switch (choice) {
                case 1:
                    // View Flights
                    manager.displayFlights();
                    break;

                case 2:
                    // Book Ticket
                    bookTicketFlow(scanner, manager);
                    break;

                case 3:
                    // Cancel Ticket
                    cancelTicketFlow(scanner, manager);
                    break;

                case 4:
                    // Generate Daily Report
                    manager.generateReport();
                    break;

                case 5:
                    // Exit
                    System.out.println("\nThank you for using SkyNav. Goodbye!");
                    running = false;
                    break;

                case 6:
                    // Hidden option - View Bookings
                    manager.viewBookings();
                    break;

                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }

            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("=== SKYNAV AVIATION SYSTEM ===");
        System.out.println("=".repeat(40));
        System.out.println("1. View Flights");
        System.out.println("2. Book Ticket");
        System.out.println("3. Cancel Ticket");
        System.out.println("4. Generate Daily Report");
        System.out.println("5. Exit");
        System.out.println("=".repeat(40));
    }

    private static void bookTicketFlow(Scanner scanner, BookingManager manager) {
        System.out.println("\n--- BOOK A TICKET ---");

        // Get flight ID
        System.out.print("Enter Flight ID (e.g., F100): ");
        String flightId = scanner.nextLine().trim();

        // Get passenger name
        System.out.print("Enter Passenger Name: ");
        String passengerName = scanner.nextLine().trim();

        if (passengerName.isEmpty()) {
            System.out.println("Error: Passenger name cannot be empty!");
            return;
        }

        // Get ticket class
        System.out.println("\nSelect Class:");
        System.out.println("1. Economy (50% refund on cancellation)");
        System.out.println("2. Business (90% refund, 1.5x price)");
        System.out.print("Enter Choice (1 or 2): ");
        int ticketClass = getIntInput(scanner);

        if (ticketClass != 1 && ticketClass != 2) {
            System.out.println("Error: Invalid class selection!");
            return;
        }

        // Confirm booking
        System.out.print("\nConfirm booking? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("yes") || confirm.equals("y")) {
            manager.bookTicket(flightId, passengerName, ticketClass);
        } else {
            System.out.println("Booking cancelled.");
        }
    }

    private static void cancelTicketFlow(Scanner scanner, BookingManager manager) {
        System.out.println("\n--- CANCEL A TICKET ---");

        System.out.print("Enter Ticket ID (e.g., T001): ");
        String ticketId = scanner.nextLine().trim();

        if (ticketId.isEmpty()) {
            System.out.println("Error: Ticket ID cannot be empty!");
            return;
        }

        System.out.print("Confirm cancellation? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("yes") || confirm.equals("y")) {
            manager.cancelTicket(ticketId);
        } else {
            System.out.println("Cancellation aborted.");
        }
    }

    // Helper method to safely get integer input
    private static int getIntInput(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
}