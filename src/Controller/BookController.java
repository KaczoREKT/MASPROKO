package Controller;

import Model.Book;
import Model.BookStatus;
import Model.Sector;
import utils.ObjectPlus;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BookController {
    public List<Book> getBookList() {
        List<Book> books = new ArrayList<>();
        try {
            for (Book b : ObjectPlus.getExtent(Book.class)) {
                books.add(b);
            }
        } catch (Exception e) {}
        return books;
    }
    public List<Book> getAvailableBooks() {
        try {
            Iterable<Book> iterable = ObjectPlus.getExtent(Book.class);
            return StreamSupport.stream(iterable.spliterator(), false)
                    .filter(b -> b.getStatus() != BookStatus.WYPOZYCZONA)
                    .toList();
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<Book> getAvailableBooksInSector(Sector sector) {
        List<Book> availableBooks = new ArrayList<>();
        if (sector != null) {
            for (Book b : sector.getBooks()) {
                if (!(b.getStatus() == BookStatus.WYPOZYCZONA)) {
                    availableBooks.add(b);
                }
            }
        }
        return availableBooks;
    }
    public void addBook(String title, String genre, String author, Sector sector) {
        Book book = new Book(title, genre, author);
        sector.addBook(book);
    }
    public void deleteBook(Book book) throws Exception {
        if (book == null) throw new IllegalArgumentException("Brak książki do usunięcia!");

        // TU sprawdzamy status!
        if (book.getStatus() == BookStatus.WYPOZYCZONA) {
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
    }
}
