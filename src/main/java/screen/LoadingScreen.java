package screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import controller.ScreenController;
import com.badlogic.gdx.Screen;
import helpers.Assets;
import helpers.Const;

public class LoadingScreen implements Screen {

    private final Main game;

    private final FitViewport fitViewport;
    private final ScreenController screenController;
    private final TextureRegion textureRegion;
    private final Texture texture;
    private final Assets assets;
    private final ShapeRenderer shapeRenderer;

    private SpriteBatch batch;
    private float progress;

    public LoadingScreen(final Main game) {
        this.game = game;
        this.screenController = game.getScreenController();
        this.assets = game.getAssets();
        this.shapeRenderer = new ShapeRenderer();
        this.fitViewport = new FitViewport(Const.V_WIDTH, Const.V_HEIGHT);
        this.texture = assets.getAssetManager().get("img/loading_bg.png", Texture.class);
        this.textureRegion = new TextureRegion(texture, Const.V_WIDTH, Const.V_HEIGHT);
        this.progress = 0f;
    }

    @Override
    public void show() {
        this.batch = game.getBatch();
    }

    @Override
    public void render(float delta) {
        fitViewport.apply();
        batch.setProjectionMatrix(fitViewport.getCamera().combined);
        batch.begin();
        batch.draw(textureRegion, 0, 0);
        batch.end();

        update();

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(width / 6f, height / 2f - 64, 2 * progress * width / 3f, 32);
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height, true);
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
        texture.dispose();
        shapeRenderer.dispose();
    }

    private void update() {
        progress = MathUtils.lerp(progress, assets.getAssetManager().getProgress(), .08f);
        if (assets.getAssetManager().update() && progress > .99998f) {
            screenController.goToMainMenuScreen();
        }
    }
}
