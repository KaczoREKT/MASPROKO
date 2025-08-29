package View.Dialogs.Manager;

import Controller.EmployeeController;
import Model.Employee;
import Model.Enum.Gender;

import javax.swing.*;
import java.awt.*;

public class RegisterEmployeeDialog extends JDialog {
    public RegisterEmployeeDialog(EmployeeController employeeController) {
        setTitle("Rejestracja nowego pracownika");
        setModal(true);
        setSize(450, 360);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        content.add(new JLabel("Stanowisko:"), gbc);
        String[] employeeTypes = {"Bibliotekarz", "Księgowy", "Menedżer"};
        JComboBox<String> typeBox = new JComboBox<>(employeeTypes);
        gbc.gridx = 1;
        content.add(typeBox, gbc);

        gbc.gridy = ++row; gbc.gridx = 0;
        content.add(new JLabel("Imię:"), gbc);
        JTextField firstNameField = new JTextField(15);
        gbc.gridx = 1;
        content.add(firstNameField, gbc);

        gbc.gridy = ++row; gbc.gridx = 0;
        content.add(new JLabel("Nazwisko:"), gbc);
        JTextField lastNameField = new JTextField(15);
        gbc.gridx = 1;
        content.add(lastNameField, gbc);

        gbc.gridy = ++row; gbc.gridx = 0;
        content.add(new JLabel("Płeć:"), gbc);
        JComboBox<Gender> genderBox = new JComboBox<>(Gender.values());
        gbc.gridx = 1;
        content.add(genderBox, gbc);

        gbc.gridy = ++row; gbc.gridx = 0;
        content.add(new JLabel("Pensja:"), gbc);
        JTextField salaryField = new JTextField(10);
        gbc.gridx = 1;
        content.add(salaryField, gbc);

        gbc.gridy = ++row; gbc.gridx = 0;
        JLabel specializationLabel = new JLabel("Specjalizacja:");
        content.add(specializationLabel, gbc);
        JTextField specializationField = new JTextField(15);
        gbc.gridx = 1;
        content.add(specializationField, gbc);

        gbc.gridy = ++row; gbc.gridx = 0;
        JLabel bonusLabel = new JLabel("Premia:");
        content.add(bonusLabel, gbc);
        JTextField bonusField = new JTextField(10);
        gbc.gridx = 1;
        content.add(bonusField, gbc);

        specializationLabel.setVisible(true);
        specializationField.setVisible(true);
        bonusLabel.setVisible(false);
        bonusField.setVisible(false);

        typeBox.addActionListener(_ -> {
            String selected = (String) typeBox.getSelectedItem();
            if ("Bibliotekarz".equals(selected)) {
                specializationLabel.setVisible(true);
                specializationField.setVisible(true);
                bonusLabel.setVisible(false);
                bonusField.setVisible(false);
            } else if ("Menedżer".equals(selected)) {
                specializationLabel.setVisible(false);
                specializationField.setVisible(false);
                bonusLabel.setVisible(true);
                bonusField.setVisible(true);
            } else {
                specializationLabel.setVisible(false);
                specializationField.setVisible(false);
                bonusLabel.setVisible(false);
                bonusField.setVisible(false);
            }
            content.revalidate();
            content.repaint();
        });

        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton btnRegister = new JButton("Zarejestruj");
        JButton btnCancel = new JButton("Anuluj");
        btnPanel.add(btnRegister);
        btnPanel.add(btnCancel);
        content.add(btnPanel, gbc);


        btnRegister.addActionListener(_ -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            Gender gender = (Gender) genderBox.getSelectedItem();
            String type = (String) typeBox.getSelectedItem();
            String salaryStr = salaryField.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || salaryStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Uzupełnij wszystkie wymagane pola.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double salary;
            try {
                salary = Double.parseDouble(salaryStr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Niepoprawna pensja.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Employee newEmployee;
                if ("Bibliotekarz".equals(type)) {
                    String specialization = specializationField.getText().trim();
                    if (specialization.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Podaj specjalizację.", "Błąd", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    newEmployee = employeeController.addLibrarian(firstName, lastName, gender, salary, specialization);
                } else if ("Menedżer".equals(type)) {
                    String bonusStr = bonusField.getText().trim();
                    double bonus;
                    try {
                        bonus = Double.parseDouble(bonusStr);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Niepoprawna premia.", "Błąd", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    newEmployee = employeeController.addManager(firstName, lastName, gender, salary, bonus);
                } else {
                    newEmployee = employeeController.addAccountant(firstName, lastName, gender, salary);
                }
                JOptionPane.showMessageDialog(this, "Zarejestrowano nowego pracownika:\n" + newEmployee);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(_ -> dispose());

        setContentPane(content);
        setVisible(true);
    }
}