package model.item.weapontype;

public class Sword implements Stat {

    public int getDamage(){ return 3; }

    public int getPrecision(){ return 55; }

    public int getWeigth(){ return 2; }

    public int getDurability(){ return 40; }

    public int getCritical(){ return 5; }

    public int[] getRange(){ return new int[]{1,1}; }

	public boolean isPhysical() {
		return true;
	}

    @Override
    public String toString() {
        return "Sword";
    }
}
