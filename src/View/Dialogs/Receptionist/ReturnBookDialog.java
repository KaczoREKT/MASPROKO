package View.Dialogs.Receptionist;

import Controller.ClientController;
import Controller.ReservationController;
import Model.Book;
import Model.Client;
import Model.Enum.BookStatus;
import Model.Enum.ReservationStatus;
import Model.Reservation;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;

public class ReturnBookDialog extends JDialog {
    public ReturnBookDialog(ClientController clientController, ReservationController reservationController) {
        setTitle("Zwrot książki przez klienta");
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

        // --- Sekcje jak wcześniej ---
        JPanel reservationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel resLabel = new JLabel("Rezerwacja:");
        reservationPanel.add(resLabel);
        JComboBox<Reservation> reservationBox = new JComboBox<>();
        reservationBox.setPreferredSize(new Dimension(250, 25));
        reservationPanel.add(reservationBox);
        content.add(reservationPanel);

        // Książki do zwrotu
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

        // Przyciski
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnReturn = new JButton("Zwróć");
        JButton btnCancel = new JButton("Anuluj");
        btnPanel.add(btnReturn);
        btnPanel.add(btnCancel);
        content.add(btnPanel);

        // --- Logika: wyszukiwanie po numerze karty ---
        btnFind.addActionListener(_ -> {
            String cardNumber = cardField.getText().trim();
            if (cardNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Podaj numer karty klienta!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Wyszukaj klienta po karcie
            Client foundClient = null;
            for (Client c : clientController.getClientList()) {
                if (c.getClientCard() != null && cardNumber.equalsIgnoreCase(c.getClientCard().getPublicId())) {
                    foundClient = c;
                    break;
                }
            }
            reservationBox.removeAllItems();
            bookListModel.clear();
            if (foundClient == null) {
                JOptionPane.showMessageDialog(this, "Nie znaleziono klienta o podanym numerze karty.", "Brak klienta", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Set<Reservation> reservations = foundClient.getReservations();
            List<Reservation> activeReservations = reservations.stream()
                    .filter(r -> r.getBooks().stream().anyMatch(b -> b.getStatus().name().equals("WYPOZYCZONA")))
                    .toList();
            for (Reservation r : activeReservations) reservationBox.addItem(r);
            if (activeReservations.isEmpty()) {
                reservationBox.addItem(null);
            } else {
                reservationBox.setSelectedIndex(0);
            }
        });

        // Po zmianie rezerwacji: pokaż książki do zwrotu
        reservationBox.addActionListener(_ -> {
            bookListModel.clear();
            Object selected = reservationBox.getSelectedItem();
            if (selected instanceof Reservation selectedReservation) {
                for (Book b : selectedReservation.getBooks()) {
                    if (b.getStatus().name().equals("WYPOZYCZONA")) bookListModel.addElement(b);
                }
            }
        });

        // Obsługa zwrotu – identycznie jak dotychczas
        btnReturn.addActionListener(_ -> {
            Object selected = reservationBox.getSelectedItem();
            if (!(selected instanceof Reservation selectedReservation)) {
                JOptionPane.showMessageDialog(this, "Wybierz rezerwację i książki do zwrotu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<Book> selectedBooks = bookList.getSelectedValuesList();
            if (selectedBooks.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Wybierz książki do zwrotu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                for (Book b : selectedBooks) {
                    b.setStatus(BookStatus.DOSTEPNA);
                    b.setReservation(null);
                }
                boolean allReturned = selectedReservation.getBooks().stream().allMatch(book -> book.getStatus() != BookStatus.WYPOZYCZONA);
                if (allReturned) {
                    selectedReservation.setStatus(ReservationStatus.ZAKOŃCZONA);
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
