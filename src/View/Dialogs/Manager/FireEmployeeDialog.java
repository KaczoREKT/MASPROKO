package View.Dialogs.Manager;

import Controller.EmployeeController;
import Model.Employee;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FireEmployeeDialog extends JDialog {
    public FireEmployeeDialog(EmployeeController employeeController) {
        setTitle("Zwolnij pracownika");
        setModal(true);
        setSize(420, 220);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        int row = 0;

        // 1. Lista pracowników
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3; gbc.gridwidth = 1;
        content.add(new JLabel("Pracownik:"), gbc);

        List<Employee> employees = employeeController.getEmployeeList();
        JComboBox<Employee> employeeBox = new JComboBox<>(employees.toArray(new Employee[0]));
        gbc.gridx = 1; gbc.weightx = 0.7; gbc.gridwidth = 1;
        content.add(employeeBox, gbc);

        // 2. Panel z przyciskami (jeden panel, oba przyciski obok siebie)
        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton btnFire = new JButton("Zwolnij");
        JButton btnCancel = new JButton("Anuluj");
        buttonPanel.add(btnFire);
        buttonPanel.add(btnCancel);
        content.add(buttonPanel, gbc);

        // --- Logika ---
        btnFire.addActionListener(_ -> {
            Employee selectedEmployee = (Employee) employeeBox.getSelectedItem();
            if (selectedEmployee == null) {
                JOptionPane.showMessageDialog(this, "Nie wybrano pracownika!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Czy na pewno chcesz zwolnić tego pracownika?\n" + selectedEmployee,
                    "Potwierdź zwolnienie", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    employeeController.deleteEmployee(selectedEmployee);
                    JOptionPane.showMessageDialog(this, "Pracownik został zwolniony.");
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCancel.addActionListener(_ -> dispose());

        setContentPane(content);
        setVisible(true);
    }
}