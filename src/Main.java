import Model.Book;
import Model.Librarian;
import utils.ObjectPlus;
import Model.Sector;
import utils.SampleData;

import java.io.*;
import java.util.*;
import Model.*;
import static utils.SampleData.addSampleData;

public class Main {
    public static void main(String[] args) throws Exception {
        String file = "extents.bin";
        List<Class<?>> extentClasses = Arrays.asList(
                // Osoby i ich podklasy
                Client.class,
                ClientCard.class,
                Reservation.class,
                Fine.class,
                // Pracownicy
                Librarian.class,
                Manager.class,
                Receptionist.class,
                SortingJob.class,
                // Książki i sektory
                Book.class,
                Sector.class
        );

        boolean shouldSave = false;

        // Próbujemy wczytać ekstensje z pliku
        File extFile = new File(file);
        if (extFile.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                ObjectPlus.readExtents(in);
                System.out.println("[INFO] Wczytano ekstensje z pliku " + file + ".");
            } catch (Exception e) {
                System.out.println("[WARN] Nie udało się wczytać ekstensji z pliku, utworzę nowe dane.");
                addSampleData();
                shouldSave = true;
            }
        } else {
            // Pliku nie ma, więc tworzymy przykładowe dane i zapisujemy
            System.out.println("[INFO] Tworzę przykładowe dane do pliku " + file + ".");
            addSampleData();
            shouldSave = true;
        }

        // Pokazujemy ekstensje dla wszystkich klas
        System.out.println("\n=== EKSTENSJE WSZYSTKICH KLAS ===");
        for (Class<?> clazz : extentClasses) {
            try {
                ObjectPlus.showExtent(clazz);
            } catch (Exception e) {
                System.out.println("[INFO] Brak ekstensji dla klasy " + clazz.getSimpleName());
            }
            System.out.println("--------------------------------");
        }

        // Zapisujemy tylko jeśli generowaliśmy nowe dane (czyli nie było pliku lub był uszkodzony)
        if (shouldSave) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
                ObjectPlus.writeExtents(out);
                System.out.println("[INFO] Zapisano ekstensje do pliku.");
            }
        }

    }
}
