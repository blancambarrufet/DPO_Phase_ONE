package persistance.json;

import business.entities.Character;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import persistance.CharacterDAO;
import persistance.exceptions.PersistanceException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CharacterJsonDAO implements CharacterDAO {

    private static final String PATH = "data/characters.json"; // JSON file path
    private final Gson gson;

    // Constructor
    public CharacterJsonDAO() {
        this.gson = new Gson();
    }

    @Override
    public boolean validateFile() {
        try {
            // Check if the file exists
            if (!Files.exists(Path.of(PATH))) {
                return false; // File is missing
            }

            // Validate JSON structure
            JsonReader reader = new JsonReader(new FileReader(PATH));
            gson.fromJson(reader, Character[].class); // Attempt parsing
            return true; // If no exception, file is valid

        } catch (JsonSyntaxException | IOException e) {
            return false; // File is invalid or unreadable
        }
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
    public Character getCharacterById(long id) {
        try (JsonReader reader = new JsonReader(new FileReader(PATH))) {
            Character[] charactersArray = gson.fromJson(reader, Character[].class);
            for (Character character : charactersArray) {
                if (character.getId() == id) {
                    return character;
                }
            }
            return null;
        } catch (IOException e) {
            throw new PersistanceException("Couldn't read characters file: " + PATH, e);
        }
    }

    @Override
    public Character getCharacterByName(String name) {
        try (JsonReader reader = new JsonReader(new FileReader(PATH))) {
            Character[] charactersArray = gson.fromJson(reader, Character[].class);
            for (Character character : charactersArray) {
                if (character.getName().equalsIgnoreCase(name)) {
                    return character;
                }
            }
            return null;
        } catch (IOException e) {
            throw new PersistanceException("Couldn't read characters file: " + PATH, e);
        }
    }

    @Override
    public List<String> getCharactersByNames() {
        try (JsonReader reader = new JsonReader(new FileReader(PATH))) {
            Character[] charactersArray = gson.fromJson(reader, Character[].class);
            List<String> names = new ArrayList<>();
            for (Character character : charactersArray) {
                names.add(character.getName());
            }
            return names;
        } catch (IOException e) {
            throw new PersistanceException("Couldn't read characters file: " + PATH, e);
        }
    }
}
