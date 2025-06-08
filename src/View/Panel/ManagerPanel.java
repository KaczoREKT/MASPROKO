package View.Panel;

import Controller.BookController;
import Controller.ClientController;
import Controller.ReservationController;
import Controller.EmployeeController;
import Controller.LibrarianController;
import Controller.SectorController;
import Controller.SortingJobController;
import View.Dialogs.Manager.RegisterEmployeeDialog;
import View.Dialogs.Manager.FireEmployeeDialog;
import View.Dialogs.Manager.AssignWorkDialog;

import javax.swing.*;

public class ManagerPanel extends EmployeePanel {
    public ManagerPanel(BookController bookController, ClientController clientController, ReservationController reservationController,
                        EmployeeController employeeController, LibrarianController librarianController, SectorController sectorController,
                        SortingJobController sortingJobController) {
        super(bookController, clientController, reservationController, "Menedżerze!");

        JPanel workButtonsPanel = getWorkButtonsPanel();

        JButton btnRegisterEmployee = new JButton("Zarejestruj pracownika");
        JButton btnFireEmployee = new JButton("Zwolnij pracownika");
        JButton btnAssignWork = new JButton("Przypisz zadanie");

        btnRegisterEmployee.addActionListener(e -> new RegisterEmployeeDialog(employeeController));
        btnFireEmployee.addActionListener(e -> new FireEmployeeDialog(employeeController));
        btnAssignWork.addActionListener(e -> new AssignWorkDialog(librarianController, sectorController, sortingJobController));

        workButtonsPanel.add(btnRegisterEmployee);
        workButtonsPanel.add(btnFireEmployee);
        workButtonsPanel.add(btnAssignWork);
    }
}