package persistance.API;

import business.entities.Character;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import edu.salle.url.api.ApiHelper;
import edu.salle.url.api.exception.ApiException;
import edu.salle.url.api.exception.status.IncorrectRequestException;
import persistance.CharacterDAO;
import persistance.exceptions.PersistanceException;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class CharacterApiDAO implements CharacterDAO {
    private static final String BASE_URL = "https://balandrau.salle.url.edu/dpoo/shared/characters";
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
            if (check == null || check.isBlank()) {
                throw new PersistanceException("API is reachable but returned an empty response.");
            }
        } catch (ApiException e){
            throw new PersistanceException(e.getMessage());
        }
    }

    @Override
    public Character getCharacterById(long id) throws PersistanceException {
        return fetchCharacter("id", String.valueOf(id), "id");
    }

    @Override
    public Character getCharacterByName(String name) throws PersistanceException {
        if (name == null || name.trim().isEmpty()) return null;

        try {
            String encodedName = java.net.URLEncoder.encode(name.trim(), "UTF-8");
            return fetchCharacter("name", encodedName, "name");
        } catch (UnsupportedEncodingException e) {
            throw new PersistanceException("Error encoding character name: " + name, e);
        }
    }

    private Character fetchCharacter(String queryParam, String value, String errorContext) throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();
            String url = BASE_URL + "?" + queryParam + "=" + value;
            String json = apiHelper.getFromUrl(url);

            if (json == null || json.trim().isEmpty()) {
                return null;
            }

            if (json.trim().startsWith("[")) {
                List<Character> characters = gson.fromJson(json, new TypeToken<List<Character>>() {}.getType());
                return characters.isEmpty() ? null : characters.getFirst();
            } else {
                return gson.fromJson(json, Character.class);
            }

        } catch (IncorrectRequestException e) {
            if (e.getStatusCode() == 404) return null;
            throw new PersistanceException("Failed to get character by " + errorContext + ": " + e.getMessage(), e);
        } catch (JsonSyntaxException e) {
            throw new PersistanceException("Invalid JSON response when fetching character by " + errorContext + ": " + e.getMessage(), e);
        } catch (ApiException e) {
            throw new PersistanceException("Error fetching character by " + errorContext + " from API", e);
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

            String url = BASE_URL + "/" + (index - 1);

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
