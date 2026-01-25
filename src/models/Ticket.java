package models;

public class Ticket {
    protected String ticketId;
    protected String passengerName;
    protected Flight flight;
    protected double basePrice;

    public Ticket(String ticketId, String passengerName, Flight flight, double basePrice) {
        this.ticketId = ticketId;
        this.passengerName = passengerName;
        this.flight = flight;
        this.basePrice = basePrice;
    }

    // Default implementation - can be overridden by child classes
    public double calculateRefund() {
        return 0.0;
    }

    // Helper method for saving to CSV file
    public String toCSV() {
        return ticketId + "," + passengerName + "," +
                flight.getFlightId() + "," + getTicketType() + "," + basePrice;
    }

    // To be overridden by child classes
    public String getTicketType() {
        return "Standard";
    }

    // Getters
    public String getTicketId() {
        return ticketId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public Flight getFlight() {
        return flight;
    }

    public double getBasePrice() {
        return basePrice;
    }

    @Override
    public String toString() {
        return String.format("Ticket: %s | Passenger: %s | Flight: %s | Price: $%.2f",
                ticketId, passengerName, flight.getFlightId(), basePrice);
    }
}