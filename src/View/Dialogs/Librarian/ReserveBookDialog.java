package View.Dialogs.Librarian;

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
    private List<Book> books;
    private Client selectedClient = null;

    public ReserveBookDialog(BookController bookController, ClientController clientController, ReservationController reservationController) {
        setTitle("Rezerwacja książki");
        setModal(true);
        setSize(550, 380);
        setLocationRelativeTo(null);

        JPanel stepPanel = new JPanel(new CardLayout());

        JPanel bookStep = new JPanel(new BorderLayout(10, 10));
        JPanel searchPanel = new JPanel(new BorderLayout(5,5));
        searchPanel.add(new JLabel("Szukaj książki:"), BorderLayout.WEST);
        JTextField searchField = new JTextField();
        searchPanel.add(searchField, BorderLayout.CENTER);
        bookStep.add(searchPanel, BorderLayout.NORTH);

        DefaultListModel<Book> bookListModel = new DefaultListModel<>();
        JList<Book> bookList = new JList<>(bookListModel);
        bookList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane bookScroll = new JScrollPane(bookList);
        bookScroll.setPreferredSize(new Dimension(510, 120));
        bookStep.add(bookScroll, BorderLayout.CENTER);

        JButton btnNextToCard = new JButton("Dalej");
        JPanel btnPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel1.add(btnNextToCard);
        bookStep.add(btnPanel1, BorderLayout.SOUTH);

        List<Book> availableBooks = bookController.getAvailableBooks();
        Runnable updateBookList = () -> {
            String query = searchField.getText().trim().toLowerCase();
            bookListModel.clear();
            List<Book> filtered = availableBooks.stream()
                    .filter(b -> query.isEmpty()
                            || b.getTitle().toLowerCase().contains(query)
                            || b.getAuthor().toLowerCase().contains(query)
                            || b.getGenre().toLowerCase().contains(query)
                    )
                    .toList();
            if (filtered.isEmpty()) {
                bookListModel.addElement(null);
            } else {
                for (Book b : filtered) bookListModel.addElement(b);
            }
        };
        updateBookList.run();
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateBookList.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateBookList.run(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateBookList.run(); }
        });

        JPanel cardStep = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
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

        JPanel summaryStep = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        row = 0;

        summaryStep.add(new JLabel("Data rozpoczęcia:"), gbc);
        JTextField dateFromField = new JTextField(LocalDate.now().toString(), 10);
        gbc.gridx = 1;
        summaryStep.add(dateFromField, gbc);

        gbc.gridy = ++row; gbc.gridx = 0;
        summaryStep.add(new JLabel("Data zakończenia:"), gbc);
        JTextField dateToField = new JTextField(LocalDate.now().plusDays(3).toString(), 10);
        gbc.gridx = 1;
        summaryStep.add(dateToField, gbc);

        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JLabel summaryLabel = new JLabel();
        summaryStep.add(summaryLabel, gbc);

        gbc.gridy = ++row; gbc.gridx = 0; gbc.gridwidth = 1;
        JButton btnReserve = new JButton("Rezerwuj");
        summaryStep.add(btnReserve, gbc);

        stepPanel.add(bookStep, "BOOK");
        stepPanel.add(cardStep, "CARD");
        stepPanel.add(summaryStep, "SUMMARY");

        setContentPane(stepPanel);

        CardLayout cardLayout = (CardLayout) stepPanel.getLayout();

        btnNextToCard.addActionListener(_ -> {
            books = bookList.getSelectedValuesList();
            if (books.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Wybierz książkę z listy!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            cardLayout.show(stepPanel, "CARD");
        });

        btnFindClient.addActionListener(_ -> {
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

        btnNextToSummary.addActionListener(_ -> {
            String dateFrom = dateFromField.getText().trim();
            String dateTo = dateToField.getText().trim();
            String selectedBooksString = String.valueOf(books.stream().map(Object::toString).toList());
            summaryLabel.setText(String.format(
                    "<html>Klient: %s %s<br/>Książka: %s<br/>Data od: %s<br/>Data do: %s<br/>Email: %s<br/>Telefon: %s</html>",
                    selectedClient.getFirstName(),
                    selectedClient.getLastName(),
                    selectedBooksString,
                    dateFrom,
                    dateTo,
                    selectedClient.getEmail(),
                    selectedClient.getPhoneNumber()
            ));
            cardLayout.show(stepPanel, "SUMMARY");
        });

        btnReserve.addActionListener(_ -> {
            try {
                LocalDate dateFrom = LocalDate.parse(dateFromField.getText().trim());
                LocalDate dateTo = LocalDate.parse(dateToField.getText().trim());
                reservationController.reserveBook(books, selectedClient, dateFrom, dateTo);
                JOptionPane.showMessageDialog(this, "Rezerwacja zakończona sukcesem.");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Błąd rezerwacji", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}
