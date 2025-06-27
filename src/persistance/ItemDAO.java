package persistance;

import business.entities.Armor;
import business.entities.Item;
import business.entities.Weapon;
import persistance.exceptions.PersistanceException;

import java.util.List;

/**
 * Interface for managing item data persistence.
 * Defines methods for validating, retrieving, and searching items such as weapons and armor.
 */
public interface ItemDAO {

    /**
     * Retrieves a random weapon from the persistence source.
     *
     * @return Weapon A randomly selected weapon object.
     * @throws PersistanceException If an error occurs during retrieval.
     */
    Weapon getRandomWeapon() throws PersistanceException;

    /**
     * Retrieves a random armor from the persistence source.
     *
     * @return Armor A randomly selected armor object.
     * @throws PersistanceException If an error occurs during retrieval.
     */
    Armor getRandomArmor() throws PersistanceException;

    /**
     * Retrieves a list of all item names stored in the system.
     *
     * @return {@code List<String>}. A list containing the names of all available items.
     * @throws PersistanceException If an error occurs during retrieval.
     */
    List<String> getItemNames() throws PersistanceException;

    /**
     * Retrieves an item by its name.
     *
     * @param name The name of the item.
     * @return Item The corresponding item object if found, otherwise null.
     * @throws PersistanceException If an error occurs during retrieval.
     */
    Item getItemByName(String name) throws PersistanceException;

}
