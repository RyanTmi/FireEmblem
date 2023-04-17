package model.terrain;

import helpers.Assets;
import model.unit.Unit;
import model.unit.movetype.Flying;

public class Field extends Tile{

    public Field(float xScreen, float yScreen, Assets assets) {
        super(xScreen,  yScreen, assets.getAssetManager().get("img/tile_img/field.png"));
    }

    @Override
    public int movementBonus(Unit u) {
        return u.getMoveType() instanceof Flying ? 0 : 1;
    }

    @Override
    public boolean unreachable() {
        return false;
    }

    @Override
    public int getMovementMalus(Unit u) {
        return 0;
    }
}
