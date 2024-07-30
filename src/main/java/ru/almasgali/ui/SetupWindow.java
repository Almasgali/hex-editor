package ru.almasgali.ui;

import ru.almasgali.util.Constants;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SetupWindow extends JFrame {

    private final JTextField rowsText;
    private final JTextField colsText;

    public SetupWindow(String title) throws HeadlessException {
        super(title);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JLabel rowsLabel = new JLabel("rows:");
        rowsLabel.setFont(Constants.LABEL_FONT);
        rowsText = new JTextField();
        rowsText.setPreferredSize(new Dimension(200, 50));

        JLabel colsLabel = new JLabel("columns:");
        colsLabel.setFont(Constants.LABEL_FONT);
        colsText = new JTextField();
        colsText.setPreferredSize(new Dimension(200, 50));

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            try {
                validateAndRun();
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        });

        panel.add(rowsLabel);
        panel.add(rowsText);
        panel.add(colsLabel);
        panel.add(colsText);
        panel.add(okButton);
        getContentPane().add(panel);

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(300, 200));
        pack();
        setVisible(true);
    }

    private void validateAndRun() throws IOException {
        int rows = validateTextField("rows", rowsText);
        int cols = validateTextField("cols", colsText);
        if (rows > 0 && cols > 0) {
            dispose();
            MainWindow mainWindow = new MainWindow(
                    "HEX-editor",
                    "./src/main/resources/test-small",
                    rows, cols);
        }
    }

    private int validateTextField(String title, JTextField tf) {
        int value;
        try {
            value = Integer.parseInt(tf.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter valid number in '" + title + "' text field.");
            return -1;
        }
        if (value <= 0) {
            JOptionPane.showMessageDialog(this, "Enter positive number in '" + title + "' text field.");
            return -1;
        }
        return value;
    }
}
