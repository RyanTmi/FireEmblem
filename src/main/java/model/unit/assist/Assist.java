package model.unit.assist;

import model.unit.Unit;

public interface Assist {

	/**
	 * @return si l'assist l peut etre active
	 */
	boolean canActivateSkill();
	
	/**
	 * @return l'unité qui possède l'assist.
	 */
	Unit getUser();
	
	/**
	 * @param unit l'unité à passer en paramètre.
	 * @return <code>true</code> si l'unité passée en paramètre possède l'assist
	 */
	default boolean isUnitUser(Unit unit) {
		return this.getUser() == unit;
	}
	
	/**
	 * @param unit l'unité qui possède l'assist
	 */
	void setUser(Unit unit);
	
}
