package View.Panel;

import javax.swing.*;
import java.awt.*;

// PANEL DLA MANAGERA
public class ManagerPanel extends JPanel {
    public ManagerPanel() {
        setLayout(new GridLayout(0, 1, 10, 10));

        add(new JLabel("Panel Managera", SwingConstants.CENTER));
        JButton btnRegisterEmployee = new JButton("Rejestracja nowego pracownika");
        JButton btnFireEmployee = new JButton("Zwolnienie pracownika");
        JButton btnAssignWork = new JButton("Przypisanie pracy bibliotekarzowi");

        btnRegisterEmployee.addActionListener(e -> {
            /* kod */});
        btnFireEmployee.addActionListener(e -> {
            /* kod */});
        btnAssignWork.addActionListener(e -> {
            /* kod */});

        add(btnRegisterEmployee);
        add(btnFireEmployee);
        add(btnAssignWork);
    }
}
