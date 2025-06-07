package View.ComboBox;

import Model.Sector;

import javax.swing.*;
import java.awt.*;
import java.util.List;
public class SectorJComboBox extends JComboBox<Sector> {
    public SectorJComboBox(List<Sector> sectors) {
        super();
        for (Sector sector : sectors) {
            this.addItem(sector);
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
                if (value instanceof Sector) {
                    setText(((Sector) value).getName());
                }
                return this;
            }
        });
    }
}