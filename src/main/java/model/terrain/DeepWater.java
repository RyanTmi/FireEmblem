package model.terrain;

import helpers.Assets;
import model.unit.Unit;
import model.unit.movetype.Flying;

public class DeepWater extends Tile {

    public DeepWater(float xScreen, float yScreen, Assets assets) {
        super(xScreen,yScreen, assets.getAssetManager().get("img/tile_img/deep_water.png"));
    }

    @Override
    public int movementBonus(Unit u) {
        return u.getMoveType() instanceof Flying ? 0 : -1;
    }

    @Override
    public int getMovementMalus(Unit u) {
        return u.getMoveType() instanceof Flying ? 0 : 1000;
    }

    @Override
    public boolean unreachable(){
        return true;
    }
}
