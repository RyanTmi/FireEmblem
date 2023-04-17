package screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import controller.ScreenController;
import helpers.Const;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class CreditsScreen implements Screen {

    private static final String creditString;

    private final Main game;
    private final Stage stage;
    private final FillViewport fillViewport;
    private final ScreenController screenController;

    private Skin skin;

    public CreditsScreen(final Main game) {
        this.game = game;
        this.screenController = game.getScreenController();
        this.fillViewport = new FillViewport(Const.V_WIDTH, Const.V_HEIGHT);
        this.stage = new Stage(fillViewport);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        this.skin = game.getSkin();
        this.initCreditPanel();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            screenController.goToMainMenuScreen();
        }

        fillViewport.apply();

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
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    private void initCreditPanel() {
        Label creditLabel = new Label(creditString, skin, "default");
        creditLabel.setAlignment(Align.center);
        creditLabel.setBounds(0, -.8f * creditLabel.getHeight(), Const.V_WIDTH, Const.V_HEIGHT);
        creditLabel.addAction(sequence(
                moveTo(0, 1.8f * creditLabel.getHeight(), 12f),
                delay(1f),
                run(screenController::goToMainMenuScreen)
        ));

        stage.addActor(creditLabel);
    }

    static {
        creditString =
                "FIRE EMBLEM\n\n" +

                "DEVELOPERS\n\n" +
                "BORDAS  Florent\n" +
                "CROCOMBETTE--PASQUET  Mathieu\n" +
                "HUYNH  Phi Vu-Charles\n" +
                "SEJOURNE  Maxime\n" +
                "TIMEUS  Ryan\n" +

                "\nASSETS USED\n\n" +

                "\n-- GRAPHICS --\n\n" +
                "FIRE EMBLEM HUB IMAGE\n" +
                "BY NINTENDO\n\n" +
                "FIRE EMBLEM AWAKENING\n" +
                "BY KAZ_KIRIGIRI\n\n" +
                "GAME IMAGES\n" +
                "BY NINTENDO\n\n" +
                "UI\n" +
                "BY TIMEUS RYAN\n\n" +

                "\n-- AUDIO --\n\n" +
                "FLESHY FIGHT SOUNDS\n" +
                "BY WILL_LEAMON\n\n" +
                "DROP SOUND\n" +
                "BY FREQMAN\n\n" +
                "BUTTON CLICK SOUND\n" +
                "FROM MIXKIT\n\n" +
                "THEME MUSICS\n" +
                "BY J.T. PETERSON\n\n" +

                "\n-- FONT --\n\n" +
                "BY TIMEUS RYAN\n\n" +

                "\n-- SPECIAL THANKS --\n\n" +
                "ROMAN-CALVO  Enrique\n" +
                "\nProjet de programmation\n2021/2022"
        ;
    }
}
