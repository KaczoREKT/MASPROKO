package View.Dialogs.Receptionist;

import Controller.BookController;
import Controller.ClientController;
import Controller.ReservationController;
import Model.Book;
import Model.Client;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class ReserveBookDialog extends JDialog {
    private Book selectedBook = null;
    private Client selectedClient = null;

    public ReserveBookDialog(BookController bookController, ClientController clientController, ReservationController reservationController) {
        setTitle("Rezerwacja książki");
        setModal(true);
        setSize(480, 350);
        setLocationRelativeTo(null);

        // Panel kroków (będzie podmieniany)
        JPanel stepPanel = new JPanel(new CardLayout());

        // ==== Krok 1: Wybór książki ====
        JPanel bookStep = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        int row = 0;

        bookStep.add(new JLabel("Książka:"), gbc);
        List<Book> availableBooks = bookController.getAvailableBooks();
        JComboBox<Book> bookBox = new JComboBox<>(availableBooks.toArray(new Book[0]));
        gbc.gridx = 1;
        bookStep.add(bookBox, gbc);

        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JButton btnNextToCard = new JButton("Dalej");
        bookStep.add(btnNextToCard, gbc);

        // ==== Krok 2: Wprowadź numer karty ====
        JPanel cardStep = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        row = 0;

        cardStep.add(new JLabel("Numer karty klienta:"), gbc);
        JTextField cardNumberField = new JTextField(15);
        gbc.gridx = 1;
        cardStep.add(cardNumberField, gbc);

        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JButton btnFindClient = new JButton("Znajdź klienta");
        cardStep.add(btnFindClient, gbc);

        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 2;
        JLabel clientInfoLabel = new JLabel();
        cardStep.add(clientInfoLabel, gbc);

        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JButton btnNextToSummary = new JButton("Dalej");
        btnNextToSummary.setEnabled(false);
        cardStep.add(btnNextToSummary, gbc);

        // ==== Krok 3: Podsumowanie i rezerwacja ====
        JPanel summaryStep = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        row = 0;

        // Terminy rezerwacji
        summaryStep.add(new JLabel("Data rozpoczęcia:"), gbc);
        JTextField dateFromField = new JTextField(LocalDate.now().toString(), 10);
        gbc.gridx = 1;
        summaryStep.add(dateFromField, gbc);

        gbc.gridy = ++row; gbc.gridx = 0;
        summaryStep.add(new JLabel("Data zakończenia:"), gbc);
        JTextField dateToField = new JTextField(LocalDate.now().plusWeeks(2).toString(), 10);
        gbc.gridx = 1;
        summaryStep.add(dateToField, gbc);

        // Podsumowanie
        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JLabel summaryLabel = new JLabel();
        summaryStep.add(summaryLabel, gbc);

        // Przycisk rezerwuj i anuluj
        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 1;
        JButton btnReserve = new JButton("Rezerwuj");
        summaryStep.add(btnReserve, gbc);

        gbc.gridx = 1;
        JButton btnCancel = new JButton("Anuluj");
        summaryStep.add(btnCancel, gbc);

        // Dodaj panele do CardLayout
        stepPanel.add(bookStep, "BOOK");
        stepPanel.add(cardStep, "CARD");
        stepPanel.add(summaryStep, "SUMMARY");

        setContentPane(stepPanel);

        // === Logika kroków ===

        CardLayout cardLayout = (CardLayout) stepPanel.getLayout();

        // Krok 1 -> Krok 2
        btnNextToCard.addActionListener(e -> {
            selectedBook = (Book) bookBox.getSelectedItem();
            if (selectedBook == null) {
                JOptionPane.showMessageDialog(this, "Wybierz książkę!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            cardLayout.show(stepPanel, "CARD");
        });

        // Krok 2 - Szukaj klienta po numerze karty
        btnFindClient.addActionListener(e -> {
            String cardNumber = cardNumberField.getText().trim();
            Client client = clientController.findClientByCardNumber(cardNumber);
            if (client == null) {
                clientInfoLabel.setText("Nie znaleziono klienta z podanym numerem karty.");
                btnNextToSummary.setEnabled(false);
            } else if (client.getClientCard().getExpirationDate().isBefore(LocalDate.now())) {
                clientInfoLabel.setText("Karta klienta straciła ważność.");
                btnNextToSummary.setEnabled(false);
            } else {
                selectedClient = client;
                clientInfoLabel.setText(String.format(
                        "<html>Imię: %s<br/>Nazwisko: %s<br/>Email: %s<br/>Telefon: %s</html>",
                        client.getFirstName(), client.getLastName(), client.getEmail(), client.getPhoneNumber()
                ));
                btnNextToSummary.setEnabled(true);
            }
        });

        // Krok 2 -> Krok 3 (podsumowanie)
        btnNextToSummary.addActionListener(e -> {
            String dateFrom = dateFromField.getText().trim();
            String dateTo = dateToField.getText().trim();

            summaryLabel.setText(String.format(
                    "<html>Klient: %s %s<br/>Książka: %s<br/>Data od: %s<br/>Data do: %s<br/>Email: %s<br/>Telefon: %s</html>",
                    selectedClient.getFirstName(),
                    selectedClient.getLastName(),
                    selectedBook,
                    dateFrom,
                    dateTo,
                    selectedClient.getEmail(),
                    selectedClient.getPhoneNumber()
            ));
            cardLayout.show(stepPanel, "SUMMARY");
        });

        // Rezerwacja
        btnReserve.addActionListener(e -> {
            try {
                LocalDate dateFrom = LocalDate.parse(dateFromField.getText().trim());
                LocalDate dateTo = LocalDate.parse(dateToField.getText().trim());
                reservationController.reserveBook(selectedBook, selectedClient, dateFrom, dateTo);
                JOptionPane.showMessageDialog(this, "Rezerwacja zakończona sukcesem.");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Błąd rezerwacji", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Anuluj
        btnCancel.addActionListener(e -> dispose());

        setVisible(true);
    }
}
