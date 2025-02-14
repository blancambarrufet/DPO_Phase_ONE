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

    public Character findCharacter(String input) throws PersistanceException {
        try {
            // First, check if input is a numeric ID
            if (input.matches("\\d+")) { //DEBUG: CHECK OTHER WAY
                return characterDAO.getCharacterById(Long.parseLong(input));
            } else {
                return characterDAO.getCharacterByName(input);
            }
        } catch (PersistanceException e) {
            return null;
        }
    }

    public List<String> getCharacterNames() {
        return characterDAO.getCharactersByNames();
    }

    public Character getCharacterById(long id) {
        return characterDAO.getCharacterById(id);
    }

    public Character getCharacterByName(String name) {
        return characterDAO.getCharacterByName(name);
    }
}
