package Model;

import Model.Enum.ReservationStatus;
import Model.utils.AutoIdEntity;
import Model.utils.ObjectPlus;

import java.io.Serial;
import java.time.LocalDate;
import java.util.*;

/**
 * Klasa Reservation – rezerwacja konkretnej książki przez klienta.
 * Pola: id (unikalne), data rozpoczęcia, data zakończenia.
 * Relacje: wiele do jednego z Client, wiele do jednego z Book.
 */
public class Reservation extends AutoIdEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    private long id;
    private ReservationStatus status = ReservationStatus.TRWAJĄCA;
    private LocalDate startDate;
    private LocalDate endDate;

    private Set<Book> books;

    private Client client;

    public Reservation(LocalDate startDate, LocalDate endDate, Set<Book> books) {
        super();
        if (books == null || books.isEmpty()) {
            throw new IllegalArgumentException("Rezerwacja musi zawierać przynajmniej jedną książkę.");
        }
        this.startDate = startDate;
        this.endDate = endDate;
        this.books = books;
        // Dodajemy rezerwację do wszystkich książek
        for (Book book : this.books) {
            book.setReservation(this); // dodaje rezerwację i zmienia status
        }
    }

    // Gettery / settery

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<Book> getBooks() {
        return books;
    }

    /**
     * Ustawia relację Reservation -> Book (wiele-do-jednego).
     */
    public void setBooks(List<Book> books) {
    }

    public Client getClient() {
        return client;
    }

    /**
     * Ustawia relację Reservation -> Client (wiele-do-jednego).
     */
    public void setClient(Client client) {
        if (this.client != null) {
            this.client.getReservations().remove(this);
        }
        this.client = client;
        if (client != null && !client.getReservations().contains(this)) {
            client.addReservation(this);
        }
    }
    public ReservationStatus getStatus() { return status; }

    public void setStatus(ReservationStatus status) { this.status = status; }

    public void cancel() {
        setStatus(ReservationStatus.ZAKOŃCZONA);
        // Zwolnij książki (ale nie usuwaj rezerwacji z klienta!)
        if (books != null) {
            for (Book b : books) {
                b.removeReservation(this);
            }
        }
    }


    @Override
    public String toString() {
        return String.format(
                "Reservation[%s, client: %s %s, books: %d, from: %s, to: %s, status: %s]",
                getPublicId(),
                (client != null ? client.getFirstName() : "brak"),
                (client != null ? client.getLastName() : ""),
                (books != null ? books.size() : 0),
                startDate,
                endDate,
                status
        );
    }

}
