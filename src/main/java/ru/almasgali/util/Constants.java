package ru.almasgali.util;

import java.awt.*;

public class Constants {
    public static final String INT_LABEL_PREFIX = "int (4b): ";
    public static final String FLOAT_LABEL_PREFIX = "float (4b): ";
    public static final String LONG_LABEL_PREFIX = "long (8b): ";
    public static final String DOUBLE_LABEL_PREFIX = "double (8b): ";
    public static final String SIGNED = "Signed";
    public static final String UNSIGNED = "Unsigned";
    public static final Font TABLE_FONT = new Font("Roboto", Font.PLAIN, 16);
    public static final Font LABEL_FONT = new Font("Century Gothic", Font.PLAIN, 16);
    public static final String BYTE_LABEL_PREFIX = "byte: ";
    public static final String SETUP_TITLE = "Setup";
    public static final String MAIN_TITLE = "HEX-editor";
    public static final String ROWS_LABEL = "rows:";
    public static final String COLUMNS_LABEL = "columns:";
    private static final int SETUP_WIDTH = 335;
    private static final int SETUP_HEIGHT = 250;
    public static final Dimension SETUP_WINDOW_SIZE = new Dimension(SETUP_WIDTH, SETUP_HEIGHT);
    private static final int MAIN_WIDTH = 1280;
    private static final int MAIN_HEIGHT = 720;
    public static final Dimension MAIN_WINDOW_SIZE = new Dimension(MAIN_WIDTH, MAIN_HEIGHT);
    public static final Dimension LABEL_PANEL_SIZE = new Dimension(310, MAIN_HEIGHT);
}
