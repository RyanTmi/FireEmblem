package controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import helpers.Const;
import model.GameModel;
import model.terrain.Tile;
import model.terrain.Tile.TileFilter;
import model.terrain.TiledMap;
import model.unit.Unit;
import model.unit.UnitData;
import model.unit.assist.*;
import screen.GameScreen;
import screen.Main;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class GameController {

    private final Main game;
    private final GameModel gameModel;

    public GameController(Main game, GameModel model) {
        this.game = game;
        this.gameModel = model;
    }

    public void setHUDLabels(Unit selectedUnit, Unit targeted, boolean other) {
        if (selectedUnit != null && targeted != null && (selectedUnit.getTeamNumber() != targeted.getTeamNumber())) {
            game.getGameScreen().getPanelHUD().setColorNameLabel(selectedUnit, other);
            game.getGameScreen().getPanelHUD().setColorNameLabel(targeted, other);
            game.getGameScreen().getPanelHUD().setCombatPreviewLabels(gameModel.getFight(), selectedUnit, targeted);
            game.getGameScreen().getPanelHUD().setVisibleLabels(true, other);
            return;
            //code qui affiche le preview pour le heal
        }
        if (selectedUnit != null && targeted != null && (selectedUnit.getTeamNumber() == targeted.getTeamNumber()) && selectedUnit.hasAssist() && selectedUnit.getAssist() instanceof HealAssist && selectedUnit.getAssist().canActivateSkill()) {
        	game.getGameScreen().getPanelHUD().setColorNameLabel(selectedUnit, other);
            game.getGameScreen().getPanelHUD().setColorNameLabel(targeted, other);
            game.getGameScreen().getPanelHUD().setCombatPreviewLabels(gameModel.getFight(), selectedUnit, targeted, false);
            game.getGameScreen().getPanelHUD().setVisibleLabels(true, other);
            return;
            //fin code qui affiche le preview pour le heal
        }
        if (selectedUnit != null) {
            game.getGameScreen().getPanelHUD().setColorNameLabel(selectedUnit, false);
            game.getGameScreen().getPanelHUD().setUnitLabels(selectedUnit, false);
            game.getGameScreen().getPanelHUD().setVisibleLabels(true, false);
            return;
        }
        game.getGameScreen().getPanelHUD().setVisibleLabels(false, true);
        game.getGameScreen().getPanelHUD().setVisibleLabels(false, false);
    }


    /**
     * Méthode appelée lorsqu'on clique sur l'écran
     * @param x coordonnées X de l'écran
     * @param y coordonnées Y de l'écran
     * @return l'unité correspondante aux coordonnées x et y de l'écran
     */
    public Unit unitTouchDown(float x, float y) {
        Vector2 location = getRelativeCoordinateToMapOrigin(x, y, true);
        for (Map.Entry<Unit, UnitData> entry : gameModel.getCurrentTeam().getUnitList().entrySet()) {
            if (location.equals(entry.getValue().getLocation()) && !entry.getValue().hasPlay()) {
                entry.getValue().setDragging(true);
                entry.getValue().setSelected(!entry.getValue().isSelected());
                entry.getValue().setLocation(getRelativeCoordinateToMapOrigin(x, y, false));
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Déplace unit sur la TiledMap
     * @param unitData data de l'unité qui se déplace
     * @param x coordonnées X de l'écran
     * @param y coordonnées Y de l'écran
     */
    public void unitTouchDrag(UnitData unitData, float x, float y) {
        unitData.setLocation(getRelativeCoordinateToMapOrigin(x, y, false));
    }

    public void unitTouchUp(UnitData unitData, ArrayList<Tile> accessibleTile, float x, float y) {
        Vector2 locationBeforeDragging =  game.getGameScreen().getUnitListener().getLocationBeforeDragging();
        Vector2 location = getRelativeCoordinateToMapOrigin(x, y, true);
        unitData.setDragging(false);
        if (isInMapArea(location) && isInRange(location, accessibleTile) && !locationBeforeDragging.equals(location)) {
            unitData.setLocation(location);
            unitData.setHasPlay(true);
            unitData.setSelected(false);
            return;
        }

        // Code qui gere les attaques des différentes unites
        if (isInMapArea(location)) {
        	Set<Unit> tmp = gameModel.getCurrentTeam().getUnitList().keySet();
        	Unit u = null;
        	for (Unit unit : tmp) {
        		if (gameModel.getCurrentTeam().getUnitList().get(unit) == unitData) {
        			u = unit;
        			break;
        		}
        	}
        	if (u == null) {
        		unitData.setLocation(locationBeforeDragging);
        		return;
        	}
            Vector2 newLocation = gameModel.searchTileNearest(u, locationBeforeDragging, location, accessibleTile);
        	if (newLocation != null) {
        		Unit enemy = gameModel.searchEnemyAtPos(u, location);
        		if (enemy != null) {
                    unitData.setLocation(newLocation);

                    game.getSoundManager().playSwordSoundEffect();
                    gameModel.getFight().fight(u, enemy);
                    gameModel.disableTeam();
                    int winner = gameModel.winner();
                    if (winner != -1) {
                        game.getGameScreen().showGameOverDialog(winner);
                        return;
                    }

                    unitData.setHasPlay(true);
                    setHUDLabels(null, null, true);
                    game.getGameScreen().getUnitListener().setLocationBeforeDragging(null);
                    return;
        		} else {

        			// Code pour les assists
        			newLocation = gameModel.searchTileNearest(u, locationBeforeDragging, location, accessibleTile, false);
        			Unit ally = gameModel.searchAllyAtPos(u, location);
        			if (newLocation != null && ally != null && u.hasAssist() && u.getAssist().canActivateSkill()) {
        				if (u.getAssist() instanceof HealAssist && gameModel.getFight().healPreview(u, ally)[0] > ally.getHp()) {
        					gameModel.getFight().heal(u, ally);
        					unitData.setLocation(newLocation);
            				unitData.setHasPlay(true);
                            setHUDLabels(null, null, true);
            				game.getGameScreen().getUnitListener().setLocationBeforeDragging(null);
            				return;
        				}
        			}
        		}
        	}
        }
        //fin du code
        if (!locationBeforeDragging.equals(location)) {
            unitData.setSelected(true);
        }
        unitData.setLocation(locationBeforeDragging);
    }

    public UnitData getUnitData(Unit unit) {
        return gameModel.getCurrentTeam().getUnitList().get(unit);
    }

    public void setTileFilter(ArrayList<Tile> accessibleTile, TileFilter tileFilter) {
        for (Tile tile : accessibleTile) {
            tile.setFilter(tileFilter);
        }
    }

    public ArrayList<Tile> getAccessibleTile(float x, float y) {
        Vector2 location = getRelativeCoordinateToMapOrigin(x, y, true);
        Vector2 unitLocation;
        for (Map.Entry<Unit, UnitData> entry : gameModel.getCurrentTeam().getUnitList().entrySet()) {
            unitLocation = new Vector2(
                    MathUtils.round((entry.getValue().getLocation().x + 10f) / Const.TILE_SIZE),
                    MathUtils.round((entry.getValue().getLocation().y + 10f) / Const.TILE_SIZE)
            );
            if (location.equals(unitLocation)) {
                return gameModel.getMovementAvailable(entry.getKey(), location, gameModel.getCurrentTeam().isAI());
            }
        }
        return new ArrayList<>();
    }

    public void nextRound() {
        gameModel.resetUnitMovement();
        gameModel.nextRound();
        game.getGameScreen().getPanelHUD().setCurrentTeamColorPixmap(gameModel.getCurrentTeamColor());
        game.getGameScreen().getUnitListener().setLocationBeforeDragging(null);
    }


    public void cancelUnitMovement() {
        UnitData unitData = game.getGameScreen().getUnitListener().getSelectedUnitData();
        if (unitData == null || game.getGameScreen().getUnitListener().getLocationBeforeDragging() == null) {
            return;
        }
        unitData.setHasPlay(false);
        unitData.setLocation(game.getGameScreen().getUnitListener().getLocationBeforeDragging());
    }

    // PRIVATE

    /**
     * Récupère les coordonnées relatives à la TileMap d'une unité (coordonnées de la case en bas à gauche = (0, 0))
     * @param x coordonnées X de l'écran
     * @param y coordonnées Y de l'écran
     * @param fitTile true : retourne la position de l'unit en coordonnées entières sur la TileMap.
     *                false : retourne la position de l'unit en float sur la TileMap.
     * @return coordonnées sur la TileMap selon fitTile
     */
    private Vector2 getRelativeCoordinateToMapOrigin(float x, float y, boolean fitTile) {
        ExtendViewport viewport = game.getGameScreen().getExtendViewport();
        OrthographicCamera camera = game.getCamera();

        // TODO gérer le resize de la fenêtre
        int xDist = (int) ((Gdx.graphics.getWidth() * camera.zoom / 2) - viewport.getCamera().position.x);
        int yDist = (int) ((Gdx.graphics.getHeight() * camera.zoom / 2) - viewport.getCamera().position.y);

        int xBoardCoordinate = (int) (camera.zoom * x - xDist);
        int yBoardCoordinate = (int) (camera.zoom * y - yDist);

        if (fitTile) {
            return new Vector2(MathUtils.floor((float)xBoardCoordinate/ Const.TILE_SIZE), MathUtils.floor((float)yBoardCoordinate / Const.TILE_SIZE));
        }
        return new Vector2(xBoardCoordinate - Const.TILE_SIZE / 2f * 1.15f, yBoardCoordinate - Const.TILE_SIZE / 2f * 1.15f);
    }

    public Unit getUnitTargeted(float x, float y){
        return gameModel.getUnitTargeted(getRelativeCoordinateToMapOrigin(x, y,true));
    }


    public TiledMap generateNewTiledMap(int mapSize, double frequency) {
        return gameModel.generateNewMap(mapSize, frequency);
    }

    public void resetPreview() {
        gameModel.reset();
        game.setGameScreen(new GameScreen(game));
    }

    public void setGameObject(int nbOfPlayer, TiledMap tiledMap, int mapSize, boolean[] playerAI) throws Exception {
        gameModel.setGameObject(nbOfPlayer, tiledMap, mapSize, playerAI);
        game.getGameScreen().initUnitListener();
    }

    // PRIVATE
    private boolean isInMapArea(Vector2 locationCoordinate) {
        return gameModel.getTiledMap().getMapWidth() > locationCoordinate.x - Const.MAP_ORIGIN &&
                gameModel.getTiledMap().getMapHeight() > locationCoordinate.y - Const.MAP_ORIGIN &&
                Const.MAP_ORIGIN <= locationCoordinate.x &&
                Const.MAP_ORIGIN <= locationCoordinate.y;
    }

    /**
     * Regarde si l'unit peut se déplacer sur la case d'arrivée
     * @param locationCoordinate case d'arrivée
     * @param accessibleTile ensemble des cases accessible
     * @return true si la case d'arrivée est accessible et n'est pas occupé par un joueur
     */
    private boolean isInRange(Vector2 locationCoordinate, ArrayList<Tile> accessibleTile) {
        for (Tile tile : accessibleTile) {
            if (
                    gameModel.getTiledMap().getTile((int)locationCoordinate.x, (int)locationCoordinate.y) == tile &&
                            (tile.getTileFilter() == Tile.blueTileFilter || tile.getTileFilter() == Tile.dangerTileFilter)
            ) {
                return true;
            }
        }
        return false;
    }

    // SOUND EFFECT
    public void playDropSoundEffect() {
        game.getSoundManager().playDropSoundEffect();
    }
}
