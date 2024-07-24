package ru.almasgali.ui;

import ru.almasgali.file.FileLoader;
import ru.almasgali.util.ByteUtil;
import ru.almasgali.util.Constants;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

public class MainWindow extends JFrame {

    private final JTable table;
    private int rows;
    private int cols;
    private int readBytes;
    private final DefaultTableModel model;
    private final FileLoader fl;
    private JTextField byteText;
    private JTextField intText;
    private JTextField longText;
    private JTextField floatText;
    private JTextField doubleText;
    private JTextField pageText;
    private JButton signButton;
    private JTextField searchField;

    public MainWindow(String title, String path, int rows, int cols) throws HeadlessException, IOException {
        super(title);
        this.rows = rows;
        this.cols = cols;
        this.fl = new FileLoader(path, rows * cols);
        this.model = new RowHeaderTableModel(rows, cols + 1);
        this.searchField = getSearchField();

        byteText = new JTextField();
        intText = new JTextField();
        floatText = new JTextField();
        longText = new JTextField();
        doubleText = new JTextField();

        table = new JTable(model);
        table.setFont(Constants.TABLE_FONT);
        table.getTableHeader().setFont(Constants.TABLE_FONT);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getSelectionModel()
                .addListSelectionListener(new RowListener());
        table.getColumnModel()
                .getSelectionModel()
                .addListSelectionListener(new ColumnListener());

        TableColumnModel tcm = table.getColumnModel();
        tcm.getColumn(0).setHeaderValue("");
        tcm.getColumn(0).setCellRenderer(new RowHeaderColorRenderer());
        for (int i = 1; i <= cols; ++i) {
            tcm.getColumn(i).setHeaderValue(i - 1);
            tcm.getColumn(i).setCellRenderer(new CellValidationRenderer(searchField));
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

        addLabelToPanel(labelPanel, Constants.BYTE_LABEL_PREFIX, byteText, textSize);
        addLabelToPanel(labelPanel, Constants.INT_LABEL_PREFIX, intText, textSize);
        addLabelToPanel(labelPanel, Constants.FLOAT_LABEL_PREFIX, floatText, textSize);
        addLabelToPanel(labelPanel, Constants.LONG_LABEL_PREFIX, longText, textSize);
        addLabelToPanel(labelPanel, Constants.DOUBLE_LABEL_PREFIX, doubleText, textSize);

        signButton = new JButton(Constants.SIGNED);
        signButton.addActionListener(e -> {
            JButton b = (JButton) e.getSource();
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

        labelPanel.add(searchField);

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

    private JTextField getSearchField() {
        JTextField searchField = new JTextField("Search");
        searchField.setForeground(Color.GRAY);

        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText("Search");
                }
            }
        });
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                table.repaint();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                table.repaint();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                table.repaint();
            }
        });
        searchField.setPreferredSize(new Dimension(270, 50));
        return searchField;
    }

    private void addLabelToPanel(
            JPanel panel,
            String LabelPrefix,
            JTextField text,
            Dimension textSize) {
        JLabel label = new JLabel(LabelPrefix);
        text.setPreferredSize(textSize);
        text.setEditable(false);
        panel.add(label);
        panel.add(text);
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
        displayValues();
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
        fillByteValue(model.getValueAt(leadX, leadY).toString());
        StringBuilder intHex = new StringBuilder();
        StringBuilder longHex = new StringBuilder();
        for (int i = 0; i < 8; ++i) {
            int curX = leadX;
            int curY = leadY + i;
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

    private void fillByteValue(String byteHex) {
        byte b;
        try {
            b = (byte) Integer.parseInt(byteHex, 16);
        } catch (NumberFormatException e) {
            b = 0;
        }
        if (signButton.getText().equals(Constants.SIGNED)) {
            byteText.setText(Byte.toString(b));
        } else {
            byteText.setText(Integer.toString(b & 0xff));
        }
    }

    private void fill8bLabels(String longHex) {
        long longValue;
        try {
            longValue = Long.parseUnsignedLong(longHex, 16);
        } catch (NumberFormatException e) {
            longValue = 0;
        }
        if (signButton.getText().equals(Constants.SIGNED)) {
            longText.setText(Long.toString(longValue));
            doubleText.setText(Double.toString(Double.longBitsToDouble(longValue)));
        } else {
            longText.setText(Long.toUnsignedString(longValue));
            doubleText.setText(Double.toString(Double.longBitsToDouble(longValue)));
        }
    }

    private void fill4bLabels(String intHex) {
        int intValue;
        try {
            intValue = Integer.parseUnsignedInt(intHex, 16);
        } catch (NumberFormatException e) {
            intValue = 0;
        }
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
