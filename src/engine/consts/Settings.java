package engine.consts;

import engine.GameContainer;
import engine.game.physics.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Settings implements FileConstants {

    private final GameContainer gc;

    private Color backgroundColor = Color.white;
    private Image backgroundImage = null;
    private Image icon;
    private boolean staticBackground;
    private int width = 1000, height = 1000;
    private boolean fullScreen;
    private String title = "Game Engine";
    private String gameVersion = "V2.4";
    private String serverName = "Default Server Name";

    public Settings(GameContainer gc) {
        this.gc = gc;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackground(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        gc.getWindow().getFrame().setSize(width, gc.getWindow().getFrame().getSize().height);
        gc.getCam().setOffSet(new Point(-width / 2, gc.getCam().getOffSet().y));
        gc.getCam().setLastOffSet(new Point(-width / 2, gc.getCam().getOffSet().y));
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        gc.getWindow().getFrame().setSize(gc.getWindow().getFrame().getSize().width, height);
        gc.getCam().setOffSet(new Point(gc.getCam().getOffSet().x, -height / 2));
        gc.getCam().setLastOffSet(new Point(gc.getCam().getOffSet().x, -height / 2));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackground(Image backgroundImage, boolean staticBackground) {
        this.backgroundImage = backgroundImage;
        this.staticBackground = staticBackground;
    }

    public void setBackground(GameContainer gc, String path, boolean staticBackground) {
        try {
            backgroundImage = ImageIO.read(new File(FOLDER_IMAGES + path));
            this.staticBackground = staticBackground;
        } catch (IOException ex) {
            gc.getEventHistory().addEvent("Image Not Found : " + path);
        }
    }

    public boolean isStaticBackground() {
        return staticBackground;
    }

    public void setStaticBackground(boolean staticBackground) {
        this.staticBackground = staticBackground;
    }

    public void setIcon(Image icon) {
        if (icon != null) {
            this.icon = icon;
            gc.getWindow().getFrame().setIconImage(icon);
            if ((gc.getServer() != null) && (gc.getServer().getConsole() != null)) {
                gc.getServer().getConsole().setIconImage(icon);
            } else {
                gc.getEventHistory().addEvent("Server is null, can't set Icon");
            }
            if ((gc.getClient() != null) && (gc.getClient().getConsole() != null)) {
                gc.getClient().getConsole().setIconImage(icon);
            } else {
                gc.getEventHistory().addEvent("Client is null, can't set Icon");
            }
        }
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
        if (fullScreen) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            width = (int) screenSize.getWidth();
            height = (int) screenSize.getHeight();

            gc.getWindow().getFrame().setVisible(false);
            gc.getWindow().getFrame().setUndecorated(fullScreen);
            gc.getWindow().getFrame().setVisible(true);
        } else {
            width = 1000;
            height = 1000;
        }
        gc.getWindow().getFrame().setExtendedState(fullScreen ? JFrame.MAXIMIZED_BOTH : JFrame.NORMAL);

    }

}
