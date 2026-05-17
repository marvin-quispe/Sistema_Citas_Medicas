package vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

public class HeaderRenderer extends JLabel implements TableCellRenderer {

    private static final long serialVersionUID = 1L;
    private static final Border BORDER =
            javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(200, 210, 220));

    private final Color bg;

    public HeaderRenderer(Color bg) {
        this.bg = bg;
        setOpaque(true);
        setBackground(bg);
        setForeground(Color.WHITE);
        setFont(new Font("Tahoma", Font.BOLD, 11));
        setHorizontalAlignment(LEFT);
        setBorder(BORDER);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        setText(value != null ? value.toString() : "");
        return this;
    }
}
