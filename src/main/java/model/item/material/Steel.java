package model.item.material;

public class Steel implements Material {

    public int getDamage() {
        return 4;
    }

    public int getPrecision() {
        return 20;
    }

    public int getWeigth() {
        return 7;
    }

    public int getDurability() {
        return 15;
    }

    public int getCritical() {
        return 15;
    }
}
