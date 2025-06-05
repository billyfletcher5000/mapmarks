package MapMarks.ui;

import MapMarks.MapMarks;
import MapMarks.utils.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import easel.ui.AbstractWidget;
import easel.ui.AnchorPosition;
import easel.ui.InterpolationSpeed;
import easel.ui.graphics.LayeredTextureWidget;
import easel.utils.EaselInputHelper;

public class DefaultsButton extends AbstractWidget<DefaultsButton> {
    private static final float WIDTH = 36;
    private static final float HEIGHT = 36;


    private static final Color DIM_COLOR = Color.valueOf("aaaaaaff");
    private static final Color HIGHLIGHT_COLOR = Color.valueOf("ffffffff");

    private static final Color SAVE_COLOR = Color.valueOf("e9bc2a");
    private static final Color CLEAR_COLOR = Color.valueOf("da3d3d");

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

        if (!MapMarks.doesRequireShiftToSeeSaveDefaults() || EaselInputHelper.isShiftPressed() && hb.hovered) {
            UIStrings uiStrings = MapMarks.getDefaultsButtonUIStrings();
            switch (_mode) {
                case SAVE:
                    TipHelper.renderGenericTip(1500.0f * Settings.xScale,
                            270.0f * Settings.scale,
                            LocalizationHelper.getDictString(uiStrings, LocalizationConstants.DefaultsButton.tipSaveHeader),
                            LocalizationHelper.getDictString(uiStrings, LocalizationConstants.DefaultsButton.tipSaveBody));
                case CLEAR:
                    TipHelper.renderGenericTip(1500.0f * Settings.xScale,
                            270.0f * Settings.scale,
                            LocalizationHelper.getDictString(uiStrings, LocalizationConstants.DefaultsButton.tipClearHeader),
                            LocalizationHelper.getDictString(uiStrings, LocalizationConstants.DefaultsButton.tipClearBody));
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
        if(!MapMarks.doesRequireShiftToSeeSaveDefaults() || EaselInputHelper.isShiftPressed())
            _ltw.render(sb);
    }
}
