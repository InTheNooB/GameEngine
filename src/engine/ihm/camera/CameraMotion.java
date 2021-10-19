package engine.ihm.camera;

import engine.GameContainer;
import engine.game.GameObject;
import engine.game.physics.Point;
import java.util.concurrent.CopyOnWriteArrayList;

public class CameraMotion {

    private final GameContainer gc;

    private CopyOnWriteArrayList<GameObject> gameObjects;

    // Cam properties (with default values)
    private float marginX = 200;
    private float marginY = 200;
    private float minWidth = 750;
    private float maxWidth = 2000;
    private float minHeigth = 750;
    private float maxHeigth = 2000;
    private float camSpeed = 20; // small number equals high speed
    private final float camErrorMargin = 10;

    public CameraMotion(GameContainer gc) {
        this.gc = gc;
    }

    public void update() {
        if (gameObjects == null || gameObjects.isEmpty()) {
            return;
        }

        float S_W = gc.getSettings().getWidth();
        float S_H = gc.getSettings().getHeight();

        // Calculate scale
        // => w = width of visible map
        // => w_s = width of the full screen
        // => w_o = width of an object
        // -----
        // => scale * w_o = scaled | scale = scaled / w_o
        // Règle de 3 entre le changement de taille de w par rapport à w_s pour trouver le scaled objet (w_o)
        // avec ce scaled object on peut déduire le scale à appliquer
        // Le scale calculé le plus petit entre X et Y est appliqué
        float wX = 2 * marginX + maxX_W() - minX();
        wX = wX < minWidth ? minWidth : wX;
        wX = wX > maxWidth ? maxWidth : wX;
        float w_oX = 10;
        float scaledX = (w_oX / wX) * S_W;
        float scaleX = scaledX / w_oX;
        float wY = 2 * marginY + maxY_H() - minY();
        wY = wY < minHeigth ? minHeigth : wY;
        wY = wY > maxHeigth ? maxHeigth : wY;
        float w_oY = 10;
        float scaledY = (w_oY / wY) * S_H;
        float scaleY = scaledY / w_oY;

        gc.getCam().setScale(scaleX < scaleY ? scaleX : scaleY);

        // Calculate offset
        // Le zoom du scale s'applique au centre de l'écran, donc on place l'offset pour avoir le centre des joueurs au millieu de l'écran
        float avgX = (maxX_W2() + minX_W2()) / 2;
        float avgY = (maxY_H2() + minY_H2()) / 2;
        Point p = new Point();
        p.x = -S_W / 2 - avgX + S_W / 2;
        p.y = -S_H / 2 - avgY + S_H / 2;

        Point actual = new Point();
        actual.x = gc.getCam().getOffSet().x;
        actual.y = gc.getCam().getOffSet().y;

        if (actual.x < p.x - camErrorMargin) {
            actual.x += (p.x - actual.x) / camSpeed;
        } else if (actual.x > p.x + camErrorMargin) {
            actual.x -= (actual.x - p.x) / camSpeed;
        }

        if (actual.y < p.y - camErrorMargin) {
            actual.y += (p.y - actual.y) / camSpeed;
        } else if (actual.y > p.y + camErrorMargin) {
            actual.y -= (actual.y - p.y) / camSpeed;
        }

        gc.getCam().setOffSet(actual);
    }

    /**
     * finds the max (X + Width) out of every gameObjects
     */
    private float maxX_W() {
        float max = Integer.MIN_VALUE;
        for (GameObject p : gameObjects) {
            if (p.getX() + p.getWidth() > max) {
                max = p.getX() + p.getWidth();
            }
        }
        return max;
    }

    /**
     * finds the max (X + Width / 2) out of every gameObjects
     */
    private float maxX_W2() {
        float max = Integer.MIN_VALUE;
        for (GameObject g : gameObjects) {
            if (g.getX() + g.getWidth() / 2 > max) {
                max = g.getX() + g.getWidth() / 2;
            }
        }
        return max;
    }

    /**
     * finds the max (Y + Height) out of every gameObjects
     */
    private float maxY_H() {
        float max = Integer.MIN_VALUE;
        for (GameObject g : gameObjects) {
            if (g.getY() + g.getHeight() > max) {
                max = g.getY() + g.getHeight();
            }
        }
        return max;
    }

    /**
     * finds the max (Y + Height / 2) out of every gameObjects
     */
    private float maxY_H2() {
        float max = Integer.MIN_VALUE;
        for (GameObject g : gameObjects) {
            if (g.getY() + g.getHeight() / 2 > max) {
                max = g.getY() + g.getHeight() / 2;
            }
        }
        return max;
    }

    /**
     * finds the min (X) out of every gameObjects
     */
    private float minX() {
        float min = Integer.MAX_VALUE;
        for (GameObject g : gameObjects) {
            if (g.getX() < min) {
                min = g.getX();
            }
        }
        return min;
    }

    /**
     * finds the min (X + Width / 2) out of every gameObjects
     */
    private float minX_W2() {
        float min = Integer.MAX_VALUE;
        for (GameObject g : gameObjects) {
            if (g.getX() + g.getWidth() / 2 < min) {
                min = g.getX() + g.getWidth() / 2;
            }
        }
        return min;
    }

    /**
     * finds the min (Y + Height/ 2) out of every gameObjects
     */
    private float minY_H2() {
        float min = Integer.MAX_VALUE;
        for (GameObject g : gameObjects) {
            if (g.getY() + g.getHeight() / 2 < min) {
                min = g.getY() + g.getHeight() / 2;
            }
        }
        return min;
    }

    /**
     * finds the min (Y) out of every gameObjects
     */
    private float minY() {
        float min = Integer.MAX_VALUE;
        for (GameObject g : gameObjects) {
            if (g.getY() < min) {
                min = g.getY();
            }
        }
        return min;
    }

    public CopyOnWriteArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void setGameObjects(CopyOnWriteArrayList<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }

    public float getMarginX() {
        return marginX;
    }

    public void setMarginX(float marginX) {
        this.marginX = marginX;
    }

    public float getMarginY() {
        return marginY;
    }

    public void setMarginY(float marginY) {
        this.marginY = marginY;
    }

    public float getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(float minWidth) {
        this.minWidth = minWidth;
    }

    public float getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(float maxWidth) {
        this.maxWidth = maxWidth;
    }

    public float getMinHeigth() {
        return minHeigth;
    }

    public void setMinHeigth(float minHeigth) {
        this.minHeigth = minHeigth;
    }

    public float getMaxHeigth() {
        return maxHeigth;
    }

    public void setMaxHeigth(float maxHeigth) {
        this.maxHeigth = maxHeigth;
    }

    public float getCamSpeed() {
        return camSpeed;
    }

    public void setCamSpeed(float camSpeed) {
        this.camSpeed = camSpeed;
    }

}
