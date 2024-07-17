package ru.almasgali.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ColorRenderer extends DefaultTableCellRenderer {

    private static final Color DEFAULT_GRAY = new Color(239, 239, 239, 255);

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        cell.setBackground(DEFAULT_GRAY);
        cell.setFocusable(false);
        setHorizontalAlignment(CENTER);
//        cell.setText(table.getValueAt(row, column).toString());
        return this;
    }
}