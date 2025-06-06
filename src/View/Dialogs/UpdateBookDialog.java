package View.Dialogs;

import Controller.SectorController;
import Model.Book;
import Model.BookStatus;
import Model.Sector;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UpdateBookDialog extends JDialog {
    public UpdateBookDialog(SectorController sectorController) {
        setTitle("Aktualizacja książki");
        setModal(true);
        setSize(500, 350);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.gridx = 0; gbc.gridy = 0;

        JLabel sectorLabel = new JLabel("Sektor:");
        add(sectorLabel, gbc);

        // PRZEKAZYWANIE SEKTORÓW
        java.util.List<Sector> sectors = sectorController.getAllSectors();
        JComboBox<Sector> sectorBox = new JComboBox<>(sectors.toArray(new Sector[0]));
        gbc.gridx = 1;
        add(sectorBox, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel bookLabel = new JLabel("Książka:");
        add(bookLabel, gbc);

        JComboBox<Book> bookBox = new JComboBox<>();
        gbc.gridx = 1;
        add(bookBox, gbc);

        // Pola do edycji
        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Tytuł:"), gbc);
        JTextField titleField = new JTextField(15);
        gbc.gridx = 1;
        add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Autor:"), gbc);
        JTextField authorField = new JTextField(15);
        gbc.gridx = 1;
        add(authorField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Gatunek:"), gbc);
        JTextField genreField = new JTextField(15);
        gbc.gridx = 1;
        add(genreField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Status:"), gbc);
        JComboBox<BookStatus> statusBox = new JComboBox<>(BookStatus.values());
        gbc.gridx = 1;
        add(statusBox, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        JButton btnSave = new JButton("Zapisz");
        add(btnSave, gbc);

        // --- LOGIKA WYBORU ---

        // Funkcja do wypełniania książek po zmianie sektora
        Runnable updateBookList = () -> {
            Sector selectedSector = (Sector) sectorBox.getSelectedItem();
            bookBox.removeAllItems();
            if (selectedSector != null) {
                for (Book b : selectedSector.getBooks()) {
                    bookBox.addItem(b);
                }
            }
        };

        // Funkcja do wypełniania pól po wyborze książki
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

        bookBox.addActionListener(e -> {
            updateBookFields.run();
        });

        // Zapisz zmiany
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

            selectedBook.setTitle(newTitle);
            selectedBook.setAuthor(newAuthor);
            selectedBook.setGenre(newGenre);
            selectedBook.setStatus(newStatus);

            JOptionPane.showMessageDialog(this, "Książka zaktualizowana!");
            dispose();
        });

        // Wstępne wypełnienie listy książek i pól (dla domyślnego sektora)
        if (sectorBox.getItemCount() > 0) {
            sectorBox.setSelectedIndex(0);
            updateBookList.run();
            updateBookFields.run();
        }

        setVisible(true);
    }
}