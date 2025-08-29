package View.Dialogs.Librarian;

import Controller.ClientController;
import Model.Enum.Gender;

import javax.swing.*;
import java.awt.*;

public class AddNewClientDialog extends JDialog {
    public AddNewClientDialog(ClientController clientController) {
        setTitle("Dodaj nowego klienta");
        setModal(true);
        setSize(400, 320);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        content.add(new JLabel("Imię:"), gbc);
        JTextField firstNameField = new JTextField(15);
        gbc.gridx = 1; gbc.weightx = 0.7;
        content.add(firstNameField, gbc);

        gbc.gridy = ++row; gbc.gridx = 0; gbc.weightx = 0.3;
        content.add(new JLabel("Nazwisko:"), gbc);
        JTextField lastNameField = new JTextField(15);
        gbc.gridx = 1; gbc.weightx = 0.7;
        content.add(lastNameField, gbc);

        gbc.gridy = ++row; gbc.gridx = 0; gbc.weightx = 0.3;
        content.add(new JLabel("Płeć:"), gbc);
        JComboBox<Gender> genderBox = new JComboBox<>(Gender.values());
        gbc.gridx = 1; gbc.weightx = 0.7;
        content.add(genderBox, gbc);

        gbc.gridy = ++row; gbc.gridx = 0; gbc.weightx = 0.3;
        content.add(new JLabel("E-mail:"), gbc);
        JTextField emailField = new JTextField(15);
        gbc.gridx = 1; gbc.weightx = 0.7;
        content.add(emailField, gbc);

        gbc.gridy = ++row; gbc.gridx = 0; gbc.weightx = 0.3;
        content.add(new JLabel("Telefon:"), gbc);
        JTextField phoneField = new JTextField(15);
        gbc.gridx = 1; gbc.weightx = 0.7;
        content.add(phoneField, gbc);

        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 1;
        JButton btnAdd = new JButton("Dodaj");
        content.add(btnAdd, gbc);

        btnAdd.addActionListener(_ -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            Gender gender = (Gender) genderBox.getSelectedItem();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            try {
                clientController.addNewClient(firstName, lastName, gender, email, phone);
                JOptionPane.showMessageDialog(this, "Dodano nowego klienta!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });

        setContentPane(content);
        setVisible(true);
    }
}
