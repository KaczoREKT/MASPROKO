package Model;

import Model.Enum.Gender;

import java.io.Serial;
import java.util.*;

public class Librarian extends Employee {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String genreSpecialization;

    private final Set<SortingJob> sortingJobs = new HashSet<>();

    public Librarian(String firstName, String lastName, Gender gender,
                     double salary, String genreSpecialization) {
        super(firstName, lastName, gender, salary);
        this.genreSpecialization = genreSpecialization;
    }

    public Set<SortingJob> getSortingJobs() {
        return Collections.unmodifiableSet(sortingJobs);
    }

    public void addSortingJob(SortingJob job) {
        if (job == null) return;
        sortingJobs.add(job);
        if (job.getLibrarian() != this) job.setLibrarian(this);
    }


    @Override
    public String toString() {
        return String.format("Librarian[id=%s, name=%s %s, salary=%.2f, spec=%s, tasks=%d]",
                getPublicId(), getFirstName(), getLastName(), getSalary(),
                genreSpecialization, sortingJobs.size());
    }
    @Override
    public String getPrefix() {
        return "L";
    }
}
