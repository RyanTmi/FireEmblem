package model.fight;

import model.unit.Unit;
import model.unit.assist.*;
import model.unit.movetype.Flying;
import model.GameModel;
import model.unit.special.*;
import model.item.weapontype.*;
import com.badlogic.gdx.math.Vector2;

public class Fight {
	
	private final GameModel gameModel;
    
    /**
     * Gere un combat entre 2 unites, on suppose que l'assaillant a la portee necessaire pour effectuer l'attaque
     * L'assaillant attaque en premier puis le defenseur riposte (s'il a la portee necessaire)
     * L'assaillant ou le defenseur peut attaquer de nouveau en fonction de leur vitesse
     * @param ass l'unite qui initie l'attaque
     * @param def l'unite qui subit l'attaque
     */
    public void fight(Unit ass, Unit def){

    	int damage = this.calculateDamage(ass, def);
        def.addHp((-1) * damage );

        if (def.isAlive()){

            if (this.isDefenderInRange(ass, def)){
            	damage = this.calculateDamage(def, ass);
                ass.addHp((-1) * damage );
            }
        }

        else{
        	this.removeUnit(def);
        	return;
        }

        if (ass.isAlive()) {
            if (ass.unitSpeedAllowsDoubleAttack(def)) {
            	damage = this.calculateDamage(ass, def);
                def.addHp((-1) * damage );

                if (!def.isAlive()){
                	this.removeUnit(def);
				}
            } else if (def.unitSpeedAllowsDoubleAttack(ass) && this.isDefenderInRange(ass, def)) {
            	damage = this.calculateDamage(def, ass);
                ass.addHp((-1) * damage );

                if (!ass.isAlive()){
                	this.removeUnit(ass);
                }
            }
        } else {
        	this.removeUnit(ass);
        }
    }
    
    public Fight(GameModel g) {
    	this.gameModel = g;
    }
    
    private boolean isDefenderInRange(Unit attacker, Unit defender) {
    	int distance = (int) gameModel.getUnitTeam(attacker.getTeamNumber()).getUnitList().get(attacker).getLocation().dst(
                gameModel.getUnitTeam(defender.getTeamNumber()).getUnitList().get(defender).getLocation());
        int[] rep = defender.getRange();
        return distance>=rep[0] && distance<=rep[1];
    }
    
    // Peut etre mettre cette fonction ailleurs
    private void removeUnit(Unit unit) {
    	gameModel.getUnitTeam(unit.getTeamNumber()).getUnitList().remove(unit);
    }
    
    /**
  	 * Calcule les degats causes par l'attaque d'une unite sans prendre en compte les speciaux
  	 * Ne prend pas en compte les bonus de terrain
  	 * Ne prend pas en compte le triangle des armes
     * @return les degats en valeur absolue
     */
    private int calculateRawDamage(Unit attacker, Unit defender) {
    	// On recupere la position des unites pour calculer les bonus de degats
    	Vector2 atkPos = gameModel.getUnitTeam(attacker.getTeamNumber()).getUnitList().get(attacker).getLocation();
    	Vector2 defPos = gameModel.getUnitTeam(defender.getTeamNumber()).getUnitList().get(defender).getLocation();

    	if (attacker.getWeapon() == null) return 0;
    	int damage;
    	if (attacker.getWeapon().isPhysical()) {
    		damage = attacker.getDisplayedAtk() - defender.getDef();
    	} else {
    		damage = attacker.getDisplayedAtk() - defender.getRes();
    	}
    	if (damage < 0) damage = 0;
    	return damage;
    }
    
    /**
  	 * Calcule les degats causes par l'attaque d'une unite en prenant en compte les speciaux
  	 * Gere la gestion du compteur de l'assaillant et du defenseur
  	 * Prend en compte le triangle des armes
     * @return les degats en valeur absolue
     */
    private int calculateDamage(Unit attacker, Unit defender) {
    	int damage = calculateRawDamage(attacker, defender);
    	
    	if (attacker.hasSpecial() && attacker.getSpecial() instanceof AttackSpecial) {
    		if (attacker.getSpecial().canActivateSkill()) {
    			damage += ((AttackSpecial)attacker.getSpecial()).getEffectValue(damage, attacker, defender);
    			attacker.getSpecial().resetCounter();
    		} else {
    			attacker.getSpecial().decreaseCounter();
    		}
    	}
    	
    	if (defender.hasSpecial() && defender.getSpecial() instanceof DefenseSpecial) {
    		if (defender.getSpecial().canActivateSkill()) {
    			damage -= ((DefenseSpecial)defender.getSpecial()).getEffectValue(damage, attacker, defender);
    			defender.getSpecial().resetCounter();
    		} else {
    			defender.getSpecial().decreaseCounter();
    		}
    	}
    	damage += this.weaponTriangleBonus(attacker, defender);
    	if (damage < 0) damage = 0;
    	return damage;
    }
    
    private int weaponTriangleBonus(Unit attacker, Unit defender) {
    	if (attacker.getWeapon() == null) return 0;
    	//dmg + 50% pour les archers contre les unites volantes
    	int bonus = 0;
    	if (attacker.getWeapon().getType() instanceof Bow && defender.getMoveType() instanceof Flying) bonus += attacker.getDisplayedAtk() / 2;
    	if (defender.getWeapon() == null) return bonus;
    	
    	//dmg + 20%
    	if (attacker.getWeapon().getType() instanceof Sword && defender.getWeapon().getType() instanceof Axe) bonus += (attacker.getDisplayedAtk() + bonus) / 5;
    	else if (attacker.getWeapon().getType() instanceof Axe && defender.getWeapon().getType() instanceof Spear) bonus += (attacker.getDisplayedAtk() + bonus) / 5;
    	else if (attacker.getWeapon().getType() instanceof Spear && defender.getWeapon().getType() instanceof Sword) bonus += (attacker.getDisplayedAtk() + bonus) / 5;
    	
    	//dmg - 20%
    	else if (attacker.getWeapon().getType() instanceof Sword && defender.getWeapon().getType() instanceof Spear) bonus -= (attacker.getDisplayedAtk() + bonus) / 5;
    	else if (attacker.getWeapon().getType() instanceof Axe && defender.getWeapon().getType() instanceof Sword) bonus -= (attacker.getDisplayedAtk() + bonus) / 5;
    	else if (attacker.getWeapon().getType() instanceof Spear && defender.getWeapon().getType() instanceof Axe) bonus -= (attacker.getDisplayedAtk() + bonus) / 5;
    	
    	return bonus;
    }
    
    // FIXME factoriser cette portion de code
    // FIXME trouver une facon plus modulaire de stocker les resultats
    /**
     * Predit les resultats d'un combat entre 2 unites
     * @return un array qui contient dans l'ordre: hp restants de l'assaillant, hp restants du defenseur, activation du special de l'assaillant 1==true, activation du special du defenseur
     */
    public int[] damagePreview(Unit attacker, Unit defender) {
    	int[] results = {attacker.getHp(), defender.getHp(), 0, 0};
    	int atkHP = attacker.getHp();
    	int defHP = defender.getHp();
    	int atkCounter = 9999;
    	int defCounter = 9999;
    	if (attacker.hasSpecial()) atkCounter = attacker.getSpecial().getCounter();
    	if (defender.hasSpecial()) defCounter = defender.getSpecial().getCounter();
    	
    	// Calcul des degats infliges par l'assaillant
    	int damage = calculateRawDamage(attacker, defender);
    	
    	if (attacker.hasSpecial() && attacker.getSpecial() instanceof AttackSpecial) {
    		if (atkCounter == 0) {
    			damage += ((AttackSpecial)attacker.getSpecial()).getEffectValue(damage, attacker, defender);
        		atkCounter = attacker.getSpecial().getDefaultCounter();
        		results[2] = 1;
    		} else {
    			atkCounter--;
    		}
    	}
    	
    	if (defender.hasSpecial() && defender.getSpecial() instanceof DefenseSpecial) {
    		if (defCounter == 0) {
    			damage -= ((DefenseSpecial)defender.getSpecial()).getEffectValue(damage, attacker, defender);
        		defCounter = defender.getSpecial().getDefaultCounter();
        		results[3] = 1;
    		} else {
    			defCounter--;
    		}
    	}
    	
    	damage += this.weaponTriangleBonus(attacker, defender);
    	if (damage > 0) defHP -= damage;
    	
    	// Calcul des degats de riposte du defenseur s'il est encore vivant
    	if (defHP > 0) {
    		// Verification que le defenseur a la portee pour riposter
    		if (this.isDefenderInPredictedRange(attacker, defender)) {
    			damage = calculateRawDamage(defender, attacker);
        		if (defender.hasSpecial() && defender.getSpecial() instanceof AttackSpecial) {
        			if (defCounter == 0) {
        				damage += ((AttackSpecial)defender.getSpecial()).getEffectValue(damage, defender, attacker);
                		defCounter = defender.getSpecial().getDefaultCounter();
                		results[3] = 1;
        			} else {
                		defCounter--;
        			} 
            	}
        		if (attacker.hasSpecial() && attacker.getSpecial() instanceof DefenseSpecial) {
        			if (atkCounter == 0) {
        				damage -= ((DefenseSpecial)attacker.getSpecial()).getEffectValue(damage, defender, attacker);
                		atkCounter = attacker.getSpecial().getDefaultCounter();
                		results[2] = 1;
        			} else {
        				atkCounter--;
        			}	
            	}
        		damage += this.weaponTriangleBonus(defender, attacker);
        		if (damage > 0) atkHP -= damage;
    		}
    		
    		// Calcul des degats de la seconde riposte si les 2 unites sont encore vivantes et si l'une d'entre elles peut encore attaquer
    		if (atkHP > 0) {
        		if (attacker.unitSpeedAllowsDoubleAttack(defender)) {
        			damage = calculateRawDamage(attacker, defender);
        			if (attacker.hasSpecial() && attacker.getSpecial() instanceof AttackSpecial) {
        	    		if (atkCounter == 0) {
        	    			damage += ((AttackSpecial)attacker.getSpecial()).getEffectValue(damage, attacker, defender);
        	        		atkCounter = attacker.getSpecial().getDefaultCounter();
        	        		results[2] = 1;
        	    		}
        	    	}
        	    	
        	    	if (defender.hasSpecial() && defender.getSpecial() instanceof DefenseSpecial) {
        	    		if (defCounter == 0) {
        	    			damage -= ((DefenseSpecial)defender.getSpecial()).getEffectValue(damage, attacker, defender);
        	        		defCounter = defender.getSpecial().getDefaultCounter();
        	        		results[3] = 1;
        	    		}
        	    	}
        	    	damage += this.weaponTriangleBonus(attacker, defender);
        	    	if (damage > 0) defHP -= damage;
        	    	
        		} else if (defender.unitSpeedAllowsDoubleAttack(attacker) && this.isDefenderInPredictedRange(attacker, defender)) {
        			damage = calculateRawDamage(defender, attacker);
        			if (defender.hasSpecial() && defender.getSpecial() instanceof AttackSpecial) {
            			if (defCounter == 0) {
            				damage += ((AttackSpecial)defender.getSpecial()).getEffectValue(damage, defender, attacker);
                    		defCounter = defender.getSpecial().getDefaultCounter();
                    		results[3] = 1;
            			}
                	}
            		if (attacker.hasSpecial() && attacker.getSpecial() instanceof DefenseSpecial) {
            			if (atkCounter == 0) {
            				damage -= ((DefenseSpecial)attacker.getSpecial()).getEffectValue(damage, defender, attacker);
                    		atkCounter = attacker.getSpecial().getDefaultCounter();
                    		results[2] = 1;
            			}
                	}
            		damage += this.weaponTriangleBonus(defender, attacker);
            		if (damage > 0) atkHP -= damage;
        		}
        	}
    	}
    	
    	// On s'assure que les valeurs des hps soient positives
    	if (atkHP < 0) atkHP = 0;
    	if (defHP < 0) defHP = 0;
    	results[0] = atkHP;
    	results[1] = defHP;
    	return results;
    }
    
    // Assume que l'assaillant peut attaquer
    private boolean isDefenderInPredictedRange(Unit attacker, Unit defender) {
    	return attacker.getRange()[1] == defender.getRange()[1];
    }
    
    public void heal(Unit healer, Unit target) {
    	if (!healer.hasAssist() || !(healer.getAssist() instanceof HealAssist)) return;
    	int healValue = this.calculateHealing(healer, target);
    	target.addHp(healValue);
    }
    
    /**
     * Predit les soins.</br>
     * Renvoie les HP de la cible apres le soin.
     * @return un array avec les HP de la cible apres le soin et si le special a ete active.
     */
    public int[] healPreview(Unit healer, Unit target) {
    	int counter = 9999;
    	int[] returnValue = new int[2];
    	if (healer.hasSpecial() && healer.getSpecial() instanceof HealSpecial) counter = healer.getSpecial().getCounter();
    	int value = target.getHp() + this.calculateRawHealing(healer, target);
    	if (counter == 0) {
    		value += ((HealSpecial)healer.getSpecial()).getEffectValue(value, healer, target);
    		returnValue[1] = 1;
    	}
    	if (value > target.getMaxHp()) value = target.getMaxHp();
    	returnValue[0] = value;
    	return returnValue;
    }
    
    private int calculateRawHealing(Unit healer, Unit target) {
		return ((HealAssist)healer.getAssist()).getEffectValue(target);
    }
    
    /**
  	 * Calcule les soins en prenant en compte les speciaux.</br>
  	 * Gere la gestion du compteur du healer.
     * @return les HP recuperes par le soin
     */
    private int calculateHealing(Unit healer, Unit target) {
    	int value = this.calculateRawHealing(healer, target);
    	if (healer.hasSpecial() && healer.getSpecial() instanceof HealSpecial) {
    		if (healer.getSpecial().canActivateSkill()) {
    			value += ((HealSpecial)healer.getSpecial()).getEffectValue(value, healer, target);
    			healer.getSpecial().resetCounter();
    		} else {
    			healer.getSpecial().decreaseCounter();
    		}
    	}
    	return value;
    }
}
