package engine.game.map;

import engine.GameContainer;
import static engine.consts.FileConstants.FILE_EXT_MAP;
import static engine.consts.FileConstants.FOLDER_MAPS;
import java.awt.Graphics2D;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AbstractMap implements Serializable {

    private HashMap<Integer, CopyOnWriteArrayList<Block>> blocks;
    private HashMap<String, String> parameters;
    private GameContainer gc;

    public AbstractMap(GameContainer gc) {
        this.gc = gc;
    }

    /**
     * Loads a serialized map
     *
     * @param mapName
     * @return an error code : (0 : No errors) - (1 : Incorrect MapName) - (2 :
     * Error loading the map)
     */
    public int loadMap(String mapName) {
        if (mapName == null || mapName.length() == 0) {
            gc.getEventHistory().addEvent("Error loading the map \"" + mapName + "\" => Bad name");
            return 1;
        }
        mapName = (!mapName.startsWith(FOLDER_MAPS) ? FOLDER_MAPS : "") + mapName + (!mapName.endsWith(FILE_EXT_MAP) ? FILE_EXT_MAP : "");
        AbstractMap map = null;
        try {
            PackageChanger t = new PackageChanger(new BufferedInputStream(new FileInputStream(mapName)));
            map = (AbstractMap) t.readObject();
            t.close();
        } catch (IOException | ClassNotFoundException | java.lang.ClassCastException ex) {
            Logger.getLogger(AbstractMap.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (map != null) {
            blocks = map.getBlocks();
            parameters = map.getParameters();
        } else {
            gc.getEventHistory().addEvent("Error loading the map \"" + mapName + "\" => Null content");
            return 2;
        }

        return 0;
    }

    public void render(Graphics2D g) {
        for (CopyOnWriteArrayList<Block> list : blocks.values()) {
            for (Block b : list) {
                b.render(gc, g);
            }
        }
    }

    public HashMap<Integer, CopyOnWriteArrayList<Block>> getBlocks() {
        return blocks;
    }

    public void setBlocks(HashMap<Integer, CopyOnWriteArrayList<Block>> blocks) {
        this.blocks = blocks;
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }

}
