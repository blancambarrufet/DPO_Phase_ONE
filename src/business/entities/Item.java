package business.entities;

/**
 * An abstract class representing an Item in the game.
 * It has a name, id, power, durability and type (armor or weapon)
 */
public abstract class Item {
    //name of the item
    private String name;

    //id of the item
    private final long id;

    //Power of the item
    private final int power;

    //durability of the item
    private int durability;




    /**
     * Constructor of the item with the specific attributes
     *
     * @param name the name of the item
     * @param id id of the item
     * @param power power of the item
     * @param durability durability of the item
     */
    public Item(String name, long id, int power, int durability) {
        this.name = name;
        this.id = id;
        this.power = power;
        this.durability = durability;
    }

    /**
     * Get the name of the item
     *
     * @return the name of the item
     */
    public String getName() {
        return name;
    }


    /**
     * Get the id of the item
     *
     * @return The item id
     */
    public long getId() {
        return id;
    }

    /**
     * Get the power of the item
     *
     * @return The power of the item
     */
    public int getPower() {
        return power;
    }

    /**
     * Get the durability of the item
     *
     * @return The durability of the item
     */
    public int getDurability() {
        return durability;
    }

    /**
     * Reduces the durability of the item by one if it is greater than zero.
     *
     */
    public void reduceDurability() {
        if (durability > 0) {
            durability--;
        }
    }

    /**
     * Checks if the item is broken when the durability is 0 or less
     *
     * @return true if the item is broken, false otherwise.
     */
    public boolean isBroken() {
        return durability <= 0;
    }

    /**
     * Polymorphic method: each item returns its effect value based on the user's weight
     *
     * @param characterWeight The weight of the character using the item
     * @return The effective value (used in attack or defense formulas)
     */
    public abstract double getEffectValue(int characterWeight);
}
