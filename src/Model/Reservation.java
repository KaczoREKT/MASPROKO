package Model;

import Model.Enum.ReservationStatus;
import Model.utils.AutoIdEntity;

import java.io.Serial;
import java.time.LocalDate;
import java.util.*;

public class Reservation extends AutoIdEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    private ReservationStatus status = ReservationStatus.PENDING;
    private LocalDate startDate;
    private LocalDate endDate;

    private final Set<Book> books;

    private Client client;

    public Reservation(LocalDate startDate, LocalDate endDate, Set<Book> books) {
        super();
        if (books == null || books.isEmpty()) {
            throw new IllegalArgumentException("Rezerwacja musi zawierać przynajmniej jedną książkę.");
        }
        this.startDate = startDate;
        this.endDate = endDate;
        this.books = books;
        for (Book book : this.books) {
            book.setReservation(this);
        }
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        if (this.client == client) return;
        if (this.client != null) this.client.getReservations().remove(this);
        this.client = client;
        if (client != null) client.addReservation(this);
    }

    public ReservationStatus getStatus() { return status; }

    public void setStatus(ReservationStatus status) { this.status = status; }

    public void cancel() {
        setStatus(ReservationStatus.ENDED);
        if (books != null) {
            books.forEach(b -> b.removeReservation(this));
        }
    }



    @Override
    public String toString() {
        return String.format(
                "Reservation[id=%s, client: %s %s, books: %d, from: %s, to: %s, status: %s]",
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
