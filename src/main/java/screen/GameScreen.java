package screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import controller.GameController;
import controller.ScreenController;
import helpers.SoundManager;
import model.GameModel;
import helpers.Const;
import model.unit.UnitListener;

public class GameScreen implements Screen {

    private final Main game;

    private final OrthographicCamera camera;
    private final Stage stage;
    private final Stage dialogStage;
    private final Stage HUDstage;
    private final SpriteBatch batch;

    private final ExtendViewport extendViewport;
    private final ExtendViewport extendViewportDialog;
    private final ScreenViewport HUDViewport;
    private final FillViewport backgroundFillViewport;
    private final TextureRegion textureRegion;
    private final Texture texture;
    private final InputMultiplexer multiplexer;

    private final ScreenController screenController;
    private final GameController gameController;
    private final PanelHUD panelHUD;
    private final Skin skin;
    private final SoundManager soundManager;

    private ExitDialog exitDialog;

    // Game Object
    private final GameModel gameModel;

    // Unit Listener Object
    private UnitListener unitListener;

    public GameScreen(final Main game) {
        this.game = game;
        this.gameModel = game.getModel();

        this.gameController = game.getGameController();
        this.screenController = game.getScreenController();
        this.camera = game.getCamera();
        this.soundManager = game.getSoundManager();

        // Viewport pour le plateau
        this.extendViewport = new ExtendViewport(Const.V_WIDTH, Const.V_HEIGHT, camera);
        this.extendViewportDialog = new ExtendViewport(Const.V_WIDTH, Const.V_HEIGHT);
        // Viewport pour le background
        this.backgroundFillViewport = new FillViewport(Const.V_WIDTH, Const.V_HEIGHT);
        // Viewport pour le HUD
        this.HUDViewport = new ScreenViewport();

        this.stage = new Stage(extendViewport);
        this.dialogStage = new Stage();
        this.HUDstage = new Stage(HUDViewport);
        this.batch = game.getBatch();

        this.skin = game.getSkin();
        this.panelHUD = new PanelHUD(game);

        this.texture = game.getAssets().getAssetManager().get("img/game_n_options_bg.png", Texture.class);
        this.textureRegion = new TextureRegion(texture, Const.V_WIDTH, Const.V_HEIGHT);

        this.multiplexer = new InputMultiplexer();
    }

    @Override
    public void show() {
        multiplexer.addProcessor(gameModel.getUnitTeam(0).getStage());
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(HUDstage);
        multiplexer.addProcessor(dialogStage);

        // Camera
        screenController.initCameraZoom(gameModel.getTiledMap());
        screenController.initCameraPosition(gameModel.getTiledMap());

        // Stage
        stage.clear();
        HUDstage.clear();
        dialogStage.clear();

        // Actors
        stage.addActor(gameModel);
        HUDstage.addActor(panelHUD);

        Gdx.input.setInputProcessor(multiplexer);

        soundManager.stopMainMenuMusic();
        soundManager.playGameMusic();

        exitDialog = new ExitDialog(game, skin, multiplexer);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            gameController.nextRound();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            gameController.cancelUnitMovement();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            showExitDialog();
        }

        // Update camera
        camera.update();
        screenController.cameraMovementInput(gameModel.getTiledMap());
        screenController.cameraZoomInput();

        // Background
        backgroundFillViewport.apply();
        batch.setProjectionMatrix(backgroundFillViewport.getCamera().combined);
        batch.begin();
        batch.draw(textureRegion, 0, 0);
        batch.end();

        // Board
        extendViewport.apply();
        stage.act(delta);
        stage.draw();

        // HUD
        HUDViewport.apply();
        HUDstage.addActor(panelHUD);
        panelHUD.update();
        HUDstage.act(delta);
        HUDstage.draw();

        // Dialog
        extendViewportDialog.apply();
        dialogStage.act(delta);
        dialogStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        extendViewport.update(width, height, false);
        extendViewportDialog.update(width, height, false);
        HUDViewport.update(width, height, true);
        backgroundFillViewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        dialogStage.dispose();
        HUDstage.dispose();
        panelHUD.getSkin().dispose();
        panelHUD.getCurrentTeamColorPixmap().dispose();
        texture.dispose();
        skin.dispose();
    }

    public void initUnitListener() {
        unitListener = new UnitListener(gameModel, gameController);
        gameModel.getCurrentTeam().getStage().addListener(unitListener);
    }

    // GETTERS
    public ExtendViewport getExtendViewport() {
        return extendViewport;
    }

    public PanelHUD getPanelHUD() {
        return panelHUD;
    }

    public UnitListener getUnitListener() {
        return unitListener;
    }

    public void showGameOverDialog(int winner) {
        Gdx.input.setInputProcessor(dialogStage);
        GameOverDialog gameOverDialog = new GameOverDialog(game, skin, winner);
        gameOverDialog.show(dialogStage);
        gameOverDialog.setWidth(Gdx.graphics.getWidth() / 4f);
        gameOverDialog.setHeight(Gdx.graphics.getHeight() / 4f);
    }

    public void showExitDialog() {
        Gdx.input.setInputProcessor(dialogStage);
        exitDialog.show(dialogStage);
        exitDialog.setWidth(Gdx.graphics.getWidth() / 4f);
        exitDialog.setHeight(Gdx.graphics.getHeight() / 3f);
    }
}
