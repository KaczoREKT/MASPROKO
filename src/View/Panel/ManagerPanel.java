package View.Panel;

import Controller.BookController;
import Controller.ClientController;
import Controller.EmployeeController;
import Controller.LibrarianController;
import Controller.SectorController;
import Controller.SortingJobController;
import View.Dialogs.Manager.RegisterEmployeeDialog;
import View.Dialogs.Manager.FireEmployeeDialog;
import View.Dialogs.Manager.AssignWorkDialog;

import javax.swing.*;

public class ManagerPanel extends EmployeePanel {
    public ManagerPanel(BookController bookController, ClientController clientController,
                        EmployeeController employeeController, LibrarianController librarianController, SectorController sectorController,
                        SortingJobController sortingJobController) {
        super(bookController, clientController, "Menedżerze!");

        JPanel workButtonsPanel = getWorkButtonsPanel();

        JButton btnRegisterEmployee = new JButton("Zarejestruj pracownika");
        JButton btnFireEmployee = new JButton("Zwolnij pracownika");
        JButton btnAssignWork = new JButton("Przypisz zadanie");

        btnRegisterEmployee.addActionListener(_-> new RegisterEmployeeDialog(employeeController));
        btnFireEmployee.addActionListener(_ -> new FireEmployeeDialog(employeeController));
        btnAssignWork.addActionListener(_ -> new AssignWorkDialog(librarianController, sectorController, sortingJobController));

        workButtonsPanel.add(btnRegisterEmployee);
        workButtonsPanel.add(btnFireEmployee);
        workButtonsPanel.add(btnAssignWork);
    }
}