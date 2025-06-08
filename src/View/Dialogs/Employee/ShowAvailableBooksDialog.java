package View.Dialogs.Employee;

import Controller.BookController;
import Model.Book;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ShowAvailableBooksDialog extends JDialog {
    public ShowAvailableBooksDialog(BookController bookController) {
        setTitle("Dostępne książki");
        setModal(true);
        setSize(700, 400);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new BorderLayout(10,10));

        // Panel górny: nagłówek + pole wyszukiwania
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        JLabel label = new JLabel("Dostępne książki w bibliotece:", SwingConstants.LEFT);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 16f));
        topPanel.add(label, BorderLayout.WEST);

        JTextField searchField = new JTextField();
        searchField.setToolTipText("Szukaj po tytule, autorze lub gatunku...");
        topPanel.add(searchField, BorderLayout.CENTER);

        content.add(topPanel, BorderLayout.NORTH);

        // Panel środkowy: lista dostępnych książek
        DefaultListModel<String> bookListModel = new DefaultListModel<>();
        JList<String> bookList = new JList<>(bookListModel);
        JScrollPane scrollPane = new JScrollPane(bookList);

        List<Book> books = bookController.getAvailableBooks();

        // Funkcja do aktualizacji listy wg filtra
        Runnable updateList = () -> {
            String query = searchField.getText().trim().toLowerCase();
            bookListModel.clear();
            List<Book> filtered = books.stream()
                    .filter(b -> query.isEmpty()
                            || b.getTitle().toLowerCase().contains(query)
                            || b.getAuthor().toLowerCase().contains(query)
                            || b.getGenre().toLowerCase().contains(query)
                    )
                    .toList();
            if (filtered.isEmpty()) {
                bookListModel.addElement("Brak dostępnych książek spełniających kryteria.");
            } else {
                for (Book b : filtered) {
                    bookListModel.addElement(b.toString());
                }
            }
        };

        // Od razu pokaż całą listę
        updateList.run();

        // Nasłuchiwanie wpisywania
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateList.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateList.run(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateList.run(); }
        });

        content.add(scrollPane, BorderLayout.CENTER);

        setContentPane(content);
        setVisible(true);
    }
}
