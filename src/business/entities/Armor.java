package business.entities;

/**
 * The armor class inheriting from the Item Class.
 */
public class Armor extends Item {

    /**
     * Constructor of the armor object with the specified parameters
     *
     * @param id the id of the armor
     * @param name the name of the armor
     * @param power the defensive power of the armor
     * @param durability the durability of the armor
     */
    public Armor(long id, String name, int power, int durability) {
        super(name, id, power, durability);
    }

    /**
     * gets the defense value of the armor
     *
     * @return the defensive value of the armor
     */
    public int getDefenseValue() {
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
        return getPower();
    }
}
