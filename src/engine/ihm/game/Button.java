package engine.ihm.game;

import engine.GameContainer;
import engine.game.physics.Physics;
import engine.game.physics.Point;
import engine.ihm.cursor.CursorOrder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.MouseInfo;

public class Button {

    private final int CLICK_DURATION = 90;

    private Point position;

    private int width, height;
    private boolean customSize;

    private String content;
    private Font font;

    private boolean pressed;
    private long pressedTime;

    private boolean visible;

    private ButtonEvent buttonEvent;

    private Color backgroundColor;
    private Color foregroundColor;

    private Color backgroundColorMemory;
    private Color foregroundColorMemory;

    private final int OFFSET_WIDTH = 10;

    public Button() {
        backgroundColor = new Color(150, 150, 150);
        foregroundColor = Color.black;
        backgroundColorMemory = new Color(150, 150, 150);
        foregroundColorMemory = Color.black;
        customSize = false;
        visible = true;
    }

    public Button(int x, int y, String content) {
        this();
        this.content = content;
        position = new Point(x, y);
    }

    public void update(GameContainer gc) {
        if (pressed) {
            if (System.currentTimeMillis() - pressedTime > CLICK_DURATION) {
                pressed = false;
            }
        } else {
            Point mouse = new Point(gc.getInput().getMouseX(), gc.getInput().getMouseY());
            if (Physics.collision(mouse, position.x, position.y, width, height)) {
                // Mouse on it
                if (gc.getInput().isButtonDown(1)) {
                    // Click
                    if (buttonEvent != null) {
                        buttonEvent.onClick(gc);
                    }
                    pressed = true;
                    pressedTime = System.currentTimeMillis();
                } else {
                    // Hover
                    if (buttonEvent != null) {
                        buttonEvent.onHover(gc);
                    }
                    if ((foregroundColor != null) && (backgroundColor != null)) {
                        foregroundColor = foregroundColorMemory.brighter();
                        backgroundColor = backgroundColorMemory.brighter();
                    }
                    gc.getWindow().getCursorManager().setCursor(Cursor.HAND_CURSOR, CursorOrder.BUTTON_HOVER);
                }
            } else {
                // Mouse not on it
                backgroundColor = backgroundColorMemory;
                foregroundColor = foregroundColorMemory;

            }
        }
    }

    public void render(GameContainer gc, Graphics2D g2) {
        if (!visible) {
            return;
        }

        int buttonWidth ;
        int buttonHeight ;
        int contentWidth ;
        int contentHeight ;
        Font defaultFont = g2.getFont();
        g2.setFont(font);
        if (customSize) {
            buttonWidth = width + OFFSET_WIDTH;
            buttonHeight = height;
        } else {
            buttonWidth = g2.getFontMetrics().stringWidth(content) + OFFSET_WIDTH;
            buttonHeight = g2.getFontMetrics().getHeight();

            width = buttonWidth;
            height = buttonHeight;
        }
        contentWidth = g2.getFontMetrics().stringWidth(content);
        contentHeight = g2.getFontMetrics().getHeight();

        g2.setColor(backgroundColor);
        g2.fill3DRect((int) position.x, (int) position.y, buttonWidth, buttonHeight, !pressed);
        g2.setColor(foregroundColor);
        if (customSize) {
            g2.drawString(content, (int) position.x + OFFSET_WIDTH / 2 + buttonWidth / 2 - contentWidth / 2, (int) position.y + buttonHeight / 2 + contentHeight / 4);
        } else {
            g2.drawString(content, (int) position.x + OFFSET_WIDTH / 2, (int) position.y + buttonHeight - buttonHeight / 4);
        }
        g2.setFont(defaultFont);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getWidth() {
        return width;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setWidth(int width) {
        this.width = width;
        customSize = true;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        customSize = true;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ButtonEvent getButtonListener() {
        return buttonEvent;
    }

    public void addListener(ButtonEvent buttonListener) {
        this.buttonEvent = buttonListener;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.backgroundColorMemory = backgroundColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
        this.foregroundColorMemory = foregroundColor;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
