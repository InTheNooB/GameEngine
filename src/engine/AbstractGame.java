package engine;

import engine.consts.FileConstants;
import java.awt.Graphics2D;

public abstract class AbstractGame implements FileConstants {

    protected boolean updated = true;
    protected boolean rendered = true;

    public abstract void setup(GameContainer gc);

    public abstract void update(GameContainer gc, float dt);

    public abstract void render(GameContainer gc, Graphics2D g);

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

}
