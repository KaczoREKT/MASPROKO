package View;

import Controller.*;
import View.Panel.*;
import Model.utils.ObjectPlus;
import java.util.stream.Collectors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;

public class MainFrame extends JFrame {
    private final JButton btnLogout;
    private final JButton btnShowExtents;
    private final JPanel cardsPanel;
    private final CardLayout cardLayout;

    public MainFrame(EmployeeController employeeController,
                     BookController bookController,
                     SectorController sectorController,
                     ClientController clientController,
                     ReservationController reservationController,
                     LibrarianController librarianController,
                     SortingJobController sortingJobController,
                     FineController fineController,
                     LoanController loanController) {
        setTitle("Aplikacja Biblioteka");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // MENU
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnLogout = new JButton("Wyloguj");
        btnShowExtents = new JButton("Ekstensje");

        menuPanel.add(btnLogout);
        menuPanel.add(btnShowExtents); // Dodaj do menu

        add(menuPanel, BorderLayout.NORTH);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ObjectPlus.saveToFile("extents.bin");
            }
        });

        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.add(new LoginPanel(this, employeeController), "Login");
        cardsPanel.add(new LibrarianPanel(bookController, clientController, sectorController, reservationController, loanController), "LibrarianPanel");
        cardsPanel.add(new ManagerPanel(bookController, clientController, employeeController, librarianController, sectorController, sortingJobController), "ManagerPanel");
        cardsPanel.add(new AccountantPanel(bookController, clientController, fineController, employeeController), "AccountantPanel");
        add(cardsPanel, BorderLayout.CENTER);

        btnLogout.setVisible(false);

        btnLogout.addActionListener(_ -> showPanel("Login"));

        btnShowExtents.addActionListener(_ -> showAllExtents());

        setVisible(true);
        showPanel("Login");

        // Zapisywanie do ekstensji podczas wyłączania aplikacji
        Runtime.getRuntime().addShutdownHook(new Thread(() -> ObjectPlus.saveToFile("extents.bin")));
    }

    public void showPanel(String name) {
        cardLayout.show(cardsPanel, name);
        btnLogout.setVisible(!name.equals("Login"));
        btnShowExtents.setVisible(!name.equals("Login"));
    }

    public void showAllExtents() {
        List<Class<?>> extentClasses = Arrays.asList(
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

}
