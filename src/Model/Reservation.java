package Model;

import utils.ObjectPlus;

import java.util.Date;

/**
 * Klasa Reservation – rezerwacja konkretnej książki przez klienta.
 * Pola: id (unikalne), data rozpoczęcia, data zakończenia.
 * Relacje: wiele do jednego z Client, wiele do jednego z Book.
 */
public class Reservation extends ObjectPlus {
    private static final long serialVersionUID = 1L;

    private long id;
    private Date startDate;
    private Date endDate;

    // Do jakiej książki się odnosi rezerwacja
    private Book book;

    // Kto wykonał tę rezerwację
    private Client client;

    public Reservation(long id, Date startDate, Date endDate) {
        super();
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Gettery / settery

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Book getBook() {
        return book;
    }

    /**
     * Ustawia relację Reservation -> Book (wiele-do-jednego).
     */
    public void setBook(Book book) {
        if (this.book != null) {
            this.book.getReservations().remove(this);
        }
        this.book = book;
        if (book != null && !book.getReservations().contains(this)) {
            book.addReservation(this);
        }
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

    @Override
    public String toString() {
        return String.format("Reservation[id=%d, klient=%s, bookID=%s, from=%s, to=%s]",
                id,
                (client != null ? client.getId() : "null"),
                (book != null ? book.getId() : "null"),
                startDate, endDate);
    }
}
