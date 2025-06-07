package View.Dialogs.Librarian;

import Controller.BookController;
import Controller.SectorController;
import Model.Book;
import Model.BookStatus;
import Model.Sector;
import View.ComboBox.BookJComboBox;
import View.ComboBox.SectorJComboBox;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UpdateBookDialog extends JDialog {
    public UpdateBookDialog(SectorController sectorController, BookController bookController) {
        setTitle("Aktualizacja książki");
        setModal(true);
        setSize(420, 320);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Sektor
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        content.add(new JLabel("Sektor:"), gbc);
        List<Sector> sectors = sectorController.getAllSectors();
        JComboBox<Sector> sectorBox = new SectorJComboBox(sectors);
        gbc.gridx = 1; gbc.weightx = 0.7;
        content.add(sectorBox, gbc);

        // Książka
        gbc.gridy = ++row; gbc.gridx = 0; gbc.weightx = 0.3;
        content.add(new JLabel("Książka:"), gbc);
        JComboBox<Book> bookBox = new BookJComboBox();
        gbc.gridx = 1; gbc.weightx = 0.7;
        content.add(bookBox, gbc);

        // Tytuł
        gbc.gridy = ++row; gbc.gridx = 0; gbc.weightx = 0.3;
        content.add(new JLabel("Tytuł:"), gbc);
        JTextField titleField = new JTextField(15);
        gbc.gridx = 1; gbc.weightx = 0.7;
        content.add(titleField, gbc);

        // Autor
        gbc.gridy = ++row; gbc.gridx = 0; gbc.weightx = 0.3;
        content.add(new JLabel("Autor:"), gbc);
        JTextField authorField = new JTextField(15);
        gbc.gridx = 1; gbc.weightx = 0.7;
        content.add(authorField, gbc);

        // Gatunek
        gbc.gridy = ++row; gbc.gridx = 0; gbc.weightx = 0.3;
        content.add(new JLabel("Gatunek:"), gbc);
        JTextField genreField = new JTextField(15);
        gbc.gridx = 1; gbc.weightx = 0.7;
        content.add(genreField, gbc);

        // Status
        gbc.gridy = ++row; gbc.gridx = 0; gbc.weightx = 0.3;
        content.add(new JLabel("Status:"), gbc);
        BookStatus[] allowedStatuses = {
                BookStatus.DOSTEPNA,
                BookStatus.WSTRZYMANA
        };
        JComboBox<BookStatus> statusBox = new JComboBox<>(allowedStatuses);
        gbc.gridx = 1; gbc.weightx = 0.7;
        content.add(statusBox, gbc);

        // Przycisk
        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 1;
        JButton btnSave = new JButton("Zapisz");
        content.add(btnSave, gbc);

        // --- LOGIKA WYBORU ---

        Runnable updateBookList = () -> {
            Sector selectedSector = (Sector) sectorBox.getSelectedItem();
            bookBox.removeAllItems();
            if (selectedSector != null) {
                for (Book b : selectedSector.getBooks()) {
                    if (!(b.getStatus() == BookStatus.WYPOZYCZONA)){
                        bookBox.addItem(b);
                    }
                }
            }
        };

        Runnable updateBookFields = () -> {
            Book selectedBook = (Book) bookBox.getSelectedItem();
            if (selectedBook != null) {
                titleField.setText(selectedBook.getTitle());
                authorField.setText(selectedBook.getAuthor());
                genreField.setText(selectedBook.getGenre());
                statusBox.setSelectedItem(selectedBook.getStatus());
            } else {
                titleField.setText("");
                authorField.setText("");
                genreField.setText("");
                statusBox.setSelectedIndex(0);
            }
        };

        sectorBox.addActionListener(e -> {
            updateBookList.run();
            updateBookFields.run();
        });

        bookBox.addActionListener(e -> updateBookFields.run());

        btnSave.addActionListener(e -> {
            Book selectedBook = (Book) bookBox.getSelectedItem();
            if (selectedBook == null) {
                JOptionPane.showMessageDialog(this, "Najpierw wybierz książkę!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String newTitle = titleField.getText().trim();
            String newAuthor = authorField.getText().trim();
            String newGenre = genreField.getText().trim();
            BookStatus newStatus = (BookStatus) statusBox.getSelectedItem();

            if (newTitle.isEmpty() || newAuthor.isEmpty() || newGenre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Wszystkie pola muszą być wypełnione!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            bookController.updateBook(selectedBook, newTitle, newAuthor, newGenre, newStatus);


            JOptionPane.showMessageDialog(this, "Książka zaktualizowana!");
            dispose();
        });

        // Inicjalizacja
        if (sectorBox.getItemCount() > 0) {
            sectorBox.setSelectedIndex(0);
            updateBookList.run();
            updateBookFields.run();
        }

        setContentPane(content);
        setVisible(true);
    }
}
