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
        // Walidacje podstawowe
        if (client == null) throw new Exception("Nie wybrano klienta.");
        if (books == null || books.isEmpty()) throw new Exception("Musisz wybrać co najmniej jedną książkę do wypożyczenia.");
        if (startDate == null || endDate == null) throw new Exception("Daty wypożyczenia nie mogą być puste.");
        if (endDate.isBefore(startDate)) throw new Exception("Data zakończenia wypożyczenia musi być po dacie rozpoczęcia.");

        // 1) Limit aktywnych wypożyczeń = 2
        long activeLoans = client.getLoans().stream()
                .filter(l -> l.getStatus() != LoanStatus.ENDED) // dopasuj do faktycznego statusu końca wypożyczenia
                .count();
        if (activeLoans >= 2) {
            throw new Exception("Klient może posiadać maksymalnie 2 aktywne wypożyczenia.");
        }

        // 2) Limit aktywnych rezerwacji = 2 (nie tworzymy rezerwacji, ale limit dotyczy klienta globalnie)
        long activeReservations = client.getReservations().stream()
                .filter(r -> r.getStatus() != ReservationStatus.ENDED)
                .count();
        if (activeReservations > 2) {
            // >2 to już stan nielegalny; jeśli chcesz egzekwować „nie więcej niż 2”, zablokuj także ==2
            throw new Exception("Klient może posiadać maksymalnie 2 aktywne rezerwacje.");
        }

        // 3) Limit łącznej liczby książek (rezerwacje + wypożyczenia) <= 5
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

        // 4) Dostępność książek do wypożyczenia
        for (Book b : books) {
            // Jeśli masz statusy, upewnij się, że książka nie jest aktualnie wypożyczona
            if (b.getStatus() == BookStatus.LOANED) {
                throw new Exception("Książka \"" + b + "\" jest już wypożyczona.");
            }
            // Opcjonalnie zablokuj, gdy zarezerwowana dla kogoś innego
            // if (b.getReservation() != null && b.getReservation().getStatus() != ReservationStatus.ENDED
            //     && !b.getReservation().getClient().equals(client)) {
            //     throw new Exception("Książka \"" + b + "\" jest zarezerwowana przez innego klienta.");
            // }
        }

        // 5) Utworzenie wypożyczenia i powiązania
        Set<Book> set = new HashSet<>(books);
        Loan loan = new Loan(startDate, endDate, LoanStatus.PENDING, set, client); // ustaw status startowy zgodnie z Twoją logiką
        client.addLoan(loan);
        for (Book b : set) {
            b.setLoan(loan);
            b.setStatus(BookStatus.LOANED); // jeśli status jest utrzymywany w Book
        }
    }

    // „Aktywne” = nie RETURNED; dopasuj do swojego enumu (np. ACTIVE, OVERDUE itp.)
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
