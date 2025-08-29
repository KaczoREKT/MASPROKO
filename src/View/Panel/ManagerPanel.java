package View.Panel;

import Controller.*;
import View.Dialogs.Manager.RegisterEmployeeDialog;
import View.Dialogs.Manager.FireEmployeeDialog;
import View.Dialogs.Manager.AssignWorkDialog;
import View.MainFrame;

import javax.swing.*;

public class ManagerPanel extends EmployeePanel {

    public ManagerPanel(MainFrame mainFrame,
                        BookController bookController,
                        ClientController clientController,
                        EmployeeController employeeController,
                        LibrarianController librarianController,
                        SectorController sectorController,
                        SortingJobController sortingJobController) {

        super(mainFrame, bookController, clientController, "Menedżerze!");

        JMenuItem btnRegisterEmployee = createMenuItem("Zarejestruj pracownika",
                _ -> new RegisterEmployeeDialog(employeeController));

        JMenuItem btnFireEmployee = createMenuItem("Zwolnij pracownika",
                _ -> new FireEmployeeDialog(employeeController));

        JMenuItem btnAssignWork = createMenuItem("Przypisz zadanie",
                _ -> new AssignWorkDialog(librarianController, sectorController, sortingJobController));

        addToDropdownMenu(btnRegisterEmployee);
        addToDropdownMenu(btnFireEmployee);
        addToDropdownMenu(btnAssignWork);
    }
}