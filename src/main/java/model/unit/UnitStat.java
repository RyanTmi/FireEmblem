package model.unit;

public class UnitStat {

	private int hp;
    private final int atk;
    private final int spd;
    private final int def;
    private final int res;
    private final int maxHp;

	public UnitStat(int hp, int atk, int spd, int def, int res) {
		this.hp = hp <= 0 ? 1 : hp;
		this.atk = atk <= 0 ? 1 : atk;
		this.spd = spd <= 0 ? 1 : spd;
		this.def = def <= 0 ? 1 : def;
		this.res = res <= 0 ? 1 : res;
		this.maxHp = hp <= 0 ? 1 : hp;
	}

	public void addHp(int value) {
		if (this.hp + value > this.maxHp) {
			this.hp = this.maxHp;
			return;
		}
		if (this.hp + value < 0) {
			this.hp = 0;
			return;
		}
		this.hp += value;
	}

	// GETTERS
	public int getHp() {
		return this.hp;
	}
	
	public int getAtk() {
		return this.atk;
	}
	
	public int getSpeed() {
		return this.spd;
	}
	
	public int getRes() {
		return this.res;
	}
	
	public int getDef() {
		return this.def;
	}

	public int getMaxHp() {
		return maxHp;
	}
}
