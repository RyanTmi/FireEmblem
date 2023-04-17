package model.unit.movetype;

public class Armor implements MoveType{

    @Override
    public int getMovementRange() {
        return 2;
    }

    @Override
    public String toString() {
        return "Armor";
    }
}
