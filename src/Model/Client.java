package Model;


import java.util.HashSet;
import java.util.Set;

public class Client extends Person {
    private String email;

    private String telefon;

    private Set<Reservation> reservations = new HashSet<>();

    private Set<Fine> fines = new HashSet<>();
}
