package engine.game.animation;

import engine.consts.FileConstants;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class AnimationSet implements Serializable, FileConstants {

    private ArrayList<Animation> animations;
    private int currentCategorie;
    private transient int nextCategorie;

    public AnimationSet() {
        animations = new ArrayList();
        nextCategorie = -1;
    }

    public void addCategorie(float cooldown, int categorie) {
        animations.add(new Animation(categorie, cooldown));
    }

    public void addCategorie(int categorie) {
        animations.add(new Animation(categorie));
    }

    public void fillCategorie(Animation animation) {
        int categorieID = getMatchingCategorieID(animation.getCategorie());
        if (animation.getCooldown() != -1 && animation.isSwitching()) {
            animations.get(categorieID).setSwitching(true);
            animations.get(categorieID).setCooldown(animation.getCooldown());
        } else {
            animations.get(categorieID).setSwitching(false);
            animations.get(categorieID).setCooldown(-1);
        }

        for (Sprite s : animation.getSprites()) {
            if (s.getCooldown() != -1) {
                animations.get(categorieID).addSprite(s.getSprite(), s.getCooldown(), s.getId(), s.getSpritePath());
            } else {
                animations.get(categorieID).addSprite(s.getSprite(), s.getId(), s.getSpritePath());
            }
        }
        animations.get(categorieID).addSprites();

    }

    public void addCategorie(Animation animation) {
        if (animation.getCooldown() != -1) {
            animations.add(new Animation(animation.getCategorie(), animation.getCooldown()));
        } else {
            animations.add(new Animation(animation.getCategorie()));
        }

        int categorieID = getMatchingCategorieID(animation.getCategorie());

        for (Sprite s : animation.getSprites()) {
            if (s.getCooldown() != -1) {
                animations.get(categorieID).addSprite(s.getSprite(), s.getCooldown(), s.getId(), s.getSpritePath());
            } else {
                animations.get(categorieID).addSprite(s.getSprite(), s.getId(), s.getSpritePath());
            }
        }
        animations.get(categorieID).addSprites();

    }

    public boolean addSprite(File file, int categorie, String spritePath) {
        boolean ok = true;
        int categorieID = getMatchingCategorieID(categorie);
        try {
            animations.get(categorieID).addSprite(ImageIO.read(file), spritePath);
        } catch (IOException ex) {
            ok = false;
        }
        return ok;
    }

    public boolean addSprite(File file, int categorie, float cooldown, String spritePath) {
        boolean ok = true;
        int categorieID = getMatchingCategorieID(categorie);
        try {
            animations.get(categorieID).addSprite(ImageIO.read(file), cooldown, spritePath);
        } catch (IOException ex) {
            ok = false;
        }
        return ok;
    }

    public boolean addSprite(File file, int categorie, int sprite, String spritePath) {
        boolean ok = true;
        int categorieID = getMatchingCategorieID(categorie);
        int spriteID = getMatchingSpriteID(categorieID, sprite);
        try {
            animations.get(categorieID).addSprite(ImageIO.read(file), spriteID, spritePath);
        } catch (IOException ex) {
            ok = false;
        }
        return ok;
    }

    public boolean addSprite(File file, int categorie, float cooldown, int sprite, String spritePath) {
        boolean ok = true;
        int categorieID = getMatchingCategorieID(categorie);
        int spriteID = getMatchingSpriteID(categorieID, sprite);
        try {
            animations.get(categorieID).addSprite(ImageIO.read(file), cooldown, spriteID, spritePath);
        } catch (IOException ex) {
            ok = false;
        }
        return ok;
    }

    public void updateAnimationSettings(int categorie, float cooldown) {
        int categorieID = getMatchingCategorieID(categorie);
        animations.get(categorieID).setCooldown(cooldown);
        animations.get(categorieID).setSwitching(true);
    }

    public void updateAnimationSettings(int categorie) {
        int categorieID = getMatchingCategorieID(categorie);
        animations.get(categorieID).setCooldown(-1);
        animations.get(categorieID).setSwitching(false);
    }

    public void updateAnimation() {
        int categorieID = getMatchingCategorieID(currentCategorie);
        if (categorieID == -1) {
            return;
        }
        boolean restarted = animations.get(categorieID).update();
        if ((nextCategorie != -1) && (restarted)) {
            setCurrentCategorie(nextCategorie);
            nextCategorie = -1;
        }
    }

    public void setCurrentCategorie(int currentCategorie) {
        if (currentCategorie != -1) {
            if ((this.currentCategorie != currentCategorie) && (nextCategorie == -1)) {
                this.currentCategorie = currentCategorie;
                setCurrentSprite(0);
            }
        }
    }

    /**
     * Play an animation, and then the next one loops when the first one has
     * ended
     *
     * @param categorie
     * @param nextCategorie
     */
    public void play(int categorie, int nextCategorie) {
        setCurrentCategorie(categorie);
        this.nextCategorie = nextCategorie;
    }

    /**
     * Returns true if the "play" method has been called and if the first
     * animation is still playing
     *
     * @return
     */
    public boolean isPlaying() {
        return nextCategorie != -1;
    }

    public void setCurrentSprite(int currentSprite) {
        int categorieID = getMatchingCategorieID(currentCategorie);
        animations.get(categorieID).setCurrentSprite(currentSprite);
    }

    public Image getSpriteToDraw() {
        int categorieID = getMatchingCategorieID(currentCategorie);
        try {
            return animations.get(categorieID).getSpriteToDraw();
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public boolean isEmpty() {
        return animations.isEmpty();
    }

    public boolean isSwitching(int categorie) {
        int categorieID = getMatchingCategorieID(categorie);
        return animations.get(categorieID).isSwitching();
    }

    public float getCooldown(int categorie) {
        int categorieID = getMatchingCategorieID(categorie);
        // cateid = -1
        return animations.get(categorieID).getCooldown();
    }

    public float getCooldown(int categorie, int sprite) {
        int categorieID = getMatchingCategorieID(categorie);
        int spriteID = getMatchingSpriteID(categorieID, sprite);
        if (spriteID == -1) {
            return animations.get(categorieID).getCooldown();
        }
        return animations.get(categorieID).getSprites().get(spriteID).getCooldown();
    }

    public boolean categorieExist(int i) {
        return animations.size() >= i;
    }

    public void removeSprite(int categorie, int chosenSprite) {
        int categorieID = getMatchingCategorieID(categorie);
        int spriteID = getMatchingSpriteID(categorieID, chosenSprite);
        if (spriteID != -1) {
            animations.get(categorieID).getSprites().remove(spriteID);
        }
    }

    public int getCurrentCategorie() {
        int categorieID = getMatchingCategorieID(currentCategorie);
        return categorieID;
    }

    public int getRealCurrentCategorie() {
        return currentCategorie;
    }

    public void spriteUpdate(int categorie, int chosenSprite, float cooldown) {
        int categorieID = getMatchingCategorieID(categorie);
        int spriteID = getMatchingSpriteID(categorieID, chosenSprite);

        if (spriteID != -1) {
            animations.get(categorieID).getSprites().get(spriteID).setCooldown(cooldown);
        }
        animations.get(categorieID).setSwitching(true);
    }

    public void categorieUpdate(int categorie, float cooldown) {
        int categorieID = getMatchingCategorieID(categorie);
        float oldCooldown = animations.get(categorieID).getCooldown();
        animations.get(categorieID).setSwitching(true);
        animations.get(categorieID).setCooldown(cooldown);
        for (Sprite s : animations.get(categorieID).getSprites()) {
            if (s.getCooldown() == oldCooldown) {
                s.setCooldown(cooldown);
            }
        }
    }

    public void categorieUpdate(int categorie, boolean switching) {
        int categorieID = getMatchingCategorieID(categorie);
        animations.get(categorieID).setSwitching(switching);
    }

    public boolean isEmpty(int categorie) {
        int categorieID = getMatchingCategorieID(categorie);
        return animations.get(categorieID).getSprites().isEmpty();
    }

    public void setCategorieSwitching(int categorie, boolean state) {
        int categorieID = getMatchingCategorieID(categorie);
        animations.get(categorieID).setSwitching(state);
        if (!state) {
            for (Sprite s : animations.get(categorieID).getSprites()) {
                s.setCooldown(-1);
            }
        }
    }

    public void setSpriteCooldown(int categorie, int chosenSprite, float cooldown) {
        int categorieID = getMatchingCategorieID(categorie);
        int spriteID = getMatchingSpriteID(categorieID, chosenSprite);
        if (cooldown != animations.get(categorieID).getCooldown()) {
            animations.get(categorieID).getSprites().get(spriteID).setCooldown(cooldown);
        } else {
            animations.get(categorieID).getSprites().get(spriteID).setCooldown(-1);
        }
    }

    private int getMatchingCategorieID(int categorie) {
        int categorieID = -1;
        for (int i = 0; i < animations.size(); i++) {
            if (animations.get(i).getCategorie() == categorie) {
                categorieID = i;
                break;
            }
        }
        return categorieID;
    }

    public Animation getAnimation(int categorie) {
        int categorieID = getMatchingCategorieID(categorie);
        return animations.get(categorieID);
    }

    private int getMatchingSpriteID(int categorie, int chosenSprite) {
        int categorieID = getMatchingCategorieID(categorie);
        int spriteID = -1;
        for (int i = 0; i < animations.get(categorieID).getSprites().size(); i++) {
            if (animations.get(categorieID).getSprites().get(i).getId() == chosenSprite) {
                spriteID = i;
                break;
            }
        }
        return spriteID;
    }

    public int importAnimation(String animationName) {
        animationName = (animationName.startsWith(FOLDER_ANIMATIONS) ? animationName : FOLDER_ANIMATIONS + animationName);
        animationName = (animationName.endsWith(FILE_EXT_ANIMATION) ? animationName : animationName + FILE_EXT_ANIMATION);
        int categorie = -1;
        try {

            Deserialiser in = null;
            Animation importedAnimation = null;
            // Deserialization
            // Reading the object from a file
            in = new Deserialiser(new FileInputStream(new File(animationName)));

            // Method for deserialization of object 
            importedAnimation = (Animation) in.readObject();
            in.close();
            categorie = importedAnimation.getCategorie();

            addCategorie(importedAnimation);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return categorie;
    }

    public int getCategoriesAmount() {
        return animations.size();
    }

    public ArrayList<Animation> getAnimations() {
        return animations;
    }

    public void setAnimations(ArrayList<Animation> animations) {
        this.animations = animations;
    }

}
