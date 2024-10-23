package ru.almasgali.ui;

import ru.almasgali.util.Constants;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;

public class TablePopupMenu extends JPopupMenu {

    private int leadX;
    private int leadY;
    private boolean cellCopied;
    private boolean cellCut;
    private boolean rowCopied;
    private boolean rowCut;
    private DefaultTableModel model;
    private final JMenuItem copyCell;
    private final JMenuItem cutCell;
    private final JMenuItem cutCellFillZero;
    private final JMenuItem pasteCellReplace;
    private final JMenuItem pasteCellAppend;
    private final JMenuItem copyRow;
    private final JMenuItem cutRow;
    private final JMenuItem cutRowFillZero;
    private final JMenuItem pasteRowReplace;
    private final JMenuItem pasteRowAppend;
    private final Clipboard clipboard;

    public TablePopupMenu(DefaultTableModel model) {
        super();
        this.model = model;
        this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        this.cellCopied = false;
        this.rowCopied = false;
        copyCell = new JMenuItem(Constants.COPY_CELL);
        copyCell.addActionListener(e -> {
            String value = model.getValueAt(leadX, leadY).toString();
            clipboard.setContents(new StringSelection(value), null);
            cellCopied = true;
            cellCut = false;
        });
        cutCell = new JMenuItem(Constants.CUT_CELL_REMOVE);
        cutCell.addActionListener(e -> {
            String value = model.getValueAt(leadX, leadY).toString();
            clipboard.setContents(new StringSelection(value), null);
            for (int i = leadX; i < model.getRowCount(); ++i) {
                for (int j = 1; j < model.getColumnCount(); ++j) {
                    if (i == leadX && j < leadY) {
                        continue;
                    }
                    int nextI = i;
                    int nextJ = j + 1;
                    if (nextJ == model.getColumnCount()) {
                        nextJ = 0;
                        ++nextI;
                    }
                    if (nextI == model.getRowCount()) {
                        model.setValueAt("", i, j);
                        return;
                    }
                    Object next = model.getValueAt(nextI, nextJ);
                    model.setValueAt(next, i, j);
                }
            }
            cellCut = true;
            cellCopied = false;
        });
        cutCellFillZero = new JMenuItem(Constants.CUT_CELL_FILL_WITH_0);
        cutCellFillZero.addActionListener(e -> {
            String value = model.getValueAt(leadX, leadY).toString();
            model.setValueAt("00", leadX, leadY);
            clipboard.setContents(new StringSelection(value), null);
            cellCut = true;
            cellCopied = false;
        });
        pasteCellReplace = new JMenuItem(Constants.PASTE_CELL_REPLACE);
        pasteCellReplace.addActionListener(e -> {
            try {
                String value = clipboard.getData(DataFlavor.stringFlavor).toString();
                model.setValueAt(value, leadX, leadY);
            } catch (UnsupportedFlavorException | IOException ex) {
                System.err.println(ex.getMessage());
            }
            if (cellCut) {
                cellCut = false;
                clipboard.setContents(new StringSelection(""), null);
            }
        });
        pasteCellAppend = new JMenuItem(Constants.PASTE_CELL_APPEND);
        pasteCellAppend.addActionListener(e -> {
            try {
                String value = clipboard.getData(DataFlavor.stringFlavor).toString();
                for (int i = leadX; i < model.getRowCount(); ++i) {
                    for (int j = 1; j < model.getColumnCount(); ++j) {
                        if (i == leadX && j < leadY) {
                            continue;
                        }
                        Object tmp = model.getValueAt(i, j);
                        model.setValueAt(value, i, j);
                        if (tmp == null || tmp.toString().isEmpty()) {
                            return;
                        }
                        value = tmp.toString();
                    }
                }
                String[] data = new String[this.model.getColumnCount()];
                Arrays.fill(data, "");
                data[0] = String.valueOf(this.model.getRowCount());
                data[1] = value;
                this.model.addRow(data);
                if (cellCut) {
                    cellCut = false;
                    clipboard.setContents(new StringSelection(""), null);
                }
            } catch (UnsupportedFlavorException | IOException ex) {
                System.err.println(ex.getMessage());
            }
        });
        copyRow = new JMenuItem(Constants.COPY_ROW);
        copyRow.addActionListener(e -> {
            StringBuilder row = new StringBuilder();
            for (int i = 1; i < model.getColumnCount(); ++i) {
                row.append(model.getValueAt(leadX, i).toString());
            }
            clipboard.setContents(new StringSelection(row.toString()), null);
            rowCopied = true;
            rowCut = false;
        });
        cutRow = new JMenuItem(Constants.CUT_ROW_REMOVE);
        cutRow.addActionListener(e -> {
            StringBuilder row = new StringBuilder();
            for (int i = 1; i < model.getColumnCount(); ++i) {
                row.append(model.getValueAt(leadX, i).toString());
            }
            clipboard.setContents(new StringSelection(row.toString()), null);
            model.removeRow(leadX);
            String[] data = new String[this.model.getColumnCount()];
            Arrays.fill(data, "");
            data[0] = String.valueOf(this.model.getRowCount());
            model.addRow(data);
            rowCut = true;
            rowCopied = false;
        });
        cutRowFillZero = new JMenuItem(Constants.CUT_ROW_FILL_WITH_0);
        cutRowFillZero.addActionListener(e -> {
            StringBuilder row = new StringBuilder();
            for (int i = 1; i < model.getColumnCount(); ++i) {
                row.append(model.getValueAt(leadX, i).toString());
                model.setValueAt("00", leadX, i);
            }
            clipboard.setContents(new StringSelection(row.toString()), null);
            rowCut = true;
            rowCopied = false;
        });
        pasteRowReplace = new JMenuItem(Constants.PASTE_ROW_REPLACE);
        pasteRowReplace.addActionListener(e -> {
            try {
                String value = clipboard.getData(DataFlavor.stringFlavor).toString();
                for (int i = 1; i < model.getColumnCount(); ++i) {
                    model.setValueAt(value.substring((i - 1) * 2, i * 2), leadX, i);
                }
                if (rowCut) {
                    rowCut = false;
                    clipboard.setContents(new StringSelection(""), null);
                }
            } catch (UnsupportedFlavorException | IOException ex) {
                System.err.println(ex.getMessage());
            }
        });
        pasteRowAppend = new JMenuItem(Constants.PASTE_ROW_APPEND);
        pasteRowAppend.addActionListener(e -> {
            try {
                String value = clipboard.getData(DataFlavor.stringFlavor).toString();
                String[] data = new String[this.model.getColumnCount()];
                data[0] = String.valueOf(leadX);
                for (int i = 1; i < model.getColumnCount(); ++i) {
                    data[i] = value.substring((i - 1) * 2, i * 2);
                }
                model.insertRow(leadX, data);
                for (int i = leadX + 1; i < model.getRowCount(); ++i) {
                    model.setValueAt(i, i, 0);
                }
                if (rowCut) {
                    rowCut = false;
                    clipboard.setContents(new StringSelection(""), null);
                }
            } catch (UnsupportedFlavorException | IOException ex) {
                System.err.println(ex.getMessage());
            }
        });

        add(copyCell);
        addSeparator();
        add(cutCell);
        add(cutCellFillZero);
        addSeparator();
        add(pasteCellReplace);
        add(pasteCellAppend);
        addSeparator();
        add(copyRow);
        addSeparator();
        add(cutRow);
        add(cutRowFillZero);
        addSeparator();
        add(pasteRowReplace);
        add(pasteRowAppend);
    }

    public void show(Component invoker, int x, int y, int X, int Y) {
        super.show(invoker, x, y);
        this.leadX = X;
        this.leadY = Y;
        boolean cellSelected = leadX >= 0 && leadY > 0;
        copyCell.setEnabled(cellSelected);
        cutCell.setEnabled(cellSelected);
        cutCellFillZero.setEnabled(cellSelected);
        pasteCellReplace.setEnabled(cellSelected && (cellCopied || cellCut));
        pasteCellAppend.setEnabled(cellSelected && (cellCopied || cellCut));
        copyRow.setEnabled(cellSelected);
        cutRow.setEnabled(cellSelected);
        cutRowFillZero.setEnabled(cellSelected);
        pasteRowReplace.setEnabled(cellSelected && (rowCopied || rowCut));
        pasteRowAppend.setEnabled(cellSelected && (rowCopied || rowCut));
    }
}
