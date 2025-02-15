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


public class ItemJsonDAO implements ItemDAO {

    private static final String PATH = "data/items.json";
    private final Gson gson;
    private Random random;

    public ItemJsonDAO() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.random = new Random();
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
    public Item getItemById(int id) {
        try (JsonReader reader = new JsonReader(new FileReader(PATH))) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                if (jsonObject.get("id").getAsLong() == id) {
                    return parseItem(jsonObject);
                }
            }
            return null;
        } catch (IOException e) {
            throw new PersistanceException("Couldn't read items file: " + PATH, e);
        }
    }

    @Override
    public Weapon getRandomWeapon() {
        return (Weapon) getRandomItem("Weapon");
    }

    @Override
    public Armor getRandomArmor() {
        return (Armor) getRandomItem("Armor");
    }

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

    private Item parseItem(JsonObject jsonObject) {
        long id = jsonObject.get("id").getAsLong();
        String name = jsonObject.get("name").getAsString();
        int power = jsonObject.get("power").getAsInt();
        int durability = jsonObject.get("durability").getAsInt();
        String itemType = jsonObject.get("class").getAsString();

        return itemType.equals("Weapon") ? new Weapon(id, name, power, durability)
                : new Armor(id, name, power, durability);
    }

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
