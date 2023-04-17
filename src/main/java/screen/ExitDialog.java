package screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ExitDialog extends Dialog {

    public ExitDialog(final Main game, Skin skin, InputMultiplexer multiplexer) {
        super("", skin);


        defaults().pad(15);
        TextButton exitGameButton = new TextButton("Comfirm exit", skin, "red");
        exitGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getScreenController().playClickButtonSound();
                game.getScreenController().goToMainMenuScreen();
                game.getGameController().resetPreview();
            }
        });
        TextButton backButton = new TextButton("Back", skin, "default");
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getScreenController().playClickButtonSound();
                Gdx.input.setInputProcessor(multiplexer);
                hide();
            }
        });

        row();
        add(new Label("Are you sure you want to leave?", skin, "default")).colspan(2);
        row();
        add(new Label("The game will be erased", skin, "default")).colspan(2);
        row();
        add(backButton).fillX();
        add(exitGameButton).fillX();
    }

    @Override
    protected void result(Object object) {

    }
}
