package engine.ihm.cursor;

import engine.GameContainer;
import java.awt.Cursor;

/**
 * The current cursor is defined by the weight of the setCursor
 *
 * @author lione
 */
public class CursorManager {

    private GameContainer gc;
    private Cursor cursor;
    private int weight;
    private boolean modified;

    public CursorManager(GameContainer gc) {
        this.gc = gc;
    }

    public void update() {
        if (!modified) {
            cursor = Cursor.getDefaultCursor();
        }
        gc.getWindow().getFrame().setCursor(cursor);
        weight = 1000;
        modified = false;
    }

    public boolean setCursor(int cursor, CursorOrder weight) {
        modified = true;
        if (weight.getWeight() < this.weight) {
            this.weight = weight.getWeight();
            this.cursor = Cursor.getPredefinedCursor(cursor);
            gc.getWindow().getFrame().setCursor(cursor);
            return true;
        } else {
            return false;
        }
    }

}
