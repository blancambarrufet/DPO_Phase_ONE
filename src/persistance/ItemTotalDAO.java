package persistance;

import business.entities.Item;
import business.entities.Weapon;
import business.entities.Armor;

import persistance.json.exceptions.PersistanceException;
import persistance.api.ApiConnectionChecker;
import persistance.api.ApiPersistence;
import persistance.api.exception.ApiException;
import persistance.json.JsonPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemTotalDAO implements ItemDAO {

    // The persistence manager is used to manage item persistence, either through an API or a JSON file.
    private final PersistenceManager<Item> persistenceManager;

    // Instance of JsonPersistence for local file validation.
    JsonPersistence<Item> jsonPersistence;

    /**
     * Validates if the JSON file is correctly formatted and available.
     *
     * @return true if the file is valid, false otherwise.
     */
    public boolean validateFile(){
        return jsonPersistence.validateFile();
    }

    /**
     * Constructor that initializes the persistence method (API or JSON).
     * If the API is available, it uses ApiPersistence; otherwise, it falls back to JsonPersistence.
     * If neither is available, it throws a PersistanceException.
     *
     * @throws ApiException if an error occurs when connecting to the API.
     */
    public ItemTotalDAO() throws ApiException {
        boolean useApi = ApiConnectionChecker.isApiAvailable();
        this.persistenceManager = useApi
                ? new ApiPersistence<>("https://balandrau.salle.url.edu/dpoo/shared/items", Item[].class)
                : new JsonPersistence<>("data/items.json", Item[].class);

        // Validate JSON if API is not available
        if (!useApi && !((JsonPersistence<Item>) persistenceManager).validateFile()) {
            throw new PersistanceException("Error: JSON file for items is missing or corrupted.");
        }
    }

    /**
     * Loads all items from the persistence manager.
     *
     * @return a list of all items.
     * @throws PersistanceException if an error occurs while loading items.
     */
    @Override
    public List<Item> loadItems() throws PersistanceException {
        return persistenceManager.loadAll();
    }

    /**
     * Retrieves an item by its unique ID.
     *
     * @param id the ID of the item.
     * @return the item if found, otherwise null.
     */
    @Override
    public Item getItemById(int id) {
        List<Item> items = loadItems();

        for (Item item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    /**
     * Retrieves a random weapon from the available items.
     *
     * @return a randomly selected Weapon, or null if no weapon is available.
     */
    @Override
    public Weapon getRandomWeapon() {
        return (Weapon) getRandomItem("Weapon");
    }

    /**
     * Retrieves a random armor from the available items.
     *
     * @return a randomly selected Armor, or null if no armor is available.
     */
    @Override
    public Armor getRandomArmor() {
        return (Armor) getRandomItem("Armor");
    }

    /**
     * Gets a random item from the list that matches the specified type.
     *
     * @param type The type of item to retrieve.
     * @return A randomly selected item of the given type, or null if no item matches.
     */
    private Item getRandomItem(String type) {
        List<Item> items = loadItems(); // Load the list of items

        // Filter the list to only include items of the specified type
        List<Item> filteredItems = items.stream()
                .filter(item -> item.getType().equalsIgnoreCase(type))
                .toList();

        // Return a random item if there are any matching items
        if (!filteredItems.isEmpty()) {
            Random random = new Random();
            return filteredItems.get(random.nextInt(filteredItems.size()));
        }

        return null; // Return null if no matching item is found
    }

    /**
     * Retrieves a list of item names from the available items.
     *
     * @return a list of item names.
     */
    public List<String> getItemNames() {
        List<String> itemNames = new ArrayList<>();
        List<Item> items = loadItems();

        for (Item item : items) {
            itemNames.add(item.getName());
        }

        return itemNames;
    }

    /**
     * Retrieves an item by its name.
     *
     * @param name the name of the item.
     * @return the item if found, otherwise null.
     */
    public Item getItemByName(String name) {
        List<Item> items = loadItems();
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }

        return null;
    }
}
