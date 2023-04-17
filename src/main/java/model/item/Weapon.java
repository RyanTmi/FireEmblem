package model.item;

import model.item.material.Material;
import model.item.weapontype.Stat;

public class Weapon {
    Stat type;
    Material name;
    int durability;

    public Weapon(Stat t, Material m) {
        type = t;
        name = m;
        durability = type.getDurability() + name.getDurability();
    }

    public int getDamage () { return type.getDamage() + name.getDamage(); }

    public int getPrecision () { return type.getPrecision() + name.getPrecision(); }

    public int getWeigth () { return type.getWeigth() + name.getWeigth(); }

    public int getDurability () { return type.getDurability() + name.getDurability(); }

    public int getCritical () { return type.getCritical() + name.getCritical(); }

    public boolean isPhysical() {
        return type.isPhysical();
    }

    /**
     * @return un tableau dont le premiere indice est la portée minimal inclus et le deuxieme indice
     * est la portée maximal inclus
     */
    public int[] getRange () { return type.getRange(); }

    @Override
    public String toString() {
        return type.toString();
    }

	public Stat getType() {
		return this.type;
	}
}