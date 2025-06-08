package Model;

import Model.Enum.FineStatus;
import Model.utils.AutoIdEntity;

import java.io.Serial;

/**
 * Klasa Fine (mandat) wystawiana klientowi.
 * Pola: price (kwota), reason (powód), status (np. 0 = nieopłacony, 1 = opłacony itp.).
 * Relacja: wiele-do-jednego z Client.
 */
public class Fine extends AutoIdEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    private double price;
    private String reason;
    private FineStatus status = FineStatus.NIEOPLACONO;

    private Client client;

    public Fine(double price, String reason) {
        super();
        this.price = price;
        this.reason = reason;
    }

    // Gettery / settery

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public FineStatus getStatus() {
        return status;
    }

    /**
     * Ustawia status mandatu (np. 0 = nieopłacony, 1 = opłacony).
     */
    public void setStatus(FineStatus status) {
        this.status = status;
    }

    public Client getClient() {
        return client;
    }

    /**
     * Relacja Fine -> Client (wiele-do-jednego).
     */
    public void setClient(Client client) {
        if (this.client != null) {
            this.client.getFines().remove(this);
        }
        this.client = client;
        if (client != null && !client.getFines().contains(this)) {
            client.addFine(this);
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Fine[price=%.2f, reason=%s, status=%s, client=%s]",
                price, reason, status,
                client != null ? client.getPublicId() : "Brak"
        );
    }


}
