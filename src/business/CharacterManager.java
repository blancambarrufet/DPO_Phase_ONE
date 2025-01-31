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
        this.characterDAO = new CharacterJsonDAO(); // Default path
    }

    public CharacterManager(CharacterDAO characterDAO) {
        this.characterDAO = characterDAO;
    }

    // Validate the persistence source through the DAO
    public boolean validatePersistenceSource() {
        return characterDAO.validateFile();
    }

    // Retrieve all characters
    public List<business.entities.Character> getAllCharacters() throws PersistanceException {
        return characterDAO.loadAllCharacters();
    }

    // Save characters
    public void saveCharacters(List<business.entities.Character> characters) throws PersistanceException {
        characterDAO.saveCharacters(characters);
    }


    // Display character information
    public void displayCharacterInfo(String characterName) throws PersistanceException {
        List<Character> characters = getAllCharacters();
        for (Character character : characters) {
            if (character.getName().equalsIgnoreCase(characterName)) {
                System.out.println("Id: " + character.getId());
                System.out.println("Name: " + character.getName());
                System.out.println("Weight: " + character.getWeight());
                return;
            }
        }
        System.out.println("Character not found.");
    }

    public Character getCharacterID (long characterID) {
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


}
