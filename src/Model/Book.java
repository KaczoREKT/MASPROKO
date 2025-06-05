package Model;
import java.util.List;

public class Book {

    private int id;

    private String title;
    private String genre;
    private String author; // dodano pole author

    private BookStatus status;

    private List<Reservation> reservations;

    private Sector sector;
}
