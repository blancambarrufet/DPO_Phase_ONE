package business.entities;

public class Member {
    private long id;
    private String strategy;
    private Character character;
    private double damageTaken;
    private Weapon weapon;
    private Armor armor;
    private boolean defending;
    private boolean isKO;
    private boolean defendingNextTurn;
    private double pendingDamageTaken;

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
        this.defendingNextTurn = false;
        this.pendingDamageTaken = 0;
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
    public boolean setKO(boolean status) { return isKO = status; }

    public void defendNextTurn() {
        defendingNextTurn = true;
    }

    public void applyDefending() {
        if (defendingNextTurn) {
            defending = true;
            defendingNextTurn = false; //reset the flag
        }
    }

    public double getDamageTaken() {
        return damageTaken;
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
    public void updatePendingDamage() {
        this.damageTaken += pendingDamageTaken;
        this.pendingDamageTaken = 0;// reset the accumulator
    }

    public void accumulateDamage(double damage) {
        this.pendingDamageTaken += damage;
    }

    public void resetDefending() {
        defending = false;
    }

    public void resetDamage() {
        this.damageTaken = 0;
    }

    public boolean isKO() {
        return isKO;
    }

    public double calculateAttack() {
        double attack;
        double weaponAttack;

        if (weapon != null) {
            // Check if it's a SuperWeapon
            if (weapon instanceof SuperWeapon) {
                weaponAttack = ((SuperWeapon) weapon).computeWeaponAttack(getWeight()); // Apply SuperWeapon formula
            } else {
                weaponAttack = weapon.getAttackPower(); // Normal weapon attack
            }
        } else {
            weaponAttack = 0;
        }

        attack = ((getWeight() * (1 - damageTaken)) / 10.0) + (weaponAttack / 20.0) + 18;

        return attack;
    }

    public double calculateFinalDamage(double incomingAttack) {
        double armorValue;
        double defenseValue;
        double finalDamage;
        double damageReduction;

        // Calculate armor value
        if (armor != null) {
            // Check if it's a SuperArmor
            if (armor instanceof SuperArmor) {
                armorValue = ((SuperArmor) armor).computeArmorValue(getWeight()); // Apply SuperArmor formula
            } else {
                armorValue = armor.getDefenseValue(); // Normal armor
            }
        } else {
            armorValue = 0;
        }

        // Calculate defense value
        defenseValue = ((200 * (1 - damageTaken)) / getWeight()) + (armorValue / 20.0);

        // Calculate final damage
        finalDamage = (incomingAttack - ((defenseValue) * 1.4)) / 100.0;

        // Check if character is defending to apply defense BONUS
        if (defending) {
            damageReduction = getWeight() / 400.0;
            finalDamage -= damageReduction;
            System.out.println("\tDEBUG: " + getName() + " reduces damage by " + String.format("%.2f", damageReduction));
        }

        return Math.max(finalDamage, 0); // Ensure no negative damage
    }


}
