package model.unit.special;

import model.unit.Unit;

public interface HealSpecial extends Special {
	
	/**
	 * @param healValue la valeur des soins
	 * @param healer l'unite qui active le special
	 * @param target l'unite ciblee
	 * @return la valeur a ajouter aux soins, 0 si <code>isUserUnit(attacker)</code> est <code>false</code>.
	 */
	int getEffectValue(int healValue, Unit healer, Unit target);
	
}
