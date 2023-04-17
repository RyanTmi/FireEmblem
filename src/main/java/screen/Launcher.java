package screen;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import helpers.Const;

public class Launcher {

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Fire Emblem");
        config.setIdleFPS(60);
        config.useVsync(true);
        config.setAutoIconify(true);
        config.setWindowedMode(Const.V_WIDTH, Const.V_HEIGHT);
        config.setWindowSizeLimits(1200, 700, 9999, 9999);
        config.setWindowIcon("img/app_icon.png");

        new Lwjgl3Application(new Main(), config);
    }
}
