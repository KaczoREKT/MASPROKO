package View;

import Controller.BookController;
import Controller.EmployeeController;
import Controller.SectorController;
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
    private JButton btnLogout;      // Przycisk wylogowania jako pole
    private JPanel cardsPanel;
    private CardLayout cardLayout;
    private EmployeeController employeeController;
    BookController bookController;
    SectorController sectorController;

    public MainFrame(EmployeeController employeeController, BookController bookController,  SectorController sectorController) {
        this.employeeController = employeeController;
        this.bookController = bookController;
        this.sectorController = sectorController;
        setTitle("Aplikacja Biblioteka");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
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
        cardsPanel.add(new ReceptionistPanel(), "ReceptionistPanel");
        cardsPanel.add(new LibrarianPanel(bookController, sectorController), "LibrarianPanel");
        cardsPanel.add(new ManagerPanel(), "ManagerPanel");
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
