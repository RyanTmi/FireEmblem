package model.unit;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import helpers.Assets;
import model.item.Weapon;
import model.unit.assist.Assist;
import model.unit.movetype.*;
import model.unit.special.Special;

public class Unit extends Actor {

	private final UnitStat stats;
	private Weapon weapon;
	private Weapon[] allowedWeaponTypes;
	private final MoveType moveType;
	private Special special;
	private Assist assist;
	private Texture texture;
	private int teamNumber;

	public Unit(UnitStat stats, Weapon weapon, Weapon[] weaponTypes, MoveType moveType, int teamNumber, Assets assets) {
		this.stats = stats;
		this.weapon = weapon;
		this.allowedWeaponTypes = weaponTypes;
		this.moveType = moveType;
		this.teamNumber = teamNumber;
		this.texture = assets.getAssetManager().get(getMatchingTexture(moveType, teamNumber));

		// Check if weapon can be equipped by unit, if not add weapon to the allowed weapon types
		if (!this.canEquipWeapon(this.weapon)) {
			this.allowedWeaponTypes = new Weapon[weaponTypes.length+1];
			System.arraycopy(weaponTypes, 0, this.allowedWeaponTypes, 0, weaponTypes.length);
			this.allowedWeaponTypes[this.allowedWeaponTypes.length-1] = this.weapon;
			
			this.texture = assets.getAssetManager().get(getMatchingTexture(moveType, teamNumber));
		}
	}
	
	public Unit(int hp, int atk, int spd, int def, int res, Weapon weapon, Weapon[] weaponType, MoveType moveType) {
		this.stats = new UnitStat(hp, atk, spd, def, res);
		this.weapon = weapon;
		this.allowedWeaponTypes = weaponType;
		this.moveType = moveType;
	}

	/**
	 * Indique si l'unité a au moins 1 HP.
	 * @return <code>true</code> si l'unité a au moins 1 HP.
	 */
	public boolean isAlive() {
		return this.getHp() > 0;
	}
	
	/**
	 * Indique si l'unité a 5 points de vitesse ou plus que l'unité passée en paramètre.
	 * @param unit l'unité avec laquelle comparer la vitesse.
	 * @return <code>true</code> si l'unité qui appelle la fonction est plus rapide d'au moins 5 points.
	 */
	public boolean unitSpeedAllowsDoubleAttack(Unit unit) {
		return this.getSpeed() >= unit.getSpeed() + 5;
	}

	public boolean hasSpecial() {
		return this.special != null;
	}
	
	/**
	 * Ajoute une valeur aux HP de l'unité.
	 * @param value la valeur à ajouter, positive ou négative.
	 */
	public void addHp(int value) {
		this.stats.addHp(value);
	}

	/**
	 * Vérifie si l'unité peut équiper l'arme passée en paramètre.
	 * @param weapon l'arme à vérifier.
	 * @return <code>true</code> si l'unité peut équiper l'arme ou si l'arme passée en paramètre est <code>null</code>.
	 */
	public boolean canEquipWeapon(Weapon weapon) {
		if (weapon == null) {
			return true;
		}
		for (Weapon allowedWeaponType : this.allowedWeaponTypes) {
			if (weapon.getClass() == allowedWeaponType.getClass()) {
				return true;
			}
		}
		return false;
	}

	// GETTERS
	public int getHp() {
		if (this.stats == null) return 0;
		return this.stats.getHp();
	}

	public int getMaxHp() {
		if (this.stats == null) return 0;
		return this.stats.getMaxHp();
	}

	public int getSpeed() {
		if (this.stats == null) return 0;
		return this.stats.getSpeed();
	}

	public int getRes() {
		if (this.stats == null) return 0;
		return this.stats.getRes();
	}

	public int getDef() {
		if (this.stats == null) return 0;
		return this.stats.getDef();
	}

	public Special getSpecial() {
		return this.special;
	}

	public void setSpecial(Special sp) {
		this.special = sp;
		this.special.setUser(this);
	}
	
	public boolean hasAssist() {
		return this.assist != null;
	}
	
	public Assist getAssist() {
		return this.assist;
	}
	
	public void setAssist(Assist a) {
		this.assist = a;
		this.assist.setUser(this);
	}
	
	/**
	 * @return la valeur d'attaque de l'unité sans arme.
	 */
	public int getAtk() {
		if (this.stats == null) return 0;
		return this.stats.getAtk();
	}

	/**
	 * @return la valeur d'attaque de l'unité avec arme.
	 */
	public int getDisplayedAtk() {
		int tmp = 0;
		if (this.stats!=null) tmp += this.stats.getAtk();
		if (this.weapon != null) tmp += this.weapon.getDamage();
		return tmp;
	}

	/**
	 * Renvoie la même chose que <code>this.weapon.getRange</code>.
	 * Renvoie {0, 0} si l'unité ne possède pas d'arme.
	 * @return un array avec la portée minimale en indice 0 et maximale en indice 1.
	 * @see model.item.Weapon#getRange()
	 */
	public int[] getRange() {
		if (this.weapon == null) {
			return new int[]{0, 0};
		}
		return this.weapon.getRange();
	}

	/**
	 * Renvoie la même chose que <code>this.moveType.getMovementRange</code>.
	 * Renvoie 0 si <code>moveType</code> est <code>null</code>.
	 * @return la distance maximale que peut parcourir l'unité en un tour.
	 * @see model.unit.movetype.MoveType#getMovementRange()
	 */
	public int getMovementRange() {
		if (this.moveType == null) {
			return 0;
		}
		return this.moveType.getMovementRange();
	}

	public static String getMatchingTexture(MoveType moveType, int teamNumber) {
		switch (teamNumber) {
			case 0:
				if (moveType instanceof Infantry) {
					return "img/unit_img/inf_0.png";
				} else if (moveType instanceof Flying) {
					return "img/unit_img/fly_0.png";
				} else if (moveType instanceof Armor) {
					return "img/unit_img/arm_0.png";
				} else if (moveType instanceof Cavalry) {
					return "img/unit_img/cav_0.png";
				}
				break;
			case 1:
				if (moveType instanceof Infantry) {
					return "img/unit_img/inf_1.png";
				} else if (moveType instanceof Flying) {
					return "img/unit_img/fly_1.png";
				} else if (moveType instanceof Armor) {
					return "img/unit_img/arm_1.png";
				} else if (moveType instanceof Cavalry) {
					return "img/unit_img/cav_1.png";
				}
				break;
			case 2:
				if (moveType instanceof Infantry) {
					return "img/unit_img/inf_2.png";
				} else if (moveType instanceof Flying) {
					return "img/unit_img/fly_2.png";
				} else if (moveType instanceof Armor) {
					return "img/unit_img/arm_2.png";
				} else if (moveType instanceof Cavalry) {
					return "img/unit_img/cav_2.png";
				}
				break;

			case 3:
				if (moveType instanceof Infantry) {
					return "img/unit_img/inf_3.png";
				} else if (moveType instanceof Flying) {
					return "img/unit_img/fly_3.png";
				} else if (moveType instanceof Armor) {
					return "img/unit_img/arm_3.png";
				} else if (moveType instanceof Cavalry) {
					return "img/unit_img/cav_3.png";
				}
				break;
		}
		return "";
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public Texture getTexture() {
		return texture;
	}

	public MoveType getMoveType() {
		return this.moveType;
	}

	public int getTeamNumber() {
		return this.teamNumber;
	}
}
