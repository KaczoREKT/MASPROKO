package Controller;

import Model.*;
import Model.Enum.ReservationStatus;
import Model.utils.ObjectPlus;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

public class ReservationController {

    public void reserveBook(List<Book> books, Client client, LocalDate dateFrom, LocalDate dateTo) throws Exception {
        if (books == null || books.isEmpty()) throw new Exception("Nie wybrano książki.");
        if (client == null) throw new Exception("Nie wybrano klienta.");
        if (dateFrom == null || dateTo == null) throw new Exception("Nieprawidłowa data.");
        if (dateTo.isBefore(dateFrom)) throw new Exception("Data zakończenia przed datą rozpoczęcia!");

        if (client.getClientCard() == null || client.getClientCard().getExpirationDate().isBefore(LocalDate.now())) {
            throw new Exception("Karta klienta jest nieważna.");
        }

        Set<Book> bookSet = new HashSet<>(books);
        client.ensureLimitsBeforeAdding(bookSet.size(), true);

        Reservation reservation = new Reservation(dateFrom, dateTo, bookSet);
        reservation.setClient(client);

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


    public void cancelExpiredReservations() {
        try {
            Iterable<Reservation> iterable = ObjectPlus.getExtent(Reservation.class);
            StreamSupport.stream(iterable.spliterator(), false)
                    .filter(r -> r.getStatus() == ReservationStatus.PENDING)
                    .filter(r -> r.getStartDate() != null)
                    .filter(Reservation::isExpired)
                    .forEach(Reservation::cancel);
        } catch (Exception _) {
        }
    }
}
