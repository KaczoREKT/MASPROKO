package View;

import Controller.*;
import View.Panel.LibrarianPanel;
import View.Panel.LoginPanel;
import View.Panel.ManagerPanel;
import View.Panel.ReceptionistPanel;
import utils.ObjectPlus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    private JPanel menuPanel;
    private JButton btnLogout;
    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private EmployeeController employeeController;
    private BookController bookController;
    private LibrarianController librarianController;
    private SortingJobController sortingJobController;
    private SectorController sectorController;
    private ClientController clientController;
    private ReservationController reservationController;

    public MainFrame(EmployeeController employeeController, BookController bookController,  SectorController sectorController, ClientController clientController, ReservationController reservationController, LibrarianController librarianController, SortingJobController sortingJobController) {
        this.employeeController = employeeController;
        this.bookController = bookController;
        this.sectorController = sectorController;
        this.clientController = clientController;
        this.reservationController = reservationController;
        this.librarianController = librarianController;
        this.sortingJobController = sortingJobController;
        setTitle("Aplikacja Biblioteka");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // MENU
        menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnLogout = new JButton("Wyloguj");
        menuPanel.add(btnLogout);
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
        cardsPanel.add(new LoginPanel(this, this.employeeController), "Login");
        cardsPanel.add(new ReceptionistPanel(this.bookController, this.clientController, this.reservationController), "ReceptionistPanel");
        cardsPanel.add(new LibrarianPanel(this.bookController, this.clientController, this.reservationController, this.sectorController), "LibrarianPanel");
        cardsPanel.add(new ManagerPanel(this.bookController, this.clientController, this.reservationController, this.employeeController, this.librarianController, this.sectorController, this.sortingJobController), "ManagerPanel");
        add(cardsPanel, BorderLayout.CENTER);

        // Domyślnie ukryj przycisk wylogowania
        btnLogout.setVisible(false);

        // Obsługa wylogowania
        btnLogout.addActionListener(e -> showPanel("Login"));

        setVisible(true);
        showPanel("Login");
    }

    // Ustawia widoczność przycisku w zależności od panelu
    public void showPanel(String name) {
        cardLayout.show(cardsPanel, name);
        // Przycisk widoczny wszędzie poza logowaniem
        btnLogout.setVisible(!name.equals("Login"));
    }
}
