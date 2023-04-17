package screen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import helpers.StringHelper;

public class GameOverDialog extends Dialog {

    public GameOverDialog(final Main game, Skin skin, int winner) {
        super("", skin);

        defaults().pad(15);
        TextButton exitGameButton = new TextButton("Comfirm exit", skin, "red");
        exitGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getScreenController().playClickButtonSound();
                game.getScreenController().goToMainMenuScreen();
            }
        });

        row();
        add(new Label("Player " + getWinnerString(winner) + " won!", skin, "default")).colspan(2);
        row();
        add(exitGameButton).fillX();
    }

    private String getWinnerString(int winner) {
        switch (winner) {
            case 0:
                return StringHelper.redString("RED");
            case 1:
                return StringHelper.blueString("BLUE");
            case 2:
                return StringHelper.greenString("GREEN");
            case 3:
                return StringHelper.violetString("VIOLET");
        }
        return "";
    }
}
