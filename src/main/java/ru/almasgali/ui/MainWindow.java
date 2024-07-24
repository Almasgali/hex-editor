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
    private JTextField intText;
    private JTextField longText;
    private JTextField floatText;
    private JTextField doubleText;
    private JTextField pageText;
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
        labelPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        Dimension textSize = new Dimension(190, 50);

        JLabel intLabel = new JLabel(Constants.INT_LABEL_PREFIX);
        intText = new JTextField();
        intText.setPreferredSize(textSize);
        intText.setEditable(false);
        JLabel floatLabel = new JLabel(Constants.FLOAT_LABEL_PREFIX);
        floatText = new JTextField();
        floatText.setPreferredSize(textSize);
        floatText.setEditable(false);
        JLabel longLabel = new JLabel(Constants.LONG_LABEL_PREFIX);
        longText = new JTextField();
        longText.setPreferredSize(textSize);
        longText.setEditable(false);
        JLabel doubleLabel = new JLabel(Constants.DOUBLE_LABEL_PREFIX);
        doubleText = new JTextField();
        doubleText.setPreferredSize(textSize);
        doubleText.setEditable(false);

        labelPanel.add(intLabel);
        labelPanel.add(intText);
        labelPanel.add(floatLabel);
        labelPanel.add(floatText);
        labelPanel.add(longLabel);
        labelPanel.add(longText);
        labelPanel.add(doubleLabel);
        labelPanel.add(doubleText);

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

        labelPanel.add(new JLabel("Page: "));
        pageText = new JTextField();
        pageText.setPreferredSize(new Dimension(50, 28));
        pageText.setEditable(false);
        labelPanel.add(pageText);

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
        pageText.setText(String.valueOf(fl.getChunkOrder()));
    }

    private void loadNextChunk() throws IOException {
        readBytes = fl.readNextChunk();
        pageText.setText(String.valueOf(fl.getChunkOrder()));
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
            longText.setText(Long.toString(longValue));
            doubleText.setText(Double.toString(Double.longBitsToDouble(longValue)));
        } else {
            longText.setText(Long.toUnsignedString(longValue));
            doubleText.setText(Double.toString(Double.longBitsToDouble(longValue)));
        }
    }

    private void fill4bLabels(String intHex) {
        int intValue = Integer.parseUnsignedInt(intHex, 16);
        if (signButton.getText().equals(Constants.SIGNED)) {
            intText.setText(Integer.toString(intValue));
            floatText.setText(Float.toString(Float.intBitsToFloat(intValue)));
        } else {
            intText.setText(Integer.toUnsignedString(intValue));
            floatText.setText(Float.toString(Float.intBitsToFloat(intValue)));
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
