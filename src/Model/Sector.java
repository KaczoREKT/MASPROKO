package Model;

import Model.utils.AutoIdEntity;
import Model.utils.ObjectPlus;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Klasa Sector – dzieli książki na sektory w zależności od przedziału liter.
 * Pola: startLetter, endLetter (np. od "A" do "C").
 * Relacje:
 * - jeden (Sector) do wielu (Book)
 * - jeden (Sector) do wielu (SortingJob)
 */
public class Sector extends AutoIdEntity {
    private static final long serialVersionUID = 1L;

    private char startLetter;
    private char endLetter;

    private Set<Book> books = new HashSet<>();
    private Set<SortingJob> sortingJobs = new HashSet<>();

    public Sector(char startLetter, char endLetter) {
        super();
        this.startLetter = startLetter;
        this.endLetter = endLetter;
    }

    // Gettery / settery

    public char getStartLetter() {
        return startLetter;
    }
    public void setStartLetter(char startLetter) {this.startLetter = startLetter; }
    public char getEndLetter() {
        return endLetter;
    }
    public void setEndLetter(char endLetter) {
        this.endLetter = endLetter;
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

    /**
     * Dodaje książkę do sektora – relacja jeden-do-wielu.
     */
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

    /**
     * Dodaje SortingJob do sektora – relacja jeden-do-wielu.
     */
    public void addSortingJob(SortingJob job) {
        if (job != null) {
            sortingJobs.add(job);
            if (job.getSector() != this) {
                job.setSector(this);
            }
        }
    }

    public void removeSortingJob(SortingJob job) {
        if (job != null && sortingJobs.remove(job)) {
            job.setSector(null);
        }
    }

    @Override
    public String toString() {
        return "Sector[" +  startLetter + "," + endLetter + "], books: " + getBooksTitles();
    }
}
