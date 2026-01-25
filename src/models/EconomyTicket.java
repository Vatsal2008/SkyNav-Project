package models;

public class EconomyTicket extends Ticket {

    public EconomyTicket(String ticketId, String passengerName, Flight flight, double basePrice) {
        super(ticketId, passengerName, flight, basePrice);
    }

    @Override
    public double calculateRefund() {
        return basePrice * 0.50;
    }

    @Override
    public String getTicketType() {
        return "Economy";
    }

    @Override
    public String toString() {
        return super.toString() + " [ECONOMY]";
    }
}