package Model;

import Model.utils.AutoIdEntity;

import java.io.Serial;
import java.time.LocalDate;

public class ClientCard extends AutoIdEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    private LocalDate expirationDate;
    private final boolean expired;
    private Client client;

    public ClientCard(Client client) {
        super();
        this.client = client;
        this.expirationDate = LocalDate.now().plusYears(2);
        this.expired = false;
        if (client != null) {
            client.setClientCard(this);
        }
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        if (this.client == client) return;
        if (this.client != null) this.client.setClientCard(null);
        this.client = client;
        if (client != null && client.getClientCard() != this) client.setClientCard(this);
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
