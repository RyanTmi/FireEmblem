package model.item.weapontype;

public class Spear implements Stat {

    public int getDamage(){ return 4; }

    public int getPrecision(){ return 45; }

    public int getWeigth(){ return 5; }

    public int getDurability(){ return 35; }

    public int getCritical(){ return 10; }

    public int[] getRange(){ return new int[]{1,1}; }

	public boolean isPhysical() {
		return true;
	}

    @Override
    public String toString() {
        return "Spear";
    }
}
