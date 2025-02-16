package business;

import business.entities.Character;
import persistance.CharacterDAO;
import persistance.exceptions.PersistanceException;
import persistance.json.CharacterJsonDAO;
import java.util.*;

/**
 * Manages character-related operations in the system.
 * This class serves as an intermediary between the business logic and the data access layer.
 * It interacts with the persistence layer to retrieve and validate character data.
 */
public class CharacterManager {
    private final CharacterDAO characterDAO;

    /**
     * Default constructor that initializes the CharacterManager with a JSON-based DAO.
     *
     * @throws PersistanceException If an error occurs while initializing the DAO.
     */
    public CharacterManager() throws PersistanceException {
        this.characterDAO = new CharacterJsonDAO();
    }

    /**
     * Validates the persistence source to ensure the characters JSON file exists and is correctly formatted.
     *
     * @return boolean True if the file is valid and accessible; otherwise, false.
     */
    public boolean validatePersistenceSource() {
        return characterDAO.validateFile();
    }

    /**
     * Searches for a character by either name or ID.
     *
     * @param input The name or ID of the character to search for.
     * @return Character The corresponding character object if found, otherwise null.
     * @throws PersistanceException If an error occurs during the search.
     */
    public Character findCharacter(String input) throws PersistanceException {
        return characterDAO.findCharacter(input);
    }

    /**
     * Retrieves a list of all character names stored in the system.
     *
     * @return {@code List<String>}. A list containing the names of all available characters.
     */
    public List<String> getCharacterNames() {
        return characterDAO.getCharactersByNames();
    }

    /**
     * Retrieves a character by its position in the list.
     *
     * @param index The index (1-based) of the character in the list.
     * @return Character The character object if found, otherwise null.
     */
    public Character findCharacterByIndex(int index){
        return characterDAO.findCharacterByIndex(index);
    }
}
