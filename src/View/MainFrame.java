package View;

import Controller.*;
import View.Panel.*;
import Model.utils.ObjectPlus;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
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

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ObjectPlus.saveToFile("extents.bin");
            }
        });

        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.add(new LoginPanel(this, employeeController), "Login");
        cardsPanel.add(new LibrarianPanel(this, bookController, clientController, sectorController, reservationController, loanController), "LibrarianPanel");
        cardsPanel.add(new ManagerPanel(this, bookController, clientController, employeeController, librarianController, sectorController, sortingJobController), "ManagerPanel");
        cardsPanel.add(new AccountantPanel(this, bookController, clientController, fineController, employeeController), "AccountantPanel");
        add(cardsPanel, BorderLayout.CENTER);

        setVisible(true);
        showPanel("Login");

        // Zapisywanie do ekstensji podczas wyłączania aplikacji
        Runtime.getRuntime().addShutdownHook(new Thread(() -> ObjectPlus.saveToFile("extents.bin")));
    }

    public void showPanel(String name) {
        cardLayout.show(cardsPanel, name);
    }
}
