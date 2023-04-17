package screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import controller.ScreenController;
import helpers.Const;

public class GameRulesScreen implements Screen {

    private final Main game;

    private final Stage stage;
    private final ShapeRenderer shapeRenderer;
    private final FillViewport fillViewport;
    private final ScreenController screenController;
    private final TextureRegion textureRegion;
    private final Texture texture;
    private final Skin skin;
    private final Label rulesLabel;

    private ScrollPane scrollPane;
    private SpriteBatch batch;

    public GameRulesScreen(final Main game) {
        this.game = game;

        this.screenController = game.getScreenController();

        this.fillViewport = new FillViewport(Const.V_WIDTH, Const.V_HEIGHT);

        this.texture = new Texture(Gdx.files.internal("img/game_n_options_bg.png"));
        this.textureRegion = new TextureRegion(texture, Const.V_WIDTH, Const.V_HEIGHT);

        this.stage = new Stage(fillViewport);
        this.shapeRenderer = new ShapeRenderer();
        this.skin = game.getSkin();

        FileHandle fileHandle = Gdx.files.internal("rules_en.txt");
        String rulesString = fileHandle.readString();
        this.rulesLabel = new Label(rulesString, skin, "default");
    }

    @Override
    public void show() {
        batch = game.getBatch();

        Gdx.input.setInputProcessor(stage);
        stage.clear();

        this.initPanel();

        stage.setScrollFocus(scrollPane);
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
    }

    private void initPanel() {
        float cw = Const.V_WIDTH * .9f;
        float ch = Const.V_HEIGHT * .9f;

        Table root = new Table(skin);
        root.setPosition((Const.V_WIDTH - cw) / 2.0f, (Const.V_HEIGHT - ch) / 2.0f);
        root.setSize(cw, ch);
        root.setBackground(skin.getDrawable("panel"));
        root.defaults().pad(15);

        TextButton backButton = new TextButton("Back", skin, "green-big");
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenController.playClickButtonSound();
                screenController.goToOptionsScreen();
            }
        });

        Label title = new Label("Game Rules", skin, "big");
        title.setAlignment(Align.center);

        Table labelsTable = new Table();
        labelsTable.add(title);
        labelsTable.row();
        labelsTable.add(rulesLabel);
        labelsTable.row();
        labelsTable.add(backButton).width(500);

        scrollPane = new ScrollPane(labelsTable, skin);

        root.add(scrollPane);
        stage.addActor(root);
    }
}
