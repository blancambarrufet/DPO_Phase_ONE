package business.entities;

public class SuperArmor extends Armor {

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
