package engine.game.particles;

import engine.GameContainer;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

public abstract class Particle {

    protected float x, y, width, height, radius;
    protected float velocity;
    protected float angle;
    protected float rotation;
    protected int alpha;
    protected float ttl; //in s
    protected double creationTime;
    protected boolean dead;
    protected Image image;
    protected Color color;
    protected boolean updated;
    protected boolean rendered;

    public Particle(GameContainer gc) {
        updated = true;
        rendered = true;
        alpha = 255;
        color = Color.red;
        ttl = 3;
        creationTime = gc.getRunningTime();
        dead = false;
    }

    public Particle(GameContainer gc, float x, float y, float width, float height) {
        this(gc);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Particle(GameContainer gc, float x, float y, float radius) {
        this(gc);
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public abstract void setup(GameContainer gc);

    public abstract void update(GameContainer gc);

    public abstract void render(GameContainer gc, Graphics2D g);

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

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getTtl() {
        return ttl;
    }

    public void setTtl(float ttl) {
        this.ttl = ttl;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public double getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(double creationTime) {
        this.creationTime = creationTime;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
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
    

}
