package persistance;

import business.entities.Armor;
import business.entities.Item;
import business.entities.Weapon;

import java.util.List;

/**
 * Interface for managing item data persistence.
 * Defines methods for validating, retrieving, and searching items such as weapons and armor.
 */
public interface ItemDAO {

    /**
     * Validates the existence and structure of the item data file.
     *
     * @return boolean True if the file is valid and accessible, otherwise false.
     */
    boolean validateFile();

    /**
     * Retrieves a random weapon from the persistence source.
     *
     * @return Weapon A randomly selected weapon object.
     */
    Weapon getRandomWeapon();

    /**
     * Retrieves a random armor from the persistence source.
     *
     * @return Armor A randomly selected armor object.
     */
    Armor getRandomArmor();

    /**
     * Retrieves a list of all item names stored in the system.
     *
     * @return {@code List<String>}. A list containing the names of all available items.
     */
    List<String> getItemNames();

    /**
     * Retrieves an item by its name.
     *
     * @param name The name of the item.
     * @return Item The corresponding item object if found, otherwise null.
     */
    Item getItemByName(String name);

}
