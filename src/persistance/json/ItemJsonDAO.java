package persistance.json;

import business.entities.Item;
import business.entities.Weapon;
import business.entities.Armor;
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
     * Validates the existence and structure of the items JSON file.
     * Ensures all items have a valid "class" field specifying whether they are weapons or armor.
     *
     * @return boolean True if the file exists and contains valid data; otherwise, false.
     */
    @Override
    public boolean validateFile() {
        try {
            if (!Files.exists(Path.of(PATH))) {
                return false; // File does not exist
            }

            JsonArray jsonArray = JsonParser.parseReader(new FileReader(PATH)).getAsJsonArray();

            // Ensure all items have a valid "class" field
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                if (!jsonObject.has("class") || (!jsonObject.get("class").getAsString().equals("Weapon") &&
                        !jsonObject.get("class").getAsString().equals("Armor"))) {
                    return false; // Invalid class field
                }
            }

            return true; // File is valid
        } catch (JsonSyntaxException | IOException e) {
            return false; // Invalid file structure
        }
    }

    /**
     * Retrieves a random weapon from the JSON file.
     *
     * @return Weapon A randomly selected weapon object.
     */
    @Override
    public Weapon getRandomWeapon() {
        return (Weapon) getRandomItem("Weapon");
    }

    /**
     * Retrieves a random armor from the JSON file.
     *
     * @return Armor A randomly selected armor object.
     */
    @Override
    public Armor getRandomArmor() {
        return (Armor) getRandomItem("Armor");
    }

    /**
     * Retrieves a random item (either a weapon or armor) based on the specified type.
     *
     * @param type The type of item to retrieve ("Weapon" or "Armor").
     * @return Item A randomly selected item of the specified type.
     * @throws PersistanceException If the JSON file cannot be read.
     */
    private Item getRandomItem(String type) {
        try (JsonReader reader = new JsonReader(new FileReader(PATH))) {
            reader.beginArray();
            Item selectedItem = null;
            int count = 0;

            while (reader.hasNext()) {
                JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                if (jsonObject.get("class").getAsString().equals(type)) {
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


    /**
     * Parses a JSON object into an Item (either Weapon or Armor).
     *
     * @param jsonObject The JSON object representing an item.
     * @return Item The parsed item object.
     */
    private Item parseItem(JsonObject jsonObject) {
        long id = jsonObject.get("id").getAsLong();
        String name = jsonObject.get("name").getAsString();
        int power = jsonObject.get("power").getAsInt();
        int durability = jsonObject.get("durability").getAsInt();
        String itemType = jsonObject.get("class").getAsString();

        return itemType.equals("Weapon") ? new Weapon(id, name, power, durability)
                : new Armor(id, name, power, durability);
    }

    /**
     * Retrieves a list of all item names from the JSON file.
     *
     * @return List<String> A list containing the names of all available items.
     * @throws PersistanceException If the JSON file cannot be read.
     */
    public List<String> getItemNames() {
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
    public Item getItemByName(String name) {
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
