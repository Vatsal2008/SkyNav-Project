import java.util.ArrayList;
import java.io.Serializable;

public class Booking implements Serializable {
    String pnr, status = "Confirmed";
    User user;
    Flight flight;
    ArrayList<Passenger> passengers;
    double totalPaid;
    boolean checkedIn = false;

    public Booking(String pnr, User user, Flight flight, ArrayList<Passenger> passengers, double totalPaid) {
        this.pnr = pnr;
        this.user = user;
        this.flight = flight;
        this.passengers = passengers;
        this.totalPaid = totalPaid;
    }
}