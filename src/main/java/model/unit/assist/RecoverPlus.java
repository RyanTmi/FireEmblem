package model.unit.assist;

import model.unit.Unit;

public class RecoverPlus implements HealAssist {

	private Unit user;
	
	@Override
	public Unit getUser() {
		return this.user;
	}

	@Override
	public void setUser(Unit unit) {
		this.user = unit;
	}

	/**
	 * Soigne 50% de (l'attaque de l'utilisateur + 10) HP de la cible.</br>
	 * Valeur minimale: 15 HP.</br>
	 * Renvoie 0 si l'utilisateur est passe en parametre ou si la cible est d'une equipe differente.
	 */
	@Override
	public int getEffectValue(Unit target) {
		if (target == this.getUser() || this.getUser().getTeamNumber() != target.getTeamNumber()) return 0;
		int value = (this.getUser().getDisplayedAtk()+10)/2;
		if (target.getHp()+value > target.getMaxHp()) return target.getMaxHp() - target.getHp();
		return Math.max(value, 15);
	}
	
	@Override
    public String toString() {
        return "Recover+";
    }
}
