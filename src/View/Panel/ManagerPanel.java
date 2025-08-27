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
import View.MainFrame;

import javax.swing.*;

public class ManagerPanel extends EmployeePanel {

    public ManagerPanel(MainFrame mainFrame, BookController bookController, ClientController clientController,
                        EmployeeController employeeController, LibrarianController librarianController,
                        SectorController sectorController, SortingJobController sortingJobController) {

        super(mainFrame, bookController, clientController, "Menedżerze!");

        // Tworzymy elementy menu dla menedżera
        JMenuItem btnRegisterEmployee = new JMenuItem("Zarejestruj pracownika");
        btnRegisterEmployee.addActionListener(_ -> new RegisterEmployeeDialog(employeeController));

        JMenuItem btnFireEmployee = new JMenuItem("Zwolnij pracownika");
        btnFireEmployee.addActionListener(_ -> new FireEmployeeDialog(employeeController));

        JMenuItem btnAssignWork = new JMenuItem("Przypisz zadanie");
        btnAssignWork.addActionListener(_ -> new AssignWorkDialog(librarianController, sectorController, sortingJobController));

        // Dodajemy wszystkie elementy do dropdown menu
        addToDropdownMenu(btnRegisterEmployee);
        addToDropdownMenu(btnFireEmployee);
        addToDropdownMenu(btnAssignWork);
    }
}
