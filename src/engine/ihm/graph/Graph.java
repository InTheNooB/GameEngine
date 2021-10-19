package engine.ihm.graph;

import engine.GameContainer;
import engine.game.GameObject;
import engine.game.physics.Physics;
import engine.game.physics.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Graph {

    private final GameContainer gc;
    private final ConcurrentHashMap<String, Data> data;
    private float realX, realY;
    private float realWidth;
    private float realHeight;
    private float realAxesMargin;

    private boolean showXNotation;
    private boolean showYNotation;
    private boolean showValuesOnMouse;
    private boolean showValues;

    private int yGraduation;
    private float fixedMaxYValue;
    private float fixedMinYValue;
    private boolean camSensitive;
    private int xPointLimit;

    private float lastMaxYPoint;
    private boolean smoothDataMovement;
    private float smoothXOffset, smoothYOffset;

    private float graphXOffset;

    public Graph(float x, float y, float width, float height, GameContainer gc) {
        this.gc = gc;
        this.realX = x;
        this.realY = y;
        this.realWidth = width;
        this.realHeight = height;
        data = new ConcurrentHashMap();
        realAxesMargin = 15;
        fixedMinYValue = 0;
        xPointLimit = 20;
        smoothXOffset = 0;
        smoothYOffset = 0;
        fixedMaxYValue = -1;

        showXNotation = false;
        showYNotation = false;
        showValuesOnMouse = false;
        showValues = false;
        camSensitive = false;
        smoothDataMovement = true;
    }

    public void addData(String dataName, Color color) {
        if ((dataName != null) && (!dataName.isEmpty())) {
            if (color != null) {
                data.put(dataName, new Data(color));
            }
        }
    }

    public void addPoint(String dataName, float value) {
        if ((dataName != null) && (!dataName.isEmpty())) {
            data.get(dataName).addPoint(value);
            if (smoothDataMovement) {
                smoothXOffset = getXAxeSpacing();
            }
        }
    }

    public void setAverage(String dataName, int average) {
        if ((dataName != null) && (!dataName.isEmpty())) {
            data.get(dataName).setAverage(average);
        }
    }

    public void setVisible(String dataName, boolean value) {
        if ((dataName != null) && (!dataName.isEmpty())) {
            data.get(dataName).setVisible(value);
        }
    }

    private float getXAxeSpacing() {
        float width, axesMargin;
        if (camSensitive) {
            float scale = gc.getCam().getScale();
            width = GameObject.getRealWidth(gc, this.realWidth);
            axesMargin = scale * this.realAxesMargin;
        } else {
            width = this.realWidth;
            axesMargin = this.realAxesMargin;
        }
        float returnValue = (width - axesMargin) / (getMaxXPoints() - 1);
        return Float.isInfinite(returnValue) ? 0 : returnValue;
    }

    public void render(Graphics g) {
        if (g == null) {
            return;
        }
        float bX = gc.getWindow().getBorderWidth(gc);
        float bY = gc.getWindow().getBorderHeight(gc);
        float mX = gc.getInput().getMouseX() - bX;
        float mY = gc.getInput().getMouseY() - bY;
        float scale = gc.getCam().getScale();
        float x, y, w, h, axesMargin;
        if (camSensitive) {
            x = GameObject.getRealPos(gc, this.realX, this.realY).x;
            y = GameObject.getRealPos(gc, this.realX, this.realY).y;
            w = GameObject.getRealWidth(gc, this.realWidth);
            h = GameObject.getRealHeight(gc, this.realHeight);
            axesMargin = scale * this.realAxesMargin;
        } else {
            x = this.realX;
            y = this.realY;
            w = this.realWidth;
            h = this.realHeight;
            axesMargin = this.realAxesMargin;
        }

        float actualGraphHeight = h - axesMargin;
        float bottomGraphY = y + actualGraphHeight;

        float xSpacing = (w - axesMargin) / (getMaxXPoints() - 1);
        float maxYValue;
        if (fixedMaxYValue != -1) {
            maxYValue = fixedMaxYValue;
        } else {
            maxYValue = getMaxYValue();
        }

        if (smoothDataMovement && getMaxXPoints() == xPointLimit) {
            if (smoothXOffset > 0) {
                smoothXOffset *= 0.8;
            }
        } else {
            smoothXOffset = 0;
        }

        if (smoothDataMovement && (fixedMaxYValue == -1)) {
            if (smoothYOffset < -1 || smoothYOffset > 1) {
                smoothYOffset *= 0.9;
            } else {
                smoothYOffset = 0;
            }
        } else {
            smoothYOffset = 0;
        }

        if (smoothDataMovement && (fixedMaxYValue == -1) && (maxYValue != lastMaxYPoint)) {
            smoothYOffset = lastMaxYPoint - maxYValue;
            lastMaxYPoint = maxYValue;
        }

        // Draw background
        g.setColor(new Color(43, 43, 43, 255));
        g.fillRect((int) x, (int) y, (int) w, (int) h);

        // Draw graph "frame"
        g.setColor(Color.white);
        g.drawLine((int) (x + axesMargin), (int) y, (int) (x + axesMargin), (int) (y + h));
        g.drawLine((int) x, (int) (bottomGraphY), (int) (x + w), (int) (bottomGraphY));
        List<Point> shownPoints;

        // Draw Y Graduation 
        if (yGraduation > 0) {
            float gapValue;
            if (fixedMaxYValue != -1) {
                gapValue = (fixedMaxYValue - fixedMinYValue) / yGraduation;
            } else {
                gapValue = (maxYValue - fixedMinYValue) / yGraduation;
            }
            g.setColor(Color.orange);
            for (int i = 0; i <= yGraduation; i++) {
                int lX1 = (int) (x + axesMargin * 0.5f);
                int lY = (int) ((bottomGraphY) - (((actualGraphHeight) / (maxYValue - fixedMinYValue)) * (i * gapValue)));
                int lX2 = (int) (x + axesMargin * 1.5f);
                g.drawLine(lX1, lY, lX2, lY);
                g.drawString("" + (Math.floor((i * gapValue + fixedMinYValue) * 100) / 100), lX1, (int) (lY + 15f));
            }

            // Draw "Prime axes"
            if (fixedMinYValue != 0) {
                g.setColor(Color.white.darker());
                int lX1 = (int) (x + axesMargin);
                int lY = (int) ((bottomGraphY) - (((actualGraphHeight) / (maxYValue - fixedMinYValue + smoothYOffset)) * (Math.abs(fixedMinYValue))));
                g.drawLine(lX1, lY, (int) (lX1 + w - axesMargin), lY);
            }
        }

        // For each data set
        for (Data d : data.values()) {
            // Continue if the data set is hidden
            if (!d.isVisible()) {
                continue;
            }
            float pointRadius = scale * d.getPointRadius();

            // Get points that need to be rendered
            shownPoints = d.getShownPoints(xPointLimit, graphXOffset);

            // Loop to draw each line
            for (int i = 0; i < shownPoints.size(); i++) {
                Point p1 = shownPoints.get(i);

                // Draw Value if mouse is hover it
                if ((showValues) || ((showValuesOnMouse) && (Physics.collision((int) (x + xSpacing * i + axesMargin - pointRadius), (int) ((bottomGraphY) - ((actualGraphHeight) / maxYValue * (p1.y + fixedMinYValue)) - pointRadius), (int) pointRadius * 2, (int) pointRadius * 2, mX - pointRadius, mY - pointRadius, pointRadius * 2, pointRadius * 2)))) {
                    if (i + 1 < shownPoints.size()) {
                        if (shownPoints.get(i + 1).y != p1.y) {
                            g.setColor(d.getColor());
                            int sX = (int) (x + xSpacing * i + axesMargin - pointRadius + smoothXOffset);
                            int sY = (int) ((bottomGraphY) - ((actualGraphHeight) / (maxYValue + smoothYOffset) * (p1.y + fixedMinYValue)) - pointRadius);
                            g.drawString((Math.floor(p1.y * 100) / 100) + "", sX, sY);
                        }
                    }
                }

                // Draw the line
                if (i > 0) {
                    //Line from actual to last
                    Point p2 = shownPoints.get(i - 1);
                    g.setColor(d.getColor());
                    int lineX1 = (int) (x + xSpacing * i + axesMargin + smoothXOffset);
                    int lineY1 = (int) ((bottomGraphY) - (actualGraphHeight / (maxYValue - fixedMinYValue + smoothYOffset) * (p1.y - fixedMinYValue)));
                    int lineX2 = (int) (x + xSpacing * (i - 1) + axesMargin + smoothXOffset);
                    int lineY2 = (int) ((bottomGraphY) - (actualGraphHeight / (maxYValue - fixedMinYValue + smoothYOffset) * (p2.y - fixedMinYValue)));
                    if (lineX2 < x + axesMargin) {
                        // The smoothing sometimes places the point outside the graph area
                        lineX2 = (int) (x + axesMargin);
                    }
                    if (lineX1 > w + x) {
                        // The smoothing sometimes places the point outside the graph area
                        lineX1 = (int) (w + x);
                    }
                    g.drawLine(lineX1, lineY1, lineX2, lineY2);
                }

            }

            // Loop to draw each points / X&Y notation
            for (int i = 0; i < shownPoints.size(); i++) {
                Point p1 = shownPoints.get(i);
                //Point
                if (d.isShowPoints()) {
                    g.setColor(Color.black);
                    int pX = (int) (x + xSpacing * i - pointRadius + axesMargin + smoothXOffset);
                    int pY = (int) ((bottomGraphY - pointRadius) - (((actualGraphHeight) / (maxYValue + smoothYOffset)) * (p1.y + fixedMinYValue)));
                    int pR = (int) pointRadius * 2;
                    g.fillOval(pX, pY, pR, pR);
                }
                //Axes numerotation
                //X
                if (showXNotation) {
                    g.setColor(Color.white);
                    int lX1 = (int) (x + xSpacing * i + axesMargin);
                    int lY1 = (int) (bottomGraphY * 1.5f);
                    int lX2 = (int) (x + xSpacing * i + axesMargin);
                    int lY2 = (int) (bottomGraphY * 0.5f);
                    g.drawLine(lX1, lY1, lX2, lY2);
                }

                //Y
                if (showYNotation) {
                    g.setColor(Color.white);
                    int lX1 = (int) (x + axesMargin * 0.5f);
                    int lY1 = (int) ((bottomGraphY) - (((actualGraphHeight) / maxYValue) * (p1.y + fixedMinYValue)));
                    int lX2 = (int) (x + axesMargin * 1.5f);
                    int lY2 = (int) ((bottomGraphY) - (((actualGraphHeight) / maxYValue) * (p1.y + fixedMinYValue)));
                    g.drawLine(lX1, lY1, lX2, lY2);
                }
            }
        }
    }

    private float getMaxYValue() {
        float max = -1;
        for (Data d : data.values()) {
            if (d.isVisible()) {
                for (Point p : d.getShownPoints(xPointLimit, graphXOffset)) {
                    if (p.y > max) {
                        max = p.y;
                    }
                }
            }
        }
        return max;
    }

    private float getMaxXPoints() {
        int max = -1;
        for (Data d : data.values()) {
            if (d.getPoints().size() > max) {
                max = d.getPoints().size();
            }
        }
        return max < xPointLimit ? max : xPointLimit;
    }

    public void setX(float x) {
        this.realX = x;
    }

    public void setY(float y) {
        this.realY = y;
    }

    public void setWidth(float width) {
        this.realWidth = width;
    }

    public void setHeight(float height) {
        this.realHeight = height;
    }

    public void setAxesMargin(float margin) {
        this.realAxesMargin = margin;
    }

    public void setFixedMinYValue(float originYValue) {
        this.fixedMinYValue = originYValue;
    }

    public boolean isCam() {
        return camSensitive;
    }

    public void setCamSensitive(boolean cam) {
        this.camSensitive = cam;

    }

    public void setXPointLimit(int pointLimit) {
        this.xPointLimit = pointLimit;
    }

    public boolean isShowValuesOnMouse() {
        return showValuesOnMouse;
    }

    public void setShowValuesOnMouse(boolean showValuesOnMouse) {
        this.showValuesOnMouse = showValuesOnMouse;
    }

    public boolean isShowValues() {
        return showValues;
    }

    public void setShowValues(boolean showValues) {
        this.showValues = showValues;
    }

    public void setYGraduation(int yGraduation) {
        this.yGraduation = yGraduation;
    }

    public void setSmoothDataMovement(boolean smoothDataMovement) {
        this.smoothDataMovement = smoothDataMovement;
    }

    public void setFixedMaxYValue(float fixedMaxValue) {
        this.fixedMaxYValue = fixedMaxValue;
    }

    public void setGraphXOffset(float graphXOffset) {
        this.graphXOffset = graphXOffset;
    }

    public float getGraphXOffset() {
        return graphXOffset;
    }

}
