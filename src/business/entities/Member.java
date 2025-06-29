package business.entities;

import business.CombatStrategy;

/**
 * Represents a team member participating in the game in the combat simulation or for extracting information.
 * Each member has an associated character to extract the name, weight and id. It also has a
 * strategy, damage to track, and equipped items.
 */
public class Member {
    //id of the member
    private final long id;

    //name of the strategy
    private final CombatStrategy strategy;

    //the character associated to the member
    private Character character;

    //the damage received
    private double damageTaken;

    //weapon equipped to the Member
    private Weapon weapon;

    //armor equipped to the Member
    private Armor armor;

    //flag to check if the character is in a defensive state
    private boolean defending;

    //flag to check that the character is KO
    private boolean isKO;

    //flag to check that it will defend the next turn
    private boolean defendingNextTurn;

    //the accumulated damage to be received at the end of the turn
    private double pendingDamageTaken;

    /**
     * Constructor of the Member that has a specific character and strategy
     *
     * @param id        the id of the member
     * @param character the character associated with the member
     * @param strategy  the combat strategy of the member
     */
    public Member(long id, Character character, CombatStrategy strategy) {
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

    /**
     * Get the member id
     *
     * @return The ID of the character.
     */
    public long getCharacterId() {
        return id;
    }

    /**
     * Get the member combat strategy.
     *
     * @return The strategy name.
     */
    public CombatStrategy getStrategy() {
        return strategy;
    }

    /**
     * Get the member combat strategy String format.
     *
     * @return The strategy name.
     */
    public String getStrategyName() {
        return strategy.getStrategyName();
    }


    /**
     * Get the name of the associated character.
     *
     * @return The name of the character
     */
    public String getName() {
        return character.getName();
    }

    /**
     * Get the weight of the associated character.
     *
     * @return The weight of the character
     */
    public int getWeight() {
        return character.getWeight();
    }

    /**
     * Sets the KO status of the member.
     *
     * @param status The KO status to set (true or false)
     */
    public void setKO(boolean status) {
        isKO = status;
    }

    /**
     * set the flag to defend the next turn
     */
    public void defendNextTurn() {
        defendingNextTurn = true;
    }

    /**
     * Apply the defending flag and reset the flag of the defending Next turn
     */
    public void applyDefending() {
        if (defendingNextTurn) {
            defending = true;
            defendingNextTurn = false; //reset the flag
        }
    }

    /**
     * Get the amount of damage taken.
     *
     * @return The damage received.
     */
    public double getDamageTaken() {
        return damageTaken;
    }

    /**
     * Get the equipped weapon.
     *
     * @return The equipped weapon or null if it is empty
     */
    public Weapon getWeapon() {
        return weapon;
    }

    /**
     * Get the equipped armor.
     *
     * @return The equipped amro or null if it is empty
     */
    public Armor getArmor() {
        return armor;
    }

    /**
     * Sets a weapon for the member.
     *
     * @param weapon The weapon to equip.
     */
    public void equipWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    /**
     * Sets an armor for the member.
     *
     * @param armor The armor to equip.
     */
    public void equipArmor(Armor armor) {
        this.armor = armor;
    }

    /**
     * Updates the damage taken by applying the pending damage and reset the accumulated damage to 0
     */
    public void updatePendingDamage() {
        this.damageTaken += pendingDamageTaken;
        this.pendingDamageTaken = 0;// reset the accumulator
    }

    /**
     * It accumulates the damaged received to be applied later
     *
     * @param damage The damage to accumulate.
     */
    public void accumulateDamage(double damage) {
        this.pendingDamageTaken += damage;
    }

    /**
     * Resets the defending status to false
     */
    public void resetDefending() {
        defending = false;
    }

    /**
     * Resets the damage to 0 for the beginning of the combat
     */
    public void resetDamage() {
        this.damageTaken = 0;
    }

    /**
     * Checks if the member is KO
     *
     * @return true if KO, false otherwise
     */
    public boolean isKO() {
        return isKO;
    }

    /**
     * Calculates the attack value based on character weight and weapon power taking into account the formula.
     * For super-weapons, the weapon_attack is calculated as power_item * weight_attacker.
     * If the weapon is null the weapon attack is 0, else it is the attack power or calculated super-weapon value.
     * @return The calculated attack value.
     */
    public double calculateAttack() {
        double attack;
        double weaponAttack;

        if (weapon != null) {
            weaponAttack = weapon.getEffectValue(getWeight()); //polymorphism
        } else {
            weaponAttack = 0;
        }

        attack = ((getWeight() * (1 - damageTaken)) / 10.0) + (weaponAttack / 20.0) + 18;

        return attack;
    }

    /**
     * Calculates the final damage received from an incoming attack.
     * For super-armors, the armor_value is calculated as power_item * weight_defender.
     * If the armor is null the armorValue will be 0, else it will be the power of the armor or calculated super-armor value.
     * It will also take into account if the member is defending or not.
     *
     * @param incomingAttack The value of the incoming attack.
     * @return The calculated final damage.
     */
    public double calculateFinalDamage(double incomingAttack) {
        double armorValue;
        double defenseValue;
        double finalDamage;
        double damageReduction;

        //calculate the armor value
        if (armor != null) {
            armorValue = armor.getEffectValue(getWeight()); //Polymorphism
        } else {
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

        return Math.max(finalDamage, 0); //there will be no negative damage
    }



    /**
     * Gets the name of the equipped weapon.
     *
     * @return The weapon name, or "no Weapon" if no weapon is equipped
     */
    public String getWeaponName() {
        if (weapon != null) {
            return weapon.getName();
        }
        return "no Weapon";
    }

    /**
     * Gets the name of the equipped armor.
     *
     * @return The armor name, or "no Armor" if no armor is equipped
     */
    public String getArmorName() {
        if (armor != null) {
            return  armor.getName();
        }
        return "no Armor";
    }
}
