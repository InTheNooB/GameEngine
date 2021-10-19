package engine.consts;

import java.io.File;

public interface FileConstants {

    public final String SPR = File.separator;
    public final String FOLDER_ASSETS = "assets";
    public final String FOLDER_IMAGES = FOLDER_ASSETS + SPR + "images" + SPR;
    public final String FOLDER_SOUNDS = FOLDER_ASSETS + SPR + "audios" + SPR;
    public final String FOLDER_FONTS = FOLDER_ASSETS + SPR + "fonts" + SPR;
    public final String FOLDER_LOGS = FOLDER_ASSETS + SPR + "logs" + SPR;
    public final String FOLDER_ANIMATIONS = FOLDER_ASSETS + SPR + "animations" + SPR;
    public final String FOLDER_MAPS = FOLDER_ASSETS + SPR + "maps" + SPR;
    
    public final String FILE_EXT_ANIMATION = ".anm";
    public final String FILE_EXT_MAP = ".mp";
}
