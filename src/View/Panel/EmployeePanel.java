package View.Panel;

import Controller.BookController;
import Controller.ClientController;
import View.Dialogs.Employee.ShowAvailableBooksDialog;
import View.Dialogs.Employee.ShowBooksDialog;
import View.Dialogs.Employee.ShowReservationHistoryDialog;

import javax.swing.*;
import java.awt.*;

public class EmployeePanel extends JPanel {
    protected JPanel leftPanel;
    protected JPanel rightPanel;
    protected JLabel welcomeLabel;
    protected JPanel workButtonsPanel;

    public EmployeePanel(BookController bookController, ClientController clientController, String roleName) {
        setLayout(new BorderLayout());

        welcomeLabel = new JLabel("Witaj " + roleName, SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 20f));
        add(welcomeLabel, BorderLayout.NORTH);

        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        JPanel employeeButtonsPanel = new JPanel(new GridLayout(0, 1, 10, 10));

        JButton btnShowBooks = new JButton("Wyświetl listę książek");
        JButton btnShowAvailableBooks = new JButton("Wyświetl dostępne książki");
        JButton btnShowReservationHistory = new JButton("Wyświetl historię rezerwacji klienta");

        btnShowBooks.addActionListener(_ -> new ShowBooksDialog(bookController));
        btnShowAvailableBooks.addActionListener(_ -> new ShowAvailableBooksDialog(bookController));
        btnShowReservationHistory.addActionListener(_ -> new ShowReservationHistoryDialog(clientController));
        employeeButtonsPanel.add(btnShowBooks);
        employeeButtonsPanel.add(btnShowAvailableBooks);
        employeeButtonsPanel.add(btnShowReservationHistory);

        JLabel leftLabel = new JLabel("Funkcje pracownicze:", SwingConstants.CENTER);
        leftLabel.setFont(leftLabel.getFont().deriveFont(Font.PLAIN, 14f));

        leftPanel.add(leftLabel, BorderLayout.NORTH);
        leftPanel.add(employeeButtonsPanel, BorderLayout.CENTER);

        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        JLabel rightLabel = new JLabel("Twoja praca:", SwingConstants.CENTER);
        rightLabel.setFont(rightLabel.getFont().deriveFont(Font.PLAIN, 14f));
        rightPanel.add(rightLabel, BorderLayout.NORTH);

        workButtonsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        rightPanel.add(workButtonsPanel, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    protected JPanel getWorkButtonsPanel() {
        return workButtonsPanel;
    }
}