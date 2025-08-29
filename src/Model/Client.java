package Model;

import Model.Enum.Gender;
import Model.Enum.LoanStatus;
import Model.Enum.ReservationStatus;
import Model.utils.AutoIdEntity;

import java.io.Serial;
import java.util.*;

public class Client extends Person {
    @Serial
    private static final long serialVersionUID = 1L;
    private String email;
    private String phoneNumber;
    private ClientCard clientCard;
    private final Set<Reservation> reservations = new HashSet<>();
    private final Set<Loan> loans = new HashSet<>();

    private final Set<Fine> fines = new HashSet<>();

    public Client(String firstName, String lastName, Gender gender,
                  String email, String telefon) {
        super(firstName, lastName, gender);
        this.email = email;
        this.phoneNumber = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ClientCard getClientCard() {
        return clientCard;
    }
    public String getClientCardId() {
        return clientCard.getPublicId();
    }

    public void setClientCard(ClientCard clientCard) {
        if (this.clientCard != null) {
            this.clientCard.setClient(null);
        }
        this.clientCard = clientCard;
        if (clientCard != null && clientCard.getClient() != this) {
            clientCard.setClient(this);
        }
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public Set<Loan> getLoans() { return loans; }

    public Set<Fine> getFines() {
        return fines;
    }

    private long countActiveReservations() {
        return reservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.PENDING)
                .count();
    }

    private long countActiveLoans() {
        return loans.stream()
                .filter(l -> l.getStatus() == LoanStatus.PENDING)
                .count();
    }

    private int countActiveReservedBooks() {
        return reservations.stream()
                .filter(r -> r.getStatus() != ReservationStatus.ENDED)
                .mapToInt(r -> r.getBooks() != null ? r.getBooks().size() : 0)
                .sum();
    }

    private int countActiveLoanedBooks() {
        return loans.stream()
                .filter(l -> l.getStatus() != LoanStatus.ENDED)
                .mapToInt(l -> l.getBooks() != null ? l.getBooks().size() : 0)
                .sum();
    }

    public void ensureLimitsBeforeAdding(int incomingBooks, boolean addingReservation) {
        if (addingReservation) {
            if (countActiveReservations() >= 2) {
                throw new IllegalStateException("Klient może posiadać maksymalnie 2 aktywne rezerwacje.");
            }
        } else {
            if (countActiveLoans() >= 2) {
                throw new IllegalStateException("Klient może posiadać maksymalnie 2 aktywne wypożyczenia.");
            }
        }
        int totalBooks = countActiveReservedBooks() + countActiveLoanedBooks() + incomingBooks;
        if (totalBooks > 5) {
            throw new IllegalStateException("Łączna liczba zarezerwowanych i wypożyczonych książek nie może przekraczać 5.");
        }
    }

    // --- METODY DODAWANIA Z WALIDACJĄ NIEZMIENNIKÓW ---
    public void addReservation(Reservation r) {
        if (r == null) return;
        int incoming = (r.getBooks() != null) ? r.getBooks().size() : 0;
        ensureLimitsBeforeAdding(incoming, true);

        reservations.add(r);
        if (r.getClient() != this) r.setClient(this);
    }

    public void addLoan(Loan l) {
        if (l == null) return;
        int incoming = (l.getBooks() != null) ? l.getBooks().size() : 0;
        ensureLimitsBeforeAdding(incoming, false);

        loans.add(l);
        if (l.getClient() != this) l.setClient(this);
    }



    public void addFine(Fine f) {
        if (f == null) return;
        fines.add(f);
        if (f.getClient() != this) f.setClient(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Client[id=%s, name=%s %s, email=%s, telefon=%s]",
                getPublicId(),
                getFirstName(),
                getLastName(),
                email,
                phoneNumber));
        if (!reservations.isEmpty()) {
            sb.append(", reservations=[");
            String joined = reservations.stream()
                    .map(AutoIdEntity::getPublicId)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            sb.append(joined).append("]");
        } else {
            sb.append(", reservations=[]");
        }
        if (!loans.isEmpty()) {
            sb.append(", loans=[");
            String joined = loans.stream()
                    .map(AutoIdEntity::getPublicId)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            sb.append(joined).append("]");
        } else {
            sb.append(", loans=[]");
        }
        return sb.toString();
    }

}
