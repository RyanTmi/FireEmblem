package model.unit.special;

import model.unit.Unit;

public class Pavise implements DefenseSpecial {

	private Unit user;
	private final int defaultCounter = 3;
	private int counter = 3;
	
	@Override
	public int getCounter() {
		return this.counter;
	}

	@Override
	public void resetCounter() {
		this.counter = this.defaultCounter;
	}

	@Override
	public void decreaseCounter() {
		if (this.getCounter() <= 0) {
			this.counter = 0;
			return;
		}
		this.counter--;
		
	}

	/**
	 * Special qui reduit les degats de 50%.
	 * Renvoie 50% des degats passes en parametre.
	 */
	@Override
	public int getEffectValue(int damage, Unit attacker, Unit defender) {
		if (!this.isUnitUser(defender)) return 0;
		return damage / 2;
	}

	@Override
	public boolean canActivateSkill() {
		return this.getCounter() == 0;
	}

	@Override
	public Unit getUser() {
		return this.user;
	}

	@Override
	public boolean isUnitUser(Unit unit) {
		return this.getUser() == unit;
	}

	@Override
	public void setUser(Unit unit) {
		this.user = unit;	
	}

	@Override
	public int getDefaultCounter() {
		return this.defaultCounter;
	}

	@Override
	public String toString() {
		return "Pavise";
	}
}
