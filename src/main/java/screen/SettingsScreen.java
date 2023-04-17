package screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import controller.ScreenController;
import helpers.Const;

public class SettingsScreen implements Screen {

    private final Main game;
    private final Stage stage;
    private final ShapeRenderer shapeRenderer;
    private final FillViewport fillViewport;
    private final ScreenController screenController;
    private final TextureRegion textureRegion;
    private final Texture texture;
    private final boolean[] toggleSound = {true};
    private final boolean[] toggleFullScreen = {false};

    private final CreditsScreen creditsScreen;
    private final GameRulesScreen gameRulesScreen;
    private final KeyBindingScreen keyBindingScreen;

    private Skin skin;
    private SpriteBatch batch;
    private TextButton finishButton;
    private TextButton soundToggleButton;
    private TextButton fullScreenToggleButton;
    private TextButton creditsButton;
    private TextButton rulesButton;
    private TextButton keyBindingButton;

    public SettingsScreen(final Main game) {
        this.game = game;
        this.screenController = game.getScreenController();

        this.creditsScreen = new CreditsScreen(game);
        this.gameRulesScreen = new GameRulesScreen(game);
        this.keyBindingScreen = new KeyBindingScreen(game);

        this.fillViewport = new FillViewport(Const.V_WIDTH, Const.V_HEIGHT);

        this.texture = new Texture(Gdx.files.internal("img/game_n_options_bg.png"));
        this.textureRegion = new TextureRegion(texture, Const.V_WIDTH, Const.V_HEIGHT);

        this.stage = new Stage(fillViewport);
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
        batch = game.getBatch();

        stage.setDebugAll(false);
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        this.skin = game.getSkin();
        this.initTable();
    }

    @Override
    public void render(float delta) {
        fillViewport.apply();
        batch.setProjectionMatrix(fillViewport.getCamera().combined);
        batch.begin();
        batch.draw(textureRegion, 0, 0);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        shapeRenderer.dispose();
        skin.dispose();
        texture.dispose();
        gameRulesScreen.dispose();
        keyBindingScreen.dispose();
        creditsScreen.dispose();
    }

    // PRIVATE
    private void initTable() {
        float cw = Const.V_WIDTH * .8f;
        float ch = Const.V_HEIGHT * .8f;

        Table table = new Table(skin);
        table.setPosition((Const.V_WIDTH - cw) / 2.0f, (Const.V_HEIGHT - ch) / 2.0f);
        table.setSize(cw, ch);
        table.setBackground(skin.getDrawable("panel"));

        initButtons();

        Label topLabel = new Label("Parameters", skin, "big");
        topLabel.setAlignment(Align.center);

        table.defaults().pad(15).padLeft(100).padRight(100).width(350).expandY();
        table.row().pad(50);
        table.add(topLabel).colspan(2);
        table.row();
        table.add(soundToggleButton);
        table.add(fullScreenToggleButton);
        table.row();
        table.add(creditsButton);
        table.add(rulesButton);
        table.row();
        table.add(keyBindingButton);
        table.row().pad(50);
        table.add(finishButton).colspan(2).width(500);

        stage.addActor(table);
    }

    private void initButtons() {
        finishButton = new TextButton("Back", skin, "green-big");
        finishButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenController.playClickButtonSound();
                screenController.goToMainMenuScreen();
            }
        });

        soundToggleButton = new TextButton(toggleSound[0] ? "Sound: ON" : "Sound: OFF", skin, "default-big");
        soundToggleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleSound[0] = !toggleSound[0];
                screenController.toggleMusics(!toggleSound[0]);
                soundToggleButton.setText(toggleSound[0] ? "Sound: ON" : "Sound: OFF");
                screenController.playClickButtonSound();
            }
        });

        fullScreenToggleButton = new TextButton(toggleFullScreen[0] ? "Full screen : ON" : "Full screen : OFF", skin, "default-big");
        fullScreenToggleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleFullScreen[0] = !toggleFullScreen[0];
                fullScreenToggleButton.setText(toggleFullScreen[0] ? "Full screen : ON" : "Full screen : OFF");
                screenController.setWindowMode(!toggleFullScreen[0]);
                screenController.playClickButtonSound();
            }
        });

        creditsButton = new TextButton("Credits", skin, "default-big");
        creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenController.playClickButtonSound();
                screenController.goToCreditsScreen();
            }
        });

        rulesButton = new TextButton("Game rules", skin, "default-big");
        rulesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenController.playClickButtonSound();
                screenController.goToGameRulesScreen();
            }
        });

        keyBindingButton = new TextButton("Key Binding", skin, "default-big");
        keyBindingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenController.playClickButtonSound();
                screenController.goToKeyBindingScreen();
            }
        });
    }

    public CreditsScreen getCreditsScreen() {
        return creditsScreen;
    }

    public GameRulesScreen getGameRulesScreen() {
        return gameRulesScreen;
    }

    public KeyBindingScreen getKeyBindingScreen() {
        return keyBindingScreen;
    }
}
