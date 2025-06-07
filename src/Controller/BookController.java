package Controller;

import Model.Book;
import Model.Sector;
import utils.ObjectPlus;
import java.util.ArrayList;
import java.util.List;

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
    public void addBook(String title, String genre, String author, Sector sector) {
        Book book = new Book(title, genre, author);
        sector.addBook(book);
    }
}
