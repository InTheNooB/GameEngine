package engine.game.physics;

import engine.game.GameObject;
import java.awt.Rectangle;

public abstract class Physics {

    /**
     * Returns the distance between 2 points
     *
     */
    public static double getDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    /**
     * Returns the distance between 2 game object
     *
     * @param go1
     * @param go2
     * @return
     */
    public static float getDistance(GameObject go1, GameObject go2) {
        return (float) (Math.sqrt(Math.pow((go1.getX() + go1.getWidth() / 2) - (go2.getX() + go2.getWidth() / 2), 2) + Math.pow((go1.getY() + go1.getHeight() / 2) - (go2.getY() + go2.getHeight() / 2), 2)));
    }

    /**
     * Returns the point of intersection of 2 segments
     *
     */
    public static Point collision(Segment ray, Segment wall) {
        float x1 = wall.p1.x;
        float y1 = wall.p1.y;
        float x2 = wall.p2.x;
        float y2 = wall.p2.y;

        float x3 = ray.p1.x;
        float y3 = ray.p1.y;
        float x4 = ray.p2.x;
        float y4 = ray.p2.y;

        Point point = null;

        double den = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

        // Parallele
        if (den == 0) {
            return point;
        }

        double t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / den;
        double u = -((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3)) / den;

        if ((t >= 0) && (t <= 1) && (u >= 0) && (u <= 1)) {
            // Intersects
            point = new Point((float) (x1 + t * (x2 - x1)), (float) (y1 + t * (y2 - y1)));
        }
        return point;
    }

    /**
     * Processes a collision test
     *
     * @return True if there is a collision
     */
    public static boolean collision(GameObject go1, GameObject go2) {
        Rectangle rect1 = new Rectangle((int) go1.getX(), (int) go1.getY(), (int) go1.getWidth(), (int) go1.getHeight());
        Rectangle rect2 = new Rectangle((int) go2.getX(), (int) go2.getY(), (int) go2.getWidth(), (int) go2.getHeight());
        return rect1.intersects(rect2);
    }

    /**
     * Processes a collision test
     *
     * @return True if there is a collision
     */
    public static boolean collision(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2) {
        Rectangle rect1 = new Rectangle((int) x1, (int) y1, (int) w1, (int) h1);
        Rectangle rect2 = new Rectangle((int) x2, (int) y2, (int) w2, (int) h2);
        return rect1.intersects(rect2);
    }

    public static Point collision(Segment s, GameObject go) {
        Segment goS1 = new Segment(new Point(go.getX(), go.getY()), new Point(go.getX() + go.getWidth(), go.getY()));
        Segment goS2 = new Segment(new Point(go.getX() + go.getWidth(), go.getY()), new Point(go.getX() + go.getWidth(), go.getY() + go.getHeight()));
        Segment goS3 = new Segment(new Point(go.getX() + go.getWidth(), go.getY() + go.getHeight()), new Point(go.getX(), go.getY() + go.getHeight()));
        Segment goS4 = new Segment(new Point(go.getX(), go.getY() + go.getHeight()), new Point(go.getX(), go.getY()));

        Point p1 = Physics.collision(s, goS1);
        Point p2 = Physics.collision(s, goS2);
        Point p3 = Physics.collision(s, goS3);
        Point p4 = Physics.collision(s, goS4);

        if (p1 != null) {
            return p1;
        } else if (p2 != null) {
            return p2;
        } else if (p3 != null) {
            return p3;
        } else if (p4 != null) {
            return p4;
        }
        return null;
    }

    public static boolean collision(Point p, float x, float y, float w, float h) {
        boolean sY = (p.y >= y) && (p.y <= y + h);
        boolean sX = (p.x >= x) && (p.x <= x + w);
        return (sY && sX);
    }

    public static boolean collision(GameObject go, float x, float y, float w, float h) {
        Rectangle rect1 = new Rectangle((int) go.getX(), (int) go.getY(), (int) go.getWidth(), (int) go.getHeight());
        Rectangle rect2 = new Rectangle((int) x, (int) y, (int) w, (int) h);
        return rect1.intersects(rect2);

    }

}
