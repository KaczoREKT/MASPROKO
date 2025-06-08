package utils;
import Model.*;
import java.time.LocalDate;
import java.util.*;

public class SampleData {
    public static void addSampleData() {
        // 1. Sektory
        List<Sector> sectors = new ArrayList<>();
        for (char start = 'A'; start <= 'J'; start += 2) {
            sectors.add(new Sector(start, (char)(start+1)));
        }

        // 2. Książki
        List<String> genres = List.of("Powieść", "Thriller", "Fantasy", "Sci-Fi", "Horror", "Romans", "Akcja", "Dramat");
        List<String> authors = List.of(
                "Paulo Coelho", "Carlos Ruiz Zafón", "James Patterson", "Stephen King",
                "J.K. Rowling", "George R.R. Martin", "Remigiusz Mróz", "Andrzej Sapkowski"
        );
        List<Book> books = new ArrayList<>();
        int bookCount = 40;
        Random rand = new Random(1337);
        for (int i = 1; i <= bookCount; i++) {
            String title = "Książka #" + i;
            String genre = genres.get(rand.nextInt(genres.size()));
            String author = authors.get(rand.nextInt(authors.size()));
            Book book = new Book(title, genre, author);
            Sector sector = sectors.get(rand.nextInt(sectors.size()));
            book.setSector(sector);
            books.add(book);
        }

        // 3. Pracownicy
        List<Librarian> librarians = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            librarians.add(new Librarian("Librarian" + i, "Nowak", Gender.WOMAN, 3400 + 120*i, genres.get(rand.nextInt(genres.size()))));
        }
        Manager manager = new Manager("Krzysztof", "Wiśniewski", Gender.MAN, 6000.00, 2000.00);
        Receptionist receptionist = new Receptionist("Magdalena", "Kowalczyk", Gender.WOMAN, 3100.00);

        // 4. Klienci i karty
        List<Client> clients = new ArrayList<>();
        List<ClientCard> cards = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            String imie = "Jan" + i;
            String nazwisko = "Kowalski" + i;
            Gender gender = (i % 2 == 0) ? Gender.MAN : Gender.WOMAN;
            String mail = "user" + i + "@example.com";
            String phone = "123-456-" + String.format("%03d", i);
            Client client = new Client(imie, nazwisko, gender, mail, phone);
            ClientCard card = new ClientCard(client);
            // Część kart przeterminowana
            if (i % 7 == 0) {
                card.setExpirationDate(LocalDate.now().minusYears(1));
            }
            clients.add(client);
            cards.add(card);
        }

        // 5. Rezerwacje i mandaty
        Set<Book> alreadyReservedBooks = new HashSet<>();
        int reservationId = 1;
        for (Client client : clients) {
            int ileRezerwacji = rand.nextInt(3) + 1;
            for (int k = 0; k < ileRezerwacji; k++) {
                // WYBIERZ KSIĄŻKI, KTÓRE NIE SĄ JUŻ ZAREZERWOWANE
                List<Book> availableBooks = new ArrayList<>();
                for (Book b : books) {
                    if (!alreadyReservedBooks.contains(b)) {
                        availableBooks.add(b);
                    }
                }
                if (availableBooks.isEmpty()) break; // Nie ma już dostępnych książek!

                LocalDate from = LocalDate.now().minusDays(rand.nextInt(100));
                LocalDate to = from.plusDays(rand.nextInt(15) + 3);

                Set<Book> reservedBooks = new HashSet<>();
                int ileBooks = 1 + rand.nextInt(2);
                for (int z = 0; z < ileBooks && !availableBooks.isEmpty(); z++) {
                    Book b = availableBooks.remove(rand.nextInt(availableBooks.size()));
                    reservedBooks.add(b);
                    alreadyReservedBooks.add(b);
                }
                if (reservedBooks.isEmpty()) continue;

                Reservation reservation = new Reservation(from, to, reservedBooks);
                reservation.setClient(client);

                // 50% szans na mandat
                if (rand.nextBoolean()) {
                    Fine fine = new Fine(10.0 + rand.nextInt(50), "Zwłoka " + reservationId, rand.nextInt(2));
                    fine.setClient(client);
                }
                reservationId++;
            }
        }

        // 6. Zadania sortowania
        for (int i = 0; i < 10; i++) {
            LocalDate from = LocalDate.of(2023, rand.nextInt(12)+1, rand.nextInt(20)+1);
            LocalDate to = from.plusDays(rand.nextInt(6)+1);
            SortingJob job = new SortingJob(from, to);
            job.setLibrarian(librarians.get(rand.nextInt(librarians.size())));
            job.setSector(sectors.get(rand.nextInt(sectors.size())));
        }

        // 7. Debug – podsumowanie
        System.out.println("[DEBUG] Dodano przykładowych sektorów: " + sectors.size());
        System.out.println("[DEBUG] Dodano przykładowych książek: " + books.size());
        System.out.println("[DEBUG] Dodano bibliotekarzy: " + librarians.size());
        System.out.println("[DEBUG] Dodano klientów: " + clients.size());
        System.out.println("[DEBUG] Dodano kart: " + cards.size());
    }
}