import Controller.BookController;
import Controller.EmployeeController;
import Controller.SectorController;
import Model.Book;
import Model.Librarian;
import View.MainFrame;
import utils.AutoIdEntity;
import utils.ObjectPlus;
import Model.Sector;
import utils.SampleData;

import java.io.*;
import java.util.*;
import Model.*;
import static utils.SampleData.addSampleData;

public class Main {
    public void handleObjectPlus(){
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

        // Próbujemy wczytać ekstensje z pliku
        File extFile = new File(file);
        if (extFile.exists()) {
            ObjectPlus.loadFromFile(file);
            AutoIdEntity.recalculateNextId(Book.class);
            AutoIdEntity.recalculateNextId(Client.class);
            AutoIdEntity.recalculateNextId(Manager.class);
        } else {
            System.out.println("[INFO] Tworzę dane przykładowe, plik nie istnieje: " + file);
            addSampleData();
            ObjectPlus.saveToFile(file);
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
    }
    public void testFunctions(EmployeeController employeeController){
        System.out.println(employeeController.getEmployeeList());
    }
    public static void main(String[] args) throws Exception {
        // =============MAIN=============
        Main main = new Main();
        main.handleObjectPlus();

        // =============CONTROLLERS=============
        EmployeeController employeeController = new EmployeeController();
        BookController bookController = new BookController();
        SectorController sectorController = new SectorController();
        // =============TESTING=============
        main.testFunctions(employeeController);


        // =============GUI=============
        new MainFrame(employeeController, bookController, sectorController);

    }
}
