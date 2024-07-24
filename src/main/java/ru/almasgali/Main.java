package ru.almasgali;


import ru.almasgali.ui.MainWindow;

public class Main {
    public static void main(String[] args) {
        try {
            MainWindow mainWindow = new MainWindow(
                    "HEX-editor",
                    "./src/main/resources/test",
                    4, 15);
//            JFrame mainFrame = new JFrame();
//            JPanel panel = new JPanel();
//            mainFrame.setSize(new Dimension(640, 480));
//            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            JTable table = new JTable(
//                    new DefaultTableModel(
//                            new Object[][] {
//                                    new Object[] {"a", "b", "c"},
//                                    new Object[] {"d", "e", "f"},
//                            },
//                            new Object[] {
//                                    "1", "2", "3"
//                            }
//                    )
//            );
//            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//            JScrollPane scrollPane = new JScrollPane(
//                    table,
//                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
//                    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//            JViewport rowHeader = new JViewport();
//            for (int i = 0; i < 2; ++i) {
//                JLabel label = new JLabel(String.valueOf(i));
//                label.setMaximumSize(new Dimension(48, 24));
//                label.setMinimumSize(new Dimension(48, 24));
//                label.setPreferredSize(new Dimension(48, 24));
//                rowHeader.add(label);
//            }
//            scrollPane.setRowHeaderView(rowHeader);
//            panel.add(scrollPane);
//            mainFrame.add(panel);
//            mainFrame.setVisible(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}