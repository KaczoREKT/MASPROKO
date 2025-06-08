package Controller;

import Model.*;
import Model.Enum.BookStatus;
import Model.utils.ObjectPlus;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReservationController {
    public void reserveBook(Book book, Client client, LocalDate dateFrom, LocalDate dateTo) throws Exception {
        if (book == null) throw new Exception("Nie wybrano książki.");
        if (client == null) throw new Exception("Nie wybrano klienta.");
        if (dateFrom == null || dateTo == null) throw new Exception("Nieprawidłowa data.");
        if (dateTo.isBefore(dateFrom)) throw new Exception("Data zakończenia przed datą rozpoczęcia!");

        // Sprawdź, czy książka jest dostępna
        if (book.getStatus() == BookStatus.WYPOZYCZONA) {
            throw new Exception("Wybrana książka jest aktualnie wypożyczona.");
        }

        // Sprawdź ważność karty klienta
        if (client.getClientCard() == null || client.getClientCard().getExpirationDate().isBefore(LocalDate.now())) {
            throw new Exception("Karta klienta jest nieważna.");
        }
        Set<Book> books = new HashSet<> (List.of(book));
        // Tworzenie rezerwacji (zakładam, że konstruktor Reservation robi wszystko co trzeba)
        Reservation reservation = new Reservation(dateFrom, dateTo, books);

        // Powiązania obustronne (jeśli nie robi tego konstruktor)
        client.addReservation(reservation);
        for (Book b : books){
            b.setReservation(reservation);
        }
    }

    public void changeReservation(Reservation selectedReservation, LocalDate dateFrom, LocalDate dateTo) throws Exception {
        if (selectedReservation == null) throw new Exception("Nie wybrano rezerwacji do zmiany!");
        if (dateFrom == null || dateTo == null) throw new Exception("Daty nie mogą być puste!");
        if (dateTo.isBefore(dateFrom)) throw new Exception("Data zakończenia przed datą rozpoczęcia!");

        // Możesz dodać dodatkową walidację, np. czy nowy termin nie koliduje z innymi rezerwacjami danej książki/klienta

        selectedReservation.setStartDate(dateFrom);
        selectedReservation.setEndDate(dateTo);
    }

    public void cancelReservation(Reservation reservation) throws Exception {
        if (reservation == null) throw new Exception("Nie wybrano rezerwacji do anulowania!");
        reservation.cancel();
    }
    // ReservationController.java
    public void generateFinesForExpiredReservations() {
        try {
            Iterable<Reservation> reservations = ObjectPlus.getExtent(Reservation.class);
            for (Reservation reservation : reservations) {
                LocalDate endDate = reservation.getEndDate();
                if (endDate.isBefore(LocalDate.now())) {
                    Client client = reservation.getClient();
                    if (client != null) {
                        boolean alreadyFined = client.getFines().stream()
                                .anyMatch(f -> f.getReason() != null && f.getReason().contains("rezerwacja " + reservation.getPublicId()));
                        if (alreadyFined) continue;

                        long daysLate = java.time.temporal.ChronoUnit.DAYS.between(endDate, LocalDate.now());
                        if (daysLate > 0) {
                            double price = daysLate * 0.50;
                            Fine fine = new Fine(price, "Opóźnienie za rezerwacja " + reservation.getPublicId());
                            fine.setClient(client);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
