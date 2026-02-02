import java.util.Random;
import java.io.Serializable;

public class Passenger implements Serializable {
    String name, gender, seatNumber;
    int age;

    public Passenger(String name, int age, String gender, String pref) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        // Window seats end in A, Aisle seats end in C
        this.seatNumber = (new Random().nextInt(30) + 1) + (pref.equals("Window") ? "A" : "C");
    }
}