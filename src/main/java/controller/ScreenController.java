package controller;

import helpers.Const;
import model.terrain.TiledMap;
import screen.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class ScreenController {

    private final Main game;
    private float initZoom;

    public ScreenController(final Main game) {
        this.game = game;
        this.initZoom = 1f;
    }

    public void goToMainMenuScreen() {
        game.setScreen(game.getMainMenuScreen());
    }

    public void goToGameScreen() {
        game.setScreen(game.getGameScreen());
    }

    public void goToOptionsScreen() {
        game.setScreen(game.getOptionsScreen());
    }

    public void goToGameMenuScreen() {
        game.setScreen(game.getGameMenuScreen());
    }

    public void goToCreditsScreen() {
        game.setScreen(game.getOptionsScreen().getCreditsScreen());
    }

    public void goToGameRulesScreen() {
        game.setScreen(game.getOptionsScreen().getGameRulesScreen());
    }

    public void goToKeyBindingScreen() {
        game.setScreen(game.getOptionsScreen().getKeyBindingScreen());
    }

    public void closeGame() {
        Gdx.app.exit();
        System.exit(0);
    }


    public void cameraMovementInput(TiledMap tiledMap) {
        OrthographicCamera camera = game.getCamera();

        boolean bottom = camera.position.y < Gdx.graphics.getHeight() / 2f * camera.zoom + Const.TILE_SIZE * (Const.MAP_ORIGIN + tiledMap.getMapHeight() - 1);
        boolean left = camera.position.x < Gdx.graphics.getWidth() / 2f * camera.zoom + Const.TILE_SIZE * (Const.MAP_ORIGIN + tiledMap.getMapWidth() - 1);
        boolean top = -camera.position.y < Gdx.graphics.getHeight() / 2f * camera.zoom - Const.TILE_SIZE * (Const.MAP_ORIGIN + 1);
        boolean right = -camera.position.x < Gdx.graphics.getWidth() / 2f * camera.zoom - Const.TILE_SIZE * 3f * (Const.MAP_ORIGIN - .02f) - 3f * Gdx.graphics.getWidth() / 4f;

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && top) {
            camera.position.y -= Const.CAMERA_MOVEMENT_SPEED;
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP) && bottom) {
            camera.position.y += Const.CAMERA_MOVEMENT_SPEED;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && right) {
            camera.position.x -= Const.CAMERA_MOVEMENT_SPEED;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && left) {
            camera.position.x += Const.CAMERA_MOVEMENT_SPEED;
        }
    }

    public void cameraZoomInput() {
        OrthographicCamera camera = game.getCamera();

        float maxZoom = initZoom;
        float minZoom = initZoom - Const.MAX_ZOOM_INTERVAL;

        if(Gdx.input.isKeyPressed(Input.Keys.E) && camera.zoom <= maxZoom) {
            camera.zoom += Const.CAMERA_ZOOM_SPEED;
        } else if(Gdx.input.isKeyPressed(Input.Keys.Q) && camera.zoom >= minZoom) {
            camera.zoom -= Const.CAMERA_ZOOM_SPEED;
        }
    }

    public void initCameraZoom(TiledMap tiledMap) {
        float zoom = (tiledMap.getMapWidth() + tiledMap.getMapHeight()) / 2f / Const.TILE_SIZE * 20;
        game.getCamera().zoom = zoom;
        this.initZoom = zoom;
    }

    /**
     * Centre le plateau par rapport à l'écran
     */
    public void initCameraPosition(TiledMap tiledMap) {
        game.getCamera().position.set(
                Const.TILE_SIZE * (Const.MAP_ORIGIN +  4 * tiledMap.getMapWidth() / 5f),
                Const.TILE_SIZE * (Const.MAP_ORIGIN + tiledMap.getMapHeight() / 2f),
                0
        );
    }

    public void setWindowMode(boolean fullscreen) {
        if (fullscreen) {
            Gdx.graphics.setWindowedMode(Const.V_WIDTH, Const.V_HEIGHT);
        } else {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
    }

    public void toggleMusics(boolean mute) {
        if (mute) {
            game.getSoundManager().MuteAllMusics();
            return;
        }
        game.getSoundManager().resetAllMusicsVolume();
    }

    public void playClickButtonSound() {
        game.getSoundManager().playButtonClickSound();
    }
}
