package View.Panel;

import Controller.BookController;
import Controller.ClientController;
import Controller.ReservationController;
import View.Dialogs.Receptionist.*;

import javax.swing.*;

public class ReceptionistPanel extends EmployeePanel {
    public ReceptionistPanel(BookController bookController, ClientController clientController, ReservationController reservationController) {
        super(bookController, clientController, reservationController, "Recepcjonistko!");

        JPanel workButtonsPanel = getWorkButtonsPanel();

        JButton btnAddClient = new JButton("Dodaj klienta");
        JButton btnReserveBook = new JButton("Rezerwuj książkę");
        JButton btnChangeReservation = new JButton("Zmień rezerwację");
        JButton btnCancelReservation = new JButton("Anuluj rezerwację");
        JButton btnReturnBook = new JButton("Zwróć książkę");

        btnAddClient.addActionListener(_ -> new AddNewClientDialog(clientController));
        btnReserveBook.addActionListener(_ -> new ReserveBookDialog(bookController, clientController, reservationController));
        btnChangeReservation.addActionListener(_ -> new ChangeReservationDialog(clientController, reservationController));
        btnCancelReservation.addActionListener(_ -> new CancelReservationDialog(clientController, reservationController));
        btnReturnBook.addActionListener(e -> new ReturnBookDialog(clientController, reservationController));

        workButtonsPanel.add(btnAddClient);
        workButtonsPanel.add(btnReserveBook);
        workButtonsPanel.add(btnChangeReservation);
        workButtonsPanel.add(btnCancelReservation);
        workButtonsPanel.add(btnReturnBook);
    }
}