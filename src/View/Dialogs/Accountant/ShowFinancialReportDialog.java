package View.Dialogs.Accountant;

import Controller.FineController;

import javax.swing.*;
import java.awt.*;

public class ShowFinancialReportDialog extends JDialog {
    public ShowFinancialReportDialog(FineController fineController) {
        setTitle("Raport finansowy za kary");
        setModal(true);
        setSize(500, 280);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        content.add(new JLabel("Za ostatni miesiąc (30 dni):"), gbc);
        gbc.gridy++;
        double amount = fineController.getTotalFinesInLast30Days();
        content.add(new JLabel(String.format("Łączna suma wpłat za mandaty: %.2f PLN", amount)), gbc);

        setContentPane(content);
        setVisible(true);
    }
}
