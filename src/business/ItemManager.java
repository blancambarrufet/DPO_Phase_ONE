package business;

import business.entities.Armor;
import business.entities.Item;
import business.entities.Member;
import business.entities.Weapon;
import persistance.API.ItemApiDAO;
import persistance.ItemDAO;
import persistance.exceptions.PersistanceException;
import persistance.json.ItemJsonDAO;
import java.util.List;

/**
 * Manages item-related operations in the system.
 */
public class ItemManager {

    private ItemDAO itemDAO;

    /**
     * Constructor that initializes the ItemManager with the appropriate DAO.
     * Attempts to use API persistence first, falls back to JSON if API is unavailable.
     */
    public ItemManager() {
        try {
            ItemApiDAO.validateUsage();
            this.itemDAO = new ItemApiDAO();
        } catch (PersistanceException e) {
            this.itemDAO = new ItemJsonDAO();
        }
    }

    /**
     * Validates the persistence source to ensure the data source is accessible.
     *
     * @return true if the data source is valid and accessible, false otherwise
     */
    public boolean validatePersistenceSource() {
        try {
            // Try to load a small amount of data to validate connectivity
            List<String> names = itemDAO.getItemNames();
            boolean valid = names != null && !names.isEmpty();
            return valid;
        } catch (PersistanceException e) {
            return false;
        }
    }

    /**
     * Retrieves a random armor from the available items.
     *
     * @return A random Armor object
     * @throws PersistanceException if there's an error loading the armor
     */
    public Armor getRandomArmor() throws PersistanceException {
        return itemDAO.getRandomArmor();
    }

    /**
     * Retrieves a random weapon from the available items.
     *
     * @return A random Weapon object
     * @throws PersistanceException if there's an error loading the weapon
     */
    public Weapon getRandomWeapon() throws PersistanceException {
        return itemDAO.getRandomWeapon();
    }

    /**
     * Retrieves a list of all item names stored in the system.
     *
     * @return A list containing the names of all available items
     * @throws PersistanceException if there's an error during retrieval
     */
    public List<String> getItemNames() throws PersistanceException {
        return itemDAO.getItemNames();
    }

    /**
     * Retrieves an item by its name.
     *
     * @param selectedItemName The name of the item to retrieve
     * @return The Item object if found, null otherwise
     * @throws PersistanceException if there's an error during retrieval
     */
    public Item getItemByName(String selectedItemName) throws PersistanceException {
        return itemDAO.getItemByName(selectedItemName);
    }

    /**
     * Equips a member with random weapon and armor.
     *
     * @param member The member to equip with items
     * @throws PersistanceException if there's an error loading the items
     */
    public void equipItemsMember(Member member) throws PersistanceException {
        member.equipWeapon(getRandomWeapon());
        member.equipArmor(getRandomArmor());
    }

    /**
     * Assigns a random weapon to a member.
     *
     * @param member The member to equip with a weapon
     * @throws PersistanceException if there's an error loading the weapon
     */
    public void assignRandomWeapon(Member member) throws PersistanceException {
        member.equipWeapon(getRandomWeapon());
    }
}
