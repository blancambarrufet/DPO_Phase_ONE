package business;

import business.entities.Item;
import persistance.ItemDAO;
import persistance.exceptions.PersistanceException;
import java.util.List;

public class ItemManager {

    private final ItemDAO itemDAO;

    public ItemManager(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }

    // Validate persistence
    public void validatePersistenceSource() throws PersistanceException {
        if (!itemDAO.isFileOk()) {
            throw new PersistanceException("The items.json file can't be accessed.");
        }
    }

    // Retrieve all items
    public List<Item> getAllItems() throws PersistanceException {
        return itemDAO.loadItems();
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
