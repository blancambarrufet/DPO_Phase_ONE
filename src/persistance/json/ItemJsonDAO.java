package persistance.json;

import business.entities.*;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import persistance.ItemDAO;
import persistance.exceptions.PersistanceException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementation of ItemDAO for managing item data using JSON files.
 * Provides methods for validating, retrieving, and randomly selecting items (weapons and armor).
 */
public class ItemJsonDAO implements ItemDAO {

    private static final String PATH = "data/items.json";
    private final Gson gson;
    private Random random;

    /**
     * Constructor for ItemJsonDAO.
     * Initializes Gson with pretty printing and a Random instance for item selection.
     */
    public ItemJsonDAO() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.random = new Random();
    }

    /**
     * Retrieves a random weapon from the JSON file.
     *
     * @return Weapon A randomly selected weapon object.
     * @throws PersistanceException If an error occurs during retrieval.
     */
    @Override
    public Weapon getRandomWeapon() throws PersistanceException {
        return (Weapon) getRandomItem(List.of("Weapon", "Superweapon"));
    }

    /**
     * Retrieves a random armor from the JSON file.
     *
     * @return Armor A randomly selected armor object.
     * @throws PersistanceException If an error occurs during retrieval.
     */
    @Override
    public Armor getRandomArmor() throws PersistanceException {
        return (Armor) getRandomItem(List.of("Armor", "Superarmor"));
    }


    private Item getRandomItem(List<String> types) {
        try (JsonReader reader = new JsonReader(new FileReader(PATH))) {
            reader.beginArray();
            Item selectedItem = null;
            int count = 0;

            while (reader.hasNext()) {
                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                String itemClass = jsonObject.get("class").getAsString();

                if (types.contains(itemClass)) {
                    count++;
                    if (random.nextInt(count) == 0) {
                        selectedItem = parseItem(jsonObject);
                    }
                }
            }

            reader.endArray();
            return selectedItem;
        } catch (IOException e) {
            throw new PersistanceException("Couldn't read items file: " + PATH, e);
        }
    }


    private Item parseItem(JsonObject jsonObject) {
        long id = jsonObject.get("id").getAsLong();
        String name = jsonObject.get("name").getAsString();
        int power = jsonObject.get("power").getAsInt();
        int durability = jsonObject.get("durability").getAsInt();
        String itemType = jsonObject.get("class").getAsString();

        return switch (itemType) {
            case "Weapon" -> new Weapon(id, name, power, durability);
            case "Superweapon" -> new SuperWeapon(id, name, power, durability);
            case "Armor" -> new Armor(id, name, power, durability);
            case "Superarmor" -> new SuperArmor(id, name, power, durability);
            default -> throw new PersistanceException("Invalid item type: " + itemType);
        };
    }

    /**
     * Retrieves a list of all item names from the JSON file.
     *
     * @return {@code List<String>}. A list containing the names of all available items.
     * @throws PersistanceException If the JSON file cannot be read.
     */
    @Override
    public List<String> getItemNames() throws PersistanceException {
        List<String> itemNames = new ArrayList<>();

        try (JsonReader reader = new JsonReader(new FileReader(PATH))) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                if (jsonObject.has("name")) {
                    itemNames.add(jsonObject.get("name").getAsString());
                }
            }

        } catch (IOException e) {
            throw new PersistanceException("Couldn't read items file: " + PATH, e);
        }

        return itemNames;
    }

    /**
     * Retrieves an item by its name from the JSON file.
     *
     * @param name The name of the item.
     * @return Item The corresponding item object if found, otherwise null.
     * @throws PersistanceException If the JSON file cannot be read.
     */
    @Override
    public Item getItemByName(String name) throws PersistanceException {
        try (JsonReader reader = new JsonReader(new FileReader(PATH))) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                if (jsonObject.has("name") && jsonObject.get("name").getAsString().equalsIgnoreCase(name)) {
                    return parseItem(jsonObject);
                }
            }
        } catch (IOException e) {
            throw new PersistanceException("Couldn't read items file: " + PATH, e);
        }

        return null;
    }



}
