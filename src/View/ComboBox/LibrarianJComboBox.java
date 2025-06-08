package View.ComboBox;

import Model.Librarian;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LibrarianJComboBox extends JComboBox<Librarian> {
    public LibrarianJComboBox(List<Librarian> librarians) {
        super();
        for (Librarian librarian : librarians) {
            this.addItem(librarian);
        }
        this.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Librarian) {
                    Librarian lib = (Librarian) value;
                    setText(lib.getFirstName() + " " + lib.getLastName());
                }
                return this;
            }
        });
    }
}
