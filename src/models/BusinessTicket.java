package models;

public class BusinessTicket extends Ticket {

    public BusinessTicket(String ticketId, String passengerName, Flight flight, double basePrice) {
        super(ticketId, passengerName, flight, basePrice);
    }

    @Override
    public double calculateRefund() {
        return basePrice * 0.90;
    }

    @Override
    public String getTicketType() {
        return "Business";
    }

    @Override
    public String toString() {
        return super.toString() + " [BUSINESS]";
    }
}