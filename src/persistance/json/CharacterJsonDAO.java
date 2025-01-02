package persistance.json;

import business.entities.Character;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import persistance.CharacterDAO;
import persistance.exceptions.PersistanceException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;


public class CharacterJsonDAO implements CharacterDAO {

    private static final String PATH = "data/characters.json"; // JSON file path
    private final Gson gson;

    // Constructor
    public CharacterJsonDAO() {
        this.gson = new Gson();
    }

    // Check if the file exists
    public boolean isFileOk() {
        Path filePath = Path.of(PATH);
        return Files.exists(filePath) && Files.isReadable(filePath);
    }

    @Override
    public List<Character> loadAllCharacters() throws PersistanceException {
        try {
            JsonReader reader = new JsonReader(new FileReader(PATH));
            Character[] charactersArray = gson.fromJson(reader, Character[].class);
            return Arrays.asList(charactersArray);
        } catch (IOException e) {
            throw new PersistanceException("Couldn't read characters file: " + PATH, e);
        }
    }

    @Override
    public void saveCharacters(List<Character> characters) throws PersistanceException {
        try (FileWriter writer = new FileWriter(PATH)) {
            gson.toJson(characters, writer);
        } catch (IOException e) {
            throw new PersistanceException("Couldn't write characters file: " + PATH, e);
        }
    }
}
