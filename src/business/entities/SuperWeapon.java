package business.entities;

/**
 * Represents a super weapon item in the game.
 * A super weapon is a special type of weapon that scales its power with the character's weight.
 */
public class SuperWeapon extends Weapon {

    /**
     * Constructs a new SuperWeapon instance.
     *
     * @param id         The unique identifier of the super weapon.
     * @param name       The name of the super weapon.
     * @param power      The base attack power of the super weapon.
     * @param durability The durability of the super weapon (number of uses before it breaks).
     */
    public SuperWeapon(long id, String name, int power, int durability) {
        super(id, name, power, durability);
    }

    /**
     * Polymorphic method: each item returns its effect value based on the user's weight
     *
     * @param characterWeight The weight of the character using the item
     * @return The effective value (used in attack or defense formulas)
     */
    @Override
    public double getEffectValue(int characterWeight) {
        return getPower() * characterWeight;
    }
}
