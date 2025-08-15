package View.Panel;

import Controller.BookController;
import Controller.ClientController;
import View.Dialogs.Employee.ShowAvailableBooksDialog;
import View.Dialogs.Employee.ShowBooksDialog;
import View.Dialogs.Employee.ShowReservationHistoryDialog;

import javax.swing.*;
import java.awt.*;

public class EmployeePanel extends JPanel {

    private JPanel leftTopPanel;
    private JPanel rightTopPanel;
    private JPanel resultsPanel;
    private JLabel welcomeLabel;

    public EmployeePanel(BookController bookController, ClientController clientController, String roleName) {
        setLayout(new BorderLayout());

        // Welcome label at the top
        welcomeLabel = new JLabel("Witaj " + roleName, SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 20f));
        add(welcomeLabel, BorderLayout.NORTH);

        // Left top panel with 3 main function buttons
        leftTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton btnShowBooks = new JButton("Wyświetl listę książek");
        JButton btnShowAvailableBooks = new JButton("Wyświetl dostępne książki");
        JButton btnShowReservationHistory = new JButton("Wyświetl historię rezerwacji klienta");

        // Add action listeners
        btnShowBooks.addActionListener(_ -> new ShowBooksDialog(bookController));
        btnShowAvailableBooks.addActionListener(_ -> new ShowAvailableBooksDialog(bookController));
        btnShowReservationHistory.addActionListener(_ -> new ShowReservationHistoryDialog(clientController));

        leftTopPanel.add(btnShowBooks);
        leftTopPanel.add(btnShowAvailableBooks);
        leftTopPanel.add(btnShowReservationHistory);

        // Right top panel with logout and extensions buttons
        rightTopPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnLogout = new JButton("Wyloguj");
        JButton btnExtensions = new JButton("Ekstensje");

        rightTopPanel.add(btnLogout);
        rightTopPanel.add(btnExtensions);

        // Container panel for top controls
        JPanel topControlsPanel = new JPanel(new BorderLayout());
        topControlsPanel.add(leftTopPanel, BorderLayout.WEST);
        topControlsPanel.add(rightTopPanel, BorderLayout.EAST);

        // Create a panel to hold the welcome label and top controls
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(welcomeLabel, BorderLayout.NORTH);
        headerPanel.add(topControlsPanel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Results panel below buttons - this is where dialog results will be displayed
        resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Wyniki operacji"));
        resultsPanel.setPreferredSize(new Dimension(800, 400));

        add(resultsPanel, BorderLayout.CENTER);
    }

    public JPanel getResultsPanel() {
        return resultsPanel;
    }

    // Method to clear results panel
    public void clearResults() {
        resultsPanel.removeAll();
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    // Method to add content to results panel
    public void setResultsContent(JComponent component) {
        clearResults();
        resultsPanel.add(component, BorderLayout.CENTER);
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    public JPanel getWorkButtonsPanel(){
        return leftTopPanel;
    }
}
