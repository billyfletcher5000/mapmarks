package MapMarks;

import MapMarks.ui.*;
import MapMarks.utils.*;
import basemod.BaseMod;
import basemod.ModPanel;
import basemod.interfaces.AddAudioSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.map.LegendItem;
import easel.ui.AnchorPosition;
import easel.utils.EaselInputHelper;
import easel.utils.EaselSoundHelper;
import easel.utils.textures.TextureLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@SpireInitializer
public class MapMarks implements PostInitializeSubscriber, PostUpdateSubscriber, RenderSubscriber, AddAudioSubscriber {
    public static final Logger logger = LogManager.getLogger(MapMarks.class);

    public static final String modId = "ojb_mapmarks";
    public static final String modName = "mapMarks";
    public static final String modDisplayName = "Map Marks";
    public static final String modAuthorName = "ojb, billyfletcher5000";
    public static final String modDescription = "Map Marks is a Slay the Spire mod for map node highlighting.";


    private static String prefixLocalizationID(String localizationID) { return modId + ":" + localizationID; }

    //region Config
    private static final String configFileName = "Config";
    private static final String InitialColorPropertyName = "InitialColor";
    private static final String RoomTypeToColorPropertyName = "RoomTypeToColor";
    private static final String ApplyDefaultsToAct4PropertyName = "ApplyDefaultsToAct4";
    private static final String RequireShiftToSeeSaveDefaults = "RequireShiftToSeeSaveDefaults";
    private static SpireConfig modSpireConfig = null;

    public static void saveModSpireConfig() throws IOException { modSpireConfig.save(); }
    public static boolean hasInitialColorConfigProperty() { return modSpireConfig.has(InitialColorPropertyName); }
    public static boolean hasRoomTypeToColorConfigProperty() { return modSpireConfig.has(RoomTypeToColorPropertyName); }
    public static boolean hasApplyDefaultsToAct4ConfigProperty() { return modSpireConfig.has(ApplyDefaultsToAct4PropertyName); }
    public static boolean hasRequireShiftToSeeDefaultsConfigProperty() { return modSpireConfig.has(RequireShiftToSeeSaveDefaults); }
    public static String getInitialColorConfigProperty() { return modSpireConfig.getString(InitialColorPropertyName); }
    public static String getRoomTypeToColorConfigProperty() { return modSpireConfig.getString(RoomTypeToColorPropertyName); }
    public static boolean getApplyDefaultsToAct4ConfigProperty() { return modSpireConfig.getBool(ApplyDefaultsToAct4PropertyName); }
    public static boolean getRequireShiftToSeeDefaultsConfigProperty() { return modSpireConfig.getBool(RequireShiftToSeeSaveDefaults); }
    public static void setInitialColorConfigProperty(String value) { modSpireConfig.setString(InitialColorPropertyName, value); }
    public static void setRoomTypeToColorConfigProperty(String value) { modSpireConfig.setString(RoomTypeToColorPropertyName, value); }
    public static void setApplyDefaultsToAct4ConfigProperty(boolean value) { modSpireConfig.setBool(ApplyDefaultsToAct4PropertyName, value); }
    public static void setRequireShiftToSeeSaveDefaults(boolean value) { modSpireConfig.setBool(RequireShiftToSeeSaveDefaults, value); }
    public static void removeInitialColorConfigProperty() { modSpireConfig.remove(InitialColorPropertyName); }
    public static void removeRoomTypeToColorConfigProperty() { modSpireConfig.remove(RoomTypeToColorPropertyName); }
    public static void removeApplyDefaultsToAct4ConfigProperty() { modSpireConfig.remove(ApplyDefaultsToAct4PropertyName); }
    public static void removeRequireShiftToSeeDefaultsConfigProperty() { modSpireConfig.remove(RequireShiftToSeeSaveDefaults); }


    public static boolean shouldApplyDefaultsToAct4() {
        return hasApplyDefaultsToAct4ConfigProperty() && getApplyDefaultsToAct4ConfigProperty();
    }

    public static boolean doesRequireShiftToSeeSaveDefaults() {
        return !hasRequireShiftToSeeDefaultsConfigProperty() || getRequireShiftToSeeDefaultsConfigProperty();
    }
    //endregion ~Config

    //region Localization
    public static UIStrings getLegendUIStrings() { return CardCrawlGame.languagePack.getUIString(prefixLocalizationID(LocalizationConstants.Legend.ID)); }
    public static UIStrings getDefaultsButtonUIStrings() { return CardCrawlGame.languagePack.getUIString(prefixLocalizationID(LocalizationConstants.DefaultsButton.ID)); }
    public static UIStrings getConfigUIStrings() { return CardCrawlGame.languagePack.getUIString(prefixLocalizationID(LocalizationConstants.Config.ID)); }
    //endregion ~Localization

    private ModPanel settingsPanel;

    public static void initialize() {
        new MapMarks();

        logger.info("Initializing MapMarks!");
        try {
            logger.info("Creating SpireConfig!");
            modSpireConfig = new SpireConfig(modName, configFileName);
            MapTileManager.loadDefaults();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MapMarks() {
        BaseMod.subscribe(this);
    }

    private RadialMenu menu;
    public static PaintContainer paintContainer;

    public static LegendObject legendObject;

    public static DefaultsButton saveDefaultsButton;
    public static DefaultsButton clearDefaultsButton;

    @Override
    public void receivePostInitialize() {
        TextureLoader.loadTextures(MapMarksTextureDatabase.values());
        initialiseLocalisation();

        menu = new RadialMenu();
        legendObject = new LegendObject()
                .onRightClick(onClick -> {
                    EaselSoundHelper.uiClick2();

                    if (EaselInputHelper.isAltPressed()) {
                        paintContainer.clear();
                    }
                    else {
                        MapTileManager.clearAllHighlights();
                    }
                })
                .anchoredAt(1575, 767, AnchorPosition.CENTER)
        ;

        saveDefaultsButton = new DefaultsButton(DefaultsButtonMode.SAVE)
                .onLeftClick(onClick -> {
                    MapTileManager.saveDefaults();
                })
                .anchoredAt(1760, 812, AnchorPosition.LEFT_TOP);

        clearDefaultsButton = new DefaultsButton(DefaultsButtonMode.CLEAR)
                .onLeftClick(onClick -> {
                    MapTileManager.clearDefaults();
                })
                .anchoredAt(1790, 812, AnchorPosition.LEFT_TOP);

        paintContainer = new PaintContainer();

        // TODO: Make this whole process less insane
        ColorEnum initialColor = MapTileManager.getInitialColor();
        menu.setSelectedColor(initialColor);
        legendObject.setColor(initialColor);
        MapTileManager.setHighlightingColor(initialColor);

        settingsPanel = createModPanel();
        BaseMod.registerModBadge(MapMarksTextureDatabase.MOD_ICON.getTexture(), modDisplayName, modAuthorName, modDescription, settingsPanel);
    }

    private ModPanel createModPanel() {
        ModPanel panel = new ModPanel();

        UIStrings uiStrings = getConfigUIStrings();

        FixedModLabeledToggleButton applyToAct4Btn = new FixedModLabeledToggleButton(
                LocalizationHelper.getDictString(uiStrings, LocalizationConstants.Config.ApplyAct4DefaultsLabel),
                350.0f, 750F,
                Settings.CREAM_COLOR,
                FontHelper.charDescFont,
                shouldApplyDefaultsToAct4(),
                panel,
                (label) -> {},
                (button) -> {
                    setApplyDefaultsToAct4ConfigProperty(button.enabled);
                    try {
                        saveModSpireConfig();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        panel.addUIElement(applyToAct4Btn);

        FixedModLabeledToggleButton requireShiftDefaultsBtn = new FixedModLabeledToggleButton(
                LocalizationHelper.getDictString(uiStrings, LocalizationConstants.Config.RequireShiftToSaveDefaultsLabel),
                350.0f, 700F,
                Settings.CREAM_COLOR,
                FontHelper.charDescFont,
                doesRequireShiftToSeeSaveDefaults(),
                panel,
                (label) -> {},
                (button) -> {
                    setRequireShiftToSeeSaveDefaults(button.enabled);
                    try {
                        saveModSpireConfig();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        panel.addUIElement(requireShiftDefaultsBtn);

        return panel;
    }

    @Override
    public void receiveRender(SpriteBatch sb) {
        menu.render(sb);
    }

    private boolean rightMouseDown = false;
    private int previouslySelectedIndex = -1;

    private enum RightMouseDownMode {
        RADIAL_MENU, HIGHLIGHTING, UNHIGHLIGHTING, NONE;
    }

    private RightMouseDownMode rightMouseDownMode = RightMouseDownMode.NONE;


    @Override
    public void receivePostUpdate() {
        // No updates required if we're not on the map screen
        if (!CardCrawlGame.isInARun() || AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MAP) {
            rightMouseDownMode = RightMouseDownMode.NONE;
            return;
        }

        // Update tile manager
        MapTileManager.updateAllTracked();

        if (EaselInputHelper.isAltPressed()) {
            paintContainer.update();

            // Reset the remaining and quit early, since we don't want to highlight or open the radial in this mode
            rightMouseDownMode = RightMouseDownMode.NONE;
            rightMouseDown = false;

            if (menu.isMenuOpen())
                menu.close();

            return;
        }

        // Transition: Started right clicking
        if (InputHelper.isMouseDown_R && !rightMouseDown) {
            rightMouseDown = true;

            // Control will clear any unreachable from the node under the target
            if (EaselInputHelper.isControlPressed()) {
                if (MapTileManager.isAnyTileHovered()) {
                    MapTileManager.removeHighlightsFromUnreachableNodes();
                    MapTileManager.setHoveredTileHighlightStatus(true);
                    rightMouseDownMode = RightMouseDownMode.NONE;
                }
            }
            else if (MapTileManager.isAnyTileHovered()) {
                // Preexisting highlighted tile under cursor where we start clicking
                if (MapTileManager.hoveredTileIsHighlighted()) {
                    // Check if we're doing a repaint (TODO: config option)
                    if (MapTileManager.isARepaint()) {
                        // start highlighting everything, starting with this hovered unhighlighted node
                        rightMouseDownMode = RightMouseDownMode.HIGHLIGHTING;
                        MapTileManager.setHoveredTileHighlightStatus(true);
                    } else {
                        // start unhighlighting everything, starting with this hovered highlighted node
                        rightMouseDownMode = RightMouseDownMode.UNHIGHLIGHTING;
                        MapTileManager.setHoveredTileHighlightStatus(false);
                    }
                }
                // Tile under cursor exists, but is not highlighted
                else {
                    // start highlighting everything, starting with this hovered unhighlighted node
                    rightMouseDownMode = RightMouseDownMode.HIGHLIGHTING;
                    MapTileManager.setHoveredTileHighlightStatus(true);
                }
            } else {
                boolean okayToOpenRadial = !MapMarks.legendObject.isMouseInContentBounds();

                // Probably overly zealous null checking here for no reason
                if (CardCrawlGame.isInARun() && AbstractDungeon.dungeonMapScreen != null && AbstractDungeon.dungeonMapScreen.map != null && AbstractDungeon.dungeonMapScreen.map.legend != null) {
                    for (LegendItem item : AbstractDungeon.dungeonMapScreen.map.legend.items) {
                        if (item.hb.hovered) {
                            okayToOpenRadial = false;
                            break;
                        }
                    }
                }

                // Not on a node, a legend item, or the legend color display object, so we can open the radial menu
                if (okayToOpenRadial) {
                    SoundHelper.playRadialOpenSound();
                    menu.open();

                    rightMouseDownMode = RightMouseDownMode.RADIAL_MENU;
                }
            }

        }
        // Transition: Stopped right clicking
        else if (!InputHelper.isMouseDown_R && rightMouseDown) {
            rightMouseDown = false;
            rightMouseDownMode = RightMouseDownMode.NONE;

            // Finalize the radial menu
            if (menu.isMenuOpen()) {
                menu.close();
                SoundHelper.playRadialCloseSound();

                // Update the results with the new selection
                int selectedIndex = menu.getSelectedIndex();

                if (selectedIndex != -1 && selectedIndex != previouslySelectedIndex) {
                    ColorEnum newColor = menu.getSelectedColorOrDefault();
                    legendObject.setColor(newColor);

                    MapTileManager.setHighlightingColor(newColor);

                    previouslySelectedIndex = selectedIndex;
                }
            }

        }
        // Currently right mouse is held and we're highlighting
        else if (rightMouseDownMode == RightMouseDownMode.HIGHLIGHTING) {
            MapTileManager.setHoveredTileHighlightStatus(true);
        }
        // Currently right mouse is held and we're unhighlighting
        else if (rightMouseDownMode == RightMouseDownMode.UNHIGHLIGHTING) {
            MapTileManager.setHoveredTileHighlightStatus(false);
        }

        menu.update();
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio("MAP_MARKS_CLICK", "MapMarks/output_2.wav");
    }

    private void initialiseLocalisation() {
        // Fallback
        loadLocalisation("eng");

        String langKey = Settings.language.toString().toLowerCase();
        loadLocalisation(langKey);
    }

    private void loadLocalisation(String langKey) {
        String filepath = "MapMarks/localization/" + langKey + "/UIStrings.json";
        if (Gdx.files.internal(filepath).exists()) {
            BaseMod.loadCustomStringsFile(UIStrings.class, filepath);
        }
    }
}