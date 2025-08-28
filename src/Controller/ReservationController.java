package Controller;

import Model.*;
import Model.Enum.BookStatus;
import Model.Enum.FineStatus;
import Model.Enum.LoanStatus;
import Model.Enum.ReservationStatus;
import Model.utils.ObjectPlus;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReservationController {

    public void reserveBook(List<Book> books, Client client, LocalDate dateFrom, LocalDate dateTo) throws Exception {
        // Walidacje podstawowe
        if (books == null || books.isEmpty()) throw new Exception("Nie wybrano książki.");
        if (client == null) throw new Exception("Nie wybrano klienta.");
        if (dateFrom == null || dateTo == null) throw new Exception("Nieprawidłowa data.");
        if (dateTo.isBefore(dateFrom)) throw new Exception("Data zakończenia przed datą rozpoczęcia!");

        // Ważność karty
        if (client.getClientCard() == null || client.getClientCard().getExpirationDate().isBefore(LocalDate.now())) {
            throw new Exception("Karta klienta jest nieważna.");
        }

        // 1) Limit aktywnych rezerwacji = 2
        long activeReservations = client.getReservations().stream()
                .filter(r -> r.getStatus() != ReservationStatus.ENDED)
                .count();
        if (activeReservations >= 2) {
            throw new Exception("Klient może posiadać maksymalnie 2 aktywne rezerwacje. Obecnie jest ich " + activeReservations + ".");
        }

        // 2) Limit aktywnych wypożyczeń = 2
        long activeLoans = client.getLoans().stream()
                .filter(l -> l.getStatus() != LoanStatus.ENDED) // dopasuj do własnej nazwy statusu zwrócone/zakończone
                .count();
        if (activeLoans >= 2) {
            throw new Exception("Klient może posiadać maksymalnie 2 aktywne wypożyczenia.");
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

        // 5) Utworzenie rezerwacji i powiązania z klientem oraz książkami
        Set<Book> set = new HashSet<>(books);
        Reservation reservation = new Reservation(dateFrom, dateTo, set);
        reservation.setClient(client); // podłączy obustronnie zgodnie z logiką w encjach

        // W Reservation(Book) konstruktor wywołuje book.setReservation(this), ale jeżeli nie,
        // to zapewnijmy powiązanie:
        for (Book b : set) {
            b.setReservation(reservation);
        }
    }

    public void changeReservation(Reservation selectedReservation, LocalDate dateFrom, LocalDate dateTo) throws Exception {
        if (selectedReservation == null) throw new Exception("Nie wybrano rezerwacji do zmiany!");
        if (dateFrom == null || dateTo == null) throw new Exception("Daty nie mogą być puste!");
        if (dateTo.isBefore(dateFrom)) throw new Exception("Data zakończenia przed datą rozpoczęcia!");

        selectedReservation.setStartDate(dateFrom);
        selectedReservation.setEndDate(dateTo);
    }

    public void cancelReservation(Reservation reservation) throws Exception {
        if (reservation == null) throw new Exception("Nie wybrano rezerwacji do anulowania!");
        reservation.cancel();
    }


    public void generateFinesForExpiredReservations() {
        try {
            Iterable<Reservation> reservations = ObjectPlus.getExtent(Reservation.class);
            for (Reservation reservation : reservations) {
                LocalDate endDate = reservation.getEndDate();
                if (endDate.isBefore(LocalDate.now())) {
                    Client client = reservation.getClient();
                    if (client != null) {
                        // Szukamy istniejącej kary za tę rezerwację
                        Fine fineForThis = client.getFines().stream()
                                .filter(f -> f.getReason() != null && f.getReason().contains("rezerwacja " + reservation.getPublicId()))
                                .findFirst()
                                .orElse(null);
                        long daysLate = java.time.temporal.ChronoUnit.DAYS.between(endDate, LocalDate.now());
                        if (daysLate > 0) {
                            double newPrice = daysLate * 0.50;
                            if (fineForThis == null) {
                                // Nie było jeszcze kary — dodaj nową
                                Fine fine = new Fine(newPrice, "Opóźnienie za rezerwacja " + reservation.getPublicId());
                                fine.setClient(client);
                            } else if (fineForThis.getStatus() == FineStatus.OPLACONO) {
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

    // "Aktywne" = nie ENDED. Alternatywnie można przyjąć: aktywne to takie, które mają PENDING/CONFIRMED itp.
    public List<Reservation> getActiveReservations(Client client) {
        return client.getReservations().stream()
                .filter(r -> r.getStatus() != ReservationStatus.ENDED)
                .toList();
    }
}
