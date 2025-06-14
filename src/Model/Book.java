package Model;

import Model.Enum.BookStatus;
import Model.utils.AutoIdEntity;

import java.io.Serial;

public class Book extends AutoIdEntity {
    @Serial
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
