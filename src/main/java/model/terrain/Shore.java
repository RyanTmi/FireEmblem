package model.terrain;

import helpers.Assets;
import model.unit.Unit;
import model.unit.movetype.Flying;

public class Shore extends Tile{
    public Shore(float x, float y, Assets assets) {
        super( x, y, assets.getAssetManager().get("img/tile_img/shore.png"));
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
