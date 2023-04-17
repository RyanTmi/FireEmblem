package model.item.weapontype;

//Stats copiees depuis Bow
public class Staff implements Stat {
	
	public int getDamage(){ return 1; }

    public int getPrecision(){ return 40; }

    public int getWeigth(){return 2; }

    public int getDurability(){ return 35; }

    public int getCritical(){ return 5; }

    public int[] getRange(){ return new int[]{2,2}; }

	public boolean isPhysical() {
		return false;
	}

    @Override
    public String toString() {
        return "Staff";
    }
}
