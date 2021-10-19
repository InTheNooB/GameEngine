package engine.game;

import engine.GameContainer;
import engine.game.animation.AnimationSet;
import engine.game.physics.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;

public abstract class GameObject implements Serializable {

    //Position
    protected float x, y;
    protected float width, height;
    protected boolean onTheGround;
    protected Orientation orientation;

    //Movement
    protected float velX, velY;
    protected float velXCap = 20;
    protected float velYUpCap = 20;
    protected float velYDownCap = 20;

    protected float groundDrag = 0.75f;
    protected float airDrag = 1.5f;

    //Identifiers
    protected String identifier;
    protected int clientId; // Used to create a relation between a client and his objects (player for exemple)
    protected int netId; //Used to differentiate the object on the network side

    //Appearance
    protected AnimationSet animation;
    protected int idleAnimation;
    protected int moveAnimation;
    protected GameObjectSprite sprite;

    //Others
    protected boolean held; // Used to move the object with the mouse
    protected boolean updated, rendered, selectable;
    protected boolean alive;

    public GameObject() {
        updated = true;
        rendered = true;
        selectable = true;
        alive = true;
        onTheGround = false;
        orientation = Orientation.RIGHT;
        velX = 0;
        velY = 0;
    }

    public GameObject(float x, float y, float width, float height) {
        this();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }

    public GameObject(float x, float y) {
        this();
        this.x = x;
        this.y = y;
        this.width = 0;
        this.height = 0;
    }

    public abstract void setup(GameContainer gc);

    public abstract void setupAnimation(GameContainer gc);

    public abstract void update(GameContainer gc, float dt);

    public abstract void render(GameContainer gc, Graphics2D g);

    public void updateAnimation(GameContainer gc) {
        if (animation != null) {
            animation.updateAnimation();
        }
    }

    /**
     * Gets the positions to draw on the screen considering the scale and
     * camera.
     */
    public Point getRealPos(GameContainer gc) {
        Point realPos = new Point();
        realPos.x = (int) ((x + gc.getCam().getOffSet().x) * gc.getCam().getScale() + gc.getSettings().getWidth() / 2);
        realPos.y = (int) ((y + gc.getCam().getOffSet().y) * gc.getCam().getScale() + gc.getSettings().getHeight() / 2);
        return realPos;
    }

    /**
     * Gets the positions to draw on the screen considering the scale and
     * camera.
     */
    public static Point getRealPos(GameContainer gc, float x, float y) {
        Point realPos = new Point();
        realPos.x = (int) ((x + gc.getCam().getOffSet().x) * gc.getCam().getScale() + gc.getSettings().getWidth() / 2);
        realPos.y = (int) ((y + gc.getCam().getOffSet().y) * gc.getCam().getScale() + gc.getSettings().getHeight() / 2);
        return realPos;
    }

    /**
     * Gets point from RealPos to "game" pos (depending on scale and offsets)
     */
    public static Point getGamePos(GameContainer gc, float x, float y) {
        Point realPos = new Point();
        realPos.x = (int) ((x - gc.getSettings().getWidth() / 2) / gc.getCam().getScale() - gc.getCam().getOffSet().x);
        realPos.y = (int) ((y - gc.getSettings().getHeight() / 2) / gc.getCam().getScale() - gc.getCam().getOffSet().y);
//        realPos.x = (int) ((x - gc.getCam().getOffSet().x) / gc.getCam().getScale() - gc.getSettings().getWidth() / 2);
//        realPos.y = (int) ((y - gc.getCam().getOffSet().y) / gc.getCam().getScale() - gc.getSettings().getHeight() / 2);
        return realPos;
    }

    /**
     * Gets the width to draw on the screen considering the scale and camera.
     */
    public float getRealWidth(GameContainer gc) {
        return width * gc.getCam().getScale();
    }

    /**
     * Gets the height to draw on the screen considering the scale and camera.
     */
    public float getRealHeight(GameContainer gc) {
        return height * gc.getCam().getScale();
    }

    /**
     * Gets the width to draw on the screen considering the scale and camera.
     */
    public static float getRealWidth(GameContainer gc, float width) {
        return width * gc.getCam().getScale();
    }

    /**
     * Gets the height to draw on the screen considering the scale and camera.
     */
    public static float getRealHeight(GameContainer gc, float height) {
        return height * gc.getCam().getScale();
    }

    /**
     * Draws the sprite with the correct orientation
     *
     * @param gc
     * @param g
     */
    public void drawSprite(GameContainer gc, Graphics g) {
        if (orientation == Orientation.LEFT) {
            g.drawImage(sprite.getImage(), (int) getRealPos(gc).x, (int) getRealPos(gc).y, (int) (width * gc.getCam().getScale()), (int) (height * gc.getCam().getScale()), null);
        } else {
            g.drawImage(sprite.getImage(), (int) getRealPos(gc, x + width, y).x, (int) getRealPos(gc).y, (int) (-width * gc.getCam().getScale()), (int) (height * gc.getCam().getScale()), null);
        }
    }

    /**
     * Draws the sprite with the correct orientation constrained in the WIDTH
     * and HEIGHT
     *
     * @param gc
     * @param g
     */
    public void drawConstrainedAnimation(GameContainer gc, Graphics g) {
        if (animation == null) {
            gc.getEventHistory().addEvent("Couldn't draw animation because it is null");
            return;
        }

        //Restreint dans la taille du GameObject
        if (orientation == Orientation.LEFT) {
            g.drawImage(animation.getSpriteToDraw(), (int) getRealPos(gc).x, (int) getRealPos(gc).y, (int) (width * gc.getCam().getScale()), (int) (height * gc.getCam().getScale()), null);
        } else {
            g.drawImage(animation.getSpriteToDraw(), (int) getRealPos(gc, x + width, y).x, (int) getRealPos(gc).y, (int) (-width * gc.getCam().getScale()), (int) (height * gc.getCam().getScale()), null);
        }
    }

    /**
     * Draws the sprite with the correct orientation
     *
     * @param gc
     * @param g
     */
    public void drawAnimation(GameContainer gc, Graphics g) {
        if (animation == null) {
            gc.getEventHistory().addEvent("Couldn't draw animation because it is null");
            return;
        }

        float imW = animation.getSpriteToDraw().getWidth(null);
        float imH = animation.getSpriteToDraw().getHeight(null);
        if (orientation == Orientation.LEFT) {
            g.drawImage(animation.getSpriteToDraw(), (int) (getRealPos(gc).x + (getRealWidth(gc) - getRealWidth(gc, imW))), (int) (getRealPos(gc).y + (getRealHeight(gc) - getRealHeight(gc, imH))), (int) (getRealWidth(gc, imW)), (int) (getRealHeight(gc, imH)), null);
        } else {
            g.drawImage(animation.getSpriteToDraw(), (int) getRealPos(gc, x + imW, y).x, (int) (getRealPos(gc).y + (getRealHeight(gc) - getRealHeight(gc, imH))), (int) (-getRealWidth(gc, imW)), (int) (getRealHeight(gc, imH)), null);
        }
    }

    /**
     * Changes the velocities and positions depending on the constants. Drag is
     * added, velocities are clamped and added to the cordonates
     */
    public void move() {

        if (velX > velXCap) {
            velX = velXCap;
        } else if (velX < -velXCap) {
            velX = -velXCap;
        }

        if (velY > velYDownCap) {
            velY = velYDownCap;
        } else if (velY < -velYUpCap) {
            velY = -velYUpCap;
        }

        x += velX;
        y += velY;
    }

    public void addGroundDrag() {
        if (onTheGround) {
            if (velX > groundDrag) {
                velX -= groundDrag;
            } else if (velX < -groundDrag) {
                velX += groundDrag;
            } else {
                velX = 0;
            }

        }
    }

    public void addAirDrag() {
        if (!onTheGround) {
            if (velX > airDrag) {
                velX -= airDrag;
            } else if (velX < -airDrag) {
                velX += airDrag;
            } else {
                velX = 0;
            }
        }
    }

    public void clampVel() {
        if (velX > velXCap) {
            velX = velXCap;
        } else if (velX < -velXCap) {
            velX = -velXCap;
        }

        if (velY > velYDownCap) {
            velY = velYDownCap;
        } else if (velY < -velYUpCap) {
            velY = -velYUpCap;
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

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

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public float getVelX() {
        return velX;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public float getVelY() {
        return velY;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

    public boolean isOnTheGround() {
        return onTheGround;
    }

    public void setOnTheGround(boolean onTheGround) {
        this.onTheGround = onTheGround;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public AnimationSet getAnimation() {
        return animation;
    }

    public void setAnimation(AnimationSet animation) {
        this.animation = animation;
    }

    public int getIdleAnimation() {
        return idleAnimation;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getNetId() {
        return netId;
    }

    public void setNetId(int netId) {
        this.netId = netId;
    }

    public GameObjectSprite getSprite() {
        return sprite;
    }

    public void setSprite(GameObjectSprite sprite) {
        this.sprite = sprite;
    }
}