package Model;

import java.util.*;

/**
 * Klasa Librarian – dziedziczy po Employee, dodaje pole genreSpecialization.
 * Bibliotekarz może mieć przypisanych wiele zadań sortowania (SortingJob).
 */
public class Librarian extends Employee {
    private static final long serialVersionUID = 1L;

    private String genreSpecialization;

    // zestaw zadań sortowania przypisanych temu bibliotekarzowi
    private Set<SortingJob> sortingJobs = new HashSet<>();

    public Librarian(String firstName, String lastName, Gender gender,
                     double salary, String genreSpecialization) {
        super(firstName, lastName, gender, salary);
        this.genreSpecialization = genreSpecialization;
    }

    // Gettery / settery

    public String getGenreSpecialization() {
        return genreSpecialization;
    }

    public void setGenreSpecialization(String genreSpecialization) {
        this.genreSpecialization = genreSpecialization;
    }

    public Set<SortingJob> getSortingJobs() {
        return Collections.unmodifiableSet(sortingJobs);
    }

    /**
     * Dodaje zadanie sortowania do zestawu bibliotekarza.
     */
    public void addSortingJob(SortingJob job) {
        if (job != null) {
            sortingJobs.add(job);
            if (job.getLibrarian() != this) {
                job.setLibrarian(this);
            }
        }
    }

    public void removeSortingJob(SortingJob job) {
        if (job != null && sortingJobs.remove(job)) {
            job.setLibrarian(null);
        }
    }

    @Override
    public String toString() {
        return String.format("Librarian[id=%s, name=%s %s, salary=%.2f, spec=%s, tasks=%d]",
                getPublicId(), getFirstName(), getLastName(), getSalary(),
                genreSpecialization, sortingJobs.size());
    }
}
