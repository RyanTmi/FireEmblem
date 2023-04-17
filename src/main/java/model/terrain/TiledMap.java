package model.terrain;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import helpers.Assets;
import helpers.Const;
import model.unit.Unit;
import java.util.Collections;
import java.util.LinkedList;

public class TiledMap extends Actor {

    private final Tile[][] tiledMap;
    private final double[][] worldPreset;

    private int width;
    private int height;

    public TiledMap(int width, int height, double amplitude, double frequency, Assets assets) {
        PerlinNoise2D.newPermutation();

        this.width = width;
        this.height = height;
        this.tiledMap = new Tile[width + 4][height + 4];
        this.worldPreset = new double[width + 4][height + 4];

        for (int x = 0; x < width + 4; x++) {
            for (int y = 0; y < height + 4; y++) {
                double dx = (double) x / width;
                double dy = (double) y / height;
                double timeAdvancement = 0;
                if(x < 2 || y < 2 || x > width + 1 || y > height + 1) {
                    worldPreset[x][y] = -10;
                } else {
                    timeAdvancement += 0.1;
                    worldPreset[x][y] = PerlinNoise2D.noise((dx * frequency) + timeAdvancement, (dy * frequency) + timeAdvancement) + amplitude;
                }
            }
        }

        for(int x = 0; x < width + 4; x++) {
            for(int y = 0; y < height + 4; y++) {
                if(worldPreset[x][y] == -10){
                    tiledMap[x][y] = new Void(x*Const.TILE_SIZE,y*Const.TILE_SIZE, assets);
                }
                else if(worldPreset[x][y] > -8 && worldPreset[x][y] < -0.60) {
                    tiledMap[x][y] = this.getWaterTerrainType(x,y, assets);
                }
                else if(worldPreset[x][y] >= -0.60 && worldPreset[x][y] < -0.20) {
                    tiledMap[x][y] = new Field(x * Const.TILE_SIZE, y * Const.TILE_SIZE, assets);
                }
                else if(worldPreset[x][y] >= -0.20 && worldPreset[x][y] < 0) {
                    tiledMap[x][y] = new Road(x * Const.TILE_SIZE, y * Const.TILE_SIZE, assets);
                }
                else if(worldPreset[x][y] >= 0 && worldPreset[x][y] < 0.40) {
                    tiledMap[x][y] = new Forest(x * Const.TILE_SIZE, y * Const.TILE_SIZE, assets);
                }
                else if(worldPreset[x][y] >= 0.40 && worldPreset[x][y] < 0.70) {
                    tiledMap[x][y] = new Hill(x * Const.TILE_SIZE, y * Const.TILE_SIZE, assets);
                }
                else if(worldPreset[x][y] >= 0.70) {
                    tiledMap[x][y] = new Mountains(x * Const.TILE_SIZE, y * Const.TILE_SIZE, assets);
                }
            }
        }

    }

    public Tile getWaterTerrainType(int x, int y, Assets assets){

        DeepWater_Test:
        if(this.worldPreset[x - 2][y - 2] < -0.60) {
            for(int i = x - 2; i <= x + 2; i++) {
                for(int j = y - 2; j <= y + 2; j++) {
                    if(this.worldPreset[i][j] >= -0.60 || this.worldPreset[i][j] < -5.0) {
                        break DeepWater_Test;
                    }
                }
            }
            return new DeepWater(x*Const.TILE_SIZE,y*Const.TILE_SIZE, assets);
        }


        Shallow_Test:
        if(this.worldPreset[x - 1][y - 1] < -0.60) {
            for(int i = x - 1; i <= x + 1; i++) {
                for(int j = y - 1; j <= y + 1; j++) {
                    if(this.worldPreset[i][j] >= -0.60 || this.worldPreset[i][j] < -5.0) {
                        break Shallow_Test;
                    }
                }
            }
            return new Shallow(x*Const.TILE_SIZE,y*Const.TILE_SIZE, assets);
        }
        return new Shore(x* Const.TILE_SIZE,y* Const.TILE_SIZE, assets);
    }




    /**
     * Permet d'obtenir les cases adjacentes haut, bas, gauche, droite d'une case de la map.
     * Utilisée pour le BFS de déplacement.
     * @return LinkedList
     */
    public LinkedList<Tile> getAdjacentTiles(int x,int y, boolean[][] checked){
        LinkedList<Tile> neighbors = new LinkedList<>();
        if(tiledMap[x-1][y] != null && !checked[x-1][y]) {
            neighbors.add(tiledMap[x - 1][y]);
        }
        if(tiledMap[x + 1][y] != null && !checked[x+1][y]) {
            neighbors.add(tiledMap[x + 1][y]);
        }
        if(tiledMap[x][y - 1] != null && !checked[x][y-1]) {
            neighbors.add(tiledMap[x][y - 1]);
        }

        if(tiledMap[x][y + 1] != null && !checked[x][y+1]) {
            neighbors.add(tiledMap[x][y + 1]);
        }
        return neighbors;
    }

    public boolean[][] isDangerZone(Unit u, Vector2 origin, Vector2 target, boolean[][] danger){
        int startX = (int)origin.x;
        int startY = (int)origin.y;

        int unitMaxRange = u.getMovementRange() + tiledMap[startX][startY].movementBonus(u);
        int distanceTravelled = 0;
        boolean[][] checked = new boolean[this.getMapWidth() + 4][this.getMapHeight() + 4];

        Tile tmpTile;
        Vector2 tmpVector2;

        danger[startX][startY] = true;
        LinkedList<Tile> queue = new LinkedList<>(this.getAdjacentTiles(startX, startY, checked));

        while (!queue.isEmpty()) {
            tmpTile = queue.poll();

            if (checked[tmpTile.getMapX()][tmpTile.getMapY()]) {
                continue;
            }

            tmpVector2 = new Vector2(tmpTile.getMapX(), tmpTile.getMapY());

            if(tmpVector2.equals(target)){
                danger[(int)target.x][(int)target.y] = true;
            }

            if (((Math.abs(tmpTile.getMapX() - startX) + Math.abs(tmpTile.getMapY() - startY))) > distanceTravelled) {
                distanceTravelled++;
            }

            if (tmpTile.movementBonus(u) == -1) {
                checked[tmpTile.getMapX()][tmpTile.getMapY()] = true;
                continue;
            }

            checked[tmpTile.getMapX()][tmpTile.getMapY()] = true;
            LinkedList<Tile> possibleTiles = this.getAdjacentTiles(tmpTile.getMapX(), tmpTile.getMapY(), checked);
                for (Tile t : possibleTiles) {
                    if (t.getMovementMalus(u) + distanceTravelled + 1 <= unitMaxRange) {
                        queue.add(t);
                    }
                }

        }
        return danger;
    }

    public LinkedList<Tile> getPlacementsAvailable(Vector2 start, Vector2 end) {
        // int squareSize = unitNumberToPlace*unitNumberToPlace;
        LinkedList<Tile> tilesAvailable = new LinkedList<>();
        int x0 = (int)start.x;
        int y0 = (int)start.y;
        int x1 = (int)end.x;
        int y1 = (int)end.y;

        for (int i = x0; i < x1; i++) {
            for (int j = y0; j < y1; j++) {
                if (tiledMap[i+2][j+2].unreachable()) {
                    continue;
                }
                tilesAvailable.add(tiledMap[i][j]);
            }
        }
        Collections.shuffle(tilesAvailable);
        return tilesAvailable;
    }

    public void makeStartingPointsAvailable(int teamNumber, Assets assets){
        switch (teamNumber) {
            case 0:
                for(int i = 2; i < 6; i++) {
                    for(int j = 2; j < 6; j++) {
                        this.tiledMap[i][j] = new Field(i * Const.TILE_SIZE,j * Const.TILE_SIZE, assets);
                        this.tiledMap[i][j].setScalePreview(width);
                        this.tiledMap[i][j].setPreview(false);
                    }
                }
                break;

            case 1:
                for(int i = 2; i < 6; i++) {
                    for(int j = getMapHeight() - 2; j < getMapHeight() + 2; j++) {
                        this.tiledMap[i][j] = new Field(i * Const.TILE_SIZE,j * Const.TILE_SIZE, assets);
                        this.tiledMap[i][j].setScalePreview(width);
                        this.tiledMap[i][j].setPreview(false);
                    }
                }
                break;

            case 2:
                for (int i = getMapWidth() - 2; i < getMapWidth() + 2; i++) {
                    for (int j = getMapHeight() - 2; j < getMapHeight() + 2; j++) {
                        this.tiledMap[i][j] = new Field(i * Const.TILE_SIZE,j * Const.TILE_SIZE, assets);
                        this.tiledMap[i][j].setScalePreview(width);
                        this.tiledMap[i][j].setPreview(false);
                    }
                }
                break;

            case 3:
                for (int i = getMapWidth() - 2; i < getMapWidth() +2 ; i++) {
                    for (int j = 2; j < 6; j++) {
                        this.tiledMap[i][j] = new Field(i * Const.TILE_SIZE,j * Const.TILE_SIZE, assets);
                        this.tiledMap[i][j].setScalePreview(width);
                        this.tiledMap[i][j].setPreview(false);
                    }
                }
                break;
        }
    }

    // OVERRIDING
    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (int x = 2; x < width + 2; x++) {
            for (int y = 2; y < height + 2; y++) {
                tiledMap[x][y].draw(batch, parentAlpha);
            }
        }
    }

    // GETTERS
    public Tile getTile(int x, int y) {
        return tiledMap[x][y];
    }

    public int getMapWidth() {
        return width;
    }

    public int getMapHeight() {
        return height;
    }

    // SETTERS
    public void setMapSize(int mapSize) {
        this.width = mapSize;
        this.height = mapSize;
    }

    public void setPreview(boolean preview) {
        for (int x = 2; x < width + 2; x++) {
            for (int y = 2; y < height + 2; y++) {
                tiledMap[x][y].setPreview(preview);
                tiledMap[x][y].setScalePreview(getMapWidth());
            }
        }
    }
}