package engine.ihm;

import engine.GameContainer;
import engine.ihm.cursor.CursorManager;
import engine.ihm.game.Button;
import engine.ihm.game.Renderer;
import engine.ihm.menu.Menu;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window {

    private JFrame frame;
    private final JPanel cards;
    private final Renderer mainPanel;

    private final HashMap<String, Menu> panels;

    private final CopyOnWriteArrayList<Button> buttons;
    private final CursorManager cursorManager;

    public Window(GameContainer gc) {

        // Main Panel
        mainPanel = new Renderer(gc);
        mainPanel.setDoubleBuffered(true);
        mainPanel.setOpaque(true);

        // CardLayout (Permit to have multiple panel and switch from one to an other)
        cards = new JPanel(new CardLayout());
        cards.add(mainPanel, "main");
        cards.setVisible(true);
        panels = new HashMap();

        // Frame
        frame = new JFrame(gc.getSettings().getTitle());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                gc.endProgram();
            }
        });
        frame.setLayout(new BorderLayout());
        frame.setSize(gc.getSettings().getWidth(), gc.getSettings().getHeight());
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setContentPane(cards);
        frame.setIgnoreRepaint(true);

        // Buttons
        buttons = new CopyOnWriteArrayList();

        // Cursor Manager
        cursorManager = new CursorManager(gc);
    }

    public void update(GameContainer gc) {
        frame.setTitle(gc.getSettings().getTitle());
        cursorManager.update();
        for (Button button : buttons) {
            if (button != null) {
                button.update(gc);
            }
        }
    }

    /**
     * Adds a panel and assigns a name to it (Similar to HashMap).
     *
     * @param name
     * @param p
     */
    public void addPanel(String name, CustomPanel p) {
        if ((name != null) && (!name.isEmpty())) {
            if (p != null) {
                cards.add(p, name);
            }
        }
    }

    public void addPanel(String name, Menu p) {
        if ((name != null) && (!name.isEmpty())) {
            if (p != null) {
                cards.add(p, name);
                panels.put(name, p);
            }
        }
    }

    /**
     * Switchs from one panel to an other. Main panel is called "main".
     *
     * @param name
     */
    public void switchPanel(String name) {
        if ((name != null) && (!name.isEmpty())) {
            CardLayout cl = (CardLayout) (cards.getLayout());
            cl.show(cards, name);
            buttons.clear();
            if (panels.containsKey(name)) {
                if (panels.get(name) != null) {
                    buttons.addAll(panels.get(name).getButtons());
                }
            }
        }
    }

    public void render() {
        cards.repaint();
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public Renderer getRenderer() {
        return mainPanel;
    }

    public float getBorderWidth(GameContainer gc) {
        return gc.getSettings().getWidth() - frame.getContentPane().getSize().width;
    }

    public float getBorderHeight(GameContainer gc) {
        return gc.getSettings().getHeight() - frame.getContentPane().getSize().height;
    }

    public void addButton(Button b) {
        buttons.add(b);
    }

    public CopyOnWriteArrayList<Button> getButtons() {
        return buttons;
    }

    public CursorManager getCursorManager() {
        return cursorManager;
    }

}
