package View.Dialogs.Receptionist;

import Controller.ClientController;
import Controller.ReservationController;
import Model.Client;
import Model.Reservation;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Set;

public class ChangeReservationDialog extends JDialog {
    public ChangeReservationDialog(ClientController clientController, ReservationController reservationController) {
        setTitle("Zmiana rezerwacji");
        setModal(true);
        setSize(500, 380);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        int row = 0;

        // 1. Numer karty klienta
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        content.add(new JLabel("Numer karty klienta:"), gbc);
        JTextField cardNumberField = new JTextField(15);
        gbc.gridx = 1; gbc.weightx = 0.7;
        content.add(cardNumberField, gbc);

        // 2. Przycisk "Znajdź klienta"
        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JButton btnFindClient = new JButton("Znajdź klienta");
        content.add(btnFindClient, gbc);

        // 3. Info o kliencie
        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 2;
        JLabel clientInfoLabel = new JLabel("Brak klienta.");
        content.add(clientInfoLabel, gbc);

        // 4. Lista rezerwacji klienta
        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 2;
        JComboBox<Reservation> reservationBox = new JComboBox<>();
        reservationBox.setEnabled(false);
        content.add(reservationBox, gbc);

        // 5. Pola do zmiany dat (ComboBoxy)
        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        content.add(new JLabel("Data rozpoczęcia:"), gbc);

        JPanel dateFromPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        JComboBox<Integer> yearFromBox = new JComboBox<>();
        JComboBox<Integer> monthFromBox = new JComboBox<>();
        JComboBox<Integer> dayFromBox = new JComboBox<>();
        int currentYear = LocalDate.now().getYear();
        for (int y = currentYear; y <= currentYear + 2; y++) yearFromBox.addItem(y);
        for (int m = 1; m <= 12; m++) monthFromBox.addItem(m);
        fillDays(dayFromBox, (int) yearFromBox.getSelectedItem(), (int) monthFromBox.getSelectedItem());
        dateFromPanel.add(yearFromBox); dateFromPanel.add(new JLabel("r."));
        dateFromPanel.add(monthFromBox); dateFromPanel.add(new JLabel("mies."));
        dateFromPanel.add(dayFromBox); dateFromPanel.add(new JLabel("d."));
        monthFromBox.addActionListener(e -> fillDays(dayFromBox, (int) yearFromBox.getSelectedItem(), (int) monthFromBox.getSelectedItem()));
        yearFromBox.addActionListener(e -> fillDays(dayFromBox, (int) yearFromBox.getSelectedItem(), (int) monthFromBox.getSelectedItem()));
        gbc.gridx = 1;
        content.add(dateFromPanel, gbc);

        // Data zakończenia
        gbc.gridy = ++row; gbc.gridx = 0;
        content.add(new JLabel("Data zakończenia:"), gbc);

        JPanel dateToPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        JComboBox<Integer> yearToBox = new JComboBox<>();
        JComboBox<Integer> monthToBox = new JComboBox<>();
        JComboBox<Integer> dayToBox = new JComboBox<>();
        for (int y = currentYear; y <= currentYear + 2; y++) yearToBox.addItem(y);
        for (int m = 1; m <= 12; m++) monthToBox.addItem(m);
        fillDays(dayToBox, (int) yearToBox.getSelectedItem(), (int) monthToBox.getSelectedItem());
        dateToPanel.add(yearToBox); dateToPanel.add(new JLabel("r."));
        dateToPanel.add(monthToBox); dateToPanel.add(new JLabel("mies."));
        dateToPanel.add(dayToBox); dateToPanel.add(new JLabel("d."));
        monthToBox.addActionListener(e -> fillDays(dayToBox, (int) yearToBox.getSelectedItem(), (int) monthToBox.getSelectedItem()));
        yearToBox.addActionListener(e -> fillDays(dayToBox, (int) yearToBox.getSelectedItem(), (int) monthToBox.getSelectedItem()));
        gbc.gridx = 1;
        content.add(dateToPanel, gbc);

        // 6. Przycisk "Zmień"
        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.CENTER;
        JButton btnChange = new JButton("Zmień");
        btnChange.setEnabled(false);
        content.add(btnChange, gbc);

        // 7. Przycisk "Anuluj"
        gbc.gridx = 1;
        JButton btnCancel = new JButton("Anuluj");
        content.add(btnCancel, gbc);

        // LOGIKA
        final Client[] foundClient = {null};

        btnFindClient.addActionListener(e -> {
            String cardNumber = cardNumberField.getText().trim();
            Client client = clientController.findClientByCardNumber(cardNumber);
            foundClient[0] = client;
            reservationBox.removeAllItems();
            reservationBox.setEnabled(false);
            btnChange.setEnabled(false);
            if (client == null) {
                clientInfoLabel.setText("Nie znaleziono klienta.");
            } else {
                clientInfoLabel.setText(String.format(
                        "<html>Imię: %s<br/>Nazwisko: %s<br/>Email: %s<br/>Telefon: %s</html>",
                        client.getFirstName(), client.getLastName(), client.getEmail(), client.getPhoneNumber()
                ));
                Set<Reservation> reservations = client.getReservations();
                if (reservations.isEmpty()) {
                    clientInfoLabel.setText(clientInfoLabel.getText() + "<br/>Brak rezerwacji.");
                } else {
                    for (Reservation r : reservations) {
                        reservationBox.addItem(r);
                    }
                    reservationBox.setEnabled(true);
                }
            }
        });

        reservationBox.addActionListener(e -> {
            Reservation selectedReservation = (Reservation) reservationBox.getSelectedItem();
            if (selectedReservation != null) {
                LocalDate from = selectedReservation.getStartDate();
                LocalDate to = selectedReservation.getEndDate();
                // Ustaw datę rozpoczęcia
                yearFromBox.setSelectedItem(from.getYear());
                monthFromBox.setSelectedItem(from.getMonthValue());
                fillDays(dayFromBox, from.getYear(), from.getMonthValue());
                dayFromBox.setSelectedItem(from.getDayOfMonth());
                // Ustaw datę zakończenia
                yearToBox.setSelectedItem(to.getYear());
                monthToBox.setSelectedItem(to.getMonthValue());
                fillDays(dayToBox, to.getYear(), to.getMonthValue());
                dayToBox.setSelectedItem(to.getDayOfMonth());
                btnChange.setEnabled(true);
            } else {
                btnChange.setEnabled(false);
            }
        });

        btnChange.addActionListener(e -> {
            Reservation selectedReservation = (Reservation) reservationBox.getSelectedItem();
            if (selectedReservation == null) return;
            try {
                LocalDate dateFrom = LocalDate.of(
                        (int) yearFromBox.getSelectedItem(),
                        (int) monthFromBox.getSelectedItem(),
                        (int) dayFromBox.getSelectedItem()
                );
                LocalDate dateTo = LocalDate.of(
                        (int) yearToBox.getSelectedItem(),
                        (int) monthToBox.getSelectedItem(),
                        (int) dayToBox.getSelectedItem()
                );
                reservationController.changeReservation(selectedReservation, dateFrom, dateTo);
                JOptionPane.showMessageDialog(this, "Zmieniono rezerwację.");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dispose());

        setContentPane(content);
        setVisible(true);
    }

    // Pomocnicza metoda do ustawiania dni miesiąca
    private static void fillDays(JComboBox<Integer> dayBox, int year, int month) {
        dayBox.removeAllItems();
        int days = java.time.YearMonth.of(year, month).lengthOfMonth();
        for (int d = 1; d <= days; d++) dayBox.addItem(d);
    }
}
