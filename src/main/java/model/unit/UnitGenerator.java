package model.unit;

import helpers.Assets;
import model.item.Weapon;
import model.item.material.*;
import model.item.weapontype.*;
import model.unit.assist.*;
import model.unit.movetype.*;
import model.unit.special.*;

public class UnitGenerator {

	public static Unit generateSwordInfantry(Assets assets, int teamNumber) {
		final UnitStat DEFAULT_STATS = new UnitStat(44, 31, 31, 30, 22);
		final Weapon DEFAULT_WEAPON = new Weapon(new Sword(), new Steel());
		final Weapon[] DEFAULT_WEAPON_TYPES = {DEFAULT_WEAPON};
		final MoveType DEFAULT_MOVE_TYPE = new Infantry();
		return new Unit(DEFAULT_STATS, DEFAULT_WEAPON, DEFAULT_WEAPON_TYPES, DEFAULT_MOVE_TYPE, teamNumber, assets);
	}
	
	public static Unit generateBowInfantry(Assets assets,int teamNumber) {
		final UnitStat DEFAULT_STATS = new UnitStat(43, 32, 29, 29, 16);
		final Weapon DEFAULT_WEAPON = new Weapon(new Bow(), new Steel());
		final Weapon[] DEFAULT_WEAPON_TYPES = {DEFAULT_WEAPON};
		final MoveType DEFAULT_MOVE_TYPE = new Infantry();
		return new Unit(DEFAULT_STATS, DEFAULT_WEAPON, DEFAULT_WEAPON_TYPES, DEFAULT_MOVE_TYPE, teamNumber, assets);
	}

    public static Unit generateSpearCavalry(Assets assets, int teamNumber){
        final UnitStat DEFAULT_STATS = new UnitStat(41, 34, 24, 25, 30);
        final Weapon DEFAULT_WEAPON = new Weapon(new Spear(), new Steel());
        final Weapon[] DEFAULT_WEAPON_TYPES = {DEFAULT_WEAPON};
        final MoveType DEFAULT_MOVE_TYPE = new Cavalry();
		return new Unit(DEFAULT_STATS, DEFAULT_WEAPON, DEFAULT_WEAPON_TYPES, DEFAULT_MOVE_TYPE, teamNumber, assets);
    }
	
	public static Unit generateCatria(Assets assets, int teamNumber) {
		final UnitStat DEFAULT_STATS = new UnitStat(39, 31, 37, 29, 22);
		final Weapon DEFAULT_WEAPON = new Weapon(new Spear(), new Silver());
		final Weapon[] DEFAULT_WEAPON_TYPES = {DEFAULT_WEAPON};
		final Special DEFAULT_SPECIAL = new Luna();
		final MoveType DEFAULT_MOVE_TYPE = new Flying();
        Unit unit = new Unit(DEFAULT_STATS, DEFAULT_WEAPON, DEFAULT_WEAPON_TYPES, DEFAULT_MOVE_TYPE, teamNumber, assets);
		unit.setSpecial(DEFAULT_SPECIAL);
		return unit;
	}
	
	public static Unit generateHector(Assets assets, int teamNumber) {
		final UnitStat DEFAULT_STATS = new UnitStat(52, 39, 24, 37, 16);
		final Weapon DEFAULT_WEAPON = new Weapon(new Axe(), new Silver());
		final Weapon[] DEFAULT_WEAPON_TYPES = {DEFAULT_WEAPON};
		final Special DEFAULT_SPECIAL = new Pavise();
		final MoveType DEFAULT_MOVE_TYPE = new Armor();
        Unit unit = new Unit(DEFAULT_STATS, DEFAULT_WEAPON, DEFAULT_WEAPON_TYPES, DEFAULT_MOVE_TYPE, teamNumber, assets);
		unit.setSpecial(DEFAULT_SPECIAL);
		return unit;
	}
	
	public static Unit generateMist(Assets assets, int teamNumber) {
		final UnitStat DEFAULT_STATS = new UnitStat(39, 27, 28, 20, 34);
		final Weapon DEFAULT_WEAPON = new Weapon(new Staff(), new Silver());
		final Weapon[] DEFAULT_WEAPON_TYPES = {DEFAULT_WEAPON};
		final Assist DEFAULT_ASSIST = new RecoverPlus();
		final Special DEFAULT_SPECIAL = new Imbue();
		final MoveType DEFAULT_MOVE_TYPE = new Infantry();
        Unit unit = new Unit(DEFAULT_STATS, DEFAULT_WEAPON, DEFAULT_WEAPON_TYPES, DEFAULT_MOVE_TYPE, teamNumber, assets);
		unit.setSpecial(DEFAULT_SPECIAL);
		unit.setAssist(DEFAULT_ASSIST);
		return unit;
	}
}
