package model.unit.special;

import model.unit.Unit;

public interface AttackSpecial extends Special {
	
	/**
	 * @param damage les degats sans compter les speciaux
	 * @param attacker l'unite qui active le special
	 * @param defender l'unite ciblee
	 * @return la valeur a ajouter aux degats, 0 si <code>isUserUnit(attacker)</code> est <code>false</code>.
	 * @see model.unit.special.Special#isUnitUser(Unit)
	 */
	int getEffectValue(int damage, Unit attacker, Unit defender);
	
}
