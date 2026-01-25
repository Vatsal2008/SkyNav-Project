package services;

import models.Flight;
import models.Ticket;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FileManager {
    private static final String FLIGHTS_FILE = "data/flights.csv";
    private static final String BOOKINGS_FILE = "data/bookings.csv";
    private static final String TRANSACTIONS_FILE = "data/transactions.csv";
    private static final String REPORT_FILE = "reports/daily_report.txt";

    // Reads flights from CSV file and returns an array
    public static Flight[] loadFlights() {
        ArrayList<Flight> flightList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FLIGHTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String flightId = parts[0].trim();
                    String destination = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    int totalSeats = Integer.parseInt(parts[3].trim());

                    Flight flight = new Flight(flightId, destination, price, totalSeats);
                    flightList.add(flight);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading flights: " + e.getMessage());
        }

        return flightList.toArray(new Flight[0]);
    }

    // Appends a booking to the bookings.csv file
    public static void saveBooking(Ticket ticket) {
        try (FileWriter fw = new FileWriter(BOOKINGS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            out.println(ticket.toCSV());

        } catch (IOException e) {
            System.out.println("Error saving booking: " + e.getMessage());
        }
    }

    // Logs a transaction (booking or refund) to transactions.csv
    public static void logTransaction(String type, double amount) {
        try (FileWriter fw = new FileWriter(TRANSACTIONS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            String timestamp = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            out.println(timestamp + "," + type + "," + amount);

        } catch (IOException e) {
            System.out.println("Error logging transaction: " + e.getMessage());
        }
    }

    // Loads all bookings from file
    public static ArrayList<String[]> loadBookings() {
        ArrayList<String[]> bookings = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(BOOKINGS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                bookings.add(parts);
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet - that's okay
        } catch (IOException e) {
            System.out.println("Error loading bookings: " + e.getMessage());
        }

        return bookings;
    }

    // Generates a daily financial report
    public static void generateDailyReport() {
        double totalIncome = 0.0;
        double totalRefunds = 0.0;
        int transactionCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String type = parts[1].trim();
                    double amount = Double.parseDouble(parts[2].trim());

                    if (type.equalsIgnoreCase("BOOKING")) {
                        totalIncome += amount;
                    } else if (type.equalsIgnoreCase("REFUND")) {
                        totalRefunds += amount;
                    }
                    transactionCount++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No transactions found.");
        } catch (IOException e) {
            System.out.println("Error reading transactions: " + e.getMessage());
        }

        double netProfit = totalIncome - totalRefunds;

        // Display to console
        System.out.println("\n=== DAILY FINANCIAL REPORT ===");
        System.out.println("Total Bookings Income: $" + String.format("%.2f", totalIncome));
        System.out.println("Total Refunds:         $" + String.format("%.2f", totalRefunds));
        System.out.println("Net Profit:            $" + String.format("%.2f", netProfit));
        System.out.println("Total Transactions:    " + transactionCount);

        // Save to file
        try (PrintWriter out = new PrintWriter(new FileWriter(REPORT_FILE))) {
            out.println("SkyNav Aviation - Daily Financial Report");
            out.println("Generated: " + LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            out.println("==========================================");
            out.println("Total Bookings Income: $" + String.format("%.2f", totalIncome));
            out.println("Total Refunds:         $" + String.format("%.2f", totalRefunds));
            out.println("Net Profit:            $" + String.format("%.2f", netProfit));
            out.println("Total Transactions:    " + transactionCount);

            System.out.println("\nReport saved to: " + REPORT_FILE);
        } catch (IOException e) {
            System.out.println("Error saving report: " + e.getMessage());
        }
    }

    // Ensures data directories exist
    public static void initializeDirectories() {
        new File("data").mkdirs();
        new File("reports").mkdirs();
    }
}