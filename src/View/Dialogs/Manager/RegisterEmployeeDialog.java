package View.Dialogs.Manager;

import Controller.EmployeeController;
import Model.Employee;
import Model.Gender;
import Model.Librarian;
import Model.Manager;
import Model.Receptionist;

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

        // Typ pracownika
        gbc.gridx = 0; gbc.gridy = row;
        content.add(new JLabel("Stanowisko:"), gbc);
        String[] employeeTypes = {"Bibliotekarz", "Recepcjonistka", "Menedżer"};
        JComboBox<String> typeBox = new JComboBox<>(employeeTypes);
        gbc.gridx = 1;
        content.add(typeBox, gbc);

        // Imię
        gbc.gridy = ++row; gbc.gridx = 0;
        content.add(new JLabel("Imię:"), gbc);
        JTextField firstNameField = new JTextField(15);
        gbc.gridx = 1;
        content.add(firstNameField, gbc);

        // Nazwisko
        gbc.gridy = ++row; gbc.gridx = 0;
        content.add(new JLabel("Nazwisko:"), gbc);
        JTextField lastNameField = new JTextField(15);
        gbc.gridx = 1;
        content.add(lastNameField, gbc);

        // Płeć
        gbc.gridy = ++row; gbc.gridx = 0;
        content.add(new JLabel("Płeć:"), gbc);
        JComboBox<Gender> genderBox = new JComboBox<>(Gender.values());
        gbc.gridx = 1;
        content.add(genderBox, gbc);

        // Pensja
        gbc.gridy = ++row; gbc.gridx = 0;
        content.add(new JLabel("Pensja:"), gbc);
        JTextField salaryField = new JTextField(10);
        gbc.gridx = 1;
        content.add(salaryField, gbc);

        // Pole specjalizacja (tylko dla bibliotekarza)
        gbc.gridy = ++row; gbc.gridx = 0;
        JLabel specializationLabel = new JLabel("Specjalizacja:");
        content.add(specializationLabel, gbc);
        JTextField specializationField = new JTextField(15);
        gbc.gridx = 1;
        content.add(specializationField, gbc);

        // Pole premia (tylko dla managera)
        gbc.gridy = ++row; gbc.gridx = 0;
        JLabel bonusLabel = new JLabel("Premia:");
        content.add(bonusLabel, gbc);
        JTextField bonusField = new JTextField(10);
        gbc.gridx = 1;
        content.add(bonusField, gbc);

        // Ukryj na start
        specializationLabel.setVisible(true);
        specializationField.setVisible(true);
        bonusLabel.setVisible(false);
        bonusField.setVisible(false);

        // Zmiana widoczności pól w zależności od wybranego typu pracownika
        typeBox.addActionListener(e -> {
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

        // Przycisk Zarejestruj
        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.CENTER;
        JButton btnRegister = new JButton("Zarejestruj");
        content.add(btnRegister, gbc);

        // Przycisk Anuluj
        gbc.gridx = 1;
        JButton btnCancel = new JButton("Anuluj");
        content.add(btnCancel, gbc);

        btnRegister.addActionListener(e -> {
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
                    double bonus = 0;
                    try {
                        bonus = Double.parseDouble(bonusStr);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Niepoprawna premia.", "Błąd", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    newEmployee = employeeController.addManager(firstName, lastName, gender, salary, bonus);
                } else { // Recepcjonistka
                    newEmployee = employeeController.addReceptionist(firstName, lastName, gender, salary);
                }
                JOptionPane.showMessageDialog(this, "Zarejestrowano nowego pracownika:\n" + newEmployee);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dispose());

        setContentPane(content);
        setVisible(true);
    }
}