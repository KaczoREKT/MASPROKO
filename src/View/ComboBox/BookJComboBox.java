package View.ComboBox;

import Model.Book;

import javax.swing.*;
import java.awt.*;

public class BookJComboBox extends JComboBox<Book> {
    public BookJComboBox() {
        super();
        this.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Book) {
                    setText(((Book) value).getTitle());
                }
                return this;
            }
        });
    }
}
