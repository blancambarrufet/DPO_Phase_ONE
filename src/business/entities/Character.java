package business.entities;

import java.util.Random;

public class Character {
    private long id;
    private String name;
    private int weight;
    private double damageTaken;
    private Weapon weapon;
    private Armor armor;
    private boolean defending;

    public Character(long id, String name, int weight) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.damageTaken = 0;
        this.weapon = null;
        this.armor = null;
        this.defending = false;
    }

    public long getId() {
        return id;
    }
    public  String getName() {
        return name;
    }
    public int getWeight() {
        return weight;
    }
    public double getDamageTaken() { return damageTaken; }
    public boolean isDefending() { return defending; }

    public Weapon getWeapon() {
        return weapon;
    }

    public Armor getArmor() {
        return armor;
    }

    //equipping Weapon
    public void equipWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    //Equipping Armor
    public void equipArmor(Armor armor) {
        this.armor = armor;
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

        attack = ((weight * (1 - damageTaken)) / 10.0) + (weaponAttack/ 20.0) + 18;



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
        defenseValue = ((200 * (1 - damageTaken)) / weight) + (armorValue / 20.0);

        //calculate the final damage
        finalDamage = (incomingAttack - ((defenseValue) * 1.4)) / 100.0;

        //check if character is defending to apply defense BONUS
        if (defending) {
            damageReduction = weight / 400.0;
            finalDamage -= damageReduction;
        }

        return finalDamage;
        // return Math.max(finalDamage, 0); // Ensure non-negative damage
    }

    //applying damage (Step 3 of 2.5.2)
    public void takeDamage(double damage) {
        damageTaken += damage;
    }

    //setting the position of the character to DEFEND
    public void defend() {
        defending = true;
    }

    // End turn
    public void endTurn() {
        defending = false;
    }

    // Check if character is KO
    public boolean isKO() {
        Random random = new Random();

        // Random value between 1-200
        int knockOutThreshold = random.nextInt(200) + 1;

        // KO if threshold < accumulated damage
        return knockOutThreshold < (damageTaken * 100);
    }

}
