package ru.almasgali.ui;

import ru.almasgali.file.FileLoader;
import ru.almasgali.util.ByteUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.IOException;

public class MainWindow extends JFrame {

    private int rows;
    private int cols;
    private DefaultTableModel model;
    private final FileLoader fl;
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;


    public MainWindow(String title, String path, int rows, int cols) throws HeadlessException, IOException {
        super(title);
        this.rows = rows;
        this.cols = cols;
        this.fl = new FileLoader(path, rows * cols);
        this.model = new RowHeaderTableModel(rows, cols + 1);

        JTable jTable = new JTable(model);
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumnModel tcm = jTable.getColumnModel();
        tcm.getColumn(0).setHeaderValue("");
        tcm.getColumn(0).setCellRenderer(new ColorRenderer());
        for (int i = 1; i <= cols; ++i) {
            tcm.getColumn(i).setHeaderValue(i);
        }

        JPanel panel = new JPanel();
        panel.setSize(new Dimension(640, 480));

        JScrollPane scroll = new JScrollPane(jTable);

        panel.add(scroll);

        add(panel);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        loadNextChunk();
        display();
    }

    private void display() {
        int index = 0;
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j <= cols; ++j) {
                if (j == 0) {
                    model.setValueAt(i, i, j);

                } else {
                    model.setValueAt(getValue(index), i, j);
                    ++index;
                }
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
