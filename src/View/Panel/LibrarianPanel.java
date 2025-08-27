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

        // Tworzymy elementy menu dla bibliotekarz
        JMenuItem btnAddBook = new JMenuItem("Katalogowanie nowej książki");
        btnAddBook.addActionListener(_ -> new CatalogBookDialog(bookController, sectorController));

        JMenuItem btnUpdateBook = new JMenuItem("Aktualizacja danych książki");
        btnUpdateBook.addActionListener(_ -> new UpdateBookDialog(sectorController, bookController));

        JMenuItem btnDeleteBook = new JMenuItem("Usunięcie książki");
        btnDeleteBook.addActionListener(_ -> new DeleteBookDialog(sectorController, bookController));

        JMenuItem btnAddClient = new JMenuItem("Dodaj klienta");
        btnAddClient.addActionListener(_ -> new AddNewClientDialog(clientController));

        JMenuItem btnReserveBook = new JMenuItem("Rezerwuj książkę");
        if (bookController.getAvailableBooks().isEmpty()) {
            btnReserveBook.setEnabled(false);
            btnReserveBook.setToolTipText("Brak dostępnych książek do rezerwacji.");
        }
        btnReserveBook.addActionListener(_ -> new ReserveBookDialog(bookController, clientController, reservationController));

        JMenuItem btnLoanBooks = new JMenuItem("Wypożyczenie książek");
        btnLoanBooks.addActionListener(_ -> new LoanBooksDialog(bookController, clientController, loanController));

        JMenuItem btnCancelReservation = new JMenuItem("Anuluj rezerwację");
        btnCancelReservation.addActionListener(_ -> new CancelReservationDialog(clientController, reservationController));

        JMenuItem btnReturnBook = new JMenuItem("Zwróć książkę");
        btnReturnBook.addActionListener(_ -> new ReturnBookDialog(clientController));

        // Dodajemy wszystkie elementy do dropdown menu
        addToDropdownMenu(btnAddBook);
        addToDropdownMenu(btnUpdateBook);
        addToDropdownMenu(btnDeleteBook);
        addToDropdownMenu(btnAddClient);
        addToDropdownMenu(btnReserveBook);
        addToDropdownMenu(btnLoanBooks);
        addToDropdownMenu(btnCancelReservation);
        addToDropdownMenu(btnReturnBook);
    }
}
