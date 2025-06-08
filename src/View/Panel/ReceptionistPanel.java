package View.Panel;

import Controller.BookController;
import Controller.ClientController;
import Controller.ReservationController;
import View.Dialogs.Receptionist.AddNewClientDialog;
import View.Dialogs.Receptionist.ReserveBookDialog;
import View.Dialogs.Receptionist.ChangeReservationDialog;
import View.Dialogs.Receptionist.CancelReservationDialog;

import javax.swing.*;

public class ReceptionistPanel extends EmployeePanel {
    public ReceptionistPanel(BookController bookController, ClientController clientController, ReservationController reservationController) {
        super(bookController, clientController, reservationController, "Recepcjonistko!");

        JPanel workButtonsPanel = getWorkButtonsPanel();

        JButton btnAddClient = new JButton("Dodaj klienta");
        JButton btnReserveBook = new JButton("Rezerwuj książkę");
        JButton btnChangeReservation = new JButton("Zmień rezerwację");
        JButton btnCancelReservation = new JButton("Anuluj rezerwację");

        btnAddClient.addActionListener(e -> new AddNewClientDialog(clientController));
        btnReserveBook.addActionListener(e -> new ReserveBookDialog(bookController, clientController, reservationController));
        btnChangeReservation.addActionListener(e -> new ChangeReservationDialog(clientController, reservationController));
        btnCancelReservation.addActionListener(e -> new CancelReservationDialog(clientController, reservationController));

        workButtonsPanel.add(btnAddClient);
        workButtonsPanel.add(btnReserveBook);
        workButtonsPanel.add(btnChangeReservation);
        workButtonsPanel.add(btnCancelReservation);
    }
}