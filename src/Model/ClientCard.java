package Model;

import utils.ObjectPlus;

import java.util.Date;

/**
 * Klasa ClientCard – przypisana do klienta (jedno do jednego).
 * Zawiera datę wygaśnięcia i flagę expired.
 */
public class ClientCard extends ObjectPlus {
    private static final long serialVersionUID = 1L;

    private long id;               // unikalny identyfikator karty
    private Date expirationDate;   // data wygaśnięcia
    private boolean expired;       // flaga czy karta już wygasła

    // wskazanie, do którego Client-a należy ta karta
    private Client client;

    public ClientCard(long id, Date expirationDate, boolean expired) {
        super();
        this.id = id;
        this.expirationDate = expirationDate;
        this.expired = expired;
    }

    // Gettery / settery

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public Client getClient() {
        return client;
    }

    /**
     * Ustawia powiązanie karty z klientem – relacja jeden-do-jednego.
     */
    public void setClient(Client client) {
        if (this.client != null) {
            this.client.setClientCard(null);
        }
        this.client = client;
        if (client != null && client.getClientCard() != this) {
            client.setClientCard(this);
        }
    }

    @Override
    public String toString() {
        return String.format("ClientCard[id=%d, expires=%s, expired=%b]",
                id, expirationDate, expired);
    }
}
