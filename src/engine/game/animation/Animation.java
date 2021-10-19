package engine.game.animation;

import engine.consts.FileConstants;
import engine.ihm.game.Popup;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Animation implements Serializable {

    private ArrayList<Sprite> sprites;
    private float cooldown;
    private int currentSprite;
    private String name;
    private boolean switching;
    private Timer timer;
    private float lastTimeAnimationChange;
    private int categorie;

    public Animation(int categorie, float cooldown) {
        this.cooldown = cooldown;
        if (cooldown != -1) {
            this.switching = true;
        } else {
            switching = false;
        }
        this.categorie = categorie;
        currentSprite = 0;
        sprites = new ArrayList();
        timer = new Timer();
    }

    public Animation(int categorie) {
        this.cooldown = -1;
        this.switching = false;
        this.categorie = categorie;
        currentSprite = 0;
        sprites = new ArrayList();
        timer = new Timer();
    }

    
    public void addSprite(Image sprite, String spritePath) {
        sprites.add(new Sprite(sprite, sprites.size(), spritePath));
    }

    public void addSprite(Image sprite, float cooldown, String spritePath) {
        sprites.add(new Sprite(sprite, cooldown, sprites.size(), spritePath));
    }

    public void addSprite(Image sprite, int spriteID, String spritePath) {
        sprites.add(new Sprite(sprite, spriteID, spritePath));
        ArrayList<Sprite> temp = new ArrayList();
        temp.addAll(sprites);
        sprites.clear();

        for (int i = 0; i < temp.size(); i++) {
            if (i == spriteID) {
                sprites.add(new Sprite(sprite, cooldown, spriteID, spritePath));
            } else {
                sprites.add(new Sprite(temp.get(i).getSprite(), i, temp.get(i).getSpritePath()));

            }
        }
    }

    public void addSprite(Image sprite, float cooldown, int spriteID, String spritePath) {
        sprites.add(new Sprite(sprite, cooldown, spriteID, spritePath));
        ArrayList<Sprite> temp = new ArrayList();
        temp.addAll(sprites);
        sprites.clear();

        for (int i = 0; i < temp.size(); i++) {
            if (i == spriteID) {
                sprites.add(new Sprite(sprite, cooldown, spriteID, spritePath));
            } else {
                sprites.add(new Sprite(temp.get(i).getSprite(), temp.get(i).getCooldown(), i, temp.get(i).getSpritePath()));

            }
        }
    }

    /**
     *
     * @return true if the animation restarted
     */
    public boolean update() {
        int last = currentSprite;
        timer.update();
        if (switching) {
            float cooldown = sprites.get(currentSprite).getCooldown() == -1f ? this.cooldown : sprites.get(currentSprite).getCooldown();
            if (timer.getCurrentTimePassed() - lastTimeAnimationChange >= cooldown * 1000) {
                lastTimeAnimationChange = timer.getCurrentTimePassed();
                currentSprite++;
                if (sprites.size() <= currentSprite) {
                    currentSprite = 0;
                }
            }
        }
        return (currentSprite == 0) && (last != 0);
        
    }

    public void addSprites() {
        for (Sprite s : sprites) {
            s.setSpritePath(FileConstants.FOLDER_IMAGES + s.getSpritePath().split("\\\\")[s.getSpritePath().split("\\\\").length - 1]);
            try {
                s.setSprite(ImageIO.read(new File(s.getSpritePath())));
            } catch (IOException ex) {
                Popup.warning("Unkown Image");
            }
        }
    }

    public void setCurrentSprite(int currentSprite) {
        this.currentSprite = currentSprite;
    }

    public Image getSpriteToDraw() {
        return sprites.get(currentSprite).getSprite();
    }

    public ArrayList<Sprite> getSprites() {
        return sprites;
    }

    public void setSprites(ArrayList<Sprite> sprites) {
        this.sprites = sprites;
    }

    public float getCooldown() {
        return cooldown;
    }

    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }

    public boolean isSwitching() {
        return switching;
    }

    public void setSwitching(boolean switching) {
        this.switching = switching;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public float getLastTimeAnimationChange() {
        return lastTimeAnimationChange;
    }

    public void setLastTimeAnimationChange(float lastTimeAnimationChange) {
        this.lastTimeAnimationChange = lastTimeAnimationChange;
    }

    public int getCategorie() {
        return categorie;
    }

    public void setCategorie(int categorie) {
        this.categorie = categorie;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
