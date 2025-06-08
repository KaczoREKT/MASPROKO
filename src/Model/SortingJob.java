package Model;

import Model.utils.AutoIdEntity;

import java.time.LocalDate;

/**
 * Klasa SortingJob – zadanie sortowania książek w danym sektorze.
 * Pola: startDate, endDate.
 * Relacje:
 * - jeden (SortingJob) do wielu (brak, w diagramie jednokierunkowo do Librarian i do Sector)
 * - relacja wiele-do-jednego z Librarian (każde zadanie jest przypisane do jednej osoby)
 * - relacja wiele-do-jednego z Sector (po jakim sektorze dana osoba sortuje)
 */
public class SortingJob extends AutoIdEntity {
    private static final long serialVersionUID = 1L;

    private LocalDate startDate;
    private LocalDate endDate;

    private Librarian librarian;
    private Sector sector;

    public SortingJob(LocalDate startDate, LocalDate endDate) {
        super();
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Gettery / settery

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Librarian getLibrarian() {
        return librarian;
    }

    /**
     * Ustawia, który bibliotekarz (Librarian) ma to zadanie.
     */
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

    /**
     * Ustawia, w którym sektorze realizowane jest to zadanie.
     */
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
                (sector != null ? sector.getPrefix() : "brak"));
    }

    @Override
    public String getPrefix() {
        return "SJ";
    }

}
