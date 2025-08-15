import Controller.*;
import Model.Book;
import Model.Librarian;
import View.MainFrame;
import Model.utils.AutoIdEntity;
import Model.utils.ObjectPlus;
import Model.Sector;
import java.io.*;
import Model.*;

import static Model.utils.SampleData.addSampleData;

public class Main {
    public void handleObjectPlus(){
        String file = "extents.bin";

        File extFile = new File(file);
        if (extFile.exists()) {
            ObjectPlus.loadFromFile(file);
            recalculateID();
        } else {
            System.out.println("[INFO] Tworzę dane przykładowe, plik nie istnieje: " + file);
            addSampleData();
            recalculateID();
            ObjectPlus.saveToFile(file);
        }
    }
    public void recalculateID(){
        AutoIdEntity.recalculateNextId(Book.class);
        AutoIdEntity.recalculateNextId(Client.class);
        AutoIdEntity.recalculateNextId(Manager.class);
        AutoIdEntity.recalculateNextId(ClientCard.class);
        AutoIdEntity.recalculateNextId(Loan.class);
        AutoIdEntity.recalculateNextId(Reservation.class);
        AutoIdEntity.recalculateNextId(Manager.class);
        AutoIdEntity.recalculateNextId(Librarian.class);
        AutoIdEntity.recalculateNextId(SortingJob.class);
        AutoIdEntity.recalculateNextId(Sector.class);
        AutoIdEntity.recalculateNextId(Accountant.class);
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
        FineController fineController = new FineController();
        LoanController loanController = new LoanController();
        reservationController.generateFinesForExpiredReservations();

        // =============GUI=============
        new MainFrame(employeeController,
                bookController,
                sectorController,
                clientController,
                reservationController,
                librarianController,
                sortingJobController,
                fineController,
                loanController);

    }
}
