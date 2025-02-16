package business;

import business.entities.Armor;
import business.entities.Item;
import business.entities.Member;
import business.entities.Weapon;
import persistance.ItemDAO;
import persistance.exceptions.PersistanceException;
import persistance.json.ItemJsonDAO;


import java.util.List;

/**
 * Manages item-related operations in the system.
 * This class serves as an intermediary between the business logic and the data access layer,
 * allowing retrieval, validation, and assignment of items such as weapons and armor.
 */
public class ItemManager {

    private final ItemDAO itemDAO;
    /**
     * Constructs an ItemManager instance with a JSON-based data access object (DAO).
     *
     * @throws PersistanceException If an error occurs while initializing the DAO.
     */
    public ItemManager() throws PersistanceException {
        this.itemDAO = new ItemJsonDAO();
    }

    /**
     * Validates the persistence source to ensure the items JSON file exists and is correctly formatted.
     *
     * @return boolean True if the file is valid and accessible, otherwise false.
     */
    public boolean validatePersistenceSource() {
        return itemDAO.validateFile();
    }

    /**
     * Retrieves a random armor item from the persistence source.
     *
     * @return Armor A randomly selected armor object.
     */
    public Armor getRandomArmor() {
        return itemDAO.getRandomArmor();
    }

    /**
     * Retrieves a random weapon item from the persistence source.
     *
     * @return Weapon A randomly selected weapon object.
     */
    public Weapon getRandomWeapon() {
        return itemDAO.getRandomWeapon();
    }

    /**
     * Retrieves a list of all item names stored in the system.
     *
     * @return {@code List<String>}. A list containing the names of all available items.
     */
    public List<String> getItemNames() {
        return itemDAO.getItemNames();
    }

    /**
     * Retrieves an item by its name.
     *
     * @param selectedItemName The name of the item to retrieve.
     * @return Item The corresponding item object if found, otherwise null.
     */
    public Item getItemByName(String selectedItemName) {
        return itemDAO.getItemByName(selectedItemName);
    }

    /**
     * Equips a member with a randomly selected weapon and armor.
     *
     * @param member The member to equip.
     * @throws PersistanceException If an error occurs while retrieving items.
     */
    public void equipItemsMember(Member member) throws PersistanceException {
        Weapon weapon = getRandomWeapon();
        Armor armor = getRandomArmor();

        member.equipWeapon(weapon);
        member.equipArmor(armor);
    }

    /**
     * Assigns a random weapon to a given member.
     *
     * @param member The member to equip with a weapon.
     */
    public void assignRandomWeapon(Member member) {
        Weapon weapon = getRandomWeapon();
        member.equipWeapon(weapon);
    }
}
