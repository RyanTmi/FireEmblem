package model.unit;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.math.Vector2;
import helpers.Assets;
import helpers.Const;
import model.terrain.Tile;
import model.terrain.TiledMap;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

public class UnitList extends Actor {

    private final HashMap<Unit, UnitData> unitList;
    private final boolean isAI;

    public UnitList(int teamNumber, boolean isAI, TiledMap tiledMap, Assets assets) throws Exception {
        this.unitList = new HashMap<>();
        this.isAI = isAI;

        Vector2 tmp1 = new Vector2();
        Vector2 tmp2 = new Vector2();
        LinkedList<Tile> tileList;
        Tile tileTmp;

        switch(teamNumber) {
            case 0:
                tmp1.x = 0;
                tmp1.y = 0;
                tmp2.x = 4;
                tmp2.y = 4;

                tileList = tiledMap.getPlacementsAvailable(tmp1, tmp2);
                if(tileList.size() < 4){
                    throw new Exception();
                }

                tileTmp = tileList.poll();
                addUnit(UnitGenerator.generateMist(assets,0), new Vector2(tileTmp.getMapX(), tileTmp.getMapY()));
                tileTmp = tileList.poll();
                addUnit(UnitGenerator.generateSpearCavalry(assets,0), new Vector2(tileTmp.getMapX(), tileTmp.getMapY()));
                tileTmp = tileList.poll();
                addUnit(UnitGenerator.generateCatria(assets,0), new Vector2(tileTmp.getMapX(), tileTmp.getMapY()));
                tileTmp = tileList.poll();
                addUnit(UnitGenerator.generateHector(assets,0), new Vector2(tileTmp.getMapX(), tileTmp.getMapY()));
                tileList.clear();
                break;

            case 1:
                tmp1.x = 0;
                tmp1.y = tiledMap.getMapHeight() - 4;
                tmp2.x = 4;
                tmp2.y = tiledMap.getMapHeight();

                tileList = tiledMap.getPlacementsAvailable(tmp1, tmp2);
                if(tileList.size() < 4){
                    throw new Exception();
                }

                tileTmp = tileList.poll();
                addUnit(UnitGenerator.generateBowInfantry(assets, 1), new Vector2(tileTmp.getMapX(), tileTmp.getMapY()));
                tileTmp = tileList.poll();
                addUnit(UnitGenerator.generateSpearCavalry(assets, 1), new Vector2(tileTmp.getMapX(), tileTmp.getMapY()));
                tileTmp = tileList.poll();
                addUnit(UnitGenerator.generateCatria(assets, 1), new Vector2(tileTmp.getMapX(), tileTmp.getMapY()));
                tileTmp = tileList.poll();
                addUnit(UnitGenerator.generateHector(assets, 1), new Vector2(tileTmp.getMapX(), tileTmp.getMapY()));
                tileList.clear();
                break;

            case 2:
                tmp1.x = tiledMap.getMapWidth() - 4;
                tmp1.y = tiledMap.getMapHeight() - 4;
                tmp2.x = tiledMap.getMapWidth();
                tmp2.y = tiledMap.getMapHeight();

                tileList = tiledMap.getPlacementsAvailable(tmp1, tmp2);
                if (tileList.size() < 4) {
                    throw  new Exception();
                }

                tileTmp = tileList.poll();
                addUnit(UnitGenerator.generateSwordInfantry(assets, 2), new Vector2(tileTmp.getMapX(), tileTmp.getMapY()));
                tileTmp = tileList.poll();
                addUnit(UnitGenerator.generateSpearCavalry(assets, 2), new Vector2(tileTmp.getMapX(), tileTmp.getMapY()));
                tileTmp = tileList.poll();
                addUnit(UnitGenerator.generateCatria(assets, 2), new Vector2(tileTmp.getMapX(), tileTmp.getMapY()));
                tileTmp = tileList.poll();
                addUnit(UnitGenerator.generateHector(assets, 2), new Vector2(tileTmp.getMapX(), tileTmp.getMapY()));
                tileList.clear();
                break;

            case 3:
                tmp1.x = tiledMap.getMapWidth() - 4;
                tmp1.y = 0;
                tmp2.x = tiledMap.getMapWidth();
                tmp2.y = 4;

                tileList = tiledMap.getPlacementsAvailable(tmp1, tmp2);
                if (tileList.size() < 4) {
                    throw  new Exception();
                }

                tileTmp = tileList.poll();
                addUnit(UnitGenerator.generateSwordInfantry(assets, 3), new Vector2(tileTmp.getMapX(), tileTmp.getMapY()));
                tileTmp = tileList.poll();
                addUnit(UnitGenerator.generateSpearCavalry(assets, 3), new Vector2(tileTmp.getMapX(), tileTmp.getMapY()));
                tileTmp = tileList.poll();
                addUnit(UnitGenerator.generateCatria(assets, 3), new Vector2(tileTmp.getMapX(), tileTmp.getMapY()));
                tileTmp = tileList.poll();
                addUnit(UnitGenerator.generateHector(assets, 3), new Vector2(tileTmp.getMapX(), tileTmp.getMapY()));
                tileList.clear();
                break;

        }
        setStage(new Stage());
    }

    public boolean allUnitPlay() {
        for (Map.Entry<Unit, UnitData> entry : unitList.entrySet()) {
            if (!entry.getValue().hasPlay()) {
                return false;
            }
        }
        return true;
    }

    public boolean allUnitDead() {
        return unitList.isEmpty();
    }

    public void resetUnitMovement() {
        for (Map.Entry<Unit, UnitData> entry : unitList.entrySet()) {
            entry.getValue().setHasPlay(false);
            entry.getValue().setSelected(false);
        }
    }

    public void playRound(){
        for(Map.Entry<Unit, UnitData> unit : unitList.entrySet()){
            switch (unit.getValue().getStrategy()) {
                case ATTACK : break;
                case RETREAT : break; // a implementer si les units peuvent regagner leur PV
                default: break; // Strategy == WAIT
            }
        }
    }

    // OVERRIDING
    private float d = 0f;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        unitList.forEach((unit, unitData) -> {
            if (!unitData.isDragging()) {
                if (unitData.hasPlay()) {
                    batch.setColor(Color.GRAY);
                } else if (unitData.isSelected()) {
                    d += parentAlpha;
                    Color color = batch.getColor();
                    batch.setColor(color.r, color.g, color.b, (float) (.15f * Math.sin(d * .1f) + .85f));
                }
                batch.draw(
                        unit.getTexture(),
                        unitData.getLocation().x * Const.UNIT_SIZE,
                        unitData.getLocation().y * Const.UNIT_SIZE,
                        Const.TILE_SIZE,
                        Const.TILE_SIZE
                );
                batch.setColor(Color.WHITE);
            } else {
                batch.draw(
                        unit.getTexture(),
                        unitData.getLocation().x,
                        unitData.getLocation().y,
                        1.15f * Const.UNIT_SIZE,
                        1.15f * Const.UNIT_SIZE
                );
            }
        });
    }

    // GETTERS
    public HashMap<Unit, UnitData> getUnitList() {
        return unitList;
    }

    public boolean isAI() {
        return isAI;
    }

    // PRIVATE
    private void addUnit(Unit unit, Vector2 location) {
        unitList.put(unit, new UnitData(new Vector2(location.x + Const.MAP_ORIGIN, location.y + Const.MAP_ORIGIN)));
    }
}
