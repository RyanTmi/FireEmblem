package model.unit;

import com.badlogic.gdx.math.Vector2;
import model.ia.StrategyType;

public class UnitData {

    private Vector2 location;
    private boolean isDragging;
    private boolean isSelected;
    private boolean hasPlay;
    private final StrategyType strategy;
    private final Vector2 locationTarget;


    public UnitData(Vector2 location) {
        this.location = location;
        this.isDragging = false;
        this.isSelected = false;
        this.hasPlay = false;
        this.strategy = StrategyType.ATTACK;
        this.locationTarget = location;
    }

    // GETTERS
    public Vector2 getLocation() {
        return this.location;
    }
    public boolean isDragging() {
        return this.isDragging;
    }
    public boolean isSelected() { return isSelected; }
    public boolean hasPlay() { return this.hasPlay; }
    public StrategyType getStrategy() { return strategy; }
    public Vector2 getLocationTarget() { return locationTarget; }

    // SETTERS
    public void setLocation(Vector2 location) {
        this.location = location;
    }
    public void setDragging(boolean isDragging) {
        this.isDragging = isDragging;
    }
    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }
    public void setHasPlay(boolean hasPlay) {
        this.hasPlay = hasPlay;
    }
}
