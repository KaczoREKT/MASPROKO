package View;

import Controller.*;
import View.Panel.LibrarianPanel;
import View.Panel.LoginPanel;
import View.Panel.ManagerPanel;
import View.Panel.ReceptionistPanel;
import Model.utils.ObjectPlus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;

public class MainFrame extends JFrame {
    private final JButton btnLogout;
    private final JButton btnShowExtents; // NOWY PRZYCISK
    private final JPanel cardsPanel;
    private final CardLayout cardLayout;

    public MainFrame(EmployeeController employeeController, BookController bookController,  SectorController sectorController, ClientController clientController, ReservationController reservationController, LibrarianController librarianController, SortingJobController sortingJobController) {
        setTitle("Aplikacja Biblioteka");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 480);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // MENU
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnLogout = new JButton("Wyloguj");
        btnShowExtents = new JButton("Ekstensje"); // <<<<<<<<<<<<<<<

        menuPanel.add(btnLogout);
        menuPanel.add(btnShowExtents); // Dodaj do menu

        add(menuPanel, BorderLayout.NORTH);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ObjectPlus.saveToFile("extents.bin");
            }
        });
        // KARTY
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.add(new LoginPanel(this, employeeController), "Login");
        cardsPanel.add(new ReceptionistPanel(bookController, clientController, reservationController), "ReceptionistPanel");
        cardsPanel.add(new LibrarianPanel(bookController, clientController, reservationController, sectorController), "LibrarianPanel");
        cardsPanel.add(new ManagerPanel(bookController, clientController, reservationController, employeeController, librarianController, sectorController, sortingJobController), "ManagerPanel");
        add(cardsPanel, BorderLayout.CENTER);

        // Domyślnie ukryj przycisk wylogowania
        btnLogout.setVisible(false);

        // Obsługa wylogowania
        btnLogout.addActionListener(e -> showPanel("Login"));

        // Obsługa ekstensji – po kliknięciu pokazuje w konsoli
        btnShowExtents.addActionListener(e -> showAllExtents());

        setVisible(true);
        showPanel("Login");
    }

    // Ustawia widoczność przycisku w zależności od panelu
    public void showPanel(String name) {
        cardLayout.show(cardsPanel, name);
        // Przycisk widoczny wszędzie poza logowaniem
        btnLogout.setVisible(!name.equals("Login"));
        btnShowExtents.setVisible(!name.equals("Login")); // Ukryj też na logowaniu
    }

    // Pokazuje ekstensje wszystkich klas (na konsoli)
    public void showAllExtents() {
        List<Class<?>> extentClasses = Arrays.asList(
                Model.Client.class,
                Model.ClientCard.class,
                Model.Reservation.class,
                Model.Fine.class,
                Model.Librarian.class,
                Model.Manager.class,
                Model.Receptionist.class,
                Model.SortingJob.class,
                Model.Book.class,
                Model.Sector.class
        );

        StringBuilder sb = new StringBuilder();
        sb.append("=== EKSTENSJE WSZYSTKICH KLAS ===\n");
        for (Class<?> clazz : extentClasses) {
            try {
                sb.append("Extent of the class: ").append(clazz.getSimpleName()).append("\n");
                sb.append(ObjectPlus.getExtentString(clazz));
            } catch (Exception e) {
                sb.append("[INFO] Brak ekstensji dla klasy ").append(clazz.getSimpleName()).append("\n");
            }
            sb.append("--------------------------------\n");
        }

        // Tworzenie okna dialogowego z tekstem
        JTextArea textArea = new JTextArea(sb.toString(), 25, 80);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(this, scrollPane, "Ekstensje wszystkich klas", JOptionPane.INFORMATION_MESSAGE);
    }

}
