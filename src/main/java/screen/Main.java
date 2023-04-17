package screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import controller.GameController;
import controller.ScreenController;
import com.badlogic.gdx.Game;
import helpers.Assets;
import helpers.Const;
import helpers.SoundManager;
import model.GameModel;

public class Main extends Game {

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Assets assets;

    private ScreenController screenController;
    private GameController gameController;

    private LoadingScreen loadingScreen;
    private MainMenuScreen mainMenuScreen;
    private GameScreen gameScreen;
    private SettingsScreen settingsScreen;
    private GameMenuScreen gameMenuScreen;

    private Skin skin;
    private SoundManager soundManager;

    private GameModel model;

    @Override
    public void create() {
        Pixmap pm = new Pixmap(Gdx.files.internal("img/cursor.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 2, 2));
        pm.dispose();

        this.assets = new Assets();
        this.skin = assets.getAssetManager().get("ui/uiskin.json", Skin.class);
        this.skin.addRegions(assets.getAssetManager().get("ui/uiskin.atlas", TextureAtlas.class));
        this.skin.getFont("big-font").getData().markupEnabled = true;
        this.skin.getFont("font").getData().markupEnabled = true;

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Const.V_WIDTH, Const.V_HEIGHT);
        this.batch = new SpriteBatch();

        try {
            this.model = new GameModel(this.assets);
        } catch (Exception ignored){

        }


        this.soundManager = new SoundManager(this.assets);

        this.screenController = new ScreenController(this);
        this.gameController = new GameController(this, model);

        this.loadingScreen = new LoadingScreen(this);
        this.gameScreen = new GameScreen(this);
        this.gameMenuScreen = new GameMenuScreen(this);
        this.mainMenuScreen = new MainMenuScreen(this);
        this.settingsScreen = new SettingsScreen(this);

        this.setScreen(loadingScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        soundManager.dispose();
        skin.dispose();
        assets.getAssetManager().dispose();
        mainMenuScreen.dispose();
        gameScreen.dispose();
        gameMenuScreen.dispose();
        loadingScreen.dispose();
    }

    // GETTERS
    public MainMenuScreen getMainMenuScreen() {
        return mainMenuScreen;
    }
    public GameScreen getGameScreen() {
        return gameScreen;
    }
    public SettingsScreen getOptionsScreen() {
        return settingsScreen;
    }
    public GameMenuScreen getGameMenuScreen() {
        return gameMenuScreen;
    }
    public OrthographicCamera getCamera() {
        return camera;
    }
    public SpriteBatch getBatch() {
        return batch;
    }
    public ScreenController getScreenController() {
        return screenController;
    }
    public GameController getGameController() {
        return gameController;
    }
    public GameModel getModel() {
        return model;
    }
    public Skin getSkin() {
        return skin;
    }
    public SoundManager getSoundManager() {
        return soundManager;
    }
    public Assets getAssets() {
        return assets;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }
}
