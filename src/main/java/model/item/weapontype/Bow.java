package model.item.weapontype;

public class Bow implements Stat {

    public int getDamage(){ return 1; }

    public int getPrecision(){ return 40; }

    public int getWeigth(){return 2; }

    public int getDurability(){ return 35; }

    public int getCritical(){ return 5; }

    public int[] getRange(){ return new int[]{2,2}; }

	public boolean isPhysical() {
		return true;
	}

    @Override
    public String toString() {
        return "Bow";
    }
}
