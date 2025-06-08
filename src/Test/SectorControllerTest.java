package Test;

import Controller.SectorController;
import Model.Sector;

import java.util.List;

public class SectorControllerTest {
    public static void runTests() {
        System.out.println("=== SectorControllerTest ===");
        SectorController sectorController = new SectorController();

        // 1. Dodawanie sektorów i unikalność
        Sector sectorEF = new Sector('E', 'F');
        Sector sectorGH = new Sector('G', 'H');
        List<Sector> sectors = sectorController.getList();

        assert sectors.contains(sectorEF) : "Sektor E-F nie pojawił się na liście!";
        assert sectors.contains(sectorGH) : "Sektor G-H nie pojawił się na liście!";

        System.out.println("Aktualne sektory: " + sectors);

        // 2. Sprawdzenie, że nie da się dodać dwa razy tego samego zakresu
        boolean exceptionThrown = false;
        try {
            Sector sectorDuplicate = new Sector('E', 'F');
            System.out.println("BŁĄD: Dodano zduplikowany sektor!");
        } catch (Exception ex) {
            exceptionThrown = true;
            System.out.println("Poprawnie wykryto próbę duplikacji sektora: " + ex.getMessage());
        }
        assert exceptionThrown : "Nie wykryto duplikacji sektora!";

        // 3. Pobieranie sektora po zakresie
        Sector found = sectorController.findSectorByRange('E', 'F');
        assert found != null : "Nie znaleziono sektora E-F!";
        System.out.println("Znaleziony sektor: " + found);

        // 4. Usuwanie sektora
        try {
            sectorController.deleteSector(sectorGH);
            List<Sector> sectorsAfter = sectorController.getList();
            assert !sectorsAfter.contains(sectorGH) : "Sektor nie został usunięty!";
            System.out.println("Po usunięciu G-H: " + sectorsAfter);
        } catch (Exception ex) {
            System.out.println("Błąd przy usuwaniu sektora: " + ex.getMessage());
        }

        // 5. Próba usunięcia sektora, który już nie istnieje
        boolean deleteException = false;
        try {
            sectorController.deleteSector(sectorGH);
        } catch (Exception ex) {
            deleteException = true;
            System.out.println("Poprawnie zgłoszono błąd przy usuwaniu nieistniejącego sektora: " + ex.getMessage());
        }
        assert deleteException : "Nie zgłoszono wyjątku przy usuwaniu nieistniejącego sektora!";

        System.out.println("SectorControllerTest OK\n");
    }
}
