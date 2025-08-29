import Model.*;
import Model.Enum.BookStatus;
import Model.Enum.Gender;
import Model.Enum.LoanStatus;
import Model.Enum.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class SampleData {

    public static void addSampleData() {
        Random rand = new Random(1337);

        // 1) Sektory
        List<Sector> sectors = new ArrayList<>();
        for (char start = 'A'; start <= 'Y'; start += 2) {
            sectors.add(new Sector(start, (char) (start + 1)));
        }

        // 2) Książki
        List<String> adjectives = List.of("Tajemniczy", "Ostatni", "Mroczny", "Ukryty", "Cichy", "Złoty", "Zaginiony", "Szkarłatny", "Wielki", "Mały", "Złowrogi");
        List<String> nouns = List.of("Las", "Zamek", "Krąg", "Księżyc", "Król", "Skrzat", "Klucz", "Demon", "Skarb", "Zakon", "Statek", "Bractwo", "Wilk", "Wiatr");
        List<String> endings = List.of("Przeznaczenia", "z Bagien", "i Siedem Wzgórz", "Ze Wschodu", "Zza Lustra", "Krainy Cieni", "Bez Twarzy", "Wieczności");
        List<String> genres = List.of("Powieść", "Thriller", "Fantasy", "Sci-Fi", "Horror", "Romans", "Akcja", "Dramat");
        List<String> authors = List.of("Paulo Coelho", "Carlos Ruiz Zafón", "James Patterson", "Stephen King", "J.K. Rowling", "George R.R. Martin", "Remigiusz Mróz", "Andrzej Sapkowski");

        int bookCount = 60;
        List<Book> books = new ArrayList<>(bookCount);

        for (int i = 0; i < bookCount; i++) {
            String title = adjectives.get(rand.nextInt(adjectives.size())) + " " +
                    nouns.get(rand.nextInt(nouns.size())) + " " +
                    endings.get(rand.nextInt(endings.size()));
            String genre = genres.get(rand.nextInt(genres.size()));
            String author = authors.get(rand.nextInt(authors.size()));

            Book book = new Book(title, genre, author);
            // sektor wg pierwszej litery
            char first = Character.toUpperCase(title.charAt(0));
            for (Sector s : sectors) {
                if (first >= s.getStartLetter() && first <= s.getEndLetter()) {
                    book.setSector(s);
                    break;
                }
            }
            book.setStatus(BookStatus.AVAILABLE);
            books.add(book);
        }

        // 3) Pracownicy
        List<Librarian> librarians = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            librarians.add(new Librarian("Librarian" + i, "Nowak", Gender.WOMAN, 3400 + 120 * i, genres.get(rand.nextInt(genres.size()))));
        }
        new Manager("Krzysztof", "Wiśniewski", Gender.MAN, 6000.00, 2000.00);
        new Accountant("Jareczek", "Miłosny", Gender.OTHER, 5000.00);

        // 4) Klienci + karty
        List<String> imiona = List.of("Jan", "Anna", "Michał", "Katarzyna", "Adam", "Agnieszka", "Bartosz", "Natalia", "Marek", "Ewa",
                "Łukasz", "Oliwia", "Mateusz", "Magda", "Piotr", "Karolina", "Paweł", "Julia", "Tomasz", "Monika",
                "Grzegorz", "Zuzanna", "Marcin", "Aleksandra", "Kamil", "Joanna", "Patryk", "Sandra", "Maciej", "Wiktoria");
        List<String> nazwiska = List.of("Kowalski", "Nowak", "Wiśniewski", "Wójcik", "Kowalczyk", "Kamińska", "Lewandowski", "Woźniak", "Zielińska", "Szymański",
                "Dąbrowski", "Mazur", "Krawczyk", "Piotrowska", "Grabowski", "Jankowski", "Pawłowska", "Michalski", "Król", "Wieczorek");

        List<Client> clients = new ArrayList<>();
        List<ClientCard> cards = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            String imie = imiona.get(rand.nextInt(imiona.size()));
            String nazwisko = nazwiska.get(rand.nextInt(nazwiska.size()));
            Gender gender = (imie.endsWith("a")) ? Gender.WOMAN : Gender.MAN;
            Client client = new Client(imie, nazwisko, gender, (imie + "." + nazwisko + i + "@example.com").toLowerCase(), "123-456-" + String.format("%03d", i));

            ClientCard card = new ClientCard(client);
            if (i % 7 == 0) {
                card.setExpirationDate(LocalDate.now().minusYears(1)); // kilka przeterminowanych
            }
            clients.add(client);
            cards.add(card);
        }

        // 5) Pozostaw ~20% książek całkiem wolnych
        int freeTarget = (int) (books.size() * 0.2);
        List<Book> shuffled = new ArrayList<>(books);
        Collections.shuffle(shuffled, rand);
        Set<Book> alwaysFree = shuffled.stream().limit(freeTarget).collect(Collectors.toSet());

        // 6) Tworzenie rezerwacji i wypożyczeń z limitem:
        // aktywne = PENDING; ENDED/CANCELED nie liczą się do limitów
        for (Client client : clients) {
            // Rezerwacje: 0..2, z poszanowaniem budżetu książek (≤5 razem z loans)
            int desiredReservations = rand.nextInt(3); // 0..2
            for (int r = 0; r < desiredReservations; r++) {

                int remainingBookCap = remainingBookCapacity(client);
                if (remainingBookCap <= 0) break;

                List<Book> candidates = books.stream()
                        .filter(b -> !alwaysFree.contains(b))
                        .filter(b -> b.getLoan() == null) // nie wypożyczona
                        .filter(b -> b.getReservation() == null) // nie zarezerwowana przez kogoś aktywnie
                        .filter(b -> b.getStatus() == BookStatus.AVAILABLE)
                        .collect(Collectors.toList());
                if (candidates.isEmpty()) break;

                int howMany = Math.min(remainingBookCap, 1 + rand.nextInt(2)); // 1..2
                Collections.shuffle(candidates, rand);
                Set<Book> picked = new HashSet<>(candidates.subList(0, Math.min(howMany, candidates.size())));
                if (picked.isEmpty()) break;

                boolean makeEnded = rand.nextBoolean(); // część skończona
                LocalDate from, to;
                if (makeEnded) {
                    from = LocalDate.now().minusDays(rand.nextInt(60) + 10);
                    to = from.plusDays(rand.nextInt(5) + 3);
                    if (!to.isBefore(LocalDate.now())) {
                        to = LocalDate.now().minusDays(rand.nextInt(7) + 1);
                        from = to.minusDays(rand.nextInt(10) + 3);
                    }
                } else {
                    from = LocalDate.now().minusDays(rand.nextInt(5));
                    to = from.plusDays(rand.nextInt(14) + 3);
                }

                Reservation res = new Reservation(from, to, picked);
                res.setClient(client);
                res.setStatus(makeEnded ? ReservationStatus.ENDED : ReservationStatus.PENDING);

                // statusy książek
                for (Book b : picked) {
                    if (res.getStatus() == ReservationStatus.PENDING) {
                        b.setReservation(res);
                        b.setStatus(BookStatus.RESERVED);
                    } else {
                        b.setReservation(null);
                        if (b.getLoan() == null) b.setStatus(BookStatus.AVAILABLE);
                    }
                }
            }

            int desiredLoans = rand.nextInt(3); // 0..2
            for (int l = 0; l < desiredLoans; l++) {
                int remainingBookCap = remainingBookCapacity(client);
                if (remainingBookCap <= 0) break;

                List<Book> candidates = books.stream()
                        .filter(b -> b.getLoan() == null)
                        .filter(b -> (b.getReservation() == null && b.getStatus() == BookStatus.AVAILABLE)
                                || (b.getReservation() != null
                                && b.getReservation().getClient() == client
                                && b.getReservation().getStatus() == ReservationStatus.PENDING))
                        .collect(Collectors.toList());
                if (candidates.isEmpty()) break;

                int howMany = Math.min(remainingBookCap, 1 + rand.nextInt(2));
                Collections.shuffle(candidates, rand);
                Set<Book> picked = new HashSet<>(candidates.subList(0, Math.min(howMany, candidates.size())));
                if (picked.isEmpty()) break;

                LocalDate start = LocalDate.now().minusDays(rand.nextInt(10)); // start do 10 dni temu
                LocalDate end;

                if (rand.nextInt(10) < 3) { // ~30% będzie przeterminowane
                    end = LocalDate.now().minusDays(1 + rand.nextInt(14));
                    if (!end.isAfter(start)) {
                        start = end.minusDays(rand.nextInt(7) + 1);
                    }
                } else {
                    end = start.plusDays(rand.nextInt(22) + 7);
                }

                Loan loan = new Loan(start, end, LoanStatus.PENDING, picked, client);
                client.addLoan(loan);

                for (Book b : picked) {
                    // jeśli była rezerwacja tego klienta -> zakończ ją
                    if (b.getReservation() != null && b.getReservation().getClient() == client
                            && b.getReservation().getStatus() == ReservationStatus.PENDING) {
                        b.getReservation().setStatus(ReservationStatus.ENDED);
                        b.setReservation(null);
                    }
                    b.setLoan(loan);
                    b.setStatus(BookStatus.LOANED);
                }

                if (rand.nextInt(5) == 0) { // 20%
                    loan.cancel();
                }
            }
        }

        // 7) Zadania sortowania (przykładowe)
        for (int i = 0; i < 10; i++) {
            int month = rand.nextInt(12) + 1;
            int day = rand.nextInt(20) + 1;
            int hourFrom = rand.nextInt(8) + 8;
            int minuteFrom = rand.nextInt(60);

            LocalDateTime from = LocalDateTime.of(2023, month, day, hourFrom, minuteFrom);
            int durationHours = rand.nextInt(6) + 1;
            int durationMinutes = rand.nextInt(60);
            LocalDateTime to = from.plusHours(durationHours).plusMinutes(durationMinutes);

            SortingJob job = new SortingJob(from, to);
            job.setLibrarian(losuj(librarians, rand));
            job.setSector(losuj(sectors, rand));
        }

        // 8) Walidacja defensywna limitów
        for (Client c : clients) {
            long activeRes = c.getReservations().stream()
                    .filter(r -> r.getStatus() == ReservationStatus.PENDING)
                    .count();
            long activeLoans = c.getLoans().stream()
                    .filter(l -> l.getStatus() == LoanStatus.PENDING)
                    .count();
            int booksRes = c.getReservations().stream()
                    .filter(r -> r.getStatus() == ReservationStatus.PENDING)
                    .mapToInt(r -> r.getBooks() != null ? r.getBooks().size() : 0).sum();
            int booksLoan = c.getLoans().stream()
                    .filter(l -> l.getStatus() == LoanStatus.PENDING)
                    .mapToInt(l -> l.getBooks() != null ? l.getBooks().size() : 0).sum();
            int total = booksRes + booksLoan;

            if (activeRes > 2 || activeLoans > 2 || total > 5) {
                throw new IllegalStateException("Naruszono limit dla klienta: " + c + " res=" + activeRes + " loans=" + activeLoans + " books=" + total);
            }
        }

        System.out.println("[DEBUG] Sektory: " + sectors.size());
        System.out.println("[DEBUG] Książki: " + books.size());
        System.out.println("[DEBUG] Bibliotekarze: " + librarians.size());
        System.out.println("[DEBUG] Klienci: " + clients.size());
        System.out.println("[DEBUG] Karty: " + cards.size());
    }

    private static <T> T losuj(List<T> list, Random rand) {
        return list.get(rand.nextInt(list.size()));
    }

    private static int remainingBookCapacity(Client c) {
        int reserved = c.getReservations().stream()
                .filter(r -> r.getStatus() == ReservationStatus.PENDING)
                .mapToInt(r -> r.getBooks() != null ? r.getBooks().size() : 0).sum();
        int loaned = c.getLoans().stream()
                .filter(l -> l.getStatus() == LoanStatus.PENDING)
                .mapToInt(l -> l.getBooks() != null ? l.getBooks().size() : 0).sum();
        return Math.max(0, 5 - (reserved + loaned));
    }
}
