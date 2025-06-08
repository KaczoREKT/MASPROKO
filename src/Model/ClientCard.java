package Model;

import utils.AutoIdEntity;
import utils.ObjectPlus;

import java.time.LocalDate;
import java.util.Date;

/**
 * Klasa ClientCard – przypisana do klienta (jedno do jednego).
 * Zawiera datę wygaśnięcia i flagę expired.
 */
public class ClientCard extends AutoIdEntity {
    private static final long serialVersionUID = 1L;
    private static long nextId = 1;
    private long id;
    private LocalDate expirationDate;
    private boolean expired;
    private Client client;

    public ClientCard(Client client) {
        super();
        this.id = nextId++;
        this.client = client;
        this.expirationDate = LocalDate.now().plusYears(2);
        this.expired = false;
        if (client != null) {
            client.setClientCard(this);
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
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
        return String.format("ClientCard[id=%s, expires=%s, expired=%b]",
                getPublicId(), expirationDate, expired);
    }
    @Override
    public String getPrefix() {
        return "CC";
    }
}
