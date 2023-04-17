package model.terrain;

import helpers.Assets;
import model.unit.Unit;
import model.unit.movetype.Cavalry;
import model.unit.movetype.Infantry;

public class Forest extends Tile {

    public Forest(float xScreen, float yScreen, Assets assets) {
        super(xScreen,  yScreen, assets.getAssetManager().get("img/tile_img/forest.png"));
    }

    @Override
    public int movementBonus(Unit u) {
        return 0;
    }

    @Override
    public boolean unreachable() {
        return false;
    }

    @Override
    public int getMovementMalus(Unit u) {
        if (u.getMoveType() instanceof Cavalry) {
            return 4;
        } else if (u.getMoveType() instanceof Infantry) {
            return 1;
        }
        return 0;
    }
}
