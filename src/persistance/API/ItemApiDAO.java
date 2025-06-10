package persistance.API;

import business.entities.Armor;
import business.entities.Item;
import business.entities.Weapon;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import edu.salle.url.api.ApiHelper;
import edu.salle.url.api.exception.ApiException;
import edu.salle.url.api.exception.status.IncorrectRequestException;
import persistance.ItemDAO;
import persistance.exceptions.PersistanceException;

import java.lang.reflect.Type;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Random;

public class ItemApiDAO implements ItemDAO {
    private static final String BASE_URL = "https://balandrau.salle.url.edu/dpoo/shared/items";
    private static final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson;
    private final Random random = new Random();

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
            
            if ("Weapon".equals(itemClass) || "Superweapon".equals(itemClass)) {
                return new Weapon(id, name, power, durability);
            } else if ("Armor".equals(itemClass) || "Superarmor".equals(itemClass)) {
                return new Armor(id, name, power, durability);
            } else {
                throw new JsonParseException("Unknown item class: " + itemClass);
            }
        }
    }

    @Override
    public boolean validateFile() {
        try {
            return !getItemNames().isEmpty();
        } catch (PersistanceException e) {
            return false;
        }
    }

    public static void validateUsage() throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();
            String check = apiHelper.getFromUrl(BASE_URL);
        } catch (ApiException e){
            throw new PersistanceException(e.getMessage());
        }
    }
    @Override
    public Weapon getRandomWeapon() throws PersistanceException {
        return (Weapon) getRandomItem("Weapon");
    }

    @Override
    public Armor getRandomArmor() throws PersistanceException {
        return (Armor) getRandomItem("Armor");
    }

    private Item getRandomItem(String type) throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();

            String json = apiHelper.getFromUrl(BASE_URL);

            List<Item> items = gson.fromJson(json, new TypeToken<List<Item>>() {}.getType());

            List<Item> filteredItems = items.stream()
                    .filter(item -> item.getClass().getSimpleName().equals(type))
                    .toList();

            if (filteredItems.isEmpty()) {
                return null;
            }

            int randomIndex = random.nextInt(filteredItems.size());
            return filteredItems.get(randomIndex);

        } catch (ApiException e) {
            throw new PersistanceException("Error fetching items from API", e);
        }
    }


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
                    return items.isEmpty() ? null : items.get(0);
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
