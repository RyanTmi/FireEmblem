package model.unit;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import controller.GameController;
import helpers.Const;
import model.GameModel;
import model.terrain.Tile;

import java.util.ArrayList;

public class  UnitListener extends ClickListener {

    private final GameModel gameModel;
    private final GameController gameController;

    private Unit selectedUnit;
    private UnitData selectedUnitData;
    private Vector2 locationBeforeDragging;
    private ArrayList<Tile> accessibleTile;

    public UnitListener(final GameModel gameModel, final GameController gameController) {
        this.gameModel = gameModel;
        this.gameController = gameController;
    }

    @Override
    public boolean mouseMoved(InputEvent event, float x, float y) {
        Unit unitTargeted = gameController.getUnitTargeted(x, y);
        if (selectedUnitData != null && selectedUnitData.isSelected()) {
            return false;
        }
        gameController.setHUDLabels(unitTargeted, null, false);
        return true;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        if (selectedUnitData != null && selectedUnitData.isSelected() && keycode == Input.Keys.SPACE) {
            selectedUnitData.setHasPlay(true);
            if (gameModel.allUnitPlay()) {
                gameController.nextRound();
            }
            // gameController.setHUDLabels(null, null, false);
            selectedUnit = null;
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        selectedUnit = gameController.unitTouchDown(x, y);
        if (selectedUnit == null) {
            if (selectedUnitData != null) {
                selectedUnitData.setSelected(false);
                gameController.setHUDLabels(null, null, false);
            }
            return false;
        }
        // Si on clique sur une autre Unit, l'unit précédente n'est plus sélectionner
        if (selectedUnitData != null && gameController.getUnitData(selectedUnit) != selectedUnitData)
        {
            selectedUnitData.setSelected(false);
        }
        selectedUnitData = gameController.getUnitData(selectedUnit);
        gameController.setHUDLabels(selectedUnitData.isSelected() || selectedUnitData.isDragging() ? selectedUnit : null, null, false);
        locationBeforeDragging = new Vector2(
                MathUtils.round((selectedUnitData.getLocation().x + 10f) / Const.TILE_SIZE),
                MathUtils.round((selectedUnitData.getLocation().y + 10f) / Const.TILE_SIZE)
        );
        accessibleTile = gameController.getAccessibleTile(x, y);
        gameModel.getTiledMap().getTile((int)locationBeforeDragging.x, (int)locationBeforeDragging.y).setFilter(Tile.darkBlueTileFilter);

        gameController.playDropSoundEffect();
        return true;
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        gameController.playDropSoundEffect();

        gameController.unitTouchUp(selectedUnitData, accessibleTile, x, y);
        gameController.setTileFilter(accessibleTile, Tile.noTileFilter);
        if (gameModel.allUnitPlay()) {
            gameController.nextRound();
        }
        selectedUnit = null;
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        if(!selectedUnitData.isDragging()) return;
        gameController.unitTouchDrag(selectedUnitData, x, y);
        gameController.setHUDLabels(selectedUnit, gameController.getUnitTargeted(x, y), true);
    }

    // GETTERS
    public UnitData getSelectedUnitData() {
        return selectedUnitData;
    }

    public Vector2 getLocationBeforeDragging() {
        return locationBeforeDragging;
    }

    // SETTERS
    public void setLocationBeforeDragging(Vector2 locationBeforeDragging) {
        this.locationBeforeDragging = locationBeforeDragging;
    }
}
