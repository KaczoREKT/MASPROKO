package View.Dialogs.Librarian;

import Controller.ClientController;
import Controller.LoanController;
import Model.Book;
import Model.Client;
import Model.Enum.BookStatus;
import Model.Enum.LoanStatus;
import Model.Loan;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReturnBookDialog extends JDialog {
    public ReturnBookDialog(ClientController clientController, LoanController loanController) {
        setTitle("Zwrot książek");
        setModal(true);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        // --- Wyszukiwanie po numerze karty ---
        JPanel cardPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cardPanel.add(new JLabel("Numer karty klienta:"));
        JTextField cardField = new JTextField(16);
        cardField.setPreferredSize(new Dimension(180, 25));
        cardPanel.add(cardField);
        JButton btnFind = new JButton("Szukaj");
        cardPanel.add(btnFind);

        content.add(cardPanel);

        JPanel reservationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel resLabel = new JLabel("Wypożyczenie:");
        reservationPanel.add(resLabel);
        JComboBox<Loan> loanBox = new JComboBox<>();
        loanBox.setPreferredSize(new Dimension(250, 25));
        reservationPanel.add(loanBox);
        content.add(reservationPanel);

        DefaultListModel<Book> bookListModel = new DefaultListModel<>();
        JList<Book> bookList = new JList<>(bookListModel);
        bookList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane bookScroll = new JScrollPane(bookList);
        bookScroll.setPreferredSize(new Dimension(540, 110));
        JPanel bookPanel = new JPanel();
        bookPanel.setLayout(new BorderLayout());
        bookPanel.add(new JLabel("Książki do zwrotu:"), BorderLayout.NORTH);
        bookPanel.add(bookScroll, BorderLayout.CENTER);
        content.add(bookPanel);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnReturn = new JButton("Zwróć");
        JButton btnCancel = new JButton("Anuluj");
        btnPanel.add(btnReturn);
        btnPanel.add(btnCancel);
        content.add(btnPanel);

        btnFind.addActionListener(_ -> {
            String cardNumber = cardField.getText().trim();
            if (cardNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Podaj numer karty klienta!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Wyszukaj klienta po karcie
            Client foundClient = null;
            for (Client c : clientController.getList()) {
                if (c.getClientCard() != null && cardNumber.equalsIgnoreCase(c.getClientCard().getPublicId())) {
                    foundClient = c;
                    break;
                }
            }
            loanBox.removeAllItems();
            bookListModel.clear();
            if (foundClient == null) {
                JOptionPane.showMessageDialog(this, "Nie znaleziono klienta o podanym numerze karty.", "Brak klienta", JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<Loan> pendingLoans = loanController.getPendingLoans(foundClient);
            for (Loan l : pendingLoans) loanBox.addItem(l);
            if (pendingLoans.isEmpty()) {
                loanBox.addItem(null);
            } else {
                loanBox.setSelectedIndex(0);
            }
        });

        loanBox.addActionListener(_ -> {
            bookListModel.clear();
            Object selected = loanBox.getSelectedItem();
            if (selected instanceof Loan selectedLoan) {
                for (Book b : selectedLoan.getBooks()) {
                    if (b.getStatus().name().equals(BookStatus.LOANED.name())) bookListModel.addElement(b);
                }
            }
        });

        btnReturn.addActionListener(_ -> {
            Object selected = loanBox.getSelectedItem();
            if (!(selected instanceof Loan selectedLoan)) {
                JOptionPane.showMessageDialog(this, "Wybierz wypożyczenie i książki do zwrotu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<Book> selectedBooks = bookList.getSelectedValuesList();
            if (selectedBooks.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Wybierz książki do zwrotu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                for (Book b : selectedBooks) {
                    b.setStatus(BookStatus.AVAILABLE);
                    b.setReservation(null);
                }
                boolean allReturned = selectedLoan.getBooks().stream().allMatch(book -> book.getStatus() != BookStatus.LOANED);
                if (allReturned) {
                    selectedLoan.setStatus(LoanStatus.ENDED);
                }

                JOptionPane.showMessageDialog(this, "Książki zostały zwrócone.");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Błąd podczas zwrotu: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(_ -> dispose());

        setContentPane(content);
        setVisible(true);
    }
}
