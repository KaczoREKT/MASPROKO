package View.Panel;

import Controller.*;
import View.Dialogs.Librarian.*;
import View.MainFrame;

import javax.swing.*;

public class LibrarianPanel extends EmployeePanel {

    public LibrarianPanel(MainFrame mainFrame, BookController bookController, ClientController clientController,
                          SectorController sectorController, ReservationController reservationController,
                          LoanController loanController) {

        super(mainFrame, bookController, clientController, "Bibliotekarzu!");

        JMenuItem btnAddClient = createMenuItem("Dodaj klienta",
                _ -> new AddNewClientDialog(clientController));
        
        JMenuItem btnAddBook = createMenuItem("Katalogowanie nowej książki",
                _ -> new CatalogBookDialog(bookController, sectorController));

        JMenuItem btnUpdateBook = createMenuItem("Aktualizacja danych książki",
                _ -> new UpdateBookDialog(sectorController, bookController));
        
        JMenuItem btnDeleteBook = createMenuItem("Usunięcie książki",
                _ -> new DeleteBookDialog(sectorController, bookController));
        
        JMenuItem btnReserveBook = createMenuItem("Utwórz Rezerwację",
                _ -> new ReserveBookDialog(bookController, clientController, reservationController));

        JMenuItem btnCancelReservation = createMenuItem("Anuluj rezerwację",
                _ -> new CancelReservationDialog(clientController, reservationController));

        JMenuItem btnLoanBooks = createMenuItem("Wypożyczenie książek",
                _ -> new LoanBooksDialog(bookController, clientController, loanController));

        JMenuItem btnReturnBook = createMenuItem("Zwrot książek",
                _ -> new ReturnBookDialog(clientController, loanController));

        addToDropdownMenu(btnAddClient);
        addToDropdownMenu(btnAddBook);
        addToDropdownMenu(btnUpdateBook);
        addToDropdownMenu(btnDeleteBook);
        addToDropdownMenu(btnReserveBook);
        addToDropdownMenu(btnCancelReservation);
        addToDropdownMenu(btnLoanBooks);
        addToDropdownMenu(btnReturnBook);
    }


}