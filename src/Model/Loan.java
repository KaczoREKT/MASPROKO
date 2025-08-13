package Model;

import Model.Enum.LoanStatus;
import Model.utils.AutoIdEntity;

import java.time.LocalDateTime;
import java.util.Set;

public class Loan extends AutoIdEntity {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LoanStatus loanStatus;
    private Set<Book> books;
    private Client client;

    public Loan(LocalDateTime startDate, LocalDateTime endDate, LoanStatus loanStatus, Set<Book> books, Client client) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.loanStatus = loanStatus;
        this.books = books;
        this.client = client;
    }
    public LocalDateTime getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    public LocalDateTime getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public Client getClient(){
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LoanStatus getStatus() {
        return loanStatus;
    }
    public void setStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }

    public int getBooksCount(){
        return books.size();
    }

    public void cancel(){

    }

    @Override
    public String toString() {
        return String.format(
                "Loan[id=%s, client: %s %s, books: %d, from: %s, to: %s, status: %s]",
                getPublicId(),
                (client != null ? client.getFirstName() : "brak"),
                (client != null ? client.getLastName() : ""),
                (books != null ? books.size() : 0),
                startDate,
                endDate,
                loanStatus
        );
    }

}
