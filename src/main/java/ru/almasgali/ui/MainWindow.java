package ru.almasgali.ui;

import ru.almasgali.file.FileLoader;
import ru.almasgali.util.ByteUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;

public class MainWindow extends JFrame {

    private int rows;
    private int cols;
    private final FileLoader fl;
    private DefaultTableModel model;

    public MainWindow(String title, String path, int rows, int cols) throws HeadlessException, IOException {
        super(title);
        this.rows = rows;
        this.cols = cols;
        this.fl = new FileLoader(path, rows * cols);
        model = new DefaultTableModel(rows, cols);
        JTable jTable = new JTable(model);
        JScrollPane scroll = new JScrollPane(jTable);
        JViewport rowHeader = new JViewport();
        for (int i = 0; i < rows; ++i) {
            rowHeader.add(new JLabel(String.valueOf(i)));
        }
        scroll.setRowHeader(rowHeader);
        JViewport colHeader = new JViewport();
        for (int i = 0; i < rows; ++i) {
            colHeader.add(new JLabel(String.valueOf(i)));
        }
        scroll.setColumnHeader(colHeader);
        add(jTable);
        setSize(640, 480);
        loadNextChunk();
        display();
    }

    private void display() {
        int index = 0;
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                model.setValueAt(getValue(index), i, j);
                ++index;
            }
        }
    }

    private void loadPreviousChunk() throws IOException {
        if (fl.getChunkOrder() > 1) {
            fl.readPrevChunk();
        }
    }

    private void loadNextChunk() throws IOException {
        fl.readNextChunk();
    }

    private String getValue(int index) {
        return ByteUtil.byteToHex(fl.getChunk()[index]);
    }
}
