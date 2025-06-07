package Controller;

import Model.Book;
import Model.BookStatus;
import Model.Client;
import Model.Reservation;
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
}
