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
import com.badlogic.gdx.utils.viewport.FillViewport;
import controller.GameController;
import controller.ScreenController;
import helpers.Const;
import model.terrain.TiledMap;

import java.util.Arrays;

public class GameMenuScreen implements Screen {

    private final Main game;

    private final Stage stage;
    private final FillViewport fillViewport;
    private final ScreenController screenController;
    private final GameController gameController;

    private Skin skin;
    private SpriteBatch batch;
    private TextureRegion textureRegion;
    private Texture texture;
    private Table leftTable;
    private Table rightTable;

    private TextButton generateNewMap;
    private TextButton playButton;
    private TextButton addPlayer;
    private TextButton[] players;
    private TextButton[] removePlayers;
    private boolean[] playersAI;

    private int nbOfPlayer;
    private static final int MIN_PLAYER = 2;
    private static final int MAX_PLAYER = 4;

    private static final int MIN_MAP_SIZE = 15;
    private static final int MAX_MAP_SIZE = 30;

    private int mapSize;
    private Label mapSizeLabel;
    private TextButton smallerMap;

    private TiledMap previewTiledMap;
    private static final double AMPLITUDE = 0;
    private static final double FREQUENCY = 1.5;

    public GameMenuScreen(final Main game) {
        this.game = game;
        this.screenController = game.getScreenController();
        this.gameController = game.getGameController();
        this.fillViewport = new FillViewport(Const.V_WIDTH, Const.V_HEIGHT);
        this.stage = new Stage(fillViewport);
        this.nbOfPlayer = 2; // minimum nb of players
        this.mapSize  = MIN_MAP_SIZE;

        this.previewTiledMap = new TiledMap(MIN_MAP_SIZE, MIN_MAP_SIZE, AMPLITUDE, FREQUENCY, game.getAssets());
        previewTiledMap.sizeBy(100);
    }

    @Override
    public void show() {
        batch = game.getBatch();

        texture = game.getAssets().getAssetManager().get("img/game_n_options_bg.png", Texture.class);
        textureRegion = new TextureRegion(texture, Const.V_WIDTH, Const.V_HEIGHT);

        stage.setDebugAll(false);
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        this.skin = game.getSkin();

        initLeftTable();
        initRightTable();
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
        texture.dispose();
    }

    private void initLeftTable() {
        leftTable = new Table(skin);
        leftTable.setPosition(0, 0);
        leftTable.setSize(Const.V_WIDTH / 2f, Const.V_HEIGHT);
        leftTable.defaults().pad(25).top().left();

        playButton = new TextButton("Launch game", skin, "green");
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenController.playClickButtonSound();
                try {
                    previewTiledMap.setPreview(false);
                    gameController.setGameObject(nbOfPlayer, previewTiledMap, mapSize, playersAI);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                screenController.goToGameScreen();
            }
        });
        generateNewMap = new TextButton("Generate new map", skin, "default");
        generateNewMap.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenController.playClickButtonSound();
                refreshPreview();
            }
        });

        leftTable.top().left();
        leftTable.add(playButton).pad(getPaddingPreview());
        leftTable.add(generateNewMap).padTop(getPaddingPreview());
        leftTable.row();
        leftTable.add(previewTiledMap);
        stage.addActor(leftTable);
    }

    private void refreshPreview() {
        leftTable.clearChildren();
        previewTiledMap = gameController.generateNewTiledMap(mapSize, mapSize / 10f);
        previewTiledMap.setMapSize(mapSize);
        previewTiledMap.setPreview(true);
        leftTable.add(playButton).pad(getPaddingPreview());
        leftTable.add(generateNewMap).padTop(getPaddingPreview());
        leftTable.row();
        leftTable.add(previewTiledMap);
    }

    private void initRightTable() {
        rightTable = new Table(skin);
        rightTable.setBackground(skin.getDrawable("panel"));
        rightTable.setPosition(Const.V_WIDTH / 2f, 0);
        rightTable.setSize(Const.V_WIDTH / 2f, Const.V_HEIGHT);
        rightTable.defaults().pad(15).top();

        setSizeMapSetting();

        playersAI = new boolean[MAX_PLAYER];
        Arrays.fill(playersAI, false);

        removePlayers = new TextButton[MAX_PLAYER - MIN_PLAYER];
        players = new TextButton[MAX_PLAYER];

        for (int i = 0; i < MAX_PLAYER; i++) {
            players[i] = new TextButton("Player " + (i + 1) + (playersAI[i] ? " : AI" : " : Human"), skin, "default");
            if (i >= 1) {
                int finalI = i;
                players[i].addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        screenController.playClickButtonSound();
                        playersAI[finalI] = !playersAI[finalI];
                        players[finalI].setText("Player " + (finalI + 1) + (playersAI[finalI] ? " : AI" : " : Human"));
                    }
                });
            }

            rightTable.add(players[i]);
            if (i < MIN_PLAYER) {
                rightTable.row();
                continue;
            }
            removePlayers[i - MIN_PLAYER] = new TextButton("X", skin, "red");

            removePlayers[i - MIN_PLAYER].setVisible(false);
            players[i].setVisible(false);

            removePlayers[i - MIN_PLAYER].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    screenController.playClickButtonSound();
                    removePlayers();
                }
            });
            rightTable.add(removePlayers[i - MIN_PLAYER]);
            rightTable.row();
        }


        addPlayer = new TextButton("Add player", skin, "default-big");
        addPlayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenController.playClickButtonSound();
                addPlayer();
            }
        });
        rightTable.add(addPlayer);

        TextButton backButton = new TextButton("Back", skin, "red-big");
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenController.playClickButtonSound();
                screenController.goToMainMenuScreen();
            }
        });

        rightTable.add(backButton);

        stage.addActor(rightTable);
    }

    private void removePlayers() {
        nbOfPlayer--;
        if (nbOfPlayer != MIN_PLAYER) {
            removePlayers[nbOfPlayer - MIN_PLAYER - 1].setVisible(true);
        }
        players[nbOfPlayer].setVisible(false);
        removePlayers[nbOfPlayer - MIN_PLAYER].setVisible(false);
    }

    private void addPlayer() {
        if (nbOfPlayer == MAX_PLAYER) {
            addPlayer.setDisabled(true);
            return;
        }
        if (nbOfPlayer != MIN_PLAYER) {
            removePlayers[nbOfPlayer - MIN_PLAYER - 1].setVisible(false);
        }
        players[nbOfPlayer].setVisible(true);
        removePlayers[nbOfPlayer - MIN_PLAYER].setVisible(true);
        nbOfPlayer++;
    }

    private void setSizeMapSetting() {
        Table topTable = new Table(skin);
        topTable.defaults().pad(20);
        topTable.setBackground(skin.getDrawable("window"));

        Label mapLabel = new Label("Map size", skin, "big");
        Label playerLabel = new Label("Player settings", skin, "big");

        mapSizeLabel = new Label(Integer.toString(mapSize), skin, "big");
        smallerMap = new TextButton("<", skin, "default");
        smallerMap.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenController.playClickButtonSound();
                setSmallerMap();
            }
        });
        TextButton largerMap = new TextButton(">", skin, "default");
        largerMap.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenController.playClickButtonSound();
                setLargerMap();
            }
        });

        rightTable.add(mapLabel).colspan(2);
        rightTable.row();

        topTable.add(smallerMap);
        topTable.add(mapSizeLabel);
        topTable.add(largerMap);

        rightTable.add(topTable).width(rightTable.getWidth() * .9f).colspan(2);
        rightTable.row();
        rightTable.add(playerLabel).colspan(2);
        rightTable.row();
    }

    private void setSmallerMap() {
        if (mapSize == MIN_MAP_SIZE) {
            smallerMap.setDisabled(true);
            return;
        }
        mapSize--;
        mapSizeLabel.setText(Integer.toString(mapSize));
    }

    private void setLargerMap() {
        if (mapSize == MAX_MAP_SIZE) {
            smallerMap.setDisabled(true);
            return;
        }
        mapSize++;
        mapSizeLabel.setText(Integer.toString(mapSize));
    }

    public float getPaddingPreview() {
        return Const.MAP_ORIGIN * Const.TILE_SIZE * previewTiledMap.getTile(0, 0).getScalePreview();
    }
}
