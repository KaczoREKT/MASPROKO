package Controller;

import Model.Book;
import Model.Enum.BookStatus;
import Model.Sector;
import Model.utils.ObjectPlus;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class BookController extends AbstractController<Book> {
    public BookController() {
        super(Book.class);
    }

    public List<Book> getAvailableBooks() {
        try {
            Iterable<Book> iterable = ObjectPlus.getExtent(Book.class);
            return StreamSupport.stream(iterable.spliterator(), false)
                    .filter(b -> b.getStatus() == BookStatus.AVAILABLE)
                    .toList();
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<Book> getAvailableBooksInSector(Sector sector) {
        List<Book> availableBooks = new ArrayList<>();
        if (sector != null) {
            for (Book b : sector.getBooks()) {
                if (b.getStatus() == BookStatus.AVAILABLE) {
                    availableBooks.add(b);
                }
            }
        }
        return availableBooks;
    }
    public void addBook(String title, String genre, String author) {
        Book book = new Book(title, genre, author);
        Sector sector = matchSector(book);
        sector.addBook(book);
    }
    public void deleteBook(Book book) throws Exception {
        if (book == null) throw new IllegalArgumentException("Brak książki do usunięcia!");

        if (book.getStatus() == BookStatus.LOANED) {
            throw new Exception("Nie można usunąć książki, która jest wypożyczona.");
        }
        if (book.getSector() != null) {
            book.getSector().removeBook(book);
        }
        ObjectPlus.removeFromExtent(book);
    }
    public void updateBook(Book selectedBook, String newTitle, String newAuthor, String newGenre, BookStatus newStatus) {
        selectedBook.setTitle(newTitle);
        selectedBook.setAuthor(newAuthor);
        selectedBook.setGenre(newGenre);
        selectedBook.setStatus(newStatus);

        Sector correctSector = matchSector(selectedBook);

        if (correctSector != null && correctSector != selectedBook.getSector()) {
            Sector oldSector = selectedBook.getSector();
            if (oldSector != null) oldSector.removeBook(selectedBook);
            correctSector.addBook(selectedBook);
        }
    }


    public Book getBookById(long id) {
        try {
            for (Book b : ObjectPlus.getExtent(Book.class)) {
                if (b.getId() == id) {
                    return b;
                }
            }
        } catch (Exception _) {
        }
        return null;
    }
    public Sector matchSector(Book book) {
        if (book == null || book.getTitle() == null || book.getTitle().isEmpty()) return null;
        try {
            Iterable<Sector> sectors = ObjectPlus.getExtent(Sector.class);
            for (Sector sector : sectors) {
                if (sector.shouldContainBook(book)) {
                    return sector;
                }
            }
        } catch (Exception ignored) {}
        return null;
    }


}
