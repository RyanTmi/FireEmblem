package model.terrain;

import helpers.Assets;
import model.unit.Unit;

public class Void extends Tile {
    public Void(float xScreen, float yScreen, Assets assets) {
        super(xScreen,  yScreen, assets.getAssetManager().get("img/tile_img/void.png"));
    }

    @Override
    public int movementBonus(Unit u) {
        return -1;
    }

    @Override
    public int getMovementMalus(Unit u) {
        return 100000;
    }

    @Override
    public boolean unreachable() {
        return true;
    }
}
