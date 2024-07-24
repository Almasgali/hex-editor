package ru.almasgali;


import ru.almasgali.ui.MainWindow;

public class Main {
    public static void main(String[] args) {
        try {
            MainWindow mainWindow = new MainWindow(
                    "HEX-editor",
                    "./src/main/resources/test-small",
                    1, 8);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}