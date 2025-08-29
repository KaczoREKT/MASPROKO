package View.Panel;

import Controller.EmployeeController;
import View.MainFrame;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    public LoginPanel(MainFrame frame, EmployeeController employeeController) {
        setLayout(new GridBagLayout());

        JPanel formPanel = new JPanel();
        formPanel.setOpaque(true);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(236, 239, 244));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 2, true),
                BorderFactory.createEmptyBorder(32, 32, 32, 32)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);

        JLabel title = new JLabel("Logowanie do biblioteki");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        title.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets.bottom = 30;
        formPanel.add(title, gbc);

        JLabel label = new JLabel("Podaj swoje ID:");
        label.setFont(label.getFont().deriveFont(17f));
        label.setForeground(new Color(68, 68, 68));
        gbc.gridy = 1; gbc.gridwidth = 1; gbc.insets.bottom = 8; gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(label, gbc);

        JTextField idField = new JTextField(20);
        idField.setFont(idField.getFont().deriveFont(17f));
        idField.setBackground(Color.WHITE);
        idField.setForeground(Color.BLACK);
        idField.setMargin(new Insets(6, 8, 6, 8));
        idField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 200), 1, true),
                BorderFactory.createEmptyBorder(2,8,2,8))
        );
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        formPanel.add(idField, gbc);

        JButton loginBtn = new JButton("Zaloguj");
        loginBtn.setFont(loginBtn.getFont().deriveFont(Font.BOLD, 18f));
        loginBtn.setBackground(new Color(69, 124, 255));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorder(BorderFactory.createEmptyBorder(10, 28, 10, 28));
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets.top = 20; gbc.insets.bottom = 0;
        formPanel.add(loginBtn, gbc);

        loginBtn.addActionListener(_ -> {
            String id = idField.getText().trim();
            if (!id.isEmpty()) {
                String className = employeeController.loginEmployee(id);
                if (className != null) {
                    switch (className) {
                        case "Manager" -> frame.showPanel("ManagerPanel");
                        case "Librarian" -> frame.showPanel("LibrarianPanel");
                        case "Receptionist" -> frame.showPanel("ReceptionistPanel");
                        case "Accountant" -> frame.showPanel("AccountantPanel");
                        default -> JOptionPane.showMessageDialog(
                                frame, "Nieobsługiwany typ pracownika: " + className,
                                "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Nie znaleziono pracownika!",
                            "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "ID nie może być puste!",
                        "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(formPanel, new GridBagConstraints());
        setBackground(new Color(246, 248, 255)); // jasne tło aplikacji
    }
}
