package persistance.json;

import business.entities.Character;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import persistance.CharacterDAO;
import persistance.exceptions.PersistanceException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Implementation of CharacterDAO for managing character data using JSON files.
 * Provides methods for validating, loading, and searching character data stored in a JSON file.
 */
public class CharacterJsonDAO implements CharacterDAO {

    private static final String PATH = "data/characters.json"; // JSON file path
    private final Gson gson;

    //*************************************************
    //************ General functionalities ************
    //*************************************************

    /**
     * Constructor for CharacterJsonDAO.
     * Initializes the Gson instance for JSON processing.
     */
    public CharacterJsonDAO() {
        this.gson = new Gson();
    }


    /**
     * Loads all characters from the JSON file.
     *
     * @return {@code List<Character>}. A list of all characters stored in the JSON file.
     * @throws PersistanceException If the file cannot be read.
     */
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



    //*************************************************
    //********* Functions for retrieving Characters ***
    //*************************************************

    /**
     * Retrieves a character by its unique ID.
     *
     * @param id The ID of the character.
     * @return Character The character object if found, otherwise null.
     * @throws PersistanceException If the file cannot be read.
     */
    @Override
    public Character getCharacterById(long id) throws PersistanceException {
        try (JsonReader reader = new JsonReader(new FileReader(PATH))) {
            Character[] charactersArray = gson.fromJson(reader, Character[].class);
            for (Character character : charactersArray) {
                if (character.getId() == id) {
                    return character; // Return the correct character by ID
                }
            }
            return null;
        } catch (IOException e) {
            throw new PersistanceException("Couldn't read characters file: " + PATH, e);
        }
    }

    /**
     * Retrieves a character by its name.
     *
     * @param name The name of the character.
     * @return Character The character object if found, otherwise null.
     * @throws PersistanceException If the file cannot be read.
     */
    @Override
    public Character getCharacterByName(String name) throws PersistanceException {
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

    /**
     * Retrieves a list of all character names.
     *
     *@return {@code List<String>}. A list containing the names of all characters.
     * @throws PersistanceException If the file cannot be read.
     */
    @Override
    public List<String> getCharactersByNames() throws PersistanceException {
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


    //**************************************************
    //********* Functions for find Characters **********
    //**************************************************


    /**
     * Searches for a character by name or ID.
     *
     * @param input The input, which can be a name or an ID.
     * @return Character The corresponding character object if found, otherwise null.
     * @throws PersistanceException If the file cannot be read.
     */
    @Override
    public Character findCharacter(String input) throws PersistanceException {
        try {
            // First, check if input is a numeric ID
            if (input.matches("\\d+")) {
                return getCharacterById(Long.parseLong(input));
            } else {
                return getCharacterByName(input);
            }
        } catch (PersistanceException e) {
            return null;
        }
    }

    /**
     * Retrieves a character by its position in the list.
     *
     * @param index The index (1-based) of the character in the list.
     * @return Character The character object if found, otherwise null.
     * @throws PersistanceException If the file cannot be read or the index is invalid.
     */
    public Character findCharacterByIndex(int index) throws PersistanceException {
        try {
            List<Character> characters = loadAllCharacters();
            return characters.get(index-1);
        } catch (PersistanceException e) {
            return null;
        }

    }
}
