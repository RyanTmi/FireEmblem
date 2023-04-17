package model.unit.movetype;

public class Cavalry implements MoveType {

    @Override
    public int getMovementRange() {
        return 5;
    }

    @Override
    public String toString() {
        return "Cavalry";
    }
}
