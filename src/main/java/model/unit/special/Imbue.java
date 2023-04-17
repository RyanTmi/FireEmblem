package model.unit.special;

import model.unit.Unit;

public class Imbue implements HealSpecial {
	
	private Unit user;
	private final int defaultCounter = 1;
	private int counter = 1;

	@Override
	public int getCounter() {
		return this.counter;
	}

	@Override
	public int getDefaultCounter() {
		return this.defaultCounter;
	}

	@Override
	public void resetCounter() {
		this.counter = this.defaultCounter;
	}

	@Override
	public void decreaseCounter() {
		if (this.counter > 0) this.counter--;
		else this.counter = 0;
	}

	@Override
	public Unit getUser() {
		return this.user;
	}

	@Override
	public void setUser(Unit unit) {
		this.user = unit;
	}

	/**
	 * Ajoute 10 aux soins.</br>
	 * Renvoie 10 ou moins si la cible a perdu moins de HP que les soins + 10.
	 */
	@Override
	public int getEffectValue(int healValue, Unit healer, Unit target) {
		if (healer != this.getUser() || target.getHp() + healValue >= target.getMaxHp()) return 0;
		if (target.getHp() + healValue + 10 > target.getMaxHp()) return target.getMaxHp() - (target.getHp() + healValue);
		return 10;
	}
	
	@Override
	public String toString() {
		return "Imbue";
	}

}
