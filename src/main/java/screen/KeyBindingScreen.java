package screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import controller.ScreenController;
import helpers.Const;
import helpers.StringHelper;

public class KeyBindingScreen implements Screen {

    private final Main game;

    private final Stage stage;
    private final FillViewport fillViewport;
    private final ScreenController screenController;
    private final TextureRegion textureRegion;

    private Skin skin;
    private SpriteBatch batch;

    public KeyBindingScreen(final Main game) {
        this.game = game;

        this.screenController = game.getScreenController();

        this.fillViewport = new FillViewport(Const.V_WIDTH, Const.V_HEIGHT);

        Texture texture = new Texture(Gdx.files.internal("img/game_n_options_bg.png"));
        this.textureRegion = new TextureRegion(texture, Const.V_WIDTH, Const.V_HEIGHT);

        this.stage = new Stage(fillViewport);
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

    }

    private void initTable() {
        float cw = Const.V_WIDTH * .8f;
        float ch = Const.V_HEIGHT * .8f;

        Table table = new Table(skin);
        table.setPosition((Const.V_WIDTH - cw) / 2.0f, (Const.V_HEIGHT - ch) / 2.0f);
        table.setSize(cw, ch);
        table.setBackground(skin.getDrawable("panel"));

        Label spaceKey = new Label(
                customKeyDescriptionString("Space bar", "Skip unit movement for the current round"),
                skin,
                "big"
        );
        spaceKey.setWrap(true);
        spaceKey.setAlignment(Align.right);
        Label aKey = new Label(
                customKeyDescriptionString("A", "Zoom in"),
                skin,
                "big"
        );
        aKey.setAlignment(Align.right);
        Label sKey = new Label(
                customKeyDescriptionString("S", "Skip team round"),
                skin,
                "big"
        );
        Label cKey = new Label(
                customKeyDescriptionString("C", "Cancel unit movement"),
                skin,
                "big"
        );
        cKey.setAlignment(Align.right);
        Label eKey = new Label(
                customKeyDescriptionString("E", "Zoom out"),
                skin,
                "big"
        );
        Label arrowsKey = new Label(
                customKeyDescriptionString("Arrows keys", "Move the camera"),
                skin,
                "big"
        );

        TextButton backButton = new TextButton("Back", skin, "green-big");
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenController.playClickButtonSound();
                screenController.goToOptionsScreen();
            }
        });

        table.defaults().pad(15).width(500).expandY();
        table.add(eKey);
        table.add(aKey);
        table.row();
        table.add(arrowsKey);
        table.add(spaceKey);
        table.row();
        table.add(sKey);
        table.add(cKey);
        table.row();
        table.add(backButton).colspan(2);

        stage.addActor(table);
    }

    private String customKeyDescriptionString(String title, String description) {
        return StringHelper.orangeString(title) + "\n" + description;
    }
}
