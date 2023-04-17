package model.unit.assist;

import model.unit.Unit;

public interface HealAssist extends Assist {

	/**
	 * Un assist de type heal ne peut etre active qu'avec un baton
	 */
	@Override
	default boolean canActivateSkill() {
		return this.getUser().getWeapon().getType() instanceof model.item.weapontype.Staff;
	}
	
	/**
	 * Renvoie le nombre d'hp a recuperer.</br>
	 * On suppose que l'unite qui utilise l'assist est celle qui possede l'assist.
	 * @param target l'unite ciblee par l'assist.
	 * @return la valeur du heal
	 */
	int getEffectValue(Unit target);
	
}
