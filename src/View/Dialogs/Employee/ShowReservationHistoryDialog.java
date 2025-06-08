package View.Dialogs.Employee;

import Controller.ClientController;
import Model.Client;
import Model.Reservation;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;

public class ShowReservationHistoryDialog extends JDialog {
    public ShowReservationHistoryDialog(ClientController clientController) {
        setTitle("Historia rezerwacji klienta");
        setModal(true);
        setSize(600, 350);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new BorderLayout(10,10));

        // Panel górny: wybór klienta
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Wybierz klienta:"));

        List<Client> clients = clientController.getClientList();
        JComboBox<Client> clientBox = new JComboBox<>(clients.toArray(new Client[0]));
        clientBox.setPreferredSize(new Dimension(300, 25));
        topPanel.add(clientBox);

        content.add(topPanel, BorderLayout.NORTH);

        // Panel centralny: lista rezerwacji
        DefaultListModel<String> reservationListModel = new DefaultListModel<>();
        JList<String> reservationList = new JList<>(reservationListModel);
        JScrollPane scrollPane = new JScrollPane(reservationList);

        content.add(scrollPane, BorderLayout.CENTER);

        // Logika – zmiana wyboru klienta od razu wyświetla rezerwacje
        clientBox.addActionListener(_ -> {
            Client selectedClient = (Client) clientBox.getSelectedItem();
            reservationListModel.clear();
            if (selectedClient == null) return;
            Set<Reservation> reservations = selectedClient.getReservations();
            if (reservations.isEmpty()) {
                reservationListModel.addElement("Brak rezerwacji.");
            } else {
                for (Reservation r : reservations) {
                    reservationListModel.addElement(r.toString());
                }
            }
        });

        // Na start wyświetl rezerwacje pierwszego klienta (jeśli są)
        if (!clients.isEmpty()) {
            clientBox.setSelectedIndex(0);
        }

        setContentPane(content);
        setVisible(true);
    }
}
