package models;

public class Flight {
    private String flightId;
    private String destination;
    private double price;
    private int totalSeats;
    private int bookedSeats;

    public Flight(String flightId, String destination, double price, int totalSeats) {
        this.flightId = flightId;
        this.destination = destination;
        this.price = price;
        this.totalSeats = totalSeats;
        this.bookedSeats = 0;
    }

    public Flight(String flightId, String destination, double price, int totalSeats, int bookedSeats) {
        this.flightId = flightId;
        this.destination = destination;
        this.price = price;
        this.totalSeats = totalSeats;
        this.bookedSeats = bookedSeats;
    }

    public int getAvailableSeats() {
        return totalSeats - bookedSeats;
    }

    public void incrementBookedSeats() {
        bookedSeats++;
    }

    public void decrementBookedSeats() {
        if (bookedSeats > 0) {
            bookedSeats--;
        }
    }

    // Getters
    public String getFlightId() {
        return flightId;
    }

    public String getDestination() {
        return destination;
    }

    public double getPrice() {
        return price;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getBookedSeats() {
        return bookedSeats;
    }

    @Override
    public String toString() {
        return String.format("%-8s %-15s $%-8.2f %d/%d seats",
                flightId, destination, price, bookedSeats, totalSeats);
    }
}