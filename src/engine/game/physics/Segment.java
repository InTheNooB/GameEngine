package engine.game.physics;

public class Segment {

    public Point p1;
    public Point p2;

    public Segment() {
    }

    public Segment(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public int getDistance() {
        return (int) (Physics.getDistance(p1, p2));
    }

}
