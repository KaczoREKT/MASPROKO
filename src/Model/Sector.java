package Model;

import utils.ObjectPlus;

import java.util.*;

/**
 * Klasa Sector – dzieli książki na sektory w zależności od przedziału liter.
 * Pola: startLetter, endLetter (np. od "A" do "C").
 * Relacje:
 *   - jeden (Sector) do wielu (Book)
 *   - jeden (Sector) do wielu (SortingJob)
 */
public class Sector extends ObjectPlus {
    private static final long serialVersionUID = 1L;

    private String startLetter;
    private String endLetter;

    private Set<Book> books = new HashSet<>();
    private Set<SortingJob> sortingJobs = new HashSet<>();

    public Sector(String startLetter, String endLetter) {
        super();
        this.startLetter = startLetter;
        this.endLetter = endLetter;
    }

    // Gettery / settery

    public String getStartLetter() {
        return startLetter;
    }

    public void setStartLetter(String startLetter) {
        this.startLetter = startLetter;
    }

    public String getEndLetter() {
        return endLetter;
    }

    public void setEndLetter(String endLetter) {
        this.endLetter = endLetter;
    }

    public Set<Book> getBooks() {
        return Collections.unmodifiableSet(books);
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
        return String.format("Sector[%s–%s]", startLetter, endLetter);
    }
}
