package Model;

import utils.AutoIdEntity;
import utils.ObjectPlus;

import java.time.LocalDate;
import java.util.*;

/**
 * Klasa Reservation – rezerwacja konkretnej książki przez klienta.
 * Pola: id (unikalne), data rozpoczęcia, data zakończenia.
 * Relacje: wiele do jednego z Client, wiele do jednego z Book.
 */
public class Reservation extends AutoIdEntity {
    private static final long serialVersionUID = 1L;
    private static long nextId = 1;
    private long id;
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
    public void cancel() {
        if (client != null) client.removeReservation(this);
        if (books != null) {
            for (Book b : books) {
                b.removeReservation(this);
            }
        }
        ObjectPlus.removeFromExtent(this);
    }


    @Override
    public String toString() {
        StringBuilder booksInfo = new StringBuilder();
        for (Book book : books) {
            booksInfo.append(book.getTitle())
                    .append(" ")
                    .append(book.getId())
                    .append("], ");
        }
        // Usunięcie przecinka na końcu jeśli są książki
        if (!booksInfo.isEmpty()) {
            booksInfo.setLength(booksInfo.length() - 2);
        }

        return String.format("Reservation[id=%s, client=%s, books=[%s], from=%s, to=%s]",
                getPublicId(),
                (client != null ? client.getId() : "null"),
                booksInfo,
                startDate, endDate);
    }

}
