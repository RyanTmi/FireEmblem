package model.unit.special;

import model.unit.Unit;

public interface Special {
	
	/**
	 * @return la valeur actuelle du compteur du special
	 */
	int getCounter();
	
	/**
	 * @return la valeur initiale du compteur du special
	 */
	int getDefaultCounter();
	
	/**
	 * Remet la valeur du compteur a la valeur initiale
	 */
	void resetCounter();
	
	/**
	 * Baisse la valeur du compteur du special
	 */
	void decreaseCounter();
	
	/**
	 * @return si le skill peut etre active (en general quand le skill a un compteur a 0)
	 */
	default boolean canActivateSkill() {
		return this.getCounter() == 0;
	}
	
	/**
	 * @return l'unité qui possède le spécial.
	 */
	Unit getUser();
	
	/**
	 * @param unit l'unité à passer en paramètre.
	 * @return <code>true</code> si l'unité passée en paramètre possède le spécial
	 */
	default boolean isUnitUser(Unit unit) {
		return this.getUser() == unit;
	}
	
	/**
	 * @param unit l'unité qui possède le spécial
	 */
	void setUser(Unit unit);
	
}
