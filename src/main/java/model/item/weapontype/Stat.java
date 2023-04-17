package model.item.weapontype;

public interface Stat {

    int getDamage();

    int getPrecision();

    int getWeigth();

    int getDurability();

    int getCritical();
    
    /**
     * @return <code>true</code> si l'arme calcule les degats avec la defense ennemie
     */
    boolean isPhysical();

    /**
     * @return un tableau dont le premiere indice est la portée minimal inclus et le deuxieme indice
     * est la portée maximal inclus
     */
    int[] getRange();
}
