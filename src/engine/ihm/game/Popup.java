package engine.ihm.game;

import javax.swing.JOptionPane;

public class Popup {

    public static void error(String error) {
        JOptionPane.showMessageDialog(null, error, "Error", 0);
    }

    public static void warning(String warning) {
        JOptionPane.showMessageDialog(null, warning, "Warning", 2);
    }

    public static String askData(String txt) {
        return (String) JOptionPane.showInputDialog(txt);
    }
}
