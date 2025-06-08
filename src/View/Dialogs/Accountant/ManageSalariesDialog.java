package View.Dialogs.Accountant;

import Controller.EmployeeController;
import Model.Employee;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ManageSalariesDialog extends JDialog {
    public ManageSalariesDialog(EmployeeController employeeController) {
        setTitle("Lista płac pracowników");
        setModal(true);
        setSize(580, 350);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new BorderLayout(10,10));
        DefaultListModel<Employee> empListModel = new DefaultListModel<>();
        JList<Employee> empList = new JList<>(empListModel);
        empList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(empList);
        content.add(scrollPane, BorderLayout.CENTER);

        JPanel editPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JTextField salaryField = new JTextField(8);
        JButton btnUpdateSalary = new JButton("Zmień wypłatę");
        editPanel.add(new JLabel("Nowa pensja:"));
        editPanel.add(salaryField);
        editPanel.add(btnUpdateSalary);
        content.add(editPanel, BorderLayout.SOUTH);

        // Załaduj pracowników
        List<Employee> employees = employeeController.getEmployeeList();
        for (Employee emp : employees) empListModel.addElement(emp);

        btnUpdateSalary.addActionListener(_ -> {
            Employee selected = empList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Wybierz pracownika.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                double newSalary = Double.parseDouble(salaryField.getText().trim());
                employeeController.setEmployeeSalary(selected, newSalary);
                JOptionPane.showMessageDialog(this, "Pensja zaktualizowana.");
                empList.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Niepoprawna kwota.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });

        setContentPane(content);
        setVisible(true);
    }
}
