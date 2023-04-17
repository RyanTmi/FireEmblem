package model.item.weapontype;

public class Axe implements Stat {

    public int getDamage(){ return 6; }

    public int getPrecision(){ return 30; }

    public int getWeigth(){ return 5; }

    public int getDurability(){ return 30; }

    public int getCritical(){ return 15; }

    public int[] getRange(){ return new int[]{1,1}; }

	public boolean isPhysical() {
		return true;
	}

    @Override
    public String toString() {
        return "Axe";
    }
}
