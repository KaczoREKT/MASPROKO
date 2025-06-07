package View.Dialogs.Receptionist;

import Controller.ClientController;
import Controller.ReservationController;
import Model.Client;
import Model.Reservation;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class CancelReservationDialog extends JDialog {
    public CancelReservationDialog(ClientController clientController, ReservationController reservationController) {
        setTitle("Anulowanie rezerwacji");
        setModal(true);
        setSize(480, 250);
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

        // 5. Przycisk "Anuluj rezerwację"
        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.CENTER;
        JButton btnCancelReservation = new JButton("Anuluj rezerwację");
        btnCancelReservation.setEnabled(false);
        content.add(btnCancelReservation, gbc);

        // 6. Przycisk "Zamknij"
        gbc.gridx = 1;
        JButton btnClose = new JButton("Zamknij");
        content.add(btnClose, gbc);

        // LOGIKA
        final Client[] foundClient = {null};

        btnFindClient.addActionListener(e -> {
            String cardNumber = cardNumberField.getText().trim();
            Client client = clientController.findClientByCardNumber(cardNumber);
            foundClient[0] = client;
            reservationBox.removeAllItems();
            reservationBox.setEnabled(false);
            btnCancelReservation.setEnabled(false);
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
            btnCancelReservation.setEnabled(reservationBox.getSelectedItem() != null);
        });

        btnCancelReservation.addActionListener(e -> {
            Reservation selectedReservation = (Reservation) reservationBox.getSelectedItem();
            if (selectedReservation == null) return;
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Czy na pewno chcesz anulować tę rezerwację?",
                    "Potwierdź anulowanie",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    reservationController.cancelReservation(selectedReservation);
                    JOptionPane.showMessageDialog(this, "Rezerwacja została anulowana.");
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Jakiś błąd", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnClose.addActionListener(e -> dispose());

        setContentPane(content);
        setVisible(true);
    }
}
