package Model;

import java.util.*;

/**
 * Klasa Client – dziedziczy po Person.
 * Posiada e-mail, telefon, powiązania z rezerwacjami i opłatami (fines),
 * a także przypisaną kartę klienta (ClientCard).
 */
public class Client extends Person {
    private static final long serialVersionUID = 1L;
    private String email;
    private String phoneNumber;
    private ClientCard clientCard;
    private Set<Reservation> reservations = new HashSet<>();

    private Set<Fine> fines = new HashSet<>();

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

    /**
     * Ustawia kartę klienta; relacja jeden-do-jednego.
     */
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
        return Collections.unmodifiableSet(reservations);
    }

    public Set<Fine> getFines() {
        return Collections.unmodifiableSet(fines);
    }

    /**
     * Dodaje rezerwację do klienta – relacja jeden-do-wielu.
     */
    public void addReservation(Reservation r) {
        if (r != null) {
            reservations.add(r);
            if (r.getClient() != this) {
                r.setClient(this);
            }
        }
    }

    /**
     * Usuwa rezerwację z klienta (jeśli się takie zdarza).
     */
    public void removeReservation(Reservation r) {
        if (r != null && reservations.remove(r)) {
            r.setClient(null);
        }
    }

    /**
     * Dodaje mandat (Fine) do klienta – relacja jeden-do-wielu.
     */
    public void addFine(Fine f) {
        if (f != null) {
            fines.add(f);
            if (f.getClient() != this) {
                f.setClient(this);
            }
        }
    }

    public void removeFine(Fine f) {
        if (f != null && fines.remove(f)) {
            f.setClient(null);
        }
    }

    @Override
    public String toString() {
        return String.format("Client[id=%s, name=%s %s, email=%s, telefon=%s]",
                getPublicId(),
                getFirstName(),
                getLastName(),
                email,
                phoneNumber);
    }
}
