package business;

import business.entities.Armor;
import business.entities.Item;
import business.entities.Weapon;
import persistance.ItemDAO;
import persistance.exceptions.PersistanceException;
import persistance.json.ItemJsonDAO;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    private final ItemDAO itemDAO;

    public ItemManager() throws PersistanceException {
        this.itemDAO = new ItemJsonDAO();
    }


    public ItemManager(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }

    public boolean validatePersistenceSource() {
        return itemDAO.validateFile();
    }

    // Retrieve all items
    public List<Item> getAllItems() throws PersistanceException {
        return itemDAO.loadItems();
    }

    public List<Weapon> getAllWeapons() throws PersistanceException {
        List<Weapon> weapons = new ArrayList<>();
        for (Item item : itemDAO.loadItems()) {
            if (item instanceof Weapon) {
                weapons.add((Weapon) item);
            }
        }
        return weapons;
    }

    public List<Armor> getAllArmor() {
        List<Armor> armors = new ArrayList<>();

        for (Item item : itemDAO.loadItems()) {
            if (item instanceof Armor) {
                armors.add((Armor) item);
            }
        }

        return armors;
    }

    // Save items
    public void saveItems(List<Item> items) throws PersistanceException {
        itemDAO.saveItems(items);
    }

    // Print details of an item
    public void printItem(String name) throws PersistanceException {
        List<Item> items = getAllItems();
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                System.out.println("ID: " + item.getId());
                System.out.println("Name: " + item.getName());
                System.out.println("Power: " + item.getPower());
                System.out.println("Durability: " + item.getDurability());
                return;
            }
        }
        System.out.println("Item not found.");
    }

    // List all items
    public void listItems() throws PersistanceException {
        List<Item> items = getAllItems();
        items.forEach(item -> System.out.println("- " + item.getName()));
    }


}
