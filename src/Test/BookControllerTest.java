package Test;

import Controller.BookController;
import Model.Book;
import Model.Enum.BookStatus;
import Model.Sector;

import java.util.List;

public class BookControllerTest {
    public static void runTests() {
        System.out.println("=== BookControllerTest ===");
        BookController bookController = new BookController();

        // 1. Dodaj sektor i książkę
        Sector sector = new Sector('A', 'B');
        bookController.addBook("Pan Tadeusz", "Epos", "Adam Mickiewicz");

        List<Book> books = bookController.getList();
        assert !books.isEmpty() : "Książka nie została dodana!";
        Book book = books.getFirst();
        System.out.println("Dodana książka: " + book);

        // 2. Dodaj drugą książkę
        bookController.addBook("Lalka", "Powieść", "Bolesław Prus");
        books = bookController.getList();
        assert books.size() == 2 : "Nie dodano drugiej książki!";
        System.out.println("Wszystkie książki: " + books);

        // 3. Aktualizuj dane książki
        bookController.updateBook(book, "Pan Tadeusz (edycja)", "A. Mickiewicz", "Poemat", BookStatus.AVAILABLE);
        assert book.getTitle().equals("Pan Tadeusz (edycja)") : "Tytuł nie został zaktualizowany!";
        assert book.getAuthor().equals("A. Mickiewicz") : "Autor nie został zaktualizowany!";

        // 4. Sprawdź dostępność książek
        List<Book> available = bookController.getAvailableBooks();
        assert available.size() == 2 : "Dostępnych książek powinno być 2!";
        System.out.println("Dostępne książki: " + available);

        // 5. Ustaw status WYPOZYCZONA i sprawdź dostępność
        book.setStatus(BookStatus.LOANED);
        available = bookController.getAvailableBooks();
        assert available.size() == 1 : "Powinna być jedna dostępna książka!";
        System.out.println("Dostępne po zmianie statusu: " + available);

        // 6. Próba usunięcia wypożyczonej książki — oczekiwany wyjątek
        boolean exceptionThrown = false;
        try {
            bookController.deleteBook(book);
        } catch (Exception e) {
            exceptionThrown = true;
            System.out.println("Poprawnie złapano wyjątek przy próbie usunięcia wypożyczonej książki: " + e.getMessage());
        }
        assert exceptionThrown : "Brak wyjątku przy usuwaniu wypożyczonej książki!";

        // 7. Ustaw status DOSTEPNA i usuń książkę
        book.setStatus(BookStatus.AVAILABLE);
        try {
            bookController.deleteBook(book);
            books = bookController.getList();
            assert books.size() == 1 : "Książka nie została usunięta!";
            System.out.println("Książki po usunięciu: " + books);
        } catch (Exception e) {
            System.out.println("Błąd przy usuwaniu książki: " + e.getMessage());
        }

        // 8. Szukanie książki po ID
        Book left = books.getFirst();
        Book byId = bookController.getBookById(left.getId());
        assert byId == left : "Nie znaleziono książki po ID!";
        System.out.println("Znaleziono po ID: " + byId);

        // 9. Sprawdzenie dostępnych książek w sektorze
        List<Book> availableInSector = bookController.getAvailableBooksInSector(sector);
        assert availableInSector.size() == 1 : "W sektorze powinna być 1 dostępna książka!";
        System.out.println("Dostępne w sektorze: " + availableInSector);

        // 10. Próbuj usunąć nieistniejącą książkę
        boolean nullException = false;
        try {
            bookController.deleteBook(null);
        } catch (Exception e) {
            nullException = true;
            System.out.println("Poprawnie zgłoszono błąd przy usuwaniu null: " + e.getMessage());
        }
        assert nullException : "Brak wyjątku przy usuwaniu null!";

        System.out.println("BookControllerTest OK\n");
    }
}
