package View.Panel;

import Controller.EmployeeController;
import View.MainFrame;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    public LoginPanel(MainFrame frame, EmployeeController employeeController) {
        // panel zewnętrzny: centrowanie i oddech wokół formularza
        setLayout(new BorderLayout());
        JPanel inner = new JPanel(new GridBagLayout());
        inner.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24)); // marginesy
        add(inner, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel label = new JLabel("Podaj swoje ID:");
        JTextField idField = new JTextField(20); // dłuższe pole, ale elastyczne
        JButton loginBtn = new JButton("Zaloguj");

        // Logika przycisku bez zmian
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

        // wiersz 0: etykieta
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;                 // etykieta nie rozszerza się
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.LINE_END;
        inner.add(label, gbc);

        // wiersz 0: pole tekstowe
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;                 // pole rośnie w poziomie
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;
        inner.add(idField, gbc);

        // wiersz 1: przycisk pod spodem, wyśrodkowany
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        inner.add(loginBtn, gbc);

        // pozwól całemu panelowi „oddychać” w pionie
        // dodając sprężynę nad i pod formularzem
        JPanel topGlue = new JPanel();
        JPanel bottomGlue = new JPanel();
        topGlue.setOpaque(false);
        bottomGlue.setOpaque(false);
        add(topGlue, BorderLayout.NORTH);
        add(bottomGlue, BorderLayout.SOUTH);
    }

    // (opcjonalnie) statyczna metoda do podbicia czcionek dla HiDPI
    public static void scaleUIFont(float size) {
        UIDefaults d = UIManager.getLookAndFeelDefaults();
        d.entrySet().stream()
                .filter(e -> e.getKey().toString().endsWith(".font"))
                .forEach(e -> UIManager.put(e.getKey(),
                        ((Font) e.getValue()).deriveFont(size)));
    }
}
