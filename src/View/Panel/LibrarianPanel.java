package View.Panel;
import Controller.BookController;
import Controller.SectorController;
import View.Dialogs.Librarian.CatalogBookDialog;
import View.Dialogs.Librarian.DeleteBookDialog;
import View.Dialogs.Librarian.UpdateBookDialog;

import javax.swing.*;
import java.awt.*;

public class LibrarianPanel extends JPanel {
    BookController bookController;
    SectorController sectorController;
    public LibrarianPanel(BookController bookController, SectorController sectorController) {
        this.bookController = bookController;
        this.sectorController = sectorController;
        setLayout(new GridLayout(0, 1, 10, 10));

        add(new JLabel("Panel Bibliotekarza", SwingConstants.CENTER));
        JButton btnCatalogBook = new JButton("Katalogowanie nowej książki");
        JButton btnUpdateBook = new JButton("Aktualizacja danych książki");
        JButton btnDeleteBook = new JButton("Usunięcie książki");

        btnCatalogBook.addActionListener(e -> {
            new CatalogBookDialog(bookController, sectorController);
        });
        btnUpdateBook.addActionListener(e -> {
            new UpdateBookDialog(sectorController);
        });
        btnDeleteBook.addActionListener(e -> {
            new DeleteBookDialog();
        });

        add(btnCatalogBook);
        add(btnUpdateBook);
        add(btnDeleteBook);
    }
}
