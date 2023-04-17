package model.terrain;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.Texture;
import helpers.Const;
import model.unit.Unit;

public abstract class Tile extends Actor {

    public static final TileFilter blueTileFilter = new TileFilter(new Color(0f, 0f, 1f, .65f));
    public static final TileFilter redTileFilter = new TileFilter(new Color(1f, 0f, 0f, .65f));
    public static final TileFilter greenTileFilter = new TileFilter(new Color(0f, 1f, 0f, .65f));
    public static final TileFilter noTileFilter = new TileFilter(new Color(0f, 0f, 0f, 0f));
    public static final TileFilter darkBlueTileFilter = new TileFilter(new Color(0f, 0f, .4f, .9f));
    public static final TileFilter dangerTileFilter = new TileFilter(new Color(.75f, .1f,0f, .7f));

    private final float x, y;
    private final int width, height;
    private final Texture tileTexture;

    private boolean preview;
    private float scalePreview; // = 4.8f / map size
    private TileFilter tileFilter;

    public Tile(float x, float y, Texture terrainTexture) {
        super();
        this.x = x;
        this.y = y;
        this.width = Const.TILE_SIZE;
        this.height = Const.TILE_SIZE;
        this.tileTexture = terrainTexture;
        this.preview = true;
        this.scalePreview = .32f;

        // Transparent
        this.tileFilter = noTileFilter;
    }

    /**
     * Gives the movement bonus according to the unit placed and the tile type.
     * @return int
     */
    public abstract int movementBonus(Unit u);


    public abstract boolean unreachable();

    public abstract int getMovementMalus(Unit u);


    // OVERRIDING
    private float d = 0f;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        d += parentAlpha;
        Color color = batch.getColor();
        batch.draw(
                tileTexture,
                preview ? x * scalePreview : x, preview ? y * scalePreview : y,
                preview ? width * scalePreview : width, preview ? height * scalePreview : height
        );
        if (!tileFilter.equals(blueTileFilter)) {
            batch.setColor(color.r, color.g, color.b, (float) (.25f * Math.sin(d * .08f) + .75f));
        }
        batch.draw(tileFilter.filter, x, y, width, height);
        batch.setColor(Color.WHITE);
    }

    @Override
    public String toString(){
        return this.getClass().toString();
    }

    // GETTERS
    public int getMapX() {
        return (int)x / Const.TILE_SIZE;
    }
    public int getMapY(){
        return (int)y / Const.TILE_SIZE;
    }
    public TileFilter getTileFilter()
    {
        return this.tileFilter;
    }
    public float getScalePreview() {
        return scalePreview;
    }

    // SETTERS
    public void setFilter(TileFilter tileFilter) {
        this.tileFilter = tileFilter;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }

    public void setScalePreview(int mapSize) {
        this.scalePreview = 4.8f / mapSize;
    }

    // INNER CLASS
    public static class TileFilter extends Actor {

        private Texture filter;

        public TileFilter(Color colorFilter) {
            createTexture(colorFilter);
        }

        private void createTexture(Color color) {
            Pixmap pixmap = new Pixmap(Const.TILE_SIZE, Const.TILE_SIZE, Pixmap.Format.RGBA8888);
            pixmap.setColor(color);
            pixmap.fillRectangle(0, 0, Const.TILE_SIZE, Const.TILE_SIZE);
            filter = new Texture(pixmap);
            pixmap.dispose();
        }
    }
}
