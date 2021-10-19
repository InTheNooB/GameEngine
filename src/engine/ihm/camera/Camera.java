package engine.ihm.camera;

import engine.GameContainer;
import engine.game.GameObject;
import engine.game.physics.Point;

public class Camera {

    private Point offSet;
    private Point lastOffSet;
    private Point diff;
    private float scale;
    private Point mousePressed;

    private float scaleMod;
    private float mouseZoomAttraction;
    private boolean dragEnabled;
    private boolean zoomEnabled;

    private GameObject xFocus;
    private GameObject yFocus;

    private float[][] camLimits; // {[x][y]},{[x][y]} | {min}, {max} | 0 --> no limits

    private float[] zoomLimits; // [Min][Max] | [0][0] --> no limit

    public Camera(GameContainer gc) {
        scaleMod = 1.1f;
        mouseZoomAttraction = 10;
        zoomEnabled = true;
        dragEnabled = true;
        scale = 1f;
        offSet = new Point(-gc.getSettings().getWidth() / 2, -gc.getSettings().getHeight() / 2);
        lastOffSet = new Point(-gc.getSettings().getWidth() / 2, -gc.getSettings().getHeight() / 2);
        mousePressed = new Point();
        diff = new Point();
        zoomLimits = new float[]{0f, 0f};
        camLimits = new float[][]{
            {0, 0},
            {0, 0}
        };
    }

    public void resetCamPos(GameContainer gc) {
        offSet = new Point(-gc.getSettings().getWidth() / 2, -gc.getSettings().getHeight() / 2);
        lastOffSet = new Point(-gc.getSettings().getWidth() / 2, -gc.getSettings().getHeight() / 2);

    }

    public void resetCamScale(GameContainer gc) {
        scale = 1f;
    }

    public void updateScreenSize(GameContainer gc) {
        offSet = new Point(-gc.getSettings().getWidth() / 2, -gc.getSettings().getHeight() / 2);
        lastOffSet = new Point(-gc.getSettings().getWidth() / 2, -gc.getSettings().getHeight() / 2);
    }

    /**
     * Handles camera movement + zoom/unzoom + selectables gameobjects.
     *
     * @param gc
     */
    public void update(GameContainer gc) {

        float mX = gc.getInput().getMouseX();
        float mY = gc.getInput().getMouseY();

        // Scale
        if (zoomEnabled) {
            if (gc.getInput().getScroll() == 1) {
                // Zoom out (scale min)
                if ((zoomLimits[0] == 0) || (scale > zoomLimits[0])) {
                    scale /= scaleMod;
                }
            } else if (gc.getInput().getScroll() == -1) {
                // Zoom in (scale max)
                if ((zoomLimits[1] == 0) || (scale < zoomLimits[1])) {
                    if ((xFocus == null) && (yFocus == null) && (dragEnabled)) { // Can zoom on mouse place
                        scale *= scaleMod;
                        diff.x = (mX - gc.getSettings().getWidth() / 2) / scale / mouseZoomAttraction;
                        offSet.x -= diff.x;
                        lastOffSet.x = offSet.x;

                        diff.y = (mY - gc.getSettings().getHeight() / 2) / scale / mouseZoomAttraction;
                        offSet.y -= diff.y;
                        lastOffSet.y = offSet.y;
                    } else { //Can only zoom on the center
                        scale *= scaleMod;
                    }
                }
            }
        }

        // Cam Movement
        boolean minXReached = true;
        boolean maxXReached = true;
        boolean minYReached = true;
        boolean maxYReached = true;
        if ((xFocus != null) || (yFocus != null)) {
            if (xFocus != null) {
                // Check if left cam (min) X limit is reached
                // OR there is no limits (==0)
                float posMin = (xFocus.getX() - gc.getSettings().getWidth() / 2);
                if ((camLimits[0][0] == 0) || (camLimits[0][0] < posMin)) {
                    minXReached = false;
                }
                // Check if right cam (max) X limit is reached
                // OR there is no limits (==0)
                float posMax = xFocus.getX() + gc.getSettings().getWidth() / 2;
                if ((camLimits[1][0] == 0) || (camLimits[1][0] > posMax)) {
                    maxXReached = false;
                }

                if ((!maxXReached) && (!minXReached)) {
                    offSet.x = -xFocus.getX() - xFocus.getWidth() / 2;
                }
            }

            if (yFocus != null) {
                // Check if UP cam (min) Y limit is reached
                // OR there is no limits (==0)
                float posMin = yFocus.getY() - gc.getSettings().getHeight() / 2;
                if ((camLimits[0][1] == 0) || (camLimits[0][1] < posMin)) {
                    minYReached = false;
                }
                // Check if DOWN cam (max) Y limit is reached
                // OR there is no limits (==0)
                float posMax = yFocus.getY() + gc.getSettings().getHeight() / 2;
                if ((camLimits[1][1] == 0) || (camLimits[1][1] > posMax)) {
                    maxYReached = false;
                }

                if ((!maxYReached) && (!minYReached)) {
                    offSet.y = -yFocus.getY() - yFocus.getHeight() / 2;
                }
            }

        } else if (dragEnabled) {
            if (gc.getInput().isButtonDown(3)) {
                mousePressed = new Point(mX, mY);
            } else if (gc.getInput().isButtonUp(3)) {
                lastOffSet.x = offSet.x;
                lastOffSet.y = offSet.y;
            }

            if (gc.getInput().isButton(3)) {
                offSet.x = lastOffSet.x + (mX - mousePressed.x) / scale;
                offSet.y = lastOffSet.y + (mY - mousePressed.y) / scale;
            }
        }

    }

    /**
     * 0 == no limit.
     */
    public void setCamLimits(float minX, float minY, float maxX, float maxY) {
        camLimits = new float[][]{{minX, minY}, {maxX, maxY}};
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return scale;
    }

    public Point getOffSet() {
        return offSet;
    }

    public void setOffSet(Point offSet) {
        this.offSet = offSet;
    }

    public void setFocus(GameObject focus) {
        xFocus = focus;
        yFocus = focus;
    }

    public GameObject getxFocus() {
        return xFocus;
    }

    public void setxFocus(GameObject xFocus) {
        this.xFocus = xFocus;
    }

    public GameObject getyFocus() {
        return yFocus;
    }

    public void setyFocus(GameObject yFocus) {
        this.yFocus = yFocus;
    }

    public float getScaleMod() {
        return scaleMod;
    }

    void setScaleMod(float scaleMod) {
        this.scaleMod = scaleMod;
    }

    public boolean isDragEnabled() {
        return dragEnabled;
    }

    public void setDragEnabled(boolean dragEnabled) {
        this.dragEnabled = dragEnabled;
    }

    public boolean isZoomEnabled() {
        return zoomEnabled;
    }

    public void setZoomEnabled(boolean zoomEnabled) {
        this.zoomEnabled = zoomEnabled;
    }

    public float[] getZoomLimits() {
        return zoomLimits;
    }

    public void setZoomLimits(float min, float max) {
        this.zoomLimits = new float[]{min, max};
    }

    public float[][] getCamLimits() {
        return camLimits;
    }

    public Point getLastOffSet() {
        return lastOffSet;
    }

    public void setLastOffSet(Point lastOffSet) {
        this.lastOffSet = lastOffSet;
    }

}
