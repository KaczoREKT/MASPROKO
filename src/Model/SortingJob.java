package Model;

import Model.utils.AutoIdEntity;

import java.io.Serial;
import java.time.LocalDateTime;

public class SortingJob extends AutoIdEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    private Librarian librarian;
    private Sector sector;

    public SortingJob(LocalDateTime startDate, LocalDateTime endDate) {
        super();
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Librarian getLibrarian() {
        return librarian;
    }

    public void setLibrarian(Librarian librarian) {
        if (this.librarian != null) {
            this.librarian.getSortingJobs().remove(this);
        }
        this.librarian = librarian;
        if (librarian != null && !librarian.getSortingJobs().contains(this)) {
            librarian.addSortingJob(this);
        }
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        if (this.sector != null) {
            this.sector.getSortingJobs().remove(this);
        }
        this.sector = sector;
        if (sector != null && !sector.getSortingJobs().contains(this)) {
            sector.addSortingJob(this);
        }
    }

    @Override
    public String toString() {
        return String.format("SortingJob[id=%s, from=%s, to=%s, librarian=%s, sector=%s]",
                getPublicId(),
                startDate,
                endDate,
                (librarian != null ? librarian.getFirstName() + " " + librarian.getLastName() : "brak"),
                (sector != null ? sector.getName() : "brak"));
    }

    @Override
    public String getPrefix() {
        return "SJ";
    }

}
