package business.entities;

/**
 * Represents a weapon item in the game.
 * A weapon is a type of item that has attack power.
 */
public class Weapon extends Item {

    /**
     * Constructs a new Weapon instance.
     *
     * @param id         The unique identifier of the weapon.
     * @param name       The name of the weapon.
     * @param power      The attack power of the weapon.
     * @param durability The durability of the weapon (number of uses before it breaks).
     */
    public Weapon(long id, String name, int power, int durability) {
        super(name, id, power, durability);
    }

    /**
     * Retrieves the attack power of the weapon.
     *
     * @return int The attack power of the weapon.
     */
    public int getAttackPower() {
        return getPower();
    }

    /**
     * Polymorphic method: each item returns its effect value based on the user's weight
     *
     * @param characterWeight The weight of the character using the item
     * @return The effective value (used in attack or defense formulas)
     */
    @Override
    public double getEffectValue(int characterWeight) {
        return getPower(); //Normal weapon returns power
    }
}
