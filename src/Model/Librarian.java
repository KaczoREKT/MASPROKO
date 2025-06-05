package Model;

import java.util.HashSet;
import java.util.Set;

public class Librarian extends Employee {

    private String genreSpecialization;

    private Set<SortingJob> sortingJobs = new HashSet<>();
}
