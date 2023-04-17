package model.terrain;

import helpers.Assets;
import model.unit.Unit;
import model.unit.movetype.Armor;
import model.unit.movetype.Cavalry;
import model.unit.movetype.Infantry;

public class Hill extends Tile {

    public Hill(float xScreen, float yScreen, Assets assets) {
        super(xScreen, yScreen, assets.getAssetManager().get("img/tile_img/hill.png"));
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
        if (u.getMoveType() instanceof Infantry || u.getMoveType() instanceof Armor) {
            return 2;
        }
        if (u.getMoveType() instanceof Cavalry) {
            return 3;
        }
        return 0;
    }
}
