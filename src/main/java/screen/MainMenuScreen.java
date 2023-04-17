package screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import controller.ScreenController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import helpers.Const;
import helpers.SoundManager;

public class MainMenuScreen implements Screen {

    private final Main game;

    private final Stage stage;
    private final FillViewport fillViewport;
    private final ScreenController screenController;
    private final ShapeRenderer shapeRenderer;
    private final TextureRegion textureRegion;
    private final Texture texture;
    private final SoundManager soundManager;

    private SpriteBatch batch;
    private Skin skin;

    public MainMenuScreen(final Main game) {
        this.game = game;
        this.screenController = game.getScreenController();

        this.fillViewport = new FillViewport(Const.V_WIDTH, Const.V_HEIGHT);

        this.texture = new Texture(Gdx.files.internal("img/main_menu_bg.png"));
        this.textureRegion = new TextureRegion(texture, Const.V_WIDTH, Const.V_HEIGHT);

        this.stage = new Stage(fillViewport);
        this.shapeRenderer = new ShapeRenderer();

        this.soundManager = game.getSoundManager();
    }

    @Override
    public void show() {
        this.batch = game.getBatch();

        stage.setDebugAll(false);
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        this.skin = game.getSkin();
        this.initButtons();

        this.soundManager.stopGameMusic();
        this.soundManager.playMainMenuMusic();
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
        texture.dispose();
    }

    private void initButtons() {
        Table table = new Table();

        TextButton playButton = new TextButton("Play", skin, "default-big");
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenController.playClickButtonSound();
                screenController.goToGameMenuScreen();
            }
        });

        TextButton optionsButton = new TextButton("Settings", skin, "default-big");
        optionsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                 screenController.playClickButtonSound();
                screenController.goToOptionsScreen();
            }
        });

        TextButton exitButton = new TextButton("Exit", skin, "red-big");
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenController.playClickButtonSound();
                screenController.closeGame();
            }
        });

        table.setBounds(0, Const.V_HEIGHT / 8f, Const.V_WIDTH, Const.V_HEIGHT / 2f);

        table.defaults().pad(10).width(200);
        table.add(playButton);
        table.add(optionsButton);
        table.row();
        table.add(exitButton).colspan(2).width(420);

        stage.addActor(table);
    }
}
