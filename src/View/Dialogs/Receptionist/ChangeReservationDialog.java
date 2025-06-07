package View.Dialogs.Receptionist;

import Controller.ClientController;
import Controller.ReservationController;
import Model.Client;
import Model.Reservation;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class ChangeReservationDialog extends JDialog {
    public ChangeReservationDialog(ClientController clientController, ReservationController reservationController) {
        setTitle("Zmiana rezerwacji");
        setModal(true);
        setSize(480, 350);
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

        // 5. Pola do zmiany dat
        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        content.add(new JLabel("Data rozpoczęcia:"), gbc);
        JTextField dateFromField = new JTextField(10);
        gbc.gridx = 1; gbc.weightx = 0.7;
        content.add(dateFromField, gbc);

        gbc.gridy = ++row; gbc.gridx = 0; gbc.weightx = 0.3;
        content.add(new JLabel("Data zakończenia:"), gbc);
        JTextField dateToField = new JTextField(10);
        gbc.gridx = 1; gbc.weightx = 0.7;
        content.add(dateToField, gbc);

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
                dateFromField.setText(selectedReservation.getStartDate().toString());
                dateToField.setText(selectedReservation.getEndDate().toString());
                btnChange.setEnabled(true);
            } else {
                btnChange.setEnabled(false);
            }
        });

        btnChange.addActionListener(e -> {
            Reservation selectedReservation = (Reservation) reservationBox.getSelectedItem();
            if (selectedReservation == null) return;
            try {
                LocalDate dateFrom = LocalDate.parse(dateFromField.getText().trim());
                LocalDate dateTo = LocalDate.parse(dateToField.getText().trim());
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
}
