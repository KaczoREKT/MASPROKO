package View.Panel;

import Controller.BookController;
import Controller.ClientController;
import Controller.SectorController;
import View.Dialogs.Librarian.CatalogBookDialog;
import View.Dialogs.Librarian.UpdateBookDialog;
import View.Dialogs.Librarian.DeleteBookDialog;

import javax.swing.*;

public class LibrarianPanel extends EmployeePanel {
    public LibrarianPanel(BookController bookController, ClientController clientController, SectorController sectorController) {
        super(bookController, clientController, "Bibliotekarzu!");

        // Dodaj swoje przyciski do panelu „Twoja praca”
        JPanel workButtonsPanel = getWorkButtonsPanel();

        JButton btnAddBook = new JButton("Dodaj książkę");
        JButton btnUpdateBook = new JButton("Aktualizuj książkę");
        JButton btnDeleteBook = new JButton("Usuń książkę");


        btnAddBook.addActionListener(_ -> new CatalogBookDialog(bookController, sectorController));
        btnUpdateBook.addActionListener(_ -> new UpdateBookDialog(sectorController, bookController));
        btnDeleteBook.addActionListener(_ -> new DeleteBookDialog(sectorController, bookController));


        workButtonsPanel.add(btnAddBook);
        workButtonsPanel.add(btnUpdateBook);
        workButtonsPanel.add(btnDeleteBook);

    }
}