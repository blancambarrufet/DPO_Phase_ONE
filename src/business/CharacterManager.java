package business;

import business.entities.Character;
import persistance.CharacterDAO;
import persistance.exceptions.PersistanceException;
import persistance.json.CharacterJsonDAO;
import java.util.*;


public class CharacterManager {
    private final CharacterDAO characterDAO;

    // Default Constructor - Creates DAO internally
    public CharacterManager() throws PersistanceException {
        this.characterDAO = new CharacterJsonDAO();
    }

    public CharacterManager(CharacterDAO characterDAO) throws PersistanceException {
        this.characterDAO = characterDAO;
    }

    // Validate the persistence source through the DAO
    public boolean validatePersistenceSource() {
        return characterDAO.validateFile();
    }

    // Retrieve all characters
    public List<Character> getAllCharacters() throws PersistanceException {
        return characterDAO.loadAllCharacters();
    }

    public Character findCharacter(String input) {
        try {
            List<Character> characters = characterDAO.loadAllCharacters();

            // First, check if the input matches any character ID
            for (Character character : characters) {
                if (String.valueOf(character.getId()).equals(input)) {
                    return character;
                }
            }

            // If not found by ID, check by name
            for (Character character : characters) {
                if (character.getName().equalsIgnoreCase(input)) {
                    return character;
                }
            }

        } catch (PersistanceException e) {
            System.out.println("Error retrieving character: " + e.getMessage());
        }

        return null;
    }
}
