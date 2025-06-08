package Model;

import Model.utils.AutoIdEntity;

import java.io.Serial;
import java.util.*;
import java.util.stream.Collectors;

public class Sector extends AutoIdEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    private final char startLetter;
    private final char endLetter;

    private final Set<Book> books = new HashSet<>();
    private final Set<SortingJob> sortingJobs = new HashSet<>();

    public Sector(char startLetter, char endLetter) {
        super();
        this.startLetter = startLetter;
        this.endLetter = endLetter;
    }

    public char getStartLetter() {
        return startLetter;
    }
    public char getEndLetter() {
        return endLetter;
    }

    public String getName(){
        return String.format("Sector [%c, %c]", startLetter, endLetter);
    }
    public boolean containsLetter(char letter) {
        letter = Character.toUpperCase(letter);
        return letter >= startLetter && letter <= endLetter;
    }

    public Set<Book> getBooks() {
        return books;
    }
    public Set<String> getBooksTitles() {
        return books.stream()
                .map(Book::getTitle)
                .collect(Collectors.toSet());
    }

    public void addBook(Book b) {
        if (b != null) {
            books.add(b);
            if (b.getSector() != this) {
                b.setSector(this);
            }
        }
    }

    public void removeBook(Book b) {
        if (b != null && books.remove(b)) {
            b.setSector(null);
        }
    }

    public Set<SortingJob> getSortingJobs() {
        return Collections.unmodifiableSet(sortingJobs);
    }

    public void addSortingJob(SortingJob job) {
        if (job != null && sortingJobs.add(job) && job.getSector() != this) {
            job.setSector(this);
        }
    }

    @Override
    public String toString() {
        return "Sector[" +  startLetter + "," + endLetter + "], books: " + getBooksTitles();
    }

    public boolean shouldContainBook(Book book) {
        if (book == null || book.getTitle() == null || book.getTitle().isEmpty()) return false;
        char firstLetter = Character.toUpperCase(book.getTitle().charAt(0));
        return firstLetter >= startLetter && firstLetter <= endLetter;
    }

}
