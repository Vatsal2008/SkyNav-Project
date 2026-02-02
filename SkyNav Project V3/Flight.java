import java.time.LocalDate;
import java.time.LocalTime;
import java.io.Serializable;

public class Flight implements Serializable {
    String flightId, from, to, time;
    LocalDate date;
    int price, availableSeats;

    public Flight(String flightId, String from, String to, String dateStr, String time, int price, int seats) {
        this.flightId = flightId;
        this.from = from;
        this.to = to;
        this.date = LocalDate.parse(dateStr); 
        this.time = time;
        this.price = price;
        this.availableSeats = seats;
    }

    public boolean isBookable() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        if (date.isAfter(today)) return true;
        if (date.isEqual(today)) {
            LocalTime flightTime = LocalTime.parse(time);
            return flightTime.isAfter(now.plusHours(1)); 
        }
        return false;
    }
}