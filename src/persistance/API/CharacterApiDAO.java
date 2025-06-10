package persistance.API;

import business.entities.Character;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.salle.url.api.ApiHelper;
import edu.salle.url.api.exception.ApiException;
import edu.salle.url.api.exception.status.IncorrectRequestException;
import persistance.CharacterDAO;
import persistance.exceptions.PersistanceException;

import java.net.http.HttpClient;
import java.util.List;

public class CharacterApiDAO implements CharacterDAO {
    private static final String BASE_URL = "https://balandrau.salle.url.edu/dpoo/shared/characters";
    private static final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    @Override
    public List<Character> loadAllCharacters() throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();

            String json = apiHelper.getFromUrl(BASE_URL);

            return gson.fromJson(json, new TypeToken<List<Character>>() {}.getType());

        } catch (ApiException e) {
            throw new PersistanceException("Error fetching all characters from API", e);
        }
    }


    public static void validateUsage() throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();
            String check = apiHelper.getFromUrl(BASE_URL);
        } catch (ApiException e){
            throw new PersistanceException(e.getMessage());
        }
    }

    @Override
    public boolean validateFile() {
        try {
            return !loadAllCharacters().isEmpty();
        } catch (PersistanceException e) {
            return false;
        }
    }

    @Override
    public Character getCharacterById(long id) throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();
            String url = BASE_URL + "?id=" + id;
            String json = apiHelper.getFromUrl(url);
            
            // Handle both array and single object responses
            if (json.trim().startsWith("[")) {
                // API returned an array
                List<Character> characters = gson.fromJson(json, new TypeToken<List<Character>>() {}.getType());
                return characters.isEmpty() ? null : characters.get(0);
            } else {
                // API returned a single object
                return gson.fromJson(json, Character.class);
            }
        } catch (IncorrectRequestException e) {
            if (e.getStatusCode() == 404) {
                return null;
            }
            throw new PersistanceException("Failed to get character by id: " + e.getMessage(), e);

        } catch (ApiException e) {
            throw new PersistanceException("Error fetching character by id from API", e);
        }
    }

    @Override
    public Character getCharacterByName(String name) throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();
            String url = BASE_URL + "?name=" + name;
            String json = apiHelper.getFromUrl(url);
            
            // Handle both array and single object responses
            if (json.trim().startsWith("[")) {
                // API returned an array
                List<Character> characters = gson.fromJson(json, new TypeToken<List<Character>>() {}.getType());
                return characters.isEmpty() ? null : characters.get(0);
            } else {
                // API returned a single object
                return gson.fromJson(json, Character.class);
            }

        } catch (IncorrectRequestException e) {
            if (e.getStatusCode() == 404) {
                return null;
            }
            throw new PersistanceException("Failed to get character by name: " + e.getMessage(), e);

        } catch (ApiException e) {
            throw new PersistanceException("Error fetching character by name from API", e);
        }
    }

    @Override
    public List<String> getCharactersByNames() throws PersistanceException {
        return loadAllCharacters().stream().map(Character::getName).toList();
    }

    @Override
    public Character findCharacter(String input) throws PersistanceException {
        if (input.matches("\\d+")) {
            return getCharacterById(Long.parseLong(input));
        } else {
            return getCharacterByName(input);
        }
    }

    @Override
    public Character findCharacterByIndex(int index) throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();

            String url = BASE_URL + "/" + index;

            String json = apiHelper.getFromUrl(url);

            return gson.fromJson(json, Character.class);

        } catch (IncorrectRequestException e) {
            if (e.getStatusCode() == 404) {
                return null;
            }
            throw new PersistanceException("Failed to get character by index: " + e.getMessage(), e);

        } catch (ApiException e) {
            // Wrap any
            // other ApiExceptions in a PersistanceException
            throw new PersistanceException("Error fetching character by index from API", e);
        }
    }

}
