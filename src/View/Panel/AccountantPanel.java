package View.Panel;

import Controller.*;
import View.Dialogs.Accountant.ShowFinancialReportDialog;
import View.Dialogs.Accountant.ManageFinesDialog;
import View.Dialogs.Accountant.ManageSalariesDialog;
import View.MainFrame;

import javax.swing.*;

public class AccountantPanel extends EmployeePanel {

    public AccountantPanel(MainFrame mainFrame,
                           BookController bookController,
                           ClientController clientController,
                           FineController fineController,
                           EmployeeController employeeController) {

        super(mainFrame, bookController, clientController, "Księgowy!");

        JMenuItem btnShowFines = createMenuItem("Zarządzaj karami",
                _ -> new ManageFinesDialog(fineController));

        JMenuItem btnShowReport = createMenuItem("Generuj Raport Finansowy (30 dni)",
                _ -> new ShowFinancialReportDialog(fineController, employeeController));

        JMenuItem btnShowSalaries = createMenuItem("Zarządzaj pensją pracowników",
                _ -> new ManageSalariesDialog(employeeController));

        addToDropdownMenu(btnShowFines);
        addToDropdownMenu(btnShowReport);
        addToDropdownMenu(btnShowSalaries);
    }
}