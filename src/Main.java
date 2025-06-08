import Controller.*;
import Model.Book;
import Model.Librarian;
import View.MainFrame;
import Model.utils.AutoIdEntity;
import Model.utils.ObjectPlus;
import Model.Sector;

import java.io.*;
import java.util.*;
import Model.*;
import static Model.utils.SampleData.addSampleData;

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
            AutoIdEntity.recalculateNextId(ClientCard.class);
            AutoIdEntity.recalculateNextId(Reservation.class);
            AutoIdEntity.recalculateNextId(Receptionist.class);
            AutoIdEntity.recalculateNextId(Manager.class);
            AutoIdEntity.recalculateNextId(Librarian.class);
            AutoIdEntity.recalculateNextId(SortingJob.class);
            AutoIdEntity.recalculateNextId(Sector.class);
        } else {
            System.out.println("[INFO] Tworzę dane przykładowe, plik nie istnieje: " + file);
            addSampleData();
            ObjectPlus.saveToFile(file);
        }
    }
    public void testFunctions(EmployeeController employeeController, ClientController clientController) {
        System.out.println(employeeController.getEmployeeList());
    }
    public static void main(String[] args) {
        // =============MAIN=============
        Main main = new Main();
        main.handleObjectPlus();

        // =============CONTROLLERS=============
        EmployeeController employeeController = new EmployeeController();
        BookController bookController = new BookController();
        SectorController sectorController = new SectorController();
        ClientController clientController = new ClientController();
        ReservationController reservationController = new ReservationController();
        LibrarianController librarianController = new LibrarianController();
        SortingJobController sortingJobController = new SortingJobController();
        reservationController.generateFinesForExpiredReservations();
        // =============TESTING=============
        main.testFunctions(employeeController, clientController);

        // =============GUI=============
        new MainFrame(employeeController, bookController, sectorController, clientController, reservationController, librarianController, sortingJobController);

    }
}
