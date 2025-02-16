package persistance;

import business.entities.Character;


import business.entities.Item;
import persistance.api.ApiConnectionChecker;
import persistance.api.ApiPersistence;
import persistance.api.exception.ApiException;
import persistance.json.JsonPersistence;
import persistance.json.exceptions.PersistanceException;


import java.util.ArrayList;
import java.util.List;


public class CharacterTotalDAO implements CharacterDAO {
    private final PersistenceManager<Character> persistenceManager;



    private static final String PATH = "data/characters.json"; // JSON file path

    JsonPersistence<Item> jsonPersistence;

    /**
     * Validates if the JSON file is correctly formatted and available.
     *
     * @return true if the file is valid, false otherwise.
     */
    public boolean validateFile(){
        return jsonPersistence.validateFile();
    }


    public CharacterTotalDAO() throws ApiException {
        boolean useApi = ApiConnectionChecker.isApiAvailable();
        this.persistenceManager = useApi
                ? new ApiPersistence<>("https://balandrau.salle.url.edu/dpoo/shared/characters", Character[].class)
                : new JsonPersistence<>("data/characters.json", Character[].class);
    }


    @Override
    public List<Character> loadAllCharacters() throws PersistanceException {
        return persistenceManager.loadAll();
    }



    @Override
    public Character getCharacterById(long id) {
        List<Character> characters = loadAllCharacters();

        for (Character character : characters) {
            if (character.getId() == id) {
                return character;
            }
        }
        return null;
    }

    @Override
    public Character getCharacterByName(String name) {
        List<Character> characters = loadAllCharacters();

        for (Character character : characters) {
            if (name.equalsIgnoreCase(character.getName())) {
                return character;
            }
        }
        return null;
    }

    @Override
    public List<String> getCharactersByNames() {
        List<Character> characters = loadAllCharacters();
        List<String> names = new ArrayList<>();
        for (Character character : characters) {
            names.add(character.getName());
        }
        return names;
    }

    //Function used to return the character for the Create Team option
    @Override
    public Character findCharacter(String input) throws PersistanceException {
        try {
            // First, check if input is a numeric ID
            if (input.matches("\\d+")) { //DEBUG: CHECK OTHER WAY
                return getCharacterById(Long.parseLong(input));
            } else {
                return getCharacterByName(input);
            }
        } catch (PersistanceException e) {
            return null;
        }
    }

    public Character findCharacterByIndex(int index) throws PersistanceException {
        try {
            List<Character> characters = loadAllCharacters();
            return characters.get(index-1);
        } catch (PersistanceException e) {
            return null;
        }

    }
}
