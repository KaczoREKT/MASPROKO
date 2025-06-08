package Test;

import Controller.SortingJobController;
import Model.Enum.Gender;
import Model.Librarian;
import Model.Sector;
import Model.SortingJob;

import java.time.LocalDateTime;
import java.util.List;

public class SortingJobControllerTest {
    public static void runTests() throws Exception {
        System.out.println("=== SortingJobControllerTest ===");

        SortingJobController sortingJobController = new SortingJobController();

        Librarian librarian = new Librarian("Asia", "Sortująca", Gender.WOMAN, 3900, "Sci-Fi");
        Sector sector = new Sector('G', 'H');

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = now.plusDays(2).plusHours(3);

        // 1. Poprawne przypisanie zadania
        SortingJob job = sortingJobController.assignSortingJob(librarian, sector, now, end);
        assert job != null : "Zadanie nie zostało utworzone!";
        System.out.println("Utworzone zadanie: " + job);

        // ... (reszta testów podobnie, tylko zamień LocalDate na LocalDateTime)
        // Przypadek: Złe daty
        boolean exceptionThrown = false;
        try {
            sortingJobController.assignSortingJob(librarian, sector, now, now.minusDays(1));
        } catch (Exception e) {
            exceptionThrown = true;
            System.out.println("Poprawnie złapano błąd dla złych dat: " + e.getMessage());
        }
        assert exceptionThrown : "Nie zgłoszono wyjątku przy złych datach!";

        // Przypadek: Null librarian
        exceptionThrown = false;
        try {
            sortingJobController.assignSortingJob(null, sector, now, end);
        } catch (Exception e) {
            exceptionThrown = true;
            System.out.println("Poprawnie złapano błąd dla null bibliotekarza: " + e.getMessage());
        }
        assert exceptionThrown : "Nie zgłoszono wyjątku przy null bibliotekarzu!";

        // Przypadek: Null sector
        exceptionThrown = false;
        try {
            sortingJobController.assignSortingJob(librarian, null, now, end);
        } catch (Exception e) {
            exceptionThrown = true;
            System.out.println("Poprawnie złapano błąd dla null sektora: " + e.getMessage());
        }
        assert exceptionThrown : "Nie zgłoszono wyjątku przy null sektorze!";

        // 6. Sprawdzenie listy zadań (np. przez ekstensję)
        List<SortingJob> jobs = sortingJobController.getList();
        assert jobs.contains(job) : "Lista zadań nie zawiera przypisanego zadania!";
        System.out.println("Lista zadań: " + jobs);

        System.out.println("SortingJobControllerTest OK\n");
    }
}
