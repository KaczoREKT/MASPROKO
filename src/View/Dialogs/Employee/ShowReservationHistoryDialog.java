package View.Dialogs.Employee;

import Controller.ClientController;
import Model.Client;
import Model.Reservation;
import Model.Book;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ShowReservationHistoryDialog {

    private JPanel mainPanel;
    private JTable reservationTable;
    private JComboBox<Client> clientBox;

    public ShowReservationHistoryDialog(ClientController clientController) {
        mainPanel = new JPanel(new BorderLayout(10,10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Wybierz klienta:"));

        List<Client> clients = clientController.getList();
        clientBox = new JComboBox<>(clients.toArray(new Client[0]));
        clientBox.setPreferredSize(new Dimension(300, 25));
        topPanel.add(clientBox);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Inicjalizacja tabeli z pustymi danymi
        String[] columnNames = {"ID", "Od", "Do", "Status", "Książki"};
        Object[][] emptyData = {{"Wybierz klienta aby zobaczyć rezerwacje.", "", "", "", ""}};

        reservationTable = new JTable(emptyData, columnNames);
        JScrollPane scrollPane = new JScrollPane(reservationTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        clientBox.addActionListener(_ -> {
            Client selectedClient = (Client) clientBox.getSelectedItem();

            if (selectedClient == null) {
                Object[][] noClientData = {{"Brak wybranego klienta.", "", "", "", ""}};
                reservationTable.setModel(new javax.swing.table.DefaultTableModel(noClientData, columnNames));
                return;
            }

            Set<Reservation> reservations = selectedClient.getReservations();
            Object[][] rowData;

            if (reservations.isEmpty()) {
                rowData = new Object[][]{{"Brak rezerwacji.", "", "", "", ""}};
            } else {
                rowData = reservations.stream()
                        .map(r -> {
                            // Lista tytułów książek w rezerwacji
                            String books = r.getBooks().stream()
                                    .map(Book::getTitle)
                                    .collect(Collectors.joining(", "));
                            return new Object[]{
                                    r.getPublicId(),
                                    r.getStartDate() != null ? r.getStartDate().toString() : "Brak daty",
                                    r.getEndDate() != null ? r.getEndDate().toString() : "Brak daty",
                                    r.getStatus() != null ? r.getStatus().name() : "Brak statusu",
                                    books,
                            };
                        })
                        .toArray(Object[][]::new);
            }

            reservationTable.setModel(new javax.swing.table.DefaultTableModel(rowData, columnNames));
        });

        if (!clients.isEmpty()) {
            clientBox.setSelectedIndex(0);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
