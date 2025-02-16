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

}
