package Model.utils;
import Model.*;
import Model.Enum.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class SampleData {
    public static void addSampleData() {
        // 1. Sektory
        List<Sector> sectors = new ArrayList<>();
        for (char start = 'A'; start <= 'Y'; start += 2) {
            sectors.add(new Sector(start, (char)(start+1)));
        }

        // 2. Książki – różne tytuły!
        List<String> adjectives = List.of(
                "Tajemniczy", "Ostatni", "Mroczny", "Ukryty", "Cichy", "Złoty", "Zaginiony", "Szkarłatny", "Wielki", "Mały", "Złowrogi"
        );
        List<String> nouns = List.of(
                "Las", "Zamek", "Krąg", "Księżyc", "Król", "Skrzat", "Klucz", "Demon", "Skarb", "Zakon", "Statek", "Bractwo", "Wilk", "Wiatr"
        );
        List<String> endings = List.of(
                "Przeznaczenia", "z Bagien", "i Siedem Wzgórz", "Ze Wschodu", "Zza Lustra", "Krainy Cieni", "Bez Twarzy", "Wieczności"
        );
        List<String> genres = List.of("Powieść", "Thriller", "Fantasy", "Sci-Fi", "Horror", "Romans", "Akcja", "Dramat");
        List<String> authors = List.of(
                "Paulo Coelho", "Carlos Ruiz Zafón", "James Patterson", "Stephen King",
                "J.K. Rowling", "George R.R. Martin", "Remigiusz Mróz", "Andrzej Sapkowski"
        );
        List<Book> books = new ArrayList<>();
        int bookCount = 60;
        Random rand = new Random(1337);
        for (int i = 1; i <= bookCount; i++) {
            String title = adjectives.get(rand.nextInt(adjectives.size())) + " " +
                    nouns.get(rand.nextInt(nouns.size())) + " " +
                    endings.get(rand.nextInt(endings.size()));
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
        new Manager("Krzysztof", "Wiśniewski", Gender.MAN, 6000.00, 2000.00);
        new Receptionist("Magdalena", "Kowalczyk", Gender.WOMAN, 3100.00);
        new Accountant("Jareczek", "Miłosny", Gender.OTHER, 5000.00);

        // 4. Klienci i karty
        List<String> imiona = List.of(
                "Jan", "Anna", "Michał", "Katarzyna", "Adam", "Agnieszka", "Bartosz", "Natalia", "Marek", "Ewa",
                "Łukasz", "Oliwia", "Mateusz", "Magda", "Piotr", "Karolina", "Paweł", "Julia", "Tomasz", "Monika",
                "Grzegorz", "Zuzanna", "Marcin", "Aleksandra", "Kamil", "Joanna", "Patryk", "Sandra", "Maciej", "Wiktoria"
        );
        List<String> nazwiska = List.of(
                "Kowalski", "Nowak", "Wiśniewski", "Wójcik", "Kowalczyk", "Kamińska", "Lewandowski", "Woźniak", "Zielińska", "Szymański",
                "Dąbrowski", "Mazur", "Krawczyk", "Piotrowska", "Grabowski", "Jankowski", "Pawłowska", "Michalski", "Król", "Wieczorek"
        );

        List<Client> clients = new ArrayList<>();
        List<ClientCard> cards = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            String imie = imiona.get(rand.nextInt(imiona.size()));
            String nazwisko = nazwiska.get(rand.nextInt(nazwiska.size()));
            Gender gender = (imie.endsWith("a")) ? Gender.WOMAN : Gender.MAN;
            String mail = imie.toLowerCase() + "." + nazwisko.toLowerCase() + i + "@example.com";
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

        // 5. Rezerwacje i mandaty (z losową datą: część przeterminowana, część aktualna/przyszła)
        Set<Book> alreadyReservedBooks = new HashSet<>();
        List<Book> notReservedBooks = new ArrayList<>(books); // na koniec będziesz mieć dostępne książki

// Ustal, ile książek chcesz zostawić wolnych, np. 20%
        int freeBooks = (int) (books.size() * 0.2);
        if (freeBooks > 0) {
            // Losowo wybierz książki, które zostaną wolne
            Collections.shuffle(notReservedBooks, rand);
            notReservedBooks = notReservedBooks.subList(0, freeBooks);
            // Usuwamy je z puli możliwych do rezerwacji
            books.removeAll(notReservedBooks);
        }

// Teraz books = książki, które mogą być rezerwowane

        for (Client client : clients) {
            int ileRezerwacji = rand.nextInt(3) + 1;
            for (int k = 0; k < ileRezerwacji; k++) {
                List<Book> availableBooks = new ArrayList<>();
                for (Book b : books) {
                    if (!alreadyReservedBooks.contains(b)) {
                        availableBooks.add(b);
                    }
                }
                if (availableBooks.isEmpty()) break;

                boolean expired = rand.nextBoolean();
                LocalDate from, to;
                if (expired) {
                    from = LocalDate.now().minusDays(rand.nextInt(80) + 20);
                    to = from.plusDays(rand.nextInt(5) + 2);
                    if (!to.isBefore(LocalDate.now())) {
                        to = LocalDate.now().minusDays(rand.nextInt(10) + 1);
                        from = to.minusDays(rand.nextInt(8) + 2);
                    }
                } else {
                    from = LocalDate.now().minusDays(rand.nextInt(10));
                    to = from.plusDays(rand.nextInt(15) + 3);
                }

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
            }
        }

        // 6. Zadania sortowania (SortingJob z godzinami)
        for (int i = 0; i < 10; i++) {
            // Losowa data (rok 2023, losowy miesiąc, losowy dzień)
            int month = rand.nextInt(12) + 1;         // 1..12
            int day = rand.nextInt(20) + 1;           // 1..20 (lepiej by było sprawdzić poprawność dnia w miesiącu)
            int hourFrom = rand.nextInt(8) + 8;       // godziny 8..15, np. start 8-15
            int minuteFrom = rand.nextInt(60);        // minuty 0..59

            LocalDateTime from = LocalDateTime.of(2023, month, day, hourFrom, minuteFrom);

            // Zakończenie po 1 do 6 godzinach (z losową minutą)
            int durationHours = rand.nextInt(6) + 1;
            int durationMinutes = rand.nextInt(60);
            LocalDateTime to = from.plusHours(durationHours).plusMinutes(durationMinutes);

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
