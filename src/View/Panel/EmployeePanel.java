package View.Panel;

import Controller.BookController;
import Controller.ClientController;
import View.Dialogs.Employee.ShowAvailableBooksDialog;
import View.Dialogs.Employee.ShowBooksDialog;
import View.Dialogs.Employee.ShowReservationHistoryDialog;

import javax.swing.*;
import java.awt.*;

public class EmployeePanel extends JPanel {

    private JPanel resultsPanel;
    private JPopupMenu roleSpecificMenu;
    private JButton dropdownButton;

    public EmployeePanel(BookController bookController, ClientController clientController, String roleName) {
        setLayout(new BorderLayout());
        setBackground(new Color(246, 248, 255)); // spójne jasne tło

        // --- Nagłówek powitalny ---
        JLabel welcomeLabel = new JLabel("Witaj " + roleName, SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 28f));
        welcomeLabel.setForeground(new Color(44, 62, 80));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(32,0,18,0));

        // --- Główny panel „card" na przyciski ---
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setOpaque(true);
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 199, 214), 1, true),
                BorderFactory.createEmptyBorder(24, 30, 24, 30)
        ));


        // --- Panel przycisków „operacje" ---
        JPanel operationsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        operationsPanel.setOpaque(false);

        JButton btnShowBooks = createPrimaryButton("Wyświetl listę książek");
        JButton btnShowAvailableBooks = createPrimaryButton("Wyświetl dostępne książki");
        JButton btnShowReservationHistory = createPrimaryButton("Wyświetl historię rezerwacji klienta");

        btnShowBooks.addActionListener(_ -> {
            ShowBooksDialog dialog = new ShowBooksDialog(bookController);
            setResultsContent(dialog.getMainPanel());
        });
        btnShowAvailableBooks.addActionListener(_ -> {
            ShowAvailableBooksDialog dialog = new ShowAvailableBooksDialog(bookController);
            setResultsContent(dialog.getMainPanel());
        });
        btnShowReservationHistory.addActionListener(_ -> {
            ShowReservationHistoryDialog dialog = new ShowReservationHistoryDialog(clientController);
            setResultsContent(dialog.getMainPanel());
        });

        operationsPanel.add(btnShowBooks);
        operationsPanel.add(btnShowAvailableBooks);
        operationsPanel.add(btnShowReservationHistory);

        // --- Dropdown menu dla funkcji specyficznych dla roli ---
        roleSpecificMenu = new JPopupMenu();
        dropdownButton = createPrimaryButton("Więcej ▼");
        dropdownButton.addActionListener(e -> {
            roleSpecificMenu.show(dropdownButton, 0, dropdownButton.getHeight());
        });
        operationsPanel.add(dropdownButton);

        // --- Panel z przyciskiem wylogowania i ekstensjami ---
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actionsPanel.setOpaque(false);
        JButton btnLogout = createSecondaryButton("Wyloguj");
        JButton btnExtensions = createSecondaryButton("Ekstensje");
        actionsPanel.add(btnExtensions);
        actionsPanel.add(Box.createHorizontalStrut(22));
        actionsPanel.add(btnLogout);

        // --- Połącz przyciski: główne i akcje ---
        JPanel allButtonsPanel = new JPanel(new BorderLayout());
        allButtonsPanel.setOpaque(false);
        allButtonsPanel.add(operationsPanel, BorderLayout.CENTER);
        allButtonsPanel.add(actionsPanel, BorderLayout.EAST);

        cardPanel.add(allButtonsPanel, BorderLayout.CENTER);

        // --- Panel wyników ---
        resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setOpaque(true);
        resultsPanel.setBackground(Color.WHITE);
        resultsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 209, 219), 1, true),
                BorderFactory.createEmptyBorder(0,0,0,0)
        ));
        resultsPanel.setPreferredSize(new Dimension(800, 400));

        // --- Rozmieszczenie w głównym panelu ---
        add(welcomeLabel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.NORTH);
        add(resultsPanel, BorderLayout.CENTER);
    }

    // --- Funkcje pomocnicze dla stylowych przycisków ---
    private JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 16f));
        btn.setBackground(new Color(49, 128, 255));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(btn.getFont().deriveFont(Font.PLAIN, 15f));
        btn.setBackground(new Color(235, 240, 249));
        btn.setForeground(new Color(44, 62, 80));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // --- Metoda do dodawania elementów do dropdown menu ---
    public void addToDropdownMenu(JMenuItem menuItem) {
        roleSpecificMenu.add(menuItem);
    }

    public void clearResults() {
        resultsPanel.removeAll();
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    public void setResultsContent(JComponent component) {
        clearResults();
        resultsPanel.add(component, BorderLayout.CENTER);
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    // Usuń tę metodę, bo już nie będzie potrzebna
    // public JPanel getWorkButtonsPanel() { ... }
}
