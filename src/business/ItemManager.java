package business;

import business.entities.Item;
import persistance.ItemDAO;
import persistance.exceptions.PersistanceException;

import java.util.ArrayList;
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

    ArrayList<Item> items;


    public void addItem(Item item) {
        items.add(item);
    }

    public void printItem(String name) {
        // TODO implement here
        for (Item item: items) {
            if (item.getName().equals(name)) {
                System.out.println("\tID: "+ item.getId());
                System.out.println("\tName: "+ item.getName());
                System.out.println("\tClass: "+ item.getClass());
                System.out.println("\tPower: "+ item.getPower());
                System.out.println("\tDurability: "+ item.getDurability());
                break;
            }
        }

    }


    private void loadItem() {
        // TODO implement here
    }


    private Set<Item> listItem() {
        // TODO implement here
        return null;
    }
}
