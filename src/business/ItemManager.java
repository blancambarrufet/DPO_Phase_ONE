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

    public ItemManager() {
        try {
            ItemApiDAO.validateUsage();
            this.itemDAO = new ItemApiDAO();
        } catch (PersistanceException e) {
            System.err.println("API unavailable, switching to JSON persistence: " + e.getMessage());
            this.itemDAO = new ItemJsonDAO();
        }
    }


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

    public Armor getRandomArmor() throws PersistanceException {
        return itemDAO.getRandomArmor();
    }

    public Weapon getRandomWeapon() throws PersistanceException {
        return itemDAO.getRandomWeapon();
    }

    public List<String> getItemNames() throws PersistanceException {
        return itemDAO.getItemNames();
    }

    public Item getItemByName(String selectedItemName) throws PersistanceException {
        return itemDAO.getItemByName(selectedItemName);
    }

    public void equipItemsMember(Member member) throws PersistanceException {
        member.equipWeapon(getRandomWeapon());
        member.equipArmor(getRandomArmor());
    }

    public void assignRandomWeapon(Member member) throws PersistanceException {
        member.equipWeapon(getRandomWeapon());
    }
}
