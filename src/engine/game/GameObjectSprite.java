package engine.game;

import engine.GameContainer;
import engine.consts.FileConstants;
import java.io.File;
import java.awt.Image;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javax.imageio.ImageIO;

public class GameObjectSprite implements FileConstants, Serializable {

    private transient Image image;
    private String filePath;
    private float w, h;

    public GameObjectSprite(GameContainer gc, String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            filePath = filePath.startsWith(FOLDER_IMAGES) ? filePath : FOLDER_IMAGES + filePath;
            this.filePath = filePath;
            try {
                image = ImageIO.read(new File(filePath));
                w = image.getWidth(null);
                h = image.getHeight(null);
            } catch (IOException ex) {
                gc.getEventHistory().addEvent("Image Not Found : " + filePath);
            }
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField g = in.readFields();
        filePath = (String) g.get("filePath", null);
        try {
            image = ImageIO.read(new File(filePath));
            w = image.getWidth(null);
            h = image.getHeight(null);
        } catch (IOException ex) {
            System.out.println("Image Not Found : " + filePath);
        }
    }

    public Image getImage() {
        return image;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
