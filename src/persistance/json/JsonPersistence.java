package persistance.json;

import persistance.PersistenceManager;
import persistance.json.exceptions.PersistanceException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class JsonPersistence<T> extends PersistenceManager<T> {
    private final String filePath;
    private final Class<T[]> type;
    private final Gson gson;

    public JsonPersistence(String filePath, Class<T[]> type) {
        this.filePath = filePath;
        this.type = type;
        this.gson = new Gson();
    }

    @Override
    public List<T> loadAll() throws PersistanceException {
        if (!validateFile()) {
            throw new PersistanceException("JSON file is missing or corrupted: " + filePath);
        }

        try (JsonReader reader = new JsonReader(new FileReader(filePath))) {
            return Arrays.asList(gson.fromJson(reader, type));
        } catch (IOException e) {
            throw new PersistanceException("Couldn't read JSON file: " + filePath, e);
        }
    }

    @Override
    public void save(List<T> data) throws PersistanceException {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            throw new PersistanceException("Couldn't write JSON file: " + filePath, e);
        }
    }

    /**
     * Validates if the JSON file exists and has correct formatting.
     * @return true if the file exists and can be parsed, false otherwise.
     */
    public boolean validateFile() {
        try {
            if (!Files.exists(Path.of(filePath))) {
                return false; // File does not exist
            }

            // Attempt to read and parse JSON structure
            try (JsonReader reader = new JsonReader(new FileReader(filePath))) {
                gson.fromJson(reader, type);
            }

            return true; // JSON is valid
        } catch (JsonSyntaxException | IOException e) {
            return false; // JSON is invalid or unreadable
        }
    }
}
