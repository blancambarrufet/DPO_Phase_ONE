package business;

import business.entities.Character;
import persistance.CharacterDAO;
import persistance.exceptions.PersistanceException;
import persistance.json.CharacterJsonDAO;
import java.util.*;


public class CharacterManager {
    private final CharacterDAO characterDAO;
    private List<Character> characters;

    // Default Constructor - Creates DAO internally
    public CharacterManager() throws PersistanceException {
        this.characterDAO = new CharacterJsonDAO(); // Default path
        this.characters = new ArrayList<>();
        loadCharacters();
    }

    public CharacterManager(CharacterDAO characterDAO) throws PersistanceException {
        this.characterDAO = characterDAO;
        this.characters = new ArrayList<>();
        loadCharacters();
    }

    // Load all characters from JSON
    public void loadCharacters() throws PersistanceException {
        this.characters = characterDAO.loadAllCharacters();
        if (characters == null || characters.isEmpty()) {
            System.out.println("ERROR: No characters were loaded from JSON!");
            this.characters = new ArrayList<>();  // Ensure it's initialized
        } else {
            System.out.println("DEBUG: Loaded " + characters.size() + " characters.");
        }
    }

    // Validate the persistence source through the DAO
    public boolean validatePersistenceSource() {
        return characterDAO.validateFile();
    }

    // Retrieve all characters
    public List<Character> getAllCharacters() throws PersistanceException {
        return characters;
    }

    // Save characters
    public void saveCharacters(List<Character> characters) throws PersistanceException {
        characterDAO.saveCharacters(characters);
        this.characters = characters;
    }

    public Character getCharacterByID(long characterID) {
        try {
            List<Character> characters = getAllCharacters();

            for (Character character : characters) {
                if (character.getId() == characterID) {
                    return character;
                }
            }
        } catch (PersistanceException e) {
            System.out.println("Error retrieving character: " + e.getMessage());
        }

        return null;
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
