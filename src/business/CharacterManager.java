package business;

import business.entities.Character;
import persistance.CharacterDAO;
import persistance.api.exception.ApiException;
import persistance.json.exceptions.PersistanceException;
import persistance.CharacterTotalDAO;
import java.util.*;


public class CharacterManager {
    private final CharacterDAO characterDAO;

    // Default Constructor - Creates DAO internally
    public CharacterManager() throws PersistanceException, ApiException {
        this.characterDAO = new CharacterTotalDAO();
    }

    public CharacterManager(CharacterDAO characterDAO) throws PersistanceException {
        this.characterDAO = characterDAO;
    }

    // Validate the persistence source through the DAO
    public boolean validatePersistenceSource() {
        return characterDAO.validateFile();
    }

    public Character findCharacter(String input) throws PersistanceException {
        return characterDAO.findCharacter(input);
    }

    public List<String> getCharacterNames() {
        return characterDAO.getCharactersByNames();
    }

    public Character getCharacterByName(String name) {
        return characterDAO.getCharacterByName(name);
    }

    public Character findCharacterByIndex(int index){
        return characterDAO.findCharacterByIndex(index);
    }
}
