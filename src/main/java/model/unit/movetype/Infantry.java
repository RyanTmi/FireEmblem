package model.unit.movetype;

public class Infantry implements MoveType {

    @Override
    public int getMovementRange() {
        return 3;
    }

    @Override
    public String toString() {
        return "Infantry";
    }
}
