package Model;

import utils.AutoIdEntity;
import utils.ObjectPlus;

import java.util.*;

/**
 * Klasa Book – każda instancja to jedna książka.
 * Pola: id, title, genre, author, status (BookStatus).
 * Relacje:
 *   - relacja jeden-do-wielu z Rezerwacją (Reservation) – jedna książka może mieć wiele rezerwacji.
 *   - relacja wiele-do-jednego z Sector – jedna książka należy do dokładnie jednego sektora.
 */
public class Book extends AutoIdEntity {
    private static final long serialVersionUID = 1L;
    private String title;
    private String genre;
    private String author;

    private BookStatus status = BookStatus.DOSTEPNA;

    // Rezerwacje na tę książkę
    private List<Reservation> reservations = new ArrayList<>();

    // Do jakiego sektora należy
    private Sector sector;

    public Book(String title, String genre, String author) {
        super();
        this.title = title;
        this.genre = genre;
        this.author = author;
    }

    // Gettery / settery

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public List<Reservation> getReservations() {
        return Collections.unmodifiableList(reservations);
    }

    /**
     * Dodaje rezerwację do książki – relacja jeden-do-wielu (Book -> Reservation).
     */
    public void addReservation(Reservation r) {
        if (r != null && !reservations.contains(r)) {
            reservations.add(r);
            if (r.getBook() != this) {
                r.setBook(this);
            }
        }
    }

    public void removeReservation(Reservation r) {
        if (r != null && reservations.remove(r)) {
            r.setBook(null);
        }
    }

    public Sector getSector() {
        return sector;
    }

    /**
     * Ustawia sektora, do którego należy książka (relacja wiele-do-jednego).
     */
    public void setSector(Sector sector) {
        if (this.sector != null) {
            this.sector.getBooks().remove(this);
        }
        this.sector = sector;
        if (sector != null && !sector.getBooks().contains(this)) {
            sector.addBook(this);
        }
    }

    @Override
    public String toString() {
        return String.format("Book[id=%s, title=%s, genre=%s, author=%s, status=%s]",
                getPublicId(),
                title,
                genre,
                author,
                status);
    }
}
