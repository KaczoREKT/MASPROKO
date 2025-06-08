package Model;

import Model.Enum.Gender;
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

    public Set<Fine> getFines() {
        return fines;
    }

    public void addReservation(Reservation r) {
        if (r == null) return;
        reservations.add(r);
        if (r.getClient() != this) r.setClient(this);
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
        return sb.toString();
    }

}
