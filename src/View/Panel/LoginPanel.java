package View.Panel;

import Controller.EmployeeController;
import View.MainFrame;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    public LoginPanel(MainFrame frame, EmployeeController employeeController) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel("Podaj swoje ID:");
        JTextField idField = new JTextField(15);
        JButton loginBtn = new JButton("Zaloguj");

        loginBtn.addActionListener(_ -> {
            String id = idField.getText().trim();
            if (!id.isEmpty()) {
                String className = employeeController.loginEmployee(id);
                if (className != null) {
                    switch (className) {
                        case "Manager":
                            frame.showPanel("ManagerPanel");
                            break;
                        case "Librarian":
                            frame.showPanel("LibrarianPanel");
                            break;
                        case "Receptionist":
                            frame.showPanel("ReceptionistPanel");
                            break;
                        case "Accountant":
                            frame.showPanel("AccountantPanel");
                            break;
                        default:
                            JOptionPane.showMessageDialog(frame, "Nieobsługiwany typ pracownika: " + className, "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Nie znaleziono pracownika!", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "ID nie może być puste!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(label, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(loginBtn, gbc);
    }
}

