package Test;

import Controller.ReservationController;
import Model.*;
import Model.Enum.Gender;
import Model.Enum.BookStatus;

import java.time.LocalDate;
import java.util.Set;

public class ReservationControllerTest {
    public static void runTests() {
        System.out.println("=== ReservationControllerTest ===");
        ReservationController reservationController = new ReservationController();
        Sector sector = new Sector('C', 'D');
        Book book = new Book("W pustyni i w puszczy", "Przygoda", "Sienkiewicz");
        book.setSector(sector);

        Client client = new Client("Staś", "Tarkowski", Gender.MAN, "stas@szkolny.pl", "123-456-789");
        new ClientCard(client);

        // 1. Udana rezerwacja
        try {
            reservationController.reserveBook(book, client, LocalDate.now(), LocalDate.now().plusDays(7));
            assert !client.getReservations().isEmpty() : "Rezerwacja nie została utworzona!";
            System.out.println("Rezerwacje klienta: " + client.getReservations());
        } catch (Exception e) {
            System.out.println("Błąd rezerwacji: " + e.getMessage());
        }

        // 2. Próba ponownej rezerwacji tej samej książki (powinno się nie udać)
        try {
            reservationController.reserveBook(book, client, LocalDate.now(), LocalDate.now().plusDays(5));
            assert false : "Książka nie powinna być dostępna do ponownej rezerwacji!";
        } catch (Exception e) {
            System.out.println("Poprawnie wykryto błąd przy podwójnej rezerwacji: " + e.getMessage());
        }

        // 3. Próba rezerwacji z niepoprawnym zakresem dat
        Book book2 = new Book("Krzyżacy", "Przygoda", "Sienkiewicz");
        book2.setSector(sector);
        try {
            reservationController.reserveBook(book2, client, LocalDate.now().plusDays(10), LocalDate.now());
            assert false : "Powinien być błąd zakresu dat!";
        } catch (Exception e) {
            System.out.println("Poprawnie wykryto błąd zakresu dat: " + e.getMessage());
        }

        // 4. Sprawdzenie statusu książki po rezerwacji
        assert book.getStatus() == BookStatus.WYPOZYCZONA : "Status książki po rezerwacji powinien być WYPOZYCZONA!";

        // 5. Zakończenie rezerwacji i sprawdzenie, czy książka wraca do dostępnych
        Set<Reservation> reservations = client.getReservations();
        if (!reservations.isEmpty()) {
            Reservation r = reservations.iterator().next();
            r.cancel();  // Zakładamy, że ta metoda zwalnia książki
            assert book.getStatus() != BookStatus.WYPOZYCZONA : "Książka powinna wrócić do dostępnych po anulowaniu!";
            System.out.println("Książka po anulowaniu rezerwacji: " + book.getStatus());
        }

        // ---- TEST GENEROWANIA KAR ----
        // 1. Tworzymy przeterminowaną rezerwację
        Book expiredBook = new Book("Stara księga", "Fantasy", "Author");
        expiredBook.setSector(sector);
        try {
            reservationController.reserveBook(expiredBook, client, LocalDate.now().minusDays(10), LocalDate.now().minusDays(7));
            System.out.println("Przeterminowana rezerwacja dodana.");
        } catch (Exception e) {
            System.out.println("Błąd przy dodawaniu przeterminowanej rezerwacji: " + e.getMessage());
        }

        // 2. Wywołaj generowanie kar
        reservationController.generateFinesForExpiredReservations();

        // 3. Sprawdź, czy kara została dodana
        boolean fineAdded = client.getFines().stream()
                .anyMatch(f -> f.getReason().contains("rezerwacja"));
        assert fineAdded : "Nie wygenerowano kary za przeterminowaną rezerwację!";
        System.out.println("Kara wygenerowana: " + client.getFines());

        // 4. Sprawdź, czy kara się zaktualizuje gdy nadal NIEOPŁACONA
        var fine = client.getFines().iterator().next();
        double firstAmount = fine.getPrice();
        // Udajemy, że czas minął — zwiększamy różnicę w dniach
        expiredBook.setStatus(BookStatus.WYPOZYCZONA); // Trzyma status wypożyczonej
        reservationController.generateFinesForExpiredReservations();
        double newAmount = fine.getPrice();
        assert newAmount >= firstAmount : "Kara powinna rosnąć jeśli jest nieopłacona!";
        System.out.println("Zaktualizowana kara: " + fine);

        // 5. Oznacz karę jako OPLACONĄ i sprawdź, że się nie aktualizuje
        fine.setStatus(Model.Enum.FineStatus.OPLACONO);
        double paidAmount = fine.getPrice();
        reservationController.generateFinesForExpiredReservations();
        assert fine.getPrice() == paidAmount : "Kara nie powinna rosnąć po opłaceniu!";
        System.out.println("Kara po opłaceniu (nie powinna rosnąć): " + fine);

        System.out.println("ReservationControllerTest OK\n");
    }
}
