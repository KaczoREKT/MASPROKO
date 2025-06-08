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
        setSize(600, 350);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new BorderLayout(10,10));

        // Nagłówek
        JLabel label = new JLabel("Dostępne książki w bibliotece:", SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 16f));
        content.add(label, BorderLayout.NORTH);

        // Lista dostępnych książek
        DefaultListModel<String> bookListModel = new DefaultListModel<>();
        JList<String> bookList = new JList<>(bookListModel);
        JScrollPane scrollPane = new JScrollPane(bookList);

        List<Book> books = bookController.getAvailableBooks();
        if (books.isEmpty()) {
            bookListModel.addElement("Brak dostępnych książek.");
        } else {
            for (Book b : books) {
                bookListModel.addElement(b.toString());
            }
        }

        content.add(scrollPane, BorderLayout.CENTER);

        setContentPane(content);
        setVisible(true);
    }
}