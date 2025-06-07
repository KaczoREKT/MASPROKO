package View.Dialogs.Manager;

import Controller.LibrarianController;
import Controller.SectorController;
import Controller.SortingJobController;
import Model.Librarian;
import Model.Sector;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class AssignWorkDialog extends JDialog {
    public AssignWorkDialog(LibrarianController librarianController,
                            SectorController sectorController,
                            SortingJobController sortingJobController) {
        setTitle("Przypisz zadanie sortowania");
        setModal(true);
        setSize(450, 320);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        int row = 0;

        // Bibliotekarz
        gbc.gridx = 0; gbc.gridy = row;
        content.add(new JLabel("Bibliotekarz:"), gbc);

        List<Librarian> librarians = librarianController.getLibrarianList();
        JComboBox<Librarian> librarianBox = new JComboBox<>(librarians.toArray(new Librarian[0]));
        gbc.gridx = 1;
        content.add(librarianBox, gbc);

        // Sektor
        gbc.gridy = ++row; gbc.gridx = 0;
        content.add(new JLabel("Sektor:"), gbc);

        List<Sector> sectors = sectorController.getAllSectors();
        JComboBox<Sector> sectorBox = new JComboBox<>(sectors.toArray(new Sector[0]));
        gbc.gridx = 1;
        content.add(sectorBox, gbc);

        // Data rozpoczęcia
        gbc.gridy = ++row; gbc.gridx = 0;
        content.add(new JLabel("Data rozpoczęcia:"), gbc);
        JTextField dateFromField = new JTextField(LocalDate.now().toString(), 10);
        gbc.gridx = 1;
        content.add(dateFromField, gbc);

        // Data zakończenia
        gbc.gridy = ++row; gbc.gridx = 0;
        content.add(new JLabel("Data zakończenia:"), gbc);
        JTextField dateToField = new JTextField(LocalDate.now().plusDays(3).toString(), 10);
        gbc.gridx = 1;
        content.add(dateToField, gbc);

        // Przycisk
        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JButton btnAssign = new JButton("Przypisz");
        content.add(btnAssign, gbc);

        // Przycisk anuluj
        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 2;
        JButton btnCancel = new JButton("Anuluj");
        content.add(btnCancel, gbc);

        btnAssign.addActionListener(e -> {
            Librarian librarian = (Librarian) librarianBox.getSelectedItem();
            Sector sector = (Sector) sectorBox.getSelectedItem();
            LocalDate from;
            LocalDate to;
            try {
                from = LocalDate.parse(dateFromField.getText().trim());
                to = LocalDate.parse(dateToField.getText().trim());
                if (to.isBefore(from)) throw new Exception("Data zakończenia przed rozpoczęcia!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Niepoprawny format daty lub zakres.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (librarian == null || sector == null) {
                JOptionPane.showMessageDialog(this, "Wybierz bibliotekarza i sektor.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                sortingJobController.assignSortingJob(librarian, sector, from, to);
                JOptionPane.showMessageDialog(this, "Zadanie przypisane!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Błąd przypisywania", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dispose());

        setContentPane(content);
        setVisible(true);
    }
}
