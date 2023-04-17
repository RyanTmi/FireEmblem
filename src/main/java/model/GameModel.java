package model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import helpers.Assets;
import model.fight.Fight;
import model.terrain.Tile;
import model.terrain.TiledMap;
import model.unit.Unit;
import model.unit.UnitData;
import model.unit.UnitList;
import model.unit.movetype.Flying;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;

public class GameModel extends Actor {

    private static final double AMPLITUDE = 0;

    // Par convention l'indice 0 du tableau représente la team du joueur humain et le premier joueur à jouer.
    private final Fight fight;
    private final Assets assets;

    private TiledMap tiledMap;
    private UnitList[] unitLists;

    /**
     * true si la team à l'indice i est active,
     * i.e il reste au moins une unit dans la team
     * false sinon
     */
    private boolean[] activeTeam;
    private boolean[] playerAI;
    private int nbOfTeam;
    private int mapSize;
    private int currentTeamNumber;

    public GameModel(final Assets assets) {
        this.assets = assets;
        this.fight = new Fight(this);
    }

    public void setGameObject(int nbOfTeam, TiledMap tiledMap, int mapSize, boolean[] playerAI) throws Exception {
        this.tiledMap = tiledMap;
        this.mapSize = mapSize;
        this.nbOfTeam = nbOfTeam;
        this.currentTeamNumber = 0;

        this.playerAI = playerAI;
        this.activeTeam = new boolean[nbOfTeam];
        Arrays.fill(activeTeam, true);

        this.unitLists = new UnitList[nbOfTeam];
        for (int i = 0; i < nbOfTeam; i++) {
            try {
                this.unitLists[i] = new UnitList(i, playerAI[i], tiledMap, assets);
            } catch (Exception e) {
                this.tiledMap.makeStartingPointsAvailable(i, assets);
                this.unitLists[i] = new UnitList(i, playerAI[i], tiledMap, assets);
            }
        }
    }

    public void nextRound() {
        currentTeamNumber = ++currentTeamNumber % nbOfTeam;
        if (!activeTeam[currentTeamNumber]) nextRound();
        if (unitLists[currentTeamNumber].isAI() ) {
            playRound();
            nextRound();
        }
    }

    /**
     * @return la couleur associé a la team en train de jouer
     */
    public Color getCurrentTeamColor() {
        switch (currentTeamNumber) {
            case 0:
                return Color.RED;
            case 1:
                return Color.BLUE;
            case 2:
                return Color.TEAL;
            case 3:
                return Color.VIOLET;
            default:
                return Color.CLEAR;
        }
    }

    /**
     * Verifie si toutes les unités la team en train de jouer ont déja jouées
     */
    public boolean allUnitPlay() {
        return unitLists[currentTeamNumber].allUnitPlay();
    }

    /**
     * Remet l'attribut 'hasPlay' a 'false' pour chaque unité de la team en train de jouer
     */
    public void resetUnitMovement() {
        unitLists[currentTeamNumber].resetUnitMovement();
    }

    /**
     * Check si une team est morte et la desactive si c'est le cas
     * i.e toutes les units de la team sont mortes
     */
     public void disableTeam() {
         for (int i = 0; i < nbOfTeam; i++) {
             if (unitLists[i].allUnitDead()) {
                 activeTeam[i] = false;
             }
         }
     }

    /**
     * Check s'il y'a un gagnant
     * @return -1 s'il n'y a pas de gagnant, l'indice de la team qui a gagné sinon
     */
     public int winner() {
         int teamWinner = -1    ;
         int nbOfActive = 0;
         for (int i = 0; i < nbOfTeam; i++) {
             if (activeTeam[i]) {
                 nbOfActive++;
                 teamWinner = i;
             }
         }
         if (nbOfActive == 1) return teamWinner;
         return -1;
     }

    public TiledMap generateNewMap(int mapSize, double frequency) {
        return new TiledMap(mapSize, mapSize, AMPLITUDE, frequency, assets);
    }

    public ArrayList<Tile> getMovementAvailable(Unit u, Vector2 unitPositions, boolean IA) {
        boolean[][] checked = new boolean[tiledMap.getMapWidth() + 4][tiledMap.getMapHeight() + 4];
        boolean[][] danger = new boolean[tiledMap.getMapWidth() + 4][tiledMap.getMapHeight() + 4];
        ArrayList<Tile> availableTiles = new ArrayList<>();
        HashMap<Unit, UnitData> unitEncountered = getAllEnnemies(u);

        int startX = (int)unitPositions.x;
        int startY = (int)unitPositions.y;

        int unitMaxRange = u.getMovementRange() + tiledMap.getTile(startX, startY).movementBonus(u);
        int distanceTravelled = 0;

        Tile tmpTile;
        Vector2 tmpVector2;

        if (!IA) {
            tiledMap.getTile(startX, startY).setFilter(Tile.blueTileFilter);
        }
        availableTiles.add(tiledMap.getTile(startX, startY));
        checked[startX][startY] = true;
        LinkedList<Tile> queue = new LinkedList<>(tiledMap.getAdjacentTiles(startX, startY, checked));

        while (!queue.isEmpty()) {
            tmpTile = queue.poll();

            if (checked[tmpTile.getMapX()][tmpTile.getMapY()]) {
                continue;
            }

            tmpVector2 = new Vector2(tmpTile.getMapX(), tmpTile.getMapY());

            if (((Math.abs(tmpTile.getMapX() - startX) + Math.abs(tmpTile.getMapY() - startY))) > distanceTravelled) {
                distanceTravelled++;
            }

            if (tmpTile.movementBonus(u) == -1) {
                checked[tmpTile.getMapX()][tmpTile.getMapY()] = true;
                continue;
            }

            if (enemyUnitPresent(tmpVector2)) {
                if (!IA) {
                    tmpTile.setFilter(Tile.redTileFilter);
                }
                availableTiles.add(tmpTile);
                checked[tmpTile.getMapX()][tmpTile.getMapX()] = true;
                continue;
            }

            TEST_ALLY:
            if (allyUnitPresent(currentTeamNumber, tmpVector2)) {
                if (!IA) {
                    tmpTile.setFilter(Tile.greenTileFilter);
                }
                availableTiles.add(tmpTile);
            } else {
                if (!IA) {
                    for (Map.Entry<Unit, UnitData> unit : unitEncountered.entrySet()) {
                        danger = tiledMap.isDangerZone(unit.getKey(), unit.getValue().getLocation(), tmpVector2, danger);
                        if (danger[(int)tmpVector2.x][(int)tmpVector2.y]) {
                            tmpTile.setFilter(Tile.dangerTileFilter);
                            break TEST_ALLY;
                        }
                    }
                    tmpTile.setFilter(Tile.blueTileFilter);
                }
            }

            availableTiles.add(tmpTile);
            checked[tmpTile.getMapX()][tmpTile.getMapY()] = true;
            if (tmpTile.getTileFilter() != Tile.greenTileFilter || u.getMoveType() instanceof Flying) {
                LinkedList<Tile> possibleTiles = tiledMap.getAdjacentTiles(tmpTile.getMapX(), tmpTile.getMapY(), checked);
                for (Tile t : possibleTiles) {
                    if (t.getMovementMalus(u) + distanceTravelled + 1 <= unitMaxRange) {
                        queue.add(t);
                    }
                }
            }
        }
        return availableTiles;
    }

    public boolean enemyUnitPresent(Vector2 tmpVector2) {
        for (int i = 1; i < nbOfTeam; i++) {
            for (Map.Entry<Unit, UnitData> entry : unitLists[(currentTeamNumber + i) % nbOfTeam].getUnitList().entrySet()) {
                if (entry.getValue().getLocation().equals(tmpVector2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean allyUnitPresent(int currentTeamNumber, Vector2 tmpVector2) {
        for (Map.Entry<Unit, UnitData> entry : unitLists[currentTeamNumber].getUnitList().entrySet()) {
            if (entry.getValue().getLocation().equals(tmpVector2)) {
                return true;
            }
        }
        return false;
    }

    public void playRound() {
        HashMap<Unit, UnitData> copie = (HashMap<Unit, UnitData>) unitLists[currentTeamNumber].getUnitList().clone();
        for (Map.Entry<Unit, UnitData> unit : copie.entrySet()) {
            switch (unit.getValue().getStrategy()) {
                case ATTACK :
                    doPriority(unit.getKey(), unit.getValue());
                    break;
                case RETREAT :
                    break; // a implementer si les unitees peuvent regagner leur PV
                default:
                    break; // Strategy==WAIT
            }
        }
        resetUnitMovement();
    }

    public void doPriority(Unit unit, UnitData data) {
        HashMap<Unit, UnitData> target = canHit(unit, data);
        if (target.isEmpty()) {
            moveCloser(unit, data.getLocation(), data);
        } else {
            Unit bestTarget = null;
            Vector2 bestTargetLocation = null;
            int[] bestFigth = null;
            for (Map.Entry<Unit, UnitData> enemy : target.entrySet()) {
                int[] res = fight.damagePreview(unit, enemy.getKey());
                if (isBetterMove(res, bestFigth)) {
                    bestFigth = res;
                    bestTarget = enemy.getKey();
                    bestTargetLocation = enemy.getValue().getLocation();
                }
            }
            gofigth(unit, bestTarget, data, bestTargetLocation);
        }
    }

    /**
     * prend en paramettre 2 tableau de 2 entier contenant les PV de l attaquant [0] et du defenceur [1] apres une
     * simulation de combat.
     * @return true si les donner du tableau test sont plus avantageuse que celle du tableau best
     */
    public boolean isBetterMove(int[] test, int[] best) {
        if (best == null) {
            return true;
        }
        // l'unitee meurt elle dans le meilleur cas
        if (best[0] == 0) {
            // si l'unitee peut rester en vie ou faire plus de dommage
            if (test[0] > 0 || test[1] < best[1]) {
                return true;
            }
        }
        // l'unitee fait elle plus de degat
        if (test[1] < best[1]) {
            // l unitee meurt elle
            return test[0] != 0;
        }
        // les dégats infligés sont ils egaux et l'unitee a t'elle plus de PV a la fin du combat
        return test[1] == best[1] && test[0] > best[0];
    }

    /**
     * trouve les unitees qui peuvent etre attaquer par l unitee passer en argument
     */
    public HashMap<Unit, UnitData> canHit(Unit unit, UnitData data) {
        HashMap<Unit, UnitData> target = new HashMap<>();
        ArrayList<Tile> availableTiles = getMovementAvailable(unit, data.getLocation(), true);
        for (int i = 0; i < unitLists.length; i++) {
            if (i == currentTeamNumber) {
                continue;
            }
            for (Map.Entry<Unit, UnitData> enemy : unitLists[i].getUnitList().entrySet()){
                if (searchTileNearest(unit, data.getLocation(), enemy.getValue().getLocation(), availableTiles, true) != null) {
                    target.put(enemy.getKey(), enemy.getValue());
                }
            }
        }
        return target;
    }

    /**
     * Rapproche une unité d'une case
     */
    public void moveCloser(Unit u, Vector2 unitPos, UnitData data) {
        ArrayList<Tile> availableTiles = getMovementAvailable(u,unitPos,true);
        Vector2 finalPos = null;
        float min = u.getMovementRange()+tiledMap.getTile((int)unitPos.x, (int)unitPos.y).movementBonus(u) + 1;
        Vector2 tilePos = new Vector2();
        for (Tile tile : availableTiles) {
            tilePos.set(tile.getMapX(), tile.getMapY());
            if (
                    unitPos.dst(tilePos) < min &&
                    !this.allyUnitPresent(currentTeamNumber, tilePos) &&
                    !this.enemyUnitPresent(tilePos)
            ) {
                min = unitPos.dst(tilePos);
                finalPos = new Vector2(tilePos);
            }
        }
        data.setLocation(finalPos);
        data.setHasPlay(true);
    }

    /**
     * Deplace l'ia et la fait combattre
     */
    public void gofigth(Unit unit, Unit target, UnitData unitData, Vector2 targetLocation) {
        ArrayList<Tile> availableTiles = getMovementAvailable(unit,unitData.getLocation(), true);
        Vector2 newPos = searchTileNearest(unit, unitData.getLocation(), targetLocation, availableTiles, true);
        unitData.setLocation(newPos);
        unitData.setHasPlay(true);
        fight.fight(unit, target);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        tiledMap.draw(batch, parentAlpha);
        for (UnitList unitList : unitLists) {
            if (unitList != null) {
                unitList.draw(batch, parentAlpha);
            }
        }
    }
    
    // fonction de compatibilite avec l'ancien code
    public Vector2 searchTileNearest(Unit u, Vector2 unitPos, Vector2 location, ArrayList<Tile> availableTiles) {
    	return this.searchTileNearest(u, unitPos, location, availableTiles, true);
    }
    
    /**
     * Cherche la case la plus proche de la position specifiee
     * @param unit l'unite qui se deplace
     * @param unitPos la position de l'unite avant deplacement
     * @param location la position ou chercher les cases
     * @param availableTiles les cases accessibles par l'unite
     * @param needsToAccountRange si la case cherchee doit respecter la portee de l'unite, <code>false</code> on cherchera la case la plus proche
     */
    public Vector2 searchTileNearest(Unit unit, Vector2 unitPos, Vector2 location, ArrayList<Tile> availableTiles, boolean needsToAccountRange) {
    	Vector2 returnPos = null;
    	float min = unit.getMovementRange() + tiledMap.getTile((int)unitPos.x, (int)unitPos.y).movementBonus(unit) + 1;
    	Vector2 tilePos = new Vector2();
    	for (Tile tile : availableTiles) {
    		tilePos.set(tile.getMapX(), tile.getMapY());
    		if (
    				(needsToAccountRange &&
                    unitPos.dst(tilePos) < min &&
                    location.dst(tilePos) <= unit.getRange()[1] &&
                    location.dst(tilePos) > unit.getRange()[1] - 1 &&
                    !this.allyUnitPresent(currentTeamNumber, tilePos) &&
                    !this.enemyUnitPresent(tilePos)) ||
    				
    				(!needsToAccountRange &&
                    unitPos.dst(tilePos) < min &&
                    location.dst(tilePos) == 1 &&
                    !this.allyUnitPresent(currentTeamNumber, tilePos) &&
                    !this.enemyUnitPresent(tilePos))
            ) {
    			min = unitPos.dst(tilePos);
    			returnPos = new Vector2(tilePos);
    		}
    	}
    	return returnPos;
    }
    
    /**
     * Cherche si un ennemi est present sur la position specifiee
     * @param unit l'unite qui joue son tour
     * @param location la position ou chercher l'ennemi
     */
    public Unit searchEnemyAtPos(Unit unit, Vector2 location) {
    	Unit tmp;
        for (int i = 1; i < nbOfTeam; i++) {
            for (Map.Entry<Unit, UnitData> entry : unitLists[(unit.getTeamNumber() + i) % nbOfTeam].getUnitList().entrySet()) {
                if (entry.getValue().getLocation().equals(location)) {
                	tmp = entry.getKey();
                    if (tmp.getTeamNumber() != unit.getTeamNumber()) {
                    	return entry.getKey();
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Cherche si un allie est present sur la position specifiee
     * @param unit l'unite qui joue son tour
     * @param location la position ou chercher l'allie
     */
    public Unit searchAllyAtPos(Unit unit, Vector2 location) {
    	Unit tmp;
        for (Map.Entry<Unit, UnitData> entry : unitLists[(unit.getTeamNumber())].getUnitList().entrySet()) {
            if (entry.getValue().getLocation().equals(location) && entry != unit) {
            	tmp = entry.getKey();
                if (tmp.getTeamNumber() == unit.getTeamNumber()) {
                	return entry.getKey();
                }
            }
        }
        return null;
    }

    // GETTERS
    public Fight getFight() {
    	return fight;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public UnitList getCurrentTeam() {
        return unitLists[currentTeamNumber];
    }

    public UnitList getUnitTeam(int teamNumber) {
        teamNumber = MathUtils.clamp(teamNumber, 0, unitLists.length - 1);
        return unitLists[teamNumber];
    }

    public Unit getUnitTargeted(Vector2 unitPos) {
        for (UnitList unitList : unitLists) {
            for (Map.Entry<Unit, UnitData> unit : unitList.getUnitList().entrySet()) {
                if (unit.getValue().getLocation().equals(unitPos)) {
                    return unit.getKey();
                }
            }
        }
        return null;
    }

    public HashMap<Unit, UnitData> getAllEnnemies(Unit u) {
        HashMap<Unit, UnitData> ennemies = new HashMap<>();
        for (int i=0; i<nbOfTeam;i++) {
            if(i != u.getTeamNumber()) {
                ennemies.putAll(unitLists[i].getUnitList());
            }
        }
        return ennemies;
    }

    public void reset() {
        this.tiledMap.setPreview(true);
        for (int i = 0; i < nbOfTeam; i++) {
            this.unitLists[i].clear();
            this.unitLists[i].clearListeners();
            this.unitLists[i] = null;
        }
        // this.unitLists = null;
        this.mapSize = 0;
        this.nbOfTeam = 0;
        this.playerAI = null;
        this.activeTeam = null;
    }
}