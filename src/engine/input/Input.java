package engine.input;

import engine.GameContainer;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private final GameContainer gc;

    private final int NUM_KEYS = 256;
    private boolean[] keys = new boolean[NUM_KEYS];
    private boolean[] keysLast = new boolean[NUM_KEYS];
    private boolean isAnyKeyPressed;

    private final int NUM_BUTTONS = 5;
    private boolean[] buttons = new boolean[NUM_BUTTONS];
    private boolean[] buttonsLast = new boolean[NUM_BUTTONS];

    private int mouseX, mouseY;
    private int scroll;

    public Input(GameContainer gc) {
        this.gc = gc;
        mouseX = 0;
        mouseY = 0;
        scroll = 0;

        gc.getWindow().getFrame().addKeyListener(this);
        gc.getWindow().getFrame().getContentPane().addMouseListener(this);
        gc.getWindow().getFrame().getContentPane().addMouseMotionListener(this);
        gc.getWindow().getFrame().getContentPane().addMouseWheelListener(this);
    }

    public void updateListeners() {
        gc.getWindow().getFrame().getContentPane().addMouseListener(this);
        gc.getWindow().getFrame().getContentPane().addMouseMotionListener(this);
        gc.getWindow().getFrame().getContentPane().addMouseWheelListener(this);
    }

    public Input() {
        gc = null;
        mouseX = 0;
        mouseY = 0;
        scroll = 0;
    }

    public void update() {
        scroll = 0;

        for (int i = 0; i < NUM_KEYS; i++) {
            keysLast[i] = keys[i];
        }
        for (int i = 0; i < NUM_BUTTONS; i++) {
            buttonsLast[i] = buttons[i];
        }
    }

    public boolean isAnyKeyDown() {
        return isAnyKeyPressed;
    }

    /**
     * Check if a certain key is pressed down (Use KeyEvent.VK_[] to get
     * letter).
     *
     */
    public boolean isKey(int keyCode) {
        return keys[keyCode];
    }

    /**
     * Has just been released (Last frame UP current frame DOWN).
     *
     */
    public boolean isKeyUp(int keyCode) {
        return !keys[keyCode] && keysLast[keyCode];
    }

    /**
     * Has just been pressed (Last frame DOWN current frame UP).
     *
     */
    public boolean isKeyDown(int keyCode) {
        return keys[keyCode] && !keysLast[keyCode];
    }

    /**
     * Check if a certain button is pressed down.
     *
     */
    public boolean isButton(int button) {
        return buttons[button];
    }

    /**
     * Has just been released (Last frame UP current frame DOWN).
     *
     */
    public boolean isButtonUp(int button) {
        return !buttons[button] && buttonsLast[button];
    }

    /**
     * Has just been pressed (Last frame DOWN current frame UP).
     *
     */
    public boolean isButtonDown(int button) {
        return buttons[button] && !buttonsLast[button];
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() < NUM_KEYS) {
            keys[e.getKeyCode()] = true;
            isAnyKeyPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() < NUM_KEYS) {
            keys[e.getKeyCode()] = false;
            isAnyKeyPressed = false;
        }
    }

    public void keyPressed(int keyCode) {
        if (keyCode < NUM_KEYS) {
            keys[keyCode] = true;
        }
    }

    public void keyReleased(int keyCode) {
        if (keyCode < NUM_KEYS) {
            keys[keyCode] = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() < NUM_BUTTONS) {
            buttons[e.getButton()] = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() < NUM_BUTTONS) {
            buttons[e.getButton()] = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = (int) e.getX();
        mouseY = (int) e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = (int) e.getX();
        mouseY = (int) e.getY();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        scroll = e.getWheelRotation();
    }

    public int getMouseX() {
        return (int) (mouseX);
    }

    public int getMouseY() {
        return (int) (mouseY);
    }

    public int getScroll() {
        return scroll;
    }

}
