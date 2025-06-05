package Model;


import java.util.HashSet;
import java.util.Set;

public class Reservation {
    private Long id;

    private int startDate;
    private int endDate;

    private Client client;
    private Set<Book> books = new HashSet<>();
}
