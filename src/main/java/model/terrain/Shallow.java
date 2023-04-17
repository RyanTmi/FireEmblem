package model.terrain;

import helpers.Assets;
import model.unit.Unit;
import model.unit.movetype.Flying;

public class Shallow extends Tile {

    public Shallow(float xScreen, float yScreen, Assets assets) {
        super(xScreen, yScreen, assets.getAssetManager().get("img/tile_img/shallow.png"));
    }

    @Override
    public int movementBonus(Unit u) {
        return u.getMoveType() instanceof Flying ? 0 : -1;
    }

    @Override
    public boolean unreachable(){
        return true;
    }

    @Override
    public int getMovementMalus(Unit u) {
        return u.getMoveType() instanceof Flying ? 0 : 1000;
    }
}
