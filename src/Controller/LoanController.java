package Controller;

import Model.*;
import Model.Enum.BookStatus;
import Model.Enum.FineStatus;
import Model.Enum.LoanStatus;
import Model.Enum.ReservationStatus;
import Model.utils.ObjectPlus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoanController extends AbstractController<Loan> {
    public LoanController() { super(Loan.class); }

    public void loanBook(List<Book> books, Client client, LocalDate startDate, LocalDate endDate) throws Exception {
        if (client == null) throw new Exception("Nie wybrano klienta.");
        if (books == null || books.isEmpty()) throw new Exception("Musisz wybrać co najmniej jedną książkę do wypożyczenia.");
        if (startDate == null || endDate == null) throw new Exception("Daty wypożyczenia nie mogą być puste.");
        if (endDate.isBefore(startDate)) throw new Exception("Data zakończenia wypożyczenia musi być po dacie rozpoczęcia.");

        long activeLoans = client.getLoans().stream()
                .filter(l -> l.getStatus() != LoanStatus.ENDED)
                .count();
        if (activeLoans >= 2) {
            throw new Exception("Klient może posiadać maksymalnie 2 aktywne wypożyczenia.");
        }

        long activeReservations = client.getReservations().stream()
                .filter(r -> r.getStatus() != ReservationStatus.ENDED)
                .count();
        if (activeReservations > 2) {
            throw new Exception("Klient może posiadać maksymalnie 2 aktywne rezerwacje.");
        }

        int reservedBooks = client.getReservations().stream()
                .filter(r -> r.getStatus() != ReservationStatus.ENDED)
                .mapToInt(r -> r.getBooks().size())
                .sum();

        int loanedBooks = client.getLoans().stream()
                .filter(l -> l.getStatus() != LoanStatus.ENDED)
                .mapToInt(l -> l.getBooks().size())
                .sum();

        int incoming = books.size();
        if (reservedBooks + loanedBooks + incoming > 5) {
            throw new Exception("Łączna liczba zarezerwowanych i wypożyczonych książek nie może przekraczać 5.");
        }

        for (Book b : books) {
            if (b.getStatus() == BookStatus.LOANED) {
                throw new Exception("Książka \"" + b + "\" jest już wypożyczona.");
            }
        }

        Set<Book> set = new HashSet<>(books);
        Loan loan = new Loan(startDate, endDate, LoanStatus.PENDING, set, client);
        client.addLoan(loan);
        for (Book b : set) {
            b.setLoan(loan);
            b.setStatus(BookStatus.LOANED);
        }
    }

    public List<Loan> getPendingLoans(Client client) {
        return client.getLoans().stream()
                .filter(l -> l.getStatus() != LoanStatus.ENDED)
                .toList();
    }

    public void generateFinesForExpiredLoans() {
        try {
            Iterable<Loan> loans = ObjectPlus.getExtent(Loan.class);
            for (Loan loan : loans) {
                LocalDate endDate = loan.getEndDate();
                if (endDate.isBefore(LocalDate.now())) {
                    Client client = loan.getClient();
                    if (client != null) {
                        Fine fineForThis = client.getFines().stream()
                                .filter(f -> f.getReason() != null && f.getReason().contains("Opóźnienie za wypożyczenie " + loan.getPublicId()))
                                .findFirst()
                                .orElse(null);
                        long daysLate = ChronoUnit.DAYS.between(endDate, LocalDate.now());
                        if (daysLate > 0) {
                            double newPrice = daysLate * 0.50;
                            if (fineForThis == null) {
                                Fine fine = new Fine(newPrice, "Opóźnienie za wypożyczenie " + loan.getPublicId());
                                fine.setClient(client);
                            } else if (fineForThis.getStatus() == FineStatus.PAID) {
                                fineForThis.setPrice(newPrice);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
