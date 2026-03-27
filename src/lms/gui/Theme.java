package lms.gui;

import javax.swing.*;
import java.awt.*;

public final class Theme {

    private Theme() {}

    public static Font font(int size, int style) {
        return new Font("Dialog", style, size);
    }

    public static JLabel label(String text, int size, int style) {
        JLabel l = new JLabel(text);
        l.setFont(font(size, style));
        return l;
    }

    /** Plain panel with a titled border. */
    public static JPanel card(String title) {
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createTitledBorder(title));
        return p;
    }

    /** Unstyled card (no border). */
    public static JPanel card() {
        return new JPanel();
    }

    public static JTextField styleField(JTextField f) {
        return f;
    }

    public static void styleTextArea(JTextArea ta) {
        ta.setFont(new Font("Monospaced", Font.PLAIN, 13));
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
    }

    public static JButton button(String text) {
        return new JButton(text);
    }
}
