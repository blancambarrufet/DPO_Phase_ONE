package persistance.json;

import business.entities.Item;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import persistance.ItemDAO;
import persistance.exceptions.PersistanceException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class ItemJsonDAO implements ItemDAO {

    private static final String PATH = "data/items.json";
    private final Gson gson;

    public ItemJsonDAO() {
        this.gson = new Gson();
    }

    // Check if file exists and is readable
    public boolean isFileOk() {
        Path filePath = Path.of(PATH);
        return Files.exists(filePath) && Files.isReadable(filePath);
    }

    @Override
    public List<Item> loadItems() throws PersistanceException {
        try {
            JsonReader reader = new JsonReader(new FileReader(PATH));
            Item[] itemsArray = gson.fromJson(reader, Item[].class);
            return Arrays.asList(itemsArray);
        } catch (IOException e) {
            throw new PersistanceException("Couldn't read items file: " + PATH, e);
        }
    }

    @Override
    public void saveItems(List<Item> items) throws PersistanceException {
        try (FileWriter writer = new FileWriter(PATH)) {
            gson.toJson(items, writer);
        } catch (IOException e) {
            throw new PersistanceException("Couldn't write items file: " + PATH, e);
        }
    }
}
