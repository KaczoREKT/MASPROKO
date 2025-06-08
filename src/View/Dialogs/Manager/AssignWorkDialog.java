package View.Dialogs.Manager;

import Controller.LibrarianController;
import Controller.SectorController;
import Controller.SortingJobController;
import Model.Librarian;
import Model.Sector;
import View.ComboBox.LibrarianJComboBox;
import View.ComboBox.SectorJComboBox;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

public class AssignWorkDialog extends JDialog {
    public AssignWorkDialog(LibrarianController librarianController,
                            SectorController sectorController,
                            SortingJobController sortingJobController) {
        setTitle("Przypisz zadanie sortowania");
        setModal(true);
        setSize(480, 380);
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

        List<Librarian> librarians = librarianController.getList();
        JComboBox<Librarian> librarianBox = new LibrarianJComboBox(librarians);
        gbc.gridx = 1;
        content.add(librarianBox, gbc);

        // Sektor
        gbc.gridy = ++row; gbc.gridx = 0;
        content.add(new JLabel("Sektor:"), gbc);

        List<Sector> sectors = sectorController.getList();
        JComboBox<Sector> sectorBox = new SectorJComboBox(sectors);
        gbc.gridx = 1;
        content.add(sectorBox, gbc);

        // ==== DATA ROZPOCZĘCIA ====
        gbc.gridy = ++row; gbc.gridx = 0;
        content.add(new JLabel("Data rozpoczęcia:"), gbc);
        JPanel dateFromPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        // Rok
        JComboBox<Integer> yearFromBox = new JComboBox<>();
        int currentYear = LocalDate.now().getYear();
        for (int y = currentYear; y <= currentYear + 2; y++) yearFromBox.addItem(y);
        dateFromPanel.add(yearFromBox);
        dateFromPanel.add(new JLabel("r."));
        // Miesiąc
        JComboBox<Integer> monthFromBox = new JComboBox<>();
        for (int m = 1; m <= 12; m++) monthFromBox.addItem(m);
        dateFromPanel.add(monthFromBox);
        dateFromPanel.add(new JLabel("mies."));
        // Dzień (dynamicznie zmieniamy!)
        JComboBox<Integer> dayFromBox = new JComboBox<>();
        fillDays(dayFromBox, (int) yearFromBox.getSelectedItem(), (int) monthFromBox.getSelectedItem());
        dateFromPanel.add(dayFromBox);
        dateFromPanel.add(new JLabel("d."));

        // Obsługa dynamicznej zmiany liczby dni
        monthFromBox.addActionListener(_ -> fillDays(dayFromBox, (int) yearFromBox.getSelectedItem(), (int) monthFromBox.getSelectedItem()));
        yearFromBox.addActionListener(_ -> fillDays(dayFromBox, (int) yearFromBox.getSelectedItem(), (int) monthFromBox.getSelectedItem()));

        gbc.gridx = 1;
        content.add(dateFromPanel, gbc);

        // Godzina rozpoczęcia
        gbc.gridy = ++row; gbc.gridx = 0;
        content.add(new JLabel("Godzina rozpoczęcia:"), gbc);
        JPanel timeFromPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        JComboBox<Integer> hourFromBox = new JComboBox<>();
        JComboBox<Integer> minFromBox = new JComboBox<>();
        for (int h = 0; h < 24; h++) hourFromBox.addItem(h);
        for (int m = 0; m < 60; m++) minFromBox.addItem(m);
        timeFromPanel.add(hourFromBox);
        timeFromPanel.add(new JLabel(":"));
        timeFromPanel.add(minFromBox);
        gbc.gridx = 1;
        content.add(timeFromPanel, gbc);

        // ==== DATA ZAKOŃCZENIA ====
        gbc.gridy = ++row; gbc.gridx = 0;
        content.add(new JLabel("Data zakończenia:"), gbc);
        JPanel dateToPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        JComboBox<Integer> yearToBox = new JComboBox<>();
        for (int y = currentYear; y <= currentYear + 2; y++) yearToBox.addItem(y);
        dateToPanel.add(yearToBox);
        dateToPanel.add(new JLabel("r."));
        JComboBox<Integer> monthToBox = new JComboBox<>();
        for (int m = 1; m <= 12; m++) monthToBox.addItem(m);
        dateToPanel.add(monthToBox);
        dateToPanel.add(new JLabel("mies."));
        JComboBox<Integer> dayToBox = new JComboBox<>();
        fillDays(dayToBox, (int) yearToBox.getSelectedItem(), (int) monthToBox.getSelectedItem());
        dateToPanel.add(dayToBox);
        dateToPanel.add(new JLabel("d."));

        monthToBox.addActionListener(_ -> fillDays(dayToBox, (int) yearToBox.getSelectedItem(), (int) monthToBox.getSelectedItem()));
        yearToBox.addActionListener(_ -> fillDays(dayToBox, (int) yearToBox.getSelectedItem(), (int) monthToBox.getSelectedItem()));

        gbc.gridx = 1;
        content.add(dateToPanel, gbc);

        // Godzina zakończenia
        gbc.gridy = ++row; gbc.gridx = 0;
        content.add(new JLabel("Godzina zakończenia:"), gbc);
        JPanel timeToPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        JComboBox<Integer> hourToBox = new JComboBox<>();
        JComboBox<Integer> minToBox = new JComboBox<>();
        for (int h = 0; h < 24; h++) hourToBox.addItem(h);
        for (int m = 0; m < 60; m++) minToBox.addItem(m);
        timeToPanel.add(hourToBox);
        timeToPanel.add(new JLabel(":"));
        timeToPanel.add(minToBox);
        gbc.gridx = 1;
        content.add(timeToPanel, gbc);

        // Przycisk
        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton btnAssign = new JButton("Przypisz");
        JButton btnCancel = new JButton("Anuluj");
        btnPanel.add(btnAssign);
        btnPanel.add(btnCancel);
        content.add(btnPanel, gbc);

        // LOGIKA przycisków
        btnAssign.addActionListener(_ -> {
            Librarian librarian = (Librarian) librarianBox.getSelectedItem();
            Sector sector = (Sector) sectorBox.getSelectedItem();

            try {
                int yearFrom = (int) yearFromBox.getSelectedItem();
                int monthFrom = (int) monthFromBox.getSelectedItem();
                int dayFrom = (int) dayFromBox.getSelectedItem();
                int hourFrom = (int) hourFromBox.getSelectedItem();
                int minFrom = (int) minFromBox.getSelectedItem();

                int yearTo = (int) yearToBox.getSelectedItem();
                int monthTo = (int) monthToBox.getSelectedItem();
                int dayTo = (int) dayToBox.getSelectedItem();
                int hourTo = (int) hourToBox.getSelectedItem();
                int minTo = (int) minToBox.getSelectedItem();

                LocalDateTime fromDateTime = LocalDateTime.of(yearFrom, monthFrom, dayFrom, hourFrom, minFrom);
                LocalDateTime toDateTime = LocalDateTime.of(yearTo, monthTo, dayTo, hourTo, minTo);

                if (!toDateTime.isAfter(fromDateTime))
                    throw new Exception("Data i godzina zakończenia musi być po rozpoczęciu!");

                if (librarian == null || sector == null)
                    throw new Exception("Wybierz bibliotekarza i sektor.");

                sortingJobController.assignSortingJob(librarian, sector, fromDateTime, toDateTime);
                JOptionPane.showMessageDialog(this, "Zadanie przypisane!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(_ -> dispose());

        setContentPane(content);
        setVisible(true);
    }

    // Pomocnicza metoda do uzupełniania dni miesiąca
    private static void fillDays(JComboBox<Integer> dayBox, int year, int month) {
        dayBox.removeAllItems();
        int days = YearMonth.of(year, month).lengthOfMonth();
        for (int d = 1; d <= days; d++) dayBox.addItem(d);
    }
}
