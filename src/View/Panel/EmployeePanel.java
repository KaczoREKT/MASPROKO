package View.Panel;

import Controller.BookController;
import Controller.ClientController;
import Model.utils.ObjectPlus;
import View.Dialogs.Employee.ShowAvailableBooksDialog;
import View.Dialogs.Employee.ShowBooksDialog;
import View.Dialogs.Employee.ShowReservationHistoryDialog;
import View.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeePanel extends JPanel {

    private final MainFrame mainFrame;
    private JPanel resultsPanel;
    private JPopupMenu roleSpecificMenu;
    private JButton dropdownButton;

    public EmployeePanel(MainFrame mainFrame, BookController bookController, ClientController clientController, String roleName) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(246, 248, 255)); // spójne jasne tło

        JLabel welcomeLabel = new JLabel("Witaj " + roleName, SwingConstants.CENTER);
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 28f));
        welcomeLabel.setForeground(new Color(44, 62, 80));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(32,0,18,0));

        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setOpaque(true);
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 199, 214), 1, true),
                BorderFactory.createEmptyBorder(24, 30, 24, 30)
        ));

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

        roleSpecificMenu = new JPopupMenu();
        dropdownButton = createPrimaryButton("Więcej ▼");
        dropdownButton.addActionListener(e -> {
            roleSpecificMenu.show(dropdownButton, 0, dropdownButton.getHeight());
        });
        operationsPanel.add(dropdownButton);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actionsPanel.setOpaque(false);

        JButton btnLogout = createSecondaryButton("Wyloguj");
        btnLogout.addActionListener(_ -> logout());

        JButton btnExtensions = createSecondaryButton("Ekstensje");
        btnExtensions.addActionListener(_ -> showAllExtents());

        actionsPanel.add(btnExtensions);
        actionsPanel.add(Box.createHorizontalStrut(22));
        actionsPanel.add(btnLogout);

        JPanel allButtonsPanel = new JPanel(new BorderLayout());
        allButtonsPanel.setOpaque(false);
        allButtonsPanel.add(operationsPanel, BorderLayout.CENTER);
        allButtonsPanel.add(actionsPanel, BorderLayout.EAST);

        cardPanel.add(allButtonsPanel, BorderLayout.CENTER);

        resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setOpaque(true);
        resultsPanel.setBackground(Color.WHITE);
        resultsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 209, 219), 1, true),
                BorderFactory.createEmptyBorder(0,0,0,0)
        ));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(welcomeLabel, BorderLayout.NORTH);
        topPanel.add(cardPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        add(resultsPanel, BorderLayout.CENTER);
    }

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

    public void showAllExtents() {
        java.util.List<Class<?>> extentClasses = Arrays.asList(
                Model.Accountant.class,
                Model.Client.class,
                Model.ClientCard.class,
                Model.Reservation.class,
                Model.Loan.class,
                Model.Fine.class,
                Model.Librarian.class,
                Model.Manager.class,
                Model.SortingJob.class,
                Model.Book.class,
                Model.Sector.class
        );

        List<String> classNames = extentClasses.stream()
                .map(Class::getSimpleName)
                .collect(Collectors.toList());
        classNames.add(0, "Wszystkie");

        JComboBox<String> comboBox = new JComboBox<>(classNames.toArray(new String[0]));
        int result = JOptionPane.showConfirmDialog(
                this,
                comboBox,
                "Wybierz klasę",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) {
            return; // użytkownik anulował
        }

        String selected = (String) comboBox.getSelectedItem();
        StringBuilder sb = new StringBuilder();

        if ("Wszystkie".equals(selected)) {
            sb.append("=== EKSTENSJE WSZYSTKICH KLAS ===\n");
            for (Class<?> clazz : extentClasses) {
                appendExtentInfo(sb, clazz);
                sb.append("--------------------------------\n");
            }
        } else {
            int idx = classNames.indexOf(selected) - 1;
            if (idx >= 0 && idx < extentClasses.size()) {
                appendExtentInfo(sb, extentClasses.get(idx));
            }
        }

        JTextArea textArea = new JTextArea(sb.toString(), 25, 80);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(this, scrollPane, "Ekstensje", JOptionPane.INFORMATION_MESSAGE);
    }

    private void appendExtentInfo(StringBuilder sb, Class<?> clazz) {
        try {
            sb.append("Extent of the class: ").append(clazz.getSimpleName()).append("\n");
            sb.append(ObjectPlus.getExtentString(clazz));
        } catch (Exception e) {
            sb.append("[INFO] Brak ekstensji dla klasy ").append(clazz.getSimpleName()).append("\n");
        }
    }
    private void logout() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Czy na pewno chcesz się wylogować?",
                "Potwierdzenie wylogowania",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (option == JOptionPane.YES_OPTION) {
            clearResults(); // Wyczyść panel wyników
            mainFrame.showPanel("Login"); // Powróć do panelu logowania
        }
    }

    public JMenuItem createMenuItem(String text, java.awt.event.ActionListener listener) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(listener);
        return menuItem;
    }
}
