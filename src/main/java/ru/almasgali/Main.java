package ru.almasgali;


import ru.almasgali.file.FileLoader;
import ru.almasgali.ui.MainWindow;
import ru.almasgali.util.ByteUtil;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            MainWindow mainWindow = new MainWindow(
                    "HEX-editor",
                    "./src/main/resources/test",
                    3, 5);
            mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainWindow.setResizable(false);
            mainWindow.setLocationRelativeTo(null);
            mainWindow.setVisible(true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}