package persistance.API;

import business.entities.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import edu.salle.url.api.ApiHelper;
import edu.salle.url.api.exception.ApiException;
import edu.salle.url.api.exception.status.IncorrectRequestException;
import persistance.ItemDAO;
import persistance.exceptions.PersistanceException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * API-based implementation of ItemDAO for managing item data.
 * Uses REST API calls to fetch item information from external sources.
 */
public class ItemApiDAO implements ItemDAO {
    private static final String BASE_URL = "https://balandrau.salle.url.edu/dpoo/shared/items";

    private final Gson gson;
    private final Random random = new Random();

    /**
     * Constructor that initializes the ItemApiDAO with a custom Gson deserializer.
     */
    public ItemApiDAO() {
        // Create a custom deserializer for Item class
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Item.class, new ItemDeserializer())
                .create();
    }

    /**
     * Custom deserializer for Item class that handles the abstract class instantiation
     */
    private static class ItemDeserializer implements JsonDeserializer<Item> {
        @Override
        public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            long id = jsonObject.get("id").getAsLong();
            String name = jsonObject.get("name").getAsString();
            int power = jsonObject.get("power").getAsInt();
            int durability = jsonObject.get("durability").getAsInt();
            String itemClass = jsonObject.get("class").getAsString();

            return switch (itemClass.toLowerCase()) {
                case "weapon" -> new Weapon(id, name, power, durability);
                case "superweapon" -> new SuperWeapon(id, name, power, durability);
                case "armor" -> new Armor(id, name, power, durability);
                case "superarmor" -> new SuperArmor(id, name, power, durability);
                default -> throw new JsonParseException("Unknown item class: " + itemClass);
            };
        }
    }

    /**
     * Validates that the API is accessible and working.
     *
     * @throws PersistanceException if the API is not reachable
     */
    public static void validateUsage() throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();
            String check = apiHelper.getFromUrl(BASE_URL);
        } catch (ApiException e){
            throw new PersistanceException(e.getMessage());
        }
    }

    /**
     * Retrieves a random weapon from the API.
     *
     * @return A random Weapon object
     * @throws PersistanceException if there's an error fetching weapons from the API
     */
    @Override
    public Weapon getRandomWeapon() throws PersistanceException {
        return (Weapon) getRandomItem(List.of("Weapon", "Superweapon"));
    }

    /**
     * Retrieves a random armor from the API.
     *
     * @return A random Armor object
     * @throws PersistanceException if there's an error fetching armor from the API
     */
    @Override
    public Armor getRandomArmor() throws PersistanceException {
        return (Armor) getRandomItem(List.of("Armor", "Superarmor"));
    }

    /**
     * Retrieves a random item of the specified types from the API.
     *
     * @param acceptedTypes List of accepted item types
     * @return A random Item object of the specified types
     * @throws PersistanceException if there's an error fetching items from the API
     */
    private Item getRandomItem(List<String> acceptedTypes) throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();

            String json = apiHelper.getFromUrl(BASE_URL);

            List<Item> filteredItems = new ArrayList<>();
            JsonArray array = JsonParser.parseString(json).getAsJsonArray();

            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();
                String itemClass = obj.get("class").getAsString();

                if (acceptedTypes.contains(itemClass)) {
                    filteredItems.add(gson.fromJson(obj, Item.class));
                }
            }

            if (filteredItems.isEmpty()) return null;

            int randomIndex = random.nextInt(filteredItems.size());
            return filteredItems.get(randomIndex);

        } catch (ApiException e) {
            throw new PersistanceException("Error fetching items from API", e);
        }
    }

    /**
     * Retrieves a list of all item names from the API.
     *
     * @return A list of item names
     * @throws PersistanceException if there's an error fetching item names from the API
     */
    @Override
    public List<String> getItemNames() throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();

            String json = apiHelper.getFromUrl(BASE_URL);

            List<Item> items = gson.fromJson(json, new TypeToken<List<Item>>() {}.getType());

            return items.stream()
                    .map(Item::getName)
                    .toList();

        } catch (ApiException e) {
            throw new PersistanceException("Error fetching item names from API", e);
        }
    }

    /**
     * Retrieves an item by its name from the API.
     *
     * @param name The name of the item to retrieve
     * @return The Item object if found, null otherwise
     * @throws PersistanceException if there's an error fetching the item from the API
     */
    @Override
    public Item getItemByName(String name) throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();
            // URL encode the name parameter to handle spaces and special characters
            String encodedName = java.net.URLEncoder.encode(name, "UTF-8");
            String url = BASE_URL + "?name=" + encodedName;
            
            String json = apiHelper.getFromUrl(url);

            // If the response is null or empty, return null
            if (json == null || json.trim().isEmpty()) {
                return null;
            }

            try {
                // Handle both array and single object responses
                if (json.trim().startsWith("[")) {
                    // API returned an array
                    List<Item> items = gson.fromJson(json, new TypeToken<List<Item>>() {}.getType());
                    return items.isEmpty() ? null : items.getFirst();
                } else {
                    // API returned a single object
                    return gson.fromJson(json, Item.class);
                }
            } catch (JsonSyntaxException e) {
                throw new PersistanceException("Error parsing item JSON from API: " + e.getMessage(), e);
            }

        } catch (IncorrectRequestException e) {
            if (e.getStatusCode() == 404) {
                return null;
            }
            throw new PersistanceException("Failed to get item: " + e.getMessage(), e);
        } catch (ApiException e) {
            throw new PersistanceException("Error fetching item from API: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new PersistanceException("Unexpected error while fetching item: " + e.getMessage(), e);
        }
    }

}
