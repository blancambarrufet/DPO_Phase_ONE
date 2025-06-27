package persistance;

import business.entities.Character;
import persistance.exceptions.PersistanceException;
import java.util.List;

/**
 * Interface for managing character data persistence.
 * Defines methods for loading, validating, and searching characters in the system.
 */
public interface CharacterDAO {

    /**
     * Loads all characters from the persistence source.
     *
     * @return {@code List<Character>}. A list of all available characters.
     * @throws PersistanceException If an error occurs during loading.
     */
    List<Character> loadAllCharacters() throws PersistanceException;


    /**
     * Retrieves a character by its unique ID.
     *
     * @param id The ID of the character.
     * @return Character The corresponding character object if found, otherwise null.
     * @throws PersistanceException If an error occurs during retrieval.
     */
    Character getCharacterById(long id) throws PersistanceException;

    /**
     * Retrieves a character by its name.
     *
     * @param name The name of the character.
     * @return Character The corresponding character object if found, otherwise null.
     * @throws PersistanceException If an error occurs during retrieval.
     */
    Character getCharacterByName(String name) throws PersistanceException;

    /**
     * Retrieves a list of all character names.
     *
     * @return {@code List<String>}. A list containing the names of all available characters.
     * @throws PersistanceException If an error occurs during retrieval.
     */
    List<String> getCharactersByNames() throws PersistanceException;

    /**
     * Searches for a character by either name or ID.
     *
     * @param input The name or ID of the character to search for.
     * @return Character The corresponding character object if found, otherwise null.
     * @throws PersistanceException If an error occurs during search.
     */
    Character findCharacter(String input) throws PersistanceException;

    /**
     * Retrieves a character by its index position in the list.
     *
     * @param index The index (1-based) of the character in the list.
     * @return Character The corresponding character object if found, otherwise null.
     * @throws PersistanceException If an error occurs during retrieval.
     */
    Character findCharacterByIndex(int index) throws PersistanceException;
}
