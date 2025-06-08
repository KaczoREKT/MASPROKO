package Controller;

import Model.Librarian;
import Model.Sector;
import Model.SortingJob;

import java.time.LocalDateTime;

public class SortingJobController extends AbstractController<SortingJob> {
    public SortingJobController() {
        super(SortingJob.class);
    }

    public SortingJob assignSortingJob(Librarian librarian, Sector  sector, LocalDateTime from, LocalDateTime to) throws Exception {
        if (librarian == null || sector == null) throw new Exception("Brak danych.");
        SortingJob job = new SortingJob(from, to);
        job.setLibrarian(librarian);
        job.setSector(sector);
        return job;
    }
}
