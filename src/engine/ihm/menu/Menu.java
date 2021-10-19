package engine.ihm.menu;

import engine.GameContainer;
import static engine.consts.FileConstants.FOLDER_IMAGES;
import engine.ihm.game.Button;
import engine.ihm.game.ButtonEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Menu extends JPanel {

    private final GameContainer gc;

    private String title;
    private Font titleFont;
    private Color titleColor;
    private Font buttonFont;
    private Color buttonColor;
    private int buttonSpacing;

    private final CopyOnWriteArrayList<Button> buttons;
    private Image backgroundImage;

    public Menu(GameContainer gc) {
        super(true);
        this.gc = gc;
        buttons = new CopyOnWriteArrayList();
        buttonSpacing = 1;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (gc.getEventHistory() == null || gc.getGame() == null) {
            return;
        }
        int w = (int) (gc.getSettings().getWidth() - gc.getWindow().getBorderWidth(gc));
        int h = (int) (gc.getSettings().getHeight() - gc.getWindow().getBorderWidth(gc));
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        clear(g2, w, h);

        // Renders title
        try {
            if (title != null) {
                // Set font title
                g2.setFont(titleFont);
                g2.setColor(titleColor);
                // Get width of text to center it
                int titleW = g.getFontMetrics().stringWidth(title);
                // Draw text
                g2.drawString(title, w / 2 - titleW / 2, h / 5);
                // Reset default font
                g2.setFont(new JLabel().getFont());
            }
        } catch (NullPointerException e) {
            gc.getEventHistory().addEvent("NullPointer : Rendering Game");
        }

        //Render Buttons
        try {
            for (Button b : gc.getWindow().getButtons()) {
                b.render(gc, g2);
            }
        } catch (Exception e) {
            gc.getEventHistory().addEvent("Exception : Rendering Button");
        }
    }

    private void clear(Graphics2D g, int w, int h) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, w, h, null);
        }
    }

    public void addButton(String txt, Color backgroundColor, ButtonEvent event) {
        int buttonWidth = gc.getSettings().getWidth() / 5;
        int buttonHeight = gc.getSettings().getHeight() / 20;
        int x = gc.getSettings().getWidth() / 2 - buttonWidth / 2;
        int y = 0;
        if (buttons.isEmpty()) {
            y = (int) (gc.getSettings().getHeight() * 0.5);
        } else {
            y = (int) (buttons.get(buttons.size() - 1).getPosition().y + buttonHeight) + buttonSpacing;
        }
        Button button = new Button(x, y, txt);
        button.setVisible(true);
        button.addListener(event);
        button.setWidth(buttonWidth);
        button.setHeight(buttonHeight);
        button.setFont(buttonFont);
        button.setForegroundColor(buttonColor);
        button.setBackgroundColor(backgroundColor);
        buttons.add(button);
    }

    public CopyOnWriteArrayList<Button> getButtons() {
        return buttons;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitleFont(Font titleFont, Color titleColor) {
        this.titleFont = titleFont;
        this.titleColor = titleColor;
    }

    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void setBackgroundImage(String filePath) {
        // Add path if not already present
        filePath = filePath.startsWith(FOLDER_IMAGES) ? filePath : FOLDER_IMAGES + filePath;
        try {
            backgroundImage = ImageIO.read(new File(filePath));
        } catch (IOException ex) {
            gc.getEventHistory().addEvent("Error Loading background image \"" + FOLDER_IMAGES + filePath + "\" to use as background of menu");
        }
    }

    public void setButtonFont(Font buttonFont, Color buttonColor) {
        this.buttonFont = buttonFont;
        this.buttonColor = buttonColor;
    }

    public void setButtonSpacing(int buttonSpacing) {
        if (buttonSpacing <= 0) {
            return;
        }
        this.buttonSpacing = buttonSpacing;
    }

}
