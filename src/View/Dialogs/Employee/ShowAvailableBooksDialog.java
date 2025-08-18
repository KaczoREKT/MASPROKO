package View.Dialogs.Employee;

import Controller.BookController;
import Model.Book;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ShowAvailableBooksDialog {

    private JPanel mainPanel;
    private JTextField searchField;
    private JTable bookTable;

    public ShowAvailableBooksDialog(BookController bookController) {
        mainPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        JLabel label = new JLabel("Dostępne książki w bibliotece:");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 16f));
        topPanel.add(label, BorderLayout.WEST);

        searchField = new JTextField();
        searchField.setToolTipText("Szukaj po tytule, autorze lub gatunku...");
        topPanel.add(searchField, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        List<Book> books = bookController.getAvailableBooks();

        // Tworzymy model danych do tabeli
        String[] columnNames = { "ID", "Tytuł", "Gatunek", "Autor", "Status" };
        Object[][] rowData = books.stream()
                .map(b -> new Object[]{
                        b.getPublicId(),
                        b.getTitle(),
                        b.getGenre(),
                        b.getAuthor(),
                        b.getStatus().toString() })
                .toArray(Object[][]::new);

        bookTable = new JTable(rowData, columnNames);
        JScrollPane scrollPane = new JScrollPane(bookTable);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Filtrowanie po wpisaniu frazy w searchField
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void updateTable() {
                String query = searchField.getText().trim().toLowerCase();
                List<Book> filtered = books.stream()
                        .filter(b -> query.isEmpty()
                                || b.getTitle().toLowerCase().contains(query)
                                || b.getAuthor().toLowerCase().contains(query)
                                || b.getGenre().toLowerCase().contains(query))
                        .toList();
                Object[][] filteredRows = filtered.stream()
                        .map(b -> new Object[]{
                                b.getPublicId(),
                                b.getTitle(),
                                b.getGenre(),
                                b.getAuthor(),
                                b.getStatus().toString() })
                        .toArray(Object[][]::new);

                bookTable.setModel(new javax.swing.table.DefaultTableModel(filteredRows, columnNames));
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateTable(); }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}
