package View.Panel;

import Controller.BookController;
import Controller.ClientController;
import Controller.ReservationController;

import javax.swing.*;
import java.awt.*;

public class EmployeePanel extends JPanel {

    public EmployeePanel(BookController bookController, ClientController clientController, ReservationController reservationController) {
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
        btnShowBooks.addActionListener(e -> {
            // Kod do wyświetlenia listy książek
        });
        btnShowAvailableBooks.addActionListener(e -> {
            // Kod do wyświetlenia dostępnych książek
        });
        btnShowHistory.addActionListener(e -> {
            // Kod do wyświetlenia historii rezerwacji klienta
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

