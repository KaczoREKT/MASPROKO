package View.Dialogs.Accountant;

import Controller.FineController;
import Model.Fine;
import Model.Enum.FineStatus;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ManageFinesDialog extends JDialog {
    public ManageFinesDialog(FineController fineController) {
        setTitle("Lista mandatów");
        setModal(true);
        setSize(650, 380);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Filtruj:"));
        String[] filterOptions = {"Wszystkie", "Nieopłacone", "Opłacone"};
        JComboBox<String> filterBox = new JComboBox<>(filterOptions);
        topPanel.add(filterBox);
        content.add(topPanel, BorderLayout.NORTH);

        DefaultListModel<Fine> fineListModel = new DefaultListModel<>();
        JList<Fine> fineList = new JList<>(fineListModel);
        fineList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(fineList);
        content.add(scrollPane, BorderLayout.CENTER);

        JButton btnMarkPaid = new JButton("Oznacz jako opłacony");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btnMarkPaid);
        content.add(btnPanel, BorderLayout.SOUTH);

        Runnable updateList = () -> {
            String filter = (String) filterBox.getSelectedItem();
            List<Fine> fines = fineController.getList();
            List<Fine> filtered = fines.stream().filter(f -> {
                if ("Nieopłacone".equals(filter))
                    return f.getStatus() == FineStatus.UNPAID;
                if ("Opłacone".equals(filter))
                    return f.getStatus() == FineStatus.PAID;
                return true;
            }).toList();

            fineListModel.clear();
            for (Fine f : filtered) fineListModel.addElement(f);
            if (filtered.isEmpty()) fineListModel.addElement(null); // "Brak"
        };
        updateList.run();
        filterBox.addActionListener(_ -> updateList.run());

        btnMarkPaid.addActionListener(_ -> {
            Fine fine = fineList.getSelectedValue();
            if (fine == null) {
                JOptionPane.showMessageDialog(this, "Wybierz mandat.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (fine.getStatus() == FineStatus.PAID) {
                JOptionPane.showMessageDialog(this, "Mandat już opłacony.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int res = JOptionPane.showConfirmDialog(this, "Oznaczyć mandat jako opłacony?", "Potwierdzenie", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                fineController.markFineAsPaid(fine);
                updateList.run();
            }
        });

        setContentPane(content);
        setVisible(true);
    }
}
