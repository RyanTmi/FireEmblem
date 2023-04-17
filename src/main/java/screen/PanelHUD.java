package screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import helpers.Const;
import helpers.StringHelper;
import model.fight.Fight;
import model.unit.Unit;
import java.util.ArrayList;

public class PanelHUD extends Table {

    private final Main game;

    private Label nameLabel, otherNameLabel;
    private Label defenseLabel, otherDefenseLabel;
    private Label attackLabel, otherAttackLabel;
    private Label hitPointsLabel, otherHitPointsLabel;
    private Label specialLabel, otherSpecialLabel;
    private Label speedLabel, otherSpeedLabel;
    private Label resistanceLabel, otherResistanceLabel;
    private Label weaponLabel, otherWeaponLabel;
    private Label assistLabel, otherAssistLabel;
    private final ArrayList<Label> unitsLabel;
    private final ArrayList<Label> otherUnitsLabel;

    private Pixmap currentTeamColorPixmap;
    private Image imageTeamColor;

    private final Skin skin;

    public PanelHUD(final Main game) {
        this.game = game;
        this.skin = game.getSkin();
        setSkin(skin);

        setBounds(3f * Const.V_WIDTH / 4f, 0, Const.V_WIDTH / 4f, Const.V_HEIGHT);
        top();
        defaults().pad(10);
        initTopPanel();
        initUnitsLabel();

        this.unitsLabel = new ArrayList<>();
        this.otherUnitsLabel = new ArrayList<>();
        setLabelList(unitsLabel);
        setLabelList(otherUnitsLabel);

        addUnitLabel(unitsLabel);
        row();
        addUnitLabel(otherUnitsLabel);

        setBackground(skin.getDrawable("panel"));
    }

    public void update() {
        setBounds(3f * Gdx.graphics.getWidth() / 4f, 0, Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight());
    }

    private void setLabelList(ArrayList<Label> labels) {
        boolean other = labels == otherUnitsLabel;
        labels.add(other ? otherNameLabel : nameLabel);
        labels.add(other ? otherDefenseLabel : defenseLabel);
        labels.add(other ? otherAttackLabel : attackLabel);
        labels.add(other ? otherHitPointsLabel : hitPointsLabel);
        labels.add(other ? otherSpecialLabel : specialLabel);
        labels.add(other ? otherSpeedLabel : speedLabel);
        labels.add(other ? otherResistanceLabel : resistanceLabel);
        labels.add(other ? otherWeaponLabel : weaponLabel);
        labels.add(other ? otherAssistLabel : assistLabel);
    }

    private void initUnitsLabel() {
        nameLabel = new Label("", skin, "default");
        otherNameLabel = new Label("", skin, "default");
        defenseLabel = new Label("", skin, "default");
        otherDefenseLabel = new Label("", skin, "default");
        attackLabel = new Label("", skin, "default");
        otherAttackLabel = new Label("", skin, "default");
        hitPointsLabel = new Label("", skin, "default");
        otherHitPointsLabel = new Label("", skin, "default");
        specialLabel = new Label("", skin, "default");
        otherSpecialLabel = new Label("", skin, "default");
        speedLabel = new Label("", skin, "default");
        otherSpeedLabel = new Label("", skin, "default");
        resistanceLabel = new Label("", skin, "default");
        otherResistanceLabel = new Label("", skin, "default");
        weaponLabel = new Label("", skin, "default");
        otherWeaponLabel = new Label("", skin, "default");
        assistLabel = new Label("", skin, "default");
        otherAssistLabel = new Label("", skin, "default");
    }

    private void addUnitLabel(ArrayList<Label> labels) {
        add(labels.get(0)).expandX().colspan(3);
        row();
        add(labels.get(3)).expandX().colspan(2);
        add(labels.get(8)).expandX();
        row();
        add(labels.get(2)).expandX();
        add(labels.get(5)).expandX();
        add(labels.get(4)).expandX();
        row();
        add(labels.get(1)).expandX();
        add(labels.get(6)).expandX();
        add(labels.get(7)).expandX();
    }

    private void initTopPanel() {
        currentTeamColorPixmap = new Pixmap(25, 25, Pixmap.Format.RGBA8888);
        currentTeamColorPixmap.setColor(Color.RED);
        currentTeamColorPixmap.fillRectangle(0, 0, 25, 25);
        imageTeamColor = new Image(new Texture(currentTeamColorPixmap));

        TextButton passCurrentTeamMovementsButton = new TextButton("Skip round", skin, "default");
        passCurrentTeamMovementsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getScreenController().playClickButtonSound();
                game.getGameController().nextRound();
            }
        });
        TextButton exitButton = new TextButton("Exit", skin, "red");
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getScreenController().playClickButtonSound();
                game.getGameScreen().showExitDialog();
            }
        });
        TextButton cancelMovementButton = new TextButton("Cancel movement", skin, "default");
        cancelMovementButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getScreenController().playClickButtonSound();
                game.getGameController().cancelUnitMovement();
            }
        });

        add(imageTeamColor).colspan(2);
        add(passCurrentTeamMovementsButton).fillX();
        row();
        add(cancelMovementButton).fillX().colspan(2);
        add(exitButton).fillX();
        row();
    }

    // GETTERS
    @Override
    public Skin getSkin() {
        return this.skin;
    }

    public Pixmap getCurrentTeamColorPixmap() {
        return this.currentTeamColorPixmap;
    }

    // SETTERS
    public void setCurrentTeamColorPixmap(Color currentTeamColor) {
        currentTeamColorPixmap.setColor(currentTeamColor);
        currentTeamColorPixmap.fillRectangle(0, 0, 25, 25);
        imageTeamColor.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(currentTeamColorPixmap))));
    }

    public void setColorNameLabel(Unit unit, boolean other) {
        Label label = other ? otherNameLabel : nameLabel;
        switch (unit.getTeamNumber()) {
            case 0:
                label.setColor(Color.RED);
                break;
            case 1:
                label.setColor(Color.BLUE);
                break;
            case 2:
                label.setColor(Color.TEAL);
                break;
            default:
                label.setColor(Color.WHITE);
                break;
        }
    }

    public void setVisibleLabels(boolean visible, boolean other) {
        if (other) {
            for (Label l : otherUnitsLabel) {
                l.setVisible(visible);
            }
            return;
        }
        for (Label l : unitsLabel) {
            l.setVisible(visible);
        }
    }

    public void setUnitLabels(Unit unit, boolean other) {
        ArrayList<Label> labels = other ? otherUnitsLabel : unitsLabel;
        labels.get(3).setText("HP : " + getColoredStringForHealthUnit(unit.getHp(), unit.getMaxHp()));
        labels.get(0).setText(unit.getMoveType().toString());
        labels.get(1).setText("Def : " + unit.getDef());
        labels.get(2).setText("Att : " + unit.getDisplayedAtk());
        if (unit.getSpecial() != null) {
            labels.get(4).setText("Spe : " + unit.getSpecial() + " " +  unit.getSpecial().getCounter());
            labels.get(4).setColor(Color.WHITE);
        } else {
            labels.get(4).setText("Spe : none");
            labels.get(4).setColor(Color.WHITE);
        }
        labels.get(5).setText("Spd : " + unit.getSpeed());
        labels.get(6).setText("Res : " + unit.getRes());
        labels.get(7).setText("Weapon : " + unit.getWeapon());
        if (unit.hasAssist()) {
        	labels.get(8).setText("Ast : " + unit.getAssist());
        } else {
        	labels.get(8).setText("Ast : none");
        }
    }


    //pour eviter les problemes de compatibilite avec l'ancien code
    public void setCombatPreviewLabels(Fight fight, Unit attacker, Unit defender) {
    	this.setCombatPreviewLabels(fight, attacker, defender, true);
    }
    
    /**
     * @param fight le module qui gere les combats
     * @param defender Permet d'afficher les stats de vie et de défense pour renseigner le joueur avant de lancer un combat
     * @param isFight Indique s'il s'agit d'un combat ou d'un heal, <code>false</code> pour heal
     */
    public void setCombatPreviewLabels(Fight fight, Unit attacker, Unit defender, boolean isFight) {
        ArrayList<Label> labels = otherUnitsLabel;
        ArrayList<Label> unitLabels = unitsLabel;
       
        labels.get(0).setText(defender.getMoveType().toString());
        labels.get(1).setText("Def : " + defender.getDef());
        labels.get(2).setText("Att : " + defender.getDisplayedAtk());
        labels.get(6).setText("Res : " + defender.getRes());
        labels.get(5).setText("Spd : " + defender.getSpeed());
        labels.get(7).setText("Weapon : " + defender.getWeapon());
        if (defender.hasAssist()) {
        	labels.get(8).setText("Ast : " + defender.getAssist());
        } else {
        	labels.get(8).setText("Ast : none");
        }
        
        //preview pour un combat
        if (isFight) {
            // array qui stocke la prediction des degats
            int[] damageArray = fight.damagePreview(attacker, defender);
            // potentialDmg correspond aux hp restants du defenseur apres l'attaque
            int potentialDmg = damageArray[1];
            labels.get(3).setText("HP : " + getColoredStringForHealthUnit(defender.getHp(), defender.getMaxHp()) + " --> " + getColoredStringForHealthUnit(potentialDmg, defender.getMaxHp()));
        	if (defender.getSpecial() != null) {
                labels.get(4).setText("Spe : " + defender.getSpecial() + " " +  defender.getSpecial().getCounter());
                //on met le label de special en violet si le special s'active
                if (damageArray[3] == 1) {
                	labels.get(4).setColor(Color.PURPLE);
                } else {
                	labels.get(4).setColor(Color.WHITE);
                }
            } else {
                labels.get(4).setText("Spe : none");
                labels.get(4).setColor(Color.WHITE);
            }
        	
        	//correspond aux hp restants de l'assaillant apres l'attaque
            int unitDmg = damageArray[0];
            unitLabels.get(3).setText("HP : " + getColoredStringForHealthUnit(attacker.getHp(), attacker.getMaxHp()) + " --> " + getColoredStringForHealthUnit(unitDmg, attacker.getMaxHp()));
            if (attacker.getSpecial() != null && damageArray[2] == 1) {
            	unitLabels.get(4).setColor(Color.PURPLE);
            }
        } else {
        	int[] healArray = fight.healPreview(attacker, defender);
        	//potentialHeal correspond aux hp restants du defenseur apres heal
        	int potentialHeal = healArray[0];
            labels.get(3).setText("HP : " + getColoredStringForHealthUnit(defender.getHp(), defender.getMaxHp()) + " --> " + getColoredStringForHealthUnit(potentialHeal, defender.getMaxHp()));
            if (attacker.getSpecial() != null && healArray[1] == 1) {
            	unitLabels.get(4).setColor(Color.PURPLE);
            }
            if (defender.getSpecial() != null) {
                labels.get(4).setText("Spe : " + defender.getSpecial() + " " +  defender.getSpecial().getCounter());
            } else {
                labels.get(4).setText("Spe : none");
            }
        }
    }

    private String getColoredStringForHealthUnit(int hp, int maxHp) {
        if ((float)hp / (float)maxHp <= .3f) {
            return StringHelper.redString(hp + "/" + maxHp);
        }
        if ((float)hp / (float)maxHp >= .6f) {
            return StringHelper.greenString(hp + "/" + maxHp);
        }
        return StringHelper.orangeString(hp + "/" + maxHp);
    }
}
