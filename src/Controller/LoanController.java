package Controller;

import Model.Book;
import Model.Enum.BookStatus;
import Model.Enum.LoanStatus;
import Model.Loan;
import Model.Client;
import Model.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class LoanController extends AbstractController<Loan>{
    public LoanController() { super(Loan.class); }

    public void loanBook(List<Book> books, Client client, LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        if (client == null) {
            throw new IllegalArgumentException("Klient nie może być null.");
        }
        if (books == null || books.isEmpty()) {
            throw new IllegalArgumentException("Musisz wybrać co najmniej jedną książkę do wypożyczenia.");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Daty wypożyczenia nie mogą być null.");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Data zakończenia wypożyczenia musi być po dacie rozpoczęcia.");
        }

        Loan loan = new Loan(startDate, endDate, LoanStatus.PENDING, new HashSet<>(books), client);
        client.addLoan(loan);
        books.forEach(b -> b.setLoan(loan));

    }

    public List<Loan> getPendingLoans(Client client) {
        Set<Loan> loans = client.getLoans();
        return loans.stream()
                .filter(r -> r.getBooks().stream().anyMatch(b -> b.getStatus().name().equals(BookStatus.LOANED.name())))
                .toList();
    }

}
