package View.Panel;

import Controller.*;
import View.Dialogs.Accountant.ShowFinancialReportDialog;
import View.Dialogs.Accountant.ManageFinesDialog;
import View.Dialogs.Accountant.ManageSalariesDialog;

import javax.swing.*;

public class AccountantPanel extends EmployeePanel {
    public AccountantPanel(BookController bookController, ClientController clientController, FineController fineController, EmployeeController employeeController) {
        super(bookController, clientController, "Księgowy!");

        JPanel workButtonsPanel = getWorkButtonsPanel();

        // 1. Przycisk: Lista kar z filtrem
        JButton btnShowFines = new JButton("Zarządzaj karami");
        btnShowFines.addActionListener(_ -> new ManageFinesDialog(fineController));
        workButtonsPanel.add(btnShowFines);

        // 2. Przycisk: Raport finansowy
        JButton btnShowReport = new JButton("Generuj Raport Finansowy (30 dni)");
        btnShowReport.addActionListener(_ -> new ShowFinancialReportDialog(fineController));
        workButtonsPanel.add(btnShowReport);

        // 3. Przycisk: Lista płac pracowników
        JButton btnShowSalaries = new JButton("Zarządzaj pensją pracowników");
        btnShowSalaries.addActionListener(_ -> new ManageSalariesDialog(employeeController));
        workButtonsPanel.add(btnShowSalaries);
    }


}
