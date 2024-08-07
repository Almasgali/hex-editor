package ru.almasgali.util;

import java.awt.*;

public class Constants {
    public static final Font TABLE_FONT = new Font("Roboto", Font.PLAIN, 16);
    public static final Font LABEL_FONT = new Font("Century Gothic", Font.PLAIN, 16);
    //    public static final Font BUTTON_FONT = new Font("Verdana", Font.BOLD, 14);
    public static final String BYTE_LABEL_PREFIX = "byte: ";
    public static final String INT_LABEL_PREFIX = "int (4b): ";
    public static final String FLOAT_LABEL_PREFIX = "float (4b): ";
    public static final String LONG_LABEL_PREFIX = "long (8b): ";
    public static final String DOUBLE_LABEL_PREFIX = "double (8b): ";
    public static final String SIGNED = "Signed";
    public static final String UNSIGNED = "Unsigned";
    public static final String SETUP_TITLE = "Setup";
    public static final String MAIN_TITLE = "HEX-editor";
    public static final String ROWS_LABEL = "rows:";
    public static final String COLUMNS_LABEL = "columns:";
    public static final String CHOOSE_FILE = "Choose file...";
    public static final String OK = "OK";
    public static final String COPY_CELL = "Copy cell";
    public static final String CUT_CELL_REMOVE = "Cut cell (remove)";
    public static final String CUT_CELL_FILL_WITH_0 = "Cut cell (fill with 0)";
    public static final String PASTE_CELL_REPLACE = "Paste cell (replace)";
    public static final String PASTE_CELL_APPEND = "Paste cell (append)";
    public static final String COPY_ROW = "Copy row";
    public static final String CUT_ROW_REMOVE = "Cut row (remove)";
    public static final String CUT_ROW_FILL_WITH_0 = "Cut row (fill with 0)";
    public static final String PASTE_ROW_REPLACE = "Paste row (replace)";
    public static final String PASTE_ROW_APPEND = "Paste row (append)";
    private static final int SETUP_WIDTH = 320;
    private static final int SETUP_HEIGHT = 200;
    public static final Dimension SETUP_WINDOW_SIZE = new Dimension(SETUP_WIDTH, SETUP_HEIGHT);
    private static final int MAIN_WIDTH = 1280;
    private static final int MAIN_HEIGHT = 720;
    public static final Dimension MAIN_WINDOW_SIZE = new Dimension(MAIN_WIDTH, MAIN_HEIGHT);
    public static final Dimension LABEL_PANEL_SIZE = new Dimension(300, MAIN_HEIGHT);
}
