package ru.almasgali.ui;

import javax.swing.table.DefaultTableModel;

public class RowHeaderTableModel extends DefaultTableModel {


    public RowHeaderTableModel(int rows, int cols) {
        super(rows, cols);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == 0) {
            return false;
        }
        return super.isCellEditable(row, column);
    }


}
