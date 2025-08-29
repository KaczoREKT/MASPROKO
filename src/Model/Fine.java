package Model;

import Model.Enum.FineStatus;
import Model.utils.AutoIdEntity;

import java.io.Serial;
import java.time.LocalDate;

public class Fine extends AutoIdEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    private double price;
    private final String reason;
    private FineStatus status = FineStatus.UNPAID;
    private final LocalDate date;

    private Client client;

    public Fine(double price, String reason) {
        super();
        this.price = price;
        this.reason = reason;
        this.date = LocalDate.now();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getReason() {
        return reason;
    }


    public FineStatus getStatus() {
        return status;
    }

    public void setStatus(FineStatus status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public Client getClient() {
        return client;
    }

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
                "Fine[id=%s, price=%.2f, reason=%s, status=%s, date=%s, client=%s]",
                getPublicId(), price, reason, status, date,
                client != null ? client.getPublicId() : "Brak"
        );
    }

}
