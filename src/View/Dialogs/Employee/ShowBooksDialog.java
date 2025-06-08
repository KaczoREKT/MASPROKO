package View.Dialogs.Employee;

import Controller.BookController;
import Model.Book;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ShowBooksDialog extends JDialog {
    public ShowBooksDialog(BookController bookController) {
        setTitle("Lista wszystkich książek");
        setModal(true);
        setSize(600, 350);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new BorderLayout(10,10));

        // Panel górny: nagłówek (opcjonalnie)
        JLabel label = new JLabel("Wszystkie książki w bibliotece:", SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 16f));
        content.add(label, BorderLayout.NORTH);

        // Panel środkowy: lista książek
        DefaultListModel<String> bookListModel = new DefaultListModel<>();
        JList<String> bookList = new JList<>(bookListModel);
        JScrollPane scrollPane = new JScrollPane(bookList);

        List<Book> books = bookController.getBookList();
        if (books.isEmpty()) {
            bookListModel.addElement("Brak książek w bibliotece.");
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