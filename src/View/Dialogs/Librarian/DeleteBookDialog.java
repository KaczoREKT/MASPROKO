package View.Dialogs.Librarian;

import Controller.BookController;
import Controller.SectorController;
import Model.Book;
import Model.Sector;
import View.ComboBox.BookJComboBox;
import View.ComboBox.SectorJComboBox;

import javax.swing.*;
import java.awt.*;

public class DeleteBookDialog extends JDialog {
    public DeleteBookDialog(SectorController sectorController, BookController bookController) {
        setTitle("Usuń książkę");
        setModal(true);
        setSize(420, 220);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        content.add(new JLabel("Sektor:"), gbc);

        java.util.List<Sector> sectors = sectorController.getList();
        JComboBox<Sector> sectorBox = new SectorJComboBox(sectors);
        gbc.gridx = 1;
        content.add(sectorBox, gbc);

        gbc.gridy = ++row; gbc.gridx = 0;
        content.add(new JLabel("Książka:"), gbc);

        JComboBox<Book> bookBox = new BookJComboBox();
        gbc.gridx = 1;
        content.add(bookBox, gbc);

        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 1;
        JButton btnDelete = new JButton("Usuń");
        content.add(btnDelete, gbc);

        Runnable updateBookList = () -> {
            Sector selectedSector = (Sector) sectorBox.getSelectedItem();
            bookBox.removeAllItems();
            if (selectedSector != null) {
                for (Book b : bookController.getAvailableBooksInSector(selectedSector)) {
                    bookBox.addItem(b);
                }
            }
        };


        sectorBox.addActionListener(_ -> updateBookList.run());

        btnDelete.addActionListener(_ -> {
            Book selectedBook = (Book) bookBox.getSelectedItem();

            if (selectedBook == null) {
                JOptionPane.showMessageDialog(this, "Nie wybrano książki.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Czy na pewno chcesz usunąć tę książkę?",
                    "Potwierdź usunięcie", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Wywołanie logiki w kontrolerze!
                    bookController.deleteBook(selectedBook);

                    updateBookList.run();

                    JOptionPane.showMessageDialog(this, "Książka została usunięta.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        if (sectorBox.getItemCount() > 0) {
            sectorBox.setSelectedIndex(0);
            updateBookList.run();
        }

        setContentPane(content);
        setVisible(true);
    }
}
