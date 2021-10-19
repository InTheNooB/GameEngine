package engine.ihm.game;

import engine.GameContainer;
import engine.game.GameObject;
import engine.game.particles.Particle;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;
import javax.swing.JPanel;

public class Renderer extends JPanel {

    private final GameContainer gc;
    private BufferedImage bufferImage;

    public Renderer(GameContainer gc) {
        super(true);
        this.gc = gc;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gc.getEventHistory() == null || gc.getGame() == null) {
            return;
        }

        if (bufferImage == null) {
            bufferImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        }

        Graphics2D g2 = (Graphics2D) bufferImage.getGraphics();

//        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        clear(g2);

        // Renders game
        try {
            if (gc.getGame() != null) {
                gc.getGame().render(gc, g2);
            }
        } catch (NullPointerException e) {
            gc.getEventHistory().addEvent("NullPointer : Rendering Game");
        } catch (ConcurrentModificationException e) {
            gc.getEventHistory().addEvent("ConcurrentModification : Rendering Game");
        } catch (ArrayIndexOutOfBoundsException e) {
            gc.getEventHistory().addEvent("ArrayIndexOutOfBound : Rendering Game");
        }

        // Render each GameObject
        if (gc.getGameObjects() != null) {
            for (GameObject go : gc.getGameObjects()) {
                try {
                    if (go != null) {
                        if (go.isRendered()) {
                            go.render(gc, g2);
                        }
                    }
                } catch (NullPointerException e) {
                    gc.getEventHistory().addEvent("NullPointer : Rendering GameObject");
                } catch (ConcurrentModificationException e) {
                    gc.getEventHistory().addEvent("ConcurrentModification : Rendering GameObject (" + go.getIdentifier() + ")");
                } catch (ArrayIndexOutOfBoundsException e) {
                    gc.getEventHistory().addEvent("ArrayIndexOutOfBound : Rendering Game");
                }
            }
        }

        // Render each Particle
        if (gc.getParticles() != null) {
            for (Particle p : gc.getParticles()) {
                try {
                    if (p != null) {
                        if (p.isRendered()) {
                            p.render(gc, g2);
                        }
                    }
                } catch (NullPointerException e) {
                    gc.getEventHistory().addEvent("NullPointer : Rendering Particle");
                } catch (ConcurrentModificationException e) {
                    gc.getEventHistory().addEvent("ConcurrentModification : Rendering Particle");
                } catch (ArrayIndexOutOfBoundsException e) {
                    gc.getEventHistory().addEvent("ArrayIndexOutOfBound : Rendering Game");
                }
            }
        }
        try {
            // Render selected GameObject
            GameObject selected = gc.getGameObjectSelector().getSelected();
            if (selected != null) {
                g2.setColor(Color.white);
                g2.drawRect((int) selected.getRealPos(gc).x, (int) selected.getRealPos(gc).y, (int) selected.getRealWidth(gc), (int) selected.getRealHeight(gc));

                // Render infos
                g2.setColor(Color.black);
                int ratio = (int) (gc.getSettings().getWidth() / 5);
                g2.fillRect(gc.getSettings().getWidth() - ratio, 25, ratio, 300);
                g2.setColor(Color.white);
                g2.drawRect(gc.getSettings().getWidth() - ratio, 25, ratio, 300);

                g2.setColor(Color.white);
                g2.drawString("Identifier : " + selected.getIdentifier(), (int) (gc.getSettings().getWidth() - ratio + ratio / 5), 50);
                g2.drawString("X : " + selected.getX(), (int) (gc.getSettings().getWidth() - ratio + ratio / 5), 75);
                g2.drawString("Y : " + selected.getY(), (int) (gc.getSettings().getWidth() - ratio + ratio / 5), 100);
                g2.drawString("Height : " + selected.getHeight(), (int) (gc.getSettings().getWidth() - ratio + ratio / 5), 125);
                g2.drawString("Width : " + selected.getWidth(), (int) (gc.getSettings().getWidth() - ratio + ratio / 5), 150);
                g2.drawString("Velocity X : " + selected.getVelX(), (int) (gc.getSettings().getWidth() - ratio + ratio / 5), 175);
                g2.drawString("Velocity Y : " + selected.getVelY(), (int) (gc.getSettings().getWidth() - ratio + ratio / 5), 200);
                g2.drawString("OnTheGround : " + selected.isOnTheGround(), (int) (gc.getSettings().getWidth() - ratio + ratio / 5), 225);
                gc.getGameObjectSelector().getFollowButton().render(gc, g2);
            }
        } catch (NullPointerException e) {
            gc.getEventHistory().addEvent("NullPointer : Selected GameObject");
        }

        //Render Buttons
        try {
            for (Button b : gc.getWindow().getButtons()) {
                b.render(gc, g2);
            }
        } catch (Exception e) {
            gc.getEventHistory().addEvent("Exception : Rendering Button");
        }

        g.drawImage(bufferImage, 0, 0, null);
    }

    public void clear(Graphics2D g2) {
        int W = gc.getSettings().getWidth();
        int H = gc.getSettings().getHeight();
        g2.setColor(Color.white);
        g2.fillRect(0, 0, W, H);
        if (gc.getSettings().getBackgroundImage() == null) {
            g2.setColor(gc.getSettings().getBackgroundColor());
            g2.fillRect(0, 0, W, H);
        } else {
            if (gc.getSettings().isStaticBackground()) {
                g2.drawImage(gc.getSettings().getBackgroundImage(), 0, 0, gc.getSettings().getWidth(), gc.getSettings().getHeight(), null);
            } else {
                g2.drawImage(gc.getSettings().getBackgroundImage(), (int) GameObject.getRealPos(gc, 0, 0).x, (int) GameObject.getRealPos(gc, 0, 0).y, (int) GameObject.getRealWidth(gc, gc.getSettings().getBackgroundImage().getWidth(null)), (int) GameObject.getRealWidth(gc, gc.getSettings().getBackgroundImage().getHeight(null)), null);
            }
        }
    }

}
