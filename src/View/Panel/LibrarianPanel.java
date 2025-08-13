package View.Panel;

import Controller.*;
import View.Dialogs.Librarian.*;

import javax.swing.*;

public class LibrarianPanel extends EmployeePanel {
    public LibrarianPanel(BookController bookController, ClientController clientController, SectorController sectorController, ReservationController reservationController, LoanController loanController) {
        super(bookController, clientController, "Bibliotekarzu!");

        // Dodaj swoje przyciski do panelu „Twoja praca”
        JPanel workButtonsPanel = getWorkButtonsPanel();

        JButton btnAddBook = new JButton("Katalogowanie nowej książki");
        JButton btnUpdateBook = new JButton("Aktualizacja danych książki");
        JButton btnDeleteBook = new JButton("Usunięcie książki");
        JButton btnAddClient = new JButton("Dodaj klienta");
        JButton btnReserveBook = new JButton("Rezerwuj książkę");
        if (bookController.getAvailableBooks().isEmpty()) {
            btnReserveBook.setEnabled(false);
            btnReserveBook.setToolTipText("Brak dostępnych książek do rezerwacji.");
        }
        JButton btnLoanBooks = new JButton("Wypożyczenie Książek");
        JButton btnCancelReservation = new JButton("Anuluj rezerwację");
        JButton btnReturnBook = new JButton("Zwróć książkę");

        btnAddBook.addActionListener(_ -> new CatalogBookDialog(bookController, sectorController));
        btnUpdateBook.addActionListener(_ -> new UpdateBookDialog(sectorController, bookController));
        btnDeleteBook.addActionListener(_ -> new DeleteBookDialog(sectorController, bookController));
        btnAddClient.addActionListener(_ -> new AddNewClientDialog(clientController));
        btnReserveBook.addActionListener(_ -> new ReserveBookDialog(bookController, clientController, reservationController));
        btnLoanBooks.addActionListener(_ -> new LoanBooksDialog(bookController, clientController, loanController));
        btnCancelReservation.addActionListener(_ -> new CancelReservationDialog(clientController, reservationController));
        btnReturnBook.addActionListener(_ -> new ReturnBookDialog(clientController));

        workButtonsPanel.add(btnAddBook);
        workButtonsPanel.add(btnUpdateBook);
        workButtonsPanel.add(btnDeleteBook);
        workButtonsPanel.add(btnAddClient);
        workButtonsPanel.add(btnReserveBook);
        workButtonsPanel.add(btnLoanBooks);
        workButtonsPanel.add(btnCancelReservation);
        workButtonsPanel.add(btnReturnBook);

    }
}