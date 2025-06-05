package Model;

import java.util.List;
public class Sector {

    private Long id;

    private String startLetter;
    private String endLetter;
    private List<SortingJob> sortingJobs;
    private List<Book> books;
}
