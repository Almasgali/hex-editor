package ru.almasgali.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CellValidationRenderer extends DefaultTableCellRenderer {


    private final JTextField searchField;

    public CellValidationRenderer(JTextField searchField) {
        this.searchField = searchField;
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row, int column) {
        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value == null) {
            return cell;
        }
        String text = value.toString();
        String textToSearch = searchField.getText();
        if (!text.matches("[0-9a-f]{2}")) {
            cell.setForeground(Color.RED);
        } else if (textToSearch.matches("[0-9a-f]*") && text.contains(textToSearch)) {
            String html = "<html>"
                    + text.substring(0, text.indexOf(textToSearch))
                    + "<span bgcolor=\"yellow\">"
                    + textToSearch
                    + "</span>"
                    + text.substring(text.indexOf(textToSearch) + textToSearch.length())
                    + "</html>";
            cell.setText(html);
        } else if (textToSearch.matches("[0-9a-f.]*") && text.matches(textToSearch)) {
            String html = "<html>"
                    + "<span bgcolor=\"yellow\">"
                    + text
                    + "</span>"
                    + "</html>";
            cell.setText(html);
        } else {
            cell.setForeground(Color.BLACK);
        }
        return cell;
    }
}
