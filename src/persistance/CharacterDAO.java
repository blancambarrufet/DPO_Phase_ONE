package persistance;

import business.entities.Character;
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
     */
    List<Character> loadAllCharacters();

    /**
     * Validates the existence and structure of the character data file.
     *
     * @return boolean True if the file is valid and accessible, otherwise false.
     */
    boolean validateFile();

    /**
     * Retrieves a character by its unique ID.
     *
     * @param id The ID of the character.
     * @return Character The corresponding character object if found, otherwise null.
     */
    Character getCharacterById(long id);

    /**
     * Retrieves a character by its name.
     *
     * @param name The name of the character.
     * @return Character The corresponding character object if found, otherwise null.
     */
    Character getCharacterByName(String name);

    /**
     * Retrieves a list of all character names.
     *
     * @return {@code List<String>}. A list containing the names of all available characters.
     */
    List<String> getCharactersByNames();

    /**
     * Searches for a character by either name or ID.
     *
     * @param input The name or ID of the character to search for.
     * @return Character The corresponding character object if found, otherwise null.
     */
    Character findCharacter(String input);

    /**
     * Retrieves a character by its index position in the list.
     *
     * @param index The index (1-based) of the character in the list.
     * @return Character The corresponding character object if found, otherwise null.
     */
    Character findCharacterByIndex(int index);
}
