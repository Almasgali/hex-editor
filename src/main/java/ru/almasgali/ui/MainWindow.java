package ru.almasgali.ui;

import ru.almasgali.file.FileLoader;
import ru.almasgali.util.ByteUtil;
import ru.almasgali.util.Constants;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.IOException;

public class MainWindow extends JFrame {

    private final JTable table;
    private int rows;
    private int cols;
    private int readBytes;
    private final DefaultTableModel model;
    private final FileLoader fl;
    private JLabel intLabel;
    private JLabel longLabel;
    private JLabel floatLabel;
    private JLabel doubleLabel;
    private JButton signButton;


    public MainWindow(String title, String path, int rows, int cols) throws HeadlessException, IOException {
        super(title);
        this.rows = rows;
        this.cols = cols;
        this.fl = new FileLoader(path, rows * cols);
        this.model = new RowHeaderTableModel(rows, cols + 1);

        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getSelectionModel()
                .addListSelectionListener(new RowListener());
        table.getColumnModel()
                .getSelectionModel()
                .addListSelectionListener(new ColumnListener());

        TableColumnModel tcm = table.getColumnModel();
        tcm.getColumn(0).setHeaderValue("");
        tcm.getColumn(0).setCellRenderer(new ColorRenderer());
        for (int i = 1; i <= cols; ++i) {
            tcm.getColumn(i).setHeaderValue(i - 1);
        }

        JPanel panel = getPanel(table);

        add(panel);
        setSize(Constants.WIDTH, Constants.HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        loadNextChunk();
        display();
    }

    private JPanel getPanel(JTable table) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));

        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll);

        JPanel labelPanel = new JPanel();
        labelPanel.setPreferredSize(new Dimension(280, Constants.HEIGHT));
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));

        intLabel = new JLabel(Constants.INT_LABEL_PREFIX);
        floatLabel = new JLabel(Constants.FLOAT_LABEL_PREFIX);
        longLabel = new JLabel(Constants.LONG_LABEL_PREFIX);
        doubleLabel = new JLabel(Constants.DOUBLE_LABEL_PREFIX);

        labelPanel.add(intLabel);
        labelPanel.add(floatLabel);
        labelPanel.add(longLabel);
        labelPanel.add(doubleLabel);

        signButton = new JButton(Constants.SIGNED);
        signButton.addActionListener(e -> {
            JButton b = (JButton) e.getSource();
            System.out.println(b.getText());
            if (b.getText().equals(Constants.SIGNED)) {
                b.setText(Constants.UNSIGNED);
                displayValues();
            } else {
                b.setText(Constants.SIGNED);
                displayValues();
            }
        });

        labelPanel.add(signButton);

        panel.add(labelPanel, BorderLayout.WEST);

        JButton buttonUp = new JButton("↑");
        buttonUp.addActionListener((event) -> {
            try {
                loadPreviousChunk();
                if (readBytes != -1) {
                    display();
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        });
        panel.add(buttonUp, BorderLayout.NORTH);

        JButton buttonDown = new JButton("↓");
        buttonDown.addActionListener((event) -> {
            try {
                loadNextChunk();
                if (readBytes != -1) {
                    display();
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        });
        panel.add(buttonDown, BorderLayout.SOUTH);
        return panel;
    }

    private void display() {
        int index = 0;
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j <= cols; ++j) {
                if (j == 0) {
                    model.setValueAt(i, i, j);
                } else if (index < readBytes) {
                    model.setValueAt(getHexValue(index), i, j);
                    ++index;
                } else {
                    model.setValueAt("", i, j);
                }
            }
        }
    }

    private void loadPreviousChunk() throws IOException {
        readBytes = fl.readPrevChunk();
    }

    private void loadNextChunk() throws IOException {
        readBytes = fl.readNextChunk();
    }

    private String getHexValue(int index) {
        return ByteUtil.byteToHex(fl.getChunk()[index]);
    }

    private void displayValues() {
        int leadX = table
                .getSelectionModel()
                .getLeadSelectionIndex();
        int leadY = table
                .getColumnModel()
                .getSelectionModel()
                .getLeadSelectionIndex();
        StringBuilder intHex = new StringBuilder();
        StringBuilder longHex = new StringBuilder();
        for (int i = 0; i < 8; ++i) {
            int curX = leadX;
            int curY = leadY + i;
            System.out.println(curX + " " + curY);
            while (curY >= cols + 1) {
                curY -= cols + 1;
                ++curX;
            }
            if (curX >= rows) {
                if (i >= 4) {
                    fill4bLabels(intHex.toString());
                } else {
                    fill4bLabels("0");
                }
                fill8bLabels("0");
                return;
            }
            String value = model.getValueAt(curX, curY).toString();
            if (i < 4) {
                intHex.append(value);
            }
            longHex.append(value);
        }
        fill4bLabels(intHex.toString());

        fill8bLabels(longHex.toString());
    }

    private void fill8bLabels(String longHex) {
        long longValue = Long.parseUnsignedLong(longHex, 16);
        if (signButton.getText().equals(Constants.SIGNED)) {
            longLabel.setText(Constants.LONG_LABEL_PREFIX + longValue);
            doubleLabel.setText(Constants.DOUBLE_LABEL_PREFIX + Double.longBitsToDouble(longValue));
        } else {
            longLabel.setText(Constants.LONG_LABEL_PREFIX + Long.toUnsignedString(longValue));
            doubleLabel.setText(Constants.DOUBLE_LABEL_PREFIX + Double.longBitsToDouble(longValue));
        }
    }

    private void fill4bLabels(String intHex) {
        int intValue = Integer.parseUnsignedInt(intHex, 16);
        if (signButton.getText().equals(Constants.SIGNED)) {
            intLabel.setText(Constants.INT_LABEL_PREFIX + intValue);
            floatLabel.setText(Constants.FLOAT_LABEL_PREFIX + Float.intBitsToFloat(intValue));
        } else {
            intLabel.setText(Constants.INT_LABEL_PREFIX + Integer.toUnsignedString(intValue));
            floatLabel.setText(Constants.FLOAT_LABEL_PREFIX + Float.intBitsToFloat(intValue));
        }
    }

    private class RowListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            displayValues();
        }
    }

    private class ColumnListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            displayValues();
        }
    }
}
