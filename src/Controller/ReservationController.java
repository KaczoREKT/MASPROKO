package Controller;

import Model.*;
import Model.Enum.BookStatus;
import Model.Enum.FineStatus;
import Model.utils.ObjectPlus;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReservationController {
    public void reserveBook(List<Book> books, Client client, LocalDate dateFrom, LocalDate dateTo) throws Exception {
        if (books == null) throw new Exception("Nie wybrano książki.");
        if (client == null) throw new Exception("Nie wybrano klienta.");
        if (dateFrom == null || dateTo == null) throw new Exception("Nieprawidłowa data.");
        if (dateTo.isBefore(dateFrom)) throw new Exception("Data zakończenia przed datą rozpoczęcia!");


        if (client.getClientCard() == null || client.getClientCard().getExpirationDate().isBefore(LocalDate.now())) {
            throw new Exception("Karta klienta jest nieważna.");
        }

        Reservation reservation = new Reservation(dateFrom, dateTo, new HashSet<>(books));

        client.addReservation(reservation);
        books.forEach(b -> b.setReservation(reservation));
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

    public List<Reservation> getActiveReservations(Client client) {
        Set<Reservation> reservations = client.getReservations();
        return reservations.stream()
                .filter(r -> r.getBooks().stream().anyMatch(b -> b.getStatus().name().equals(BookStatus.LOANED.name())))
                .toList();
    }

}
