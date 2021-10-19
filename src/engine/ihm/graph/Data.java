package engine.ihm.graph;

import engine.game.physics.Point;
import java.awt.Color;
import java.util.concurrent.CopyOnWriteArrayList;

public class Data {

    private CopyOnWriteArrayList<Point> points;
    private float pointRadius;
    private boolean showPoints;
    private int average;

    private Color color;
    private boolean visible;

    public Data(Color color) {
        this.color = color;
        points = new CopyOnWriteArrayList();
        showPoints = false;
        pointRadius = 4;
        visible = true;
        average = 0;
    }

    public void addPoint(float value) {
        points.add(new Point(1, value));
    }

    public CopyOnWriteArrayList<Point> getPoints() {
        if (average == 0) {
            return points;
        } else {
            return getAveragedPoints();
        }
    }

    /**
     * FollowedAverage (5 points = 5 same points)
     *
     * @return
     */
//    private CopyOnWriteArrayList<Point> getAveragedPoints() {
//        CopyOnWriteArrayList<Point> avgPoints = new CopyOnWriteArrayList();
//        float last = 0;
//        int ite = 0;
//        for (int i = 1; i <= points.size(); i++) {
//            ite++;
//            if (ite == average) {
//                ite = 0;
//                float tot = 0;
//                for (int j = 0; j < average; j++) {
//                    tot += points.get((i - 1) - j).y;
//                }
//                tot /= average;
//                last = tot;
//
//                for (int j = 0; j < average; j++) {
//                    avgPoints.add(new Point(1, tot));
//                }
//            }
//        }
//        int toAdd = points.size() - avgPoints.size();
//        for (int i = 0; i < toAdd; i++) {
//            avgPoints.add(new Point(1, last));
//        }
//        return avgPoints;
//    }
    /**
     * FollowedAverage (5 points = 5 same points)
     *
     * @return
     */
    private CopyOnWriteArrayList<Point> getAveragedPoints() {
        CopyOnWriteArrayList<Point> avgPoints = new CopyOnWriteArrayList();
        if (points.isEmpty()) {
            return points;
        }
        int ite = 0;
        avgPoints.add(points.get(0));
        for (int i = 0; i < points.size(); i++) {
            ite++;
            if (ite == average) {
                float tot = 0;
                for (int j = 0; j < average; j++) {
                    tot += points.get(i - j).y;
                }
                tot /= average;
                avgPoints.add(new Point(0, tot));
                i -= average - 1;
                ite = 0;
            }
        }
        return avgPoints;
    }

    public CopyOnWriteArrayList<Point> getShownPoints(int pointLimit, float xOffset) {
        CopyOnWriteArrayList<Point> pointsToWorkW;
        if (average == 0) {
            pointsToWorkW = points;
        } else {
            pointsToWorkW = getAveragedPoints();
        }
        CopyOnWriteArrayList<Point> shownPoint = new CopyOnWriteArrayList();
        int start = pointsToWorkW.size() > pointLimit ? pointsToWorkW.size() - pointLimit : 0;
        start += xOffset;
        int end = pointsToWorkW.size() >= pointLimit ? start + pointLimit : pointsToWorkW.size();
        for (int i = start; i < end; i++) {
            if (i < pointsToWorkW.size() && i > 0) {
                shownPoint.add(pointsToWorkW.get(i));
            }
        }
        return shownPoint;
    }

    public void setPoints(CopyOnWriteArrayList<Point> points) {
        this.points = points;
    }

    public float getPointRadius() {
        return pointRadius;
    }

    public void setPointRadius(float pointRadius) {
        this.pointRadius = pointRadius;
    }

    public boolean isShowPoints() {
        return showPoints;
    }

    public void setShowPoints(boolean showPoints) {
        this.showPoints = showPoints;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setAverage(int average) {
        this.average = average;
    }

}
