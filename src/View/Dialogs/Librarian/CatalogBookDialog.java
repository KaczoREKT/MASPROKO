package View.Dialogs.Librarian;

import Controller.BookController;
import Controller.SectorController;
import Model.Sector;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CatalogBookDialog extends JDialog {

    public CatalogBookDialog(BookController bookController, SectorController sectorController) {

        setTitle("Katalogowanie nowej książki");
        setModal(true);
        setSize(400, 250);
        setLayout(new GridLayout(0, 2, 10, 10));
        setLocationRelativeTo(null);

        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField isbnField = new JTextField();

        add(new JLabel("Tytuł:"));
        add(titleField);
        add(new JLabel("Autor:"));
        add(authorField);
        add(new JLabel("Gatunek:"));
        add(isbnField);

        JButton btnAdd = new JButton("Dodaj książkę");
        add(new JLabel());
        add(btnAdd);

        btnAdd.addActionListener(_ -> {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String genre = isbnField.getText().trim();

            if (title.isEmpty() || author.isEmpty() || genre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Uzupełnij wszystkie dane!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            char firstLetter = Character.toUpperCase(title.charAt(0));
            List<Sector> sectors = sectorController.getList();

            Sector matchedSector = null;
            for (Sector sector : sectors) {
                if (sector.containsLetter(firstLetter)) {
                    matchedSector = sector;
                    break;
                }
            }

            if (matchedSector == null) {
                JOptionPane.showMessageDialog(this, "Brak sektora dla litery: " + firstLetter, "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            bookController.addBook(title, author, genre);

            JOptionPane.showMessageDialog(this, "Książka dodana do sektora: " + matchedSector.getName());
            dispose();

        });
        setVisible(true);
    }
}
