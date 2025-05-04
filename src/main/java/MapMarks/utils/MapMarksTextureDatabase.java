package MapMarks.utils;

import com.badlogic.gdx.graphics.Texture;
import easel.utils.textures.ITextureDatabaseEnum;

public enum MapMarksTextureDatabase implements ITextureDatabaseEnum {
    MOD_ICON("MapMarks/textures/modIcon.png"),

    PAINT_CIRCLE("MapMarks/textures/circle18.png"),

    LEGEND_SHADOW("MapMarks/textures/legend/shadow.png"),
    LEGEND_BASE("MapMarks/textures/legend/base.png"),
    LEGEND_HIGHLIGHT("MapMarks/textures/legend/highlight.png"),
    LEGEND_DIM("MapMarks/textures/legend/dim.png"),
    LEGEND_TRIM("MapMarks/textures/legend/trim.png"),

    SMALL_TILE_SHADOW("MapMarks/textures/small/shadow.png"),
    SMALL_TILE_BASE("MapMarks/textures/small/base.png"),
    SMALL_TILE_TRIM("MapMarks/textures/small/trim.png"),

    LARGE_TILE_SHADOW("MapMarks/textures/large/shadow.png"),
    LARGE_TILE_INNER_BASE("MapMarks/textures/large/inner_base.png"),
    LARGE_TILE_OUTER_BASE("MapMarks/textures/large/outer_base.png"),
    LARGE_TILE_TRIM("MapMarks/textures/large/trim.png"),

    RADIAL_BASE("MapMarks/textures/radial_base.png"),
    RADIAL_TRIM("MapMarks/textures/radial_trim.png"),

    DEFAULTS_SAVE("MapMarks/textures/defaults/default_save_icon.png"),
    DEFAULTS_CLEAR("MapMarks/textures/defaults/default_clear_icon.png")
    ;

    private final String internalPath;
    private Texture texture;

    MapMarksTextureDatabase(String internalPath) {
        this.internalPath = internalPath;
    }

    public void load() {
        this.texture = new Texture(internalPath);
    }

    public Texture getTexture() {
        return texture;
    }
}
