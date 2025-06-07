package View.Panel;

import Controller.BookController;
import Controller.ClientController;
import Controller.ReservationController;
import Model.Client;
import View.Dialogs.Receptionist.AddNewClientDialog;
import View.Dialogs.Receptionist.CancelReservationDialog;
import View.Dialogs.Receptionist.ChangeReservationDialog;
import View.Dialogs.Receptionist.ReserveBookDialog;

import javax.swing.*;
import java.awt.*;

public class ReceptionistPanel extends JPanel {

    public ReceptionistPanel(BookController bookController, ClientController clientController, ReservationController reservationController) {
        setLayout(new GridLayout(0, 1, 10, 10));

        add(new JLabel("Panel Recepcjonisty", SwingConstants.CENTER));
        JButton btnRegisterClient = new JButton("Rejestracja nowego klienta");
        JButton btnShowBooks = new JButton("Wyświetl listę książek");
        JButton btnShowAvailableBooks = new JButton("Wyświetl dostępne książki");
        JButton btnShowHistory = new JButton("Wyświetl historię rezerwacji klienta");
        JButton btnReserveBook = new JButton("Rezerwacja książki");
        JButton btnCancelReservation = new JButton("Anulowanie rezerwacji książki");
        JButton btnChangeReservation = new JButton("Zmiana terminu rezerwacji książki");

        // Dodaj obsługę przycisków tutaj (np. wywołania metod, przełączanie paneli itd.)
        btnRegisterClient.addActionListener(e -> {
            new AddNewClientDialog(clientController);
        });
        btnReserveBook.addActionListener(e -> {
           new ReserveBookDialog(bookController, clientController, reservationController);
        });
        btnCancelReservation.addActionListener(e -> {
            new CancelReservationDialog(clientController, reservationController);
        });
        btnChangeReservation.addActionListener(e -> {
            new ChangeReservationDialog(clientController, reservationController);
        });

        add(btnRegisterClient);
        add(btnShowBooks);
        add(btnShowAvailableBooks);
        add(btnShowHistory);
        add(btnReserveBook);
        add(btnCancelReservation);
        add(btnChangeReservation);
    }
}
