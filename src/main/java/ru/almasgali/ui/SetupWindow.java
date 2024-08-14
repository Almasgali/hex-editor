package ru.almasgali.ui;

import ru.almasgali.util.Constants;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.io.IOException;

public class SetupWindow extends JFrame {

    private final JTextField rowsText;
    private final JTextField colsText;
    private final JTextField fileText;

    public SetupWindow(String title) throws HeadlessException {
        super(title);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JLabel rowsLabel = new JLabel(Constants.ROWS_LABEL);
        rowsLabel.setFont(Constants.LABEL_FONT);
        rowsText = new JTextField();
        Dimension textFieldSize = new Dimension(200, 50);
        rowsText.setPreferredSize(textFieldSize);

        JLabel colsLabel = new JLabel(Constants.COLUMNS_LABEL);
        colsLabel.setFont(Constants.LABEL_FONT);
        colsText = new JTextField();
        colsText.setPreferredSize(textFieldSize);

        JButton fileButton = new JButton(Constants.CHOOSE_FILE);
        fileText = new JTextField();
        fileText.setEditable(false);
        fileText.setPreferredSize(textFieldSize);

        JFileChooser chooser = new JFileChooser();

        fileButton.addActionListener(e -> {
            int val = chooser.showOpenDialog(panel);
            if (val == JFileChooser.APPROVE_OPTION) {
                fileText.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        JButton okButton = new JButton(Constants.OK);
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
        panel.add(fileButton);
        panel.add(fileText);
        panel.add(okButton);
        getContentPane().add(panel);

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(Constants.SETUP_WINDOW_SIZE);
        pack();
        setVisible(true);
    }

    private void validateAndRun() throws IOException {
        int rows = validateTextField("rows", rowsText);
        int cols = validateTextField("cols", colsText);
        String path = fileText.getText();
        if (path.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You didn't choose file.");
        } else if (rows > 0 && cols > 0) {
            dispose();
            MainWindow mainWindow = new MainWindow(
                    Constants.MAIN_TITLE,
                    path,
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
