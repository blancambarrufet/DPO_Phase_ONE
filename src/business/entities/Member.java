package business.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Random;

public class Member {
    private long id;
    private String strategy;
    private Character character;
    private double damageTaken;
    private Weapon weapon;
    private Armor armor;
    private boolean defending;
    private boolean isKO;

    public enum Strategy { //THIS SHOULD BE IN A CLASS ENUM
        BALANCED, AGGRESSIVE, DEFENSIVE
    }

    public Member(long id, Character character, String strategy) {
        this.id = id;
        this.character = character;
        this.strategy = strategy;
        this.damageTaken = 0;
        this.weapon = null;
        this.armor = null;
        this.defending = false;
        this.isKO = false;
    }

    // Getters and Setters
    public Character getCharacter() {return character;}
    public void setCharacter(Character character) {
        this.character = character;
    }
    public long getCharacterId() { return id; }
    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }
    public String getName() { return character.getName(); }
    public int getWeight() { return character.getWeight(); }

    public double getDamageTaken() {
        return damageTaken;
    }
    public boolean isDefending() {
        return defending;
    }
    public Weapon getWeapon() {
        return weapon;
    }
    public Armor getArmor() {
        return armor;
    }

    // -----Combat methods-----
    public void equipWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
    public void equipArmor(Armor armor) {
        this.armor = armor;
    }

    //applying damage (Step 3 of 2.5.2)
    public void takeDamage(double damage) {
        damageTaken += damage;
    }

    //setting the position of the character to DEFEND
    public void defend() {
        defending = true;
    }

    public void resetDefending() {
        defending = false;
    }

    public void endTurn() {
        defending = false;
    }
    public void resetDamage() {
        this.damageTaken = 0;
    }

    // Check if character is KO
    public boolean checkForKO() {
        Random random = new Random();

        // Random value between 0-200
        double knockOutValue = (random.nextInt(200) + 1) / 100.0;

       if (knockOutValue > damageTaken) {
            this.isKO = true;
        }

        return isKO;
    }

    public boolean IsKO() {
        return isKO;
    }

    public double calculateAttack() {
        double attack;
        double weaponAttack;

        if (weapon != null) {
            weaponAttack = weapon.getAttackPower();
        }
        else {
            weaponAttack = 0;
        }

        attack = ((getWeight() * (1 - damageTaken)) / 10.0) + (weaponAttack/ 20.0) + 18;

        return attack;
    }

    public double calculateFinalDamage(double incomingAttack) {
        double armorValue;
        double defenseValue;
        double finalDamage;
        double damageReduction;

        //calculate the armor value
        if (armor != null) {
            armorValue = armor.getDefenseValue();
        }
        else {
            armorValue = 0;
        }

        //calculate the defense value
        defenseValue = ((200 * (1 - damageTaken)) / getWeight()) + (armorValue / 20.0);

        //calculate the final damage
        finalDamage = (incomingAttack - ((defenseValue) * 1.4)) / 100.0;

        //check if character is defending to apply defense BONUS
        if (defending) {
            damageReduction = getWeight() / 400.0;
            finalDamage -= damageReduction;
        }

        //return finalDamage;
        return Math.max(finalDamage, 0); //there will be no negative damage
    }
}
