package engine.game.map;

import engine.GameContainer;
import engine.game.GameObject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import static engine.game.GameObject.getRealPos;
import static engine.game.GameObject.getRealWidth;
import engine.game.Orientation;
import engine.game.animation.AnimationSet;
import java.awt.Graphics;

public class Block extends GameObject implements Serializable {

    // In game attributs
    private boolean transparent;
    private boolean platform;
    private boolean stretchSprite;

    public Block(int x, int y) {
        selectable = false;
        rendered = false;
        updated = false;
    }

    public Point calculateCentroid() {
        return new Point((int) (x + width / 2), (int) (y + height / 2));
    }

    public Rectangle getOnScreenShape(GameContainer gc) {
        int x_ = (int) getRealPos(gc, x, 0).x;
        int y_ = (int) getRealPos(gc, 0, y).y;
        int w_ = (int) getRealWidth(gc, width);
        int h_ = (int) getRealWidth(gc, height);

        return new Rectangle(x_, y_, w_, h_);
    }

    @Override
    public void setup(GameContainer gc) {
    }

    @Override
    public void setupAnimation(GameContainer gc) {
    }

    @Override
    public void update(GameContainer gc, float dt) {
        updateAnimation(gc);
    }

    public void setAnimation(GameContainer gc, String name) {
        animation = new AnimationSet();
        sprite = null;
        idleAnimation = animation.importAnimation(name);
        animation.setCurrentCategorie(idleAnimation);
    }

    @Override
    public void render(GameContainer gc, Graphics2D g) {

        // Shape
        if (sprite != null) {
//            drawSprite(gc, g);
            if (stretchSprite) {
                // Draw stretched sprite
                g.drawImage(sprite.getImage(), (int) getRealPos(gc).x, (int) getRealPos(gc).y, (int) (width * gc.getCam().getScale()), (int) (height * gc.getCam().getScale()), null);
            } else {
                // Repeat sprite pattern
                int maxX = (int) (width / sprite.getW()) + 1;
                int maxY = (int) (height / sprite.getH()) + 1;
                int defaultW = (int) sprite.getW();
                int defaultH = (int) sprite.getH();
                for (int i = 0; i < maxX; i++) {
                    for (int j = 0; j < maxY; j++) {
                        int w = defaultW;
                        int h = defaultH;

                        if (defaultW * (i + 1) > width) {
                            w = (int) (width - defaultW * i);
                        }

                        if (defaultH * (j + 1) > height) {
                            h = (int) (height - defaultH * j);
                        }
                        g.drawImage(sprite.getImage(),
                                (int) getRealPos(gc, x + defaultW * i, y + defaultH * j).x, //dx1
                                (int) getRealPos(gc, x + defaultW * i, y + defaultH * j).y, //dy1 
                                (int) getRealPos(gc, x + defaultW * i + w, y + defaultH * j + h).x, //dx2
                                (int) getRealPos(gc, x + defaultW * i + w, y + defaultH * j + h).y, //dy2
                                0, //sx1
                                0, //sy1
                                w, //sx2
                                h, //
                                null);
                    }
                }
            }
        } else if (animation != null) {
            // Draw animation
            drawAnimation(gc, g);
        } else {
            // default look
            g.setColor(Color.black);
            g.fill(getOnScreenShape(gc));
        }

        // Selected part
        int xCenter = (int) getRealPos(gc, calculateCentroid().x, 0).x;
        int yCenter = (int) getRealPos(gc, 0, calculateCentroid().y).y;

    }

    @Override
    public void drawAnimation(GameContainer gc, Graphics g) {
        if (animation == null) {
            gc.getEventHistory().addEvent("Couldn't draw animation because it is null");
            return;
        }

        //Restreint dans la taille du GameObject
        if (orientation == Orientation.RIGHT) {
            g.drawImage(animation.getSpriteToDraw(), (int) getRealPos(gc).x, (int) getRealPos(gc).y, (int) (width * gc.getCam().getScale()), (int) (height * gc.getCam().getScale()), null);
        } else {
            g.drawImage(animation.getSpriteToDraw(), (int) getRealPos(gc, x + width, y).x, (int) getRealPos(gc).y, (int) (-width * gc.getCam().getScale()), (int) (height * gc.getCam().getScale()), null);
        }

    }

    public boolean isTransparent() {
        return transparent;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    public boolean isPlatform() {
        return platform;
    }

    public void setPlatform(boolean platform) {
        this.platform = platform;
    }

    public boolean isStretchSprite() {
        return stretchSprite;
    }

    public void setStretchSprite(boolean stretchSprite) {
        this.stretchSprite = stretchSprite;
    }

}
