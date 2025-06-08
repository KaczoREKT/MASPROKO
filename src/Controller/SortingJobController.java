package Controller;

import Model.Librarian;
import Model.Sector;
import Model.SortingJob;

import java.time.LocalDate;

public class SortingJobController {
    public void assignSortingJob(Librarian librarian, Sector sector, LocalDate from, LocalDate to) throws Exception {
        if (librarian == null || sector == null) throw new Exception("Brak danych.");
        SortingJob job = new SortingJob(from, to);
        job.setLibrarian(librarian);
        job.setSector(sector);
    }

}
