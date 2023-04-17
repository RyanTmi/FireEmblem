package model.unit.special;

import model.unit.Unit;

public interface DefenseSpecial extends Special {

	/**
	 * @param damage les degats apres activation du special de l'attaquant
	 * @param attacker l'unite qui attaque
	 * @param defender l'unite qui active le skill
	 * @return la valeur (absolue) a retirer aux degats, 0 si <code>isUserUnit(defender)</code> est <code>false</code>.
	 * @see model.unit.special.Special#isUnitUser(Unit)
	 */
	int getEffectValue(int damage, Unit attacker, Unit defender);
	
	
}
