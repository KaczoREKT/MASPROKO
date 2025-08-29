package View.Dialogs.Accountant;

import Controller.FineController;
import Controller.EmployeeController;

import javax.swing.*;
import java.awt.*;

public class ShowFinancialReportDialog extends JDialog {
    public ShowFinancialReportDialog(FineController fineController, EmployeeController employeeController) {
        setTitle("Raport finansowy (30 dni)");
        setModal(true);
        setSize(520, 320);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        content.add(new JLabel("Za ostatni miesiąc (30 dni):"), gbc);

        gbc.gridy++;
        double finesAmount = fineController.getTotalFinesInLast30Days();
        content.add(new JLabel(String.format("Łączna suma wpłat za mandaty: %.2f PLN", finesAmount)), gbc);

        gbc.gridy++;
        double payroll = employeeController.getTotalMonthlyPayroll();
        content.add(new JLabel(String.format("Łączny koszt wypłat pracowników (mies.): %.2f PLN", payroll)), gbc);

        setContentPane(content);
        setVisible(true);
    }
}
