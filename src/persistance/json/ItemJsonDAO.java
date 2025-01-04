package persistance.json;

import business.entities.Item;
import business.entities.Weapon;
import business.entities.Armor;
import com.google.gson.*;
import persistance.ItemDAO;
import persistance.exceptions.PersistanceException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemJsonDAO implements ItemDAO {

    private static final String PATH = "data/items.json";
    private final Gson gson;

    public ItemJsonDAO() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

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

    @Override
    public List<Item> loadItems() throws PersistanceException {
        List<Item> items = new ArrayList<>();
        try {
            JsonArray jsonArray = JsonParser.parseReader(new FileReader(PATH)).getAsJsonArray();

            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();

                // Extract common fields
                long id = jsonObject.get("id").getAsLong();
                String name = jsonObject.get("name").getAsString();
                int power = jsonObject.get("power").getAsInt();
                int durability = jsonObject.get("durability").getAsInt();
                String itemType = jsonObject.get("class").getAsString();

                // Instantiate based on "class"
                switch (itemType) {
                    case "Weapon":
                        items.add(new Weapon(id, name, power, durability)); // Create Weapon
                        break;
                    case "Armor":
                        items.add(new Armor(id, name, power, durability)); // Create Armor
                        break;
                    default:
                        throw new PersistanceException("Unknown item type: " + itemType);
                }
            }
            return items;

        } catch (IOException | JsonSyntaxException e) {
            throw new PersistanceException("Couldn't read items file: " + PATH, e);
        }
    }

    @Override
    public void saveItems(List<Item> items) throws PersistanceException {
        try (FileWriter writer = new FileWriter(PATH)) {
            JsonArray jsonArray = new JsonArray();

            for (Item item : items) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", item.getId());
                jsonObject.addProperty("name", item.getName());
                jsonObject.addProperty("power", item.getPower());
                jsonObject.addProperty("durability", item.getDurability());

                // Add "class" field for type identification
                if (item instanceof Weapon) {
                    jsonObject.addProperty("class", "Weapon");
                } else if (item instanceof Armor) {
                    jsonObject.addProperty("class", "Armor");
                }

                jsonArray.add(jsonObject);
            }

            gson.toJson(jsonArray, writer); // Save to file
        } catch (IOException e) {
            throw new PersistanceException("Couldn't write items file: " + PATH, e);
        }
    }
}
