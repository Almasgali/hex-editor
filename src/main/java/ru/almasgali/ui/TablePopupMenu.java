package ru.almasgali.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class TablePopupMenu extends JPopupMenu {

    private int leadX;
    private int leadY;
    private final JMenuItem copyCell;
    private final JMenuItem pasteCellReplace;
    private final JMenuItem pasteCellAppend;
    private JTable table;
    private final Clipboard clipboard;

    public TablePopupMenu(JTable table) {
        super();
        this.table = table;
        this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        copyCell = new JMenuItem("Copy cell");
        copyCell.addActionListener(e -> {
            String value = table.getValueAt(leadX, leadY).toString();
            clipboard.setContents(new StringSelection(value), null);
        });
        pasteCellReplace = new JMenuItem("Paste cell (replace)");
        pasteCellReplace.addActionListener(e -> {
            try {
                String value = clipboard.getData(DataFlavor.stringFlavor).toString();
                table.setValueAt(value, leadX, leadY);
            } catch (UnsupportedFlavorException | IOException ex) {
                System.err.println(ex.getMessage());
            }
        });
        pasteCellAppend = new JMenuItem("Paste cell (append)");
        pasteCellAppend.addActionListener(e -> {
            try {
                String value = clipboard.getData(DataFlavor.stringFlavor).toString();
                for (int i = leadX; i < table.getRowCount(); ++i) {
                    for (int j = 1; j < table.getColumnCount(); ++j) {
                        if (i == leadX && j < leadY) {
                            continue;
                        }
                        Object tmp = table.getValueAt(i, j);
                        table.setValueAt(value, i, j);
                        if (tmp == null || tmp.toString().isEmpty()) {
                            return;
                        }
                        value = tmp.toString();
                    }
                }
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                String[] data = new String[model.getColumnCount()];
                data[0] = String.valueOf(model.getRowCount());
                data[1] = value;
                for (int i = 2; i < model.getColumnCount(); ++i) {
                    data[i] = "";
                }
                model.addRow(data);
            } catch (UnsupportedFlavorException | IOException ex) {
                System.err.println(ex.getMessage());
            }
        });

        add(copyCell);
        add(pasteCellReplace);
        add(pasteCellAppend);
    }

    @Override
    public void show(Component invoker, int x, int y) {
        super.show(invoker, x, y);
        leadX = table
                .getSelectionModel()
                .getLeadSelectionIndex();
        leadY = table
                .getColumnModel()
                .getSelectionModel()
                .getLeadSelectionIndex();
        boolean enabled = leadX >= 0 && leadY > 0;
        copyCell.setEnabled(enabled);
        pasteCellReplace.setEnabled(enabled);
        pasteCellAppend.setEnabled(enabled);
    }
}
