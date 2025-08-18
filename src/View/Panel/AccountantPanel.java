package View.Panel;

import Controller.*;
import View.Dialogs.Accountant.ShowFinancialReportDialog;
import View.Dialogs.Accountant.ManageFinesDialog;
import View.Dialogs.Accountant.ManageSalariesDialog;

import javax.swing.*;

public class AccountantPanel extends EmployeePanel {

    public AccountantPanel(BookController bookController, ClientController clientController,
                           FineController fineController, EmployeeController employeeController) {

        super(bookController, clientController, "Księgowy!");

        // Tworzymy elementy menu dla księgowego
        JMenuItem btnShowFines = new JMenuItem("Zarządzaj karami");
        btnShowFines.addActionListener(_ -> new ManageFinesDialog(fineController));

        JMenuItem btnShowReport = new JMenuItem("Generuj Raport Finansowy (30 dni)");
        btnShowReport.addActionListener(_ -> new ShowFinancialReportDialog(fineController));

        JMenuItem btnShowSalaries = new JMenuItem("Zarządzaj pensją pracowników");
        btnShowSalaries.addActionListener(_ -> new ManageSalariesDialog(employeeController));

        // Dodajemy wszystkie elementy do dropdown menu
        addToDropdownMenu(btnShowFines);
        addToDropdownMenu(btnShowReport);
        addToDropdownMenu(btnShowSalaries);
    }
}
