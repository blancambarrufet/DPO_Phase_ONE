package business.entities;

/**
 * Represents a super armor item in the game.
 * A super armor is a special type of armor that scales its power with the character's weight.
 */
public class SuperArmor extends Armor {

    /**
     * Constructs a new SuperArmor instance.
     *
     * @param id         The unique identifier of the super armor.
     * @param name       The name of the super armor.
     * @param power      The base defensive power of the super armor.
     * @param durability The durability of the super armor (number of uses before it breaks).
     */
    public SuperArmor(long id, String name, int power, int durability) {
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
