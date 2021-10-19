package engine.ihm;

import engine.ihm.menu.Menu;
import java.awt.Graphics;
import javax.swing.JPanel;

public abstract class CustomPanel extends JPanel {

    public CustomPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public CustomPanel(Menu menu, boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    @Override
    protected abstract void paintComponent(Graphics g);

}
