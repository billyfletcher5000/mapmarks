package MapMarks.ui;

import MapMarks.utils.ColorDatabase;
import MapMarks.utils.ColorEnum;
import MapMarks.utils.MapMarksTextureDatabase;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.TipHelper;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;
import easel.ui.graphics.LayeredTextureWidget;

public class DefaultsButton extends AbstractWidget<DefaultsButton> {
    private static final float WIDTH = 36;
    private static final float HEIGHT = 36;

    public static final String TIP_SAVE_HEADER = "Save Defaults";
    public static final String TIP_SAVE_BODY = "Saves the current colors of each encounter as defaults if the same color is applied to all encounters of that type.";

    public static final String TIP_CLEAR_HEADER = "Clear Defaults";
    public static final String TIP_CLEAR_BODY = "Clears the default colors of each encounter, including on the map if all encounters of that type are colored.";

    private static final Color DIM_COLOR = Color.valueOf("aaaaaaff");
    private static final Color HIGHLIGHT_COLOR = Color.valueOf("ffffffff");

    private static final Color SAVE_COLOR = ColorEnum.YELLOW.get();
    private static final Color CLEAR_COLOR = ColorEnum.RED.get();

    private final DefaultsButtonMode _mode;
    private final LayeredTextureWidget _ltw;

    public DefaultsButton(DefaultsButtonMode mode) {
        _ltw = new LayeredTextureWidget(WIDTH, HEIGHT)
                .withLayer(MapMarksTextureDatabase.SMALL_TILE_BASE.getTexture(), DIM_COLOR);

        _mode = mode;
        switch (_mode) {
            case SAVE:
                _ltw.withLayer(MapMarksTextureDatabase.DEFAULTS_SAVE.getTexture(), SAVE_COLOR);
                break;
            case CLEAR:
                _ltw.withLayer(MapMarksTextureDatabase.DEFAULTS_CLEAR.getTexture(), CLEAR_COLOR);
                break;
        }

        _ltw.withLayer(MapMarksTextureDatabase.SMALL_TILE_TRIM.getTexture(), ColorDatabase.UI_TRIM);

        this.onRightMouseDown(me -> me.setHighlight(false));
        this.onRightMouseUp(me -> me.setHighlight(true));

        this.onMouseEnter(me -> me.setHighlight(true));
        this.onMouseLeave(me -> me.setHighlight(false));
    }

    @Override
    protected void updateWidget() {
        super.updateWidget();
        if (hb.hovered) {
            switch (_mode) {
                case SAVE:
                    TipHelper.renderGenericTip(1500.0f * Settings.xScale,
                            270.0f * Settings.scale,
                            TIP_SAVE_HEADER,
                            TIP_SAVE_BODY);
                case CLEAR:
                    TipHelper.renderGenericTip(1500.0f * Settings.xScale,
                            270.0f * Settings.scale,
                            TIP_CLEAR_HEADER,
                            TIP_CLEAR_BODY);
            }
        }

    }

    private void setHighlight(boolean isHighlighted) {
        this._ltw.withLayerColor(0, isHighlighted ? HIGHLIGHT_COLOR : DIM_COLOR);
    }

    @Override
    public float getContentWidth() {
        return WIDTH;
    }

    @Override
    public float getContentHeight() {
        return HEIGHT;
    }

    @Override
    public DefaultsButton anchoredAt(float x, float y, AnchorPosition anchorPosition, InterpolationSpeed movementSpeed) {
        super.anchoredAt(x, y, anchorPosition, movementSpeed);
        _ltw.anchoredAt(x, y, anchorPosition, movementSpeed);
        return this;
    }

    @Override
    protected void renderWidget(SpriteBatch sb) {
        _ltw.render(sb);
    }
}
