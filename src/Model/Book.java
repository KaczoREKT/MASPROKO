package Model;

import Model.Enum.BookStatus;
import Model.utils.AutoIdEntity;

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

    private Reservation reservation;

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

    /**
     * Dodaje rezerwację do książki – relacja jeden-do-wielu (Book -> Reservation).
     */
    public void setReservation(Reservation r) {
        if (this.reservation != null && r != null && this.reservation != r) {
            throw new IllegalStateException("Książka jest już zarezerwowana!");
        }
        this.reservation = r;
        setStatus(r != null ? BookStatus.WYPOZYCZONA : BookStatus.DOSTEPNA);
    }

    public void removeReservation(Reservation r) {
        if (this.reservation == r) {
            this.reservation = null;
            setStatus(BookStatus.DOSTEPNA);
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
