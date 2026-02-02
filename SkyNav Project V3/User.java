import java.io.Serializable;

public class User implements Serializable {
    static int userCounter = 1000;
    int userId;
    String name, email, phone, dob, password;

    public User(String name, String email, String phone, String dob, String password) {
        this.userId = ++userCounter;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.password = password;
    }
}