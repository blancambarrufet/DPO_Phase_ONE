package business.entities;

/**
 * Represents a character with an id, name, and weight.
 * This class is essential for the project, because it stores the information of the character that will be later used
 * in all functionalities
 */
public class Character {
    //id of the character
    private final long id;

    //name of the character
    private final String name;

    //weight of the character
    private final int weight;

    /**
     * Constructor for character
     * Initialize a new instance of character by the id, name and weight
     *
     * @param id id of the character
     * @param name name of the character
     * @param weight weight of the character
     */
    public Character(long id, String name, int weight) {
        this.id = id;
        this.name = name;
        this.weight = weight;
    }

    /**
     * Gets the id of the character
     *
     * @return the id of the character
     */
    public long getId() {
        return id;
    }

    /**
     * Get the name of the character
     *
     * @return the name of the character
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the weight of the character
     * @return the weight of the character
     */
    public int getWeight() {
        return weight;
    }
}
