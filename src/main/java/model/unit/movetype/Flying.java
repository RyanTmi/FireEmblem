package model.unit.movetype;

public class Flying implements MoveType {

    @Override
    public int getMovementRange() {
        return 4;
    }

    @Override
    public String toString() {
        return "Flying";
    }
}
