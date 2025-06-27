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

/**
 * API-based implementation of CharacterDAO for managing character data.
 * Uses REST API calls to fetch character information from external sources.
 */
public class CharacterApiDAO implements CharacterDAO {
    private static final String BASE_URL = "https://balandrau.salle.url.edu/dpoo/shared/characters";
    private final Gson gson = new Gson();

    /**
     * Loads all characters from the API.
     *
     * @return A list of all available characters
     * @throws PersistanceException if there's an error fetching characters from the API
     */
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

    /**
     * Validates that the API is accessible and working.
     *
     * @throws PersistanceException if the API is not reachable or returns empty response
     */
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

    /**
     * Retrieves a character by its ID from the API.
     *
     * @param id The ID of the character to retrieve
     * @return The character object if found, null otherwise
     * @throws PersistanceException if there's an error fetching the character
     */
    @Override
    public Character getCharacterById(long id) throws PersistanceException {
        return fetchCharacter("id", String.valueOf(id), "id");
    }

    /**
     * Retrieves a character by its name from the API.
     *
     * @param name The name of the character to retrieve
     * @return The character object if found, null otherwise
     * @throws PersistanceException if there's an error fetching the character
     */
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

    /**
     * Fetches a character from the API using the specified query parameter.
     *
     * @param queryParam The query parameter name (e.g., "id", "name")
     * @param value The value to search for
     * @param errorContext The context for error messages
     * @return The character object if found, null otherwise
     * @throws PersistanceException if there's an error fetching the character
     */
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

    /**
     * Retrieves a list of all character names from the API.
     *
     * @return A list of character names
     * @throws PersistanceException if there's an error fetching the character names
     */
    @Override
    public List<String> getCharactersByNames() throws PersistanceException {
        return loadAllCharacters().stream().map(Character::getName).toList();
    }

    /**
     * Finds a character by either ID or name from the API.
     *
     * @param input The ID (numeric) or name of the character to find
     * @return The character object if found, null otherwise
     * @throws PersistanceException if there's an error fetching the character
     */
    @Override
    public Character findCharacter(String input) throws PersistanceException {
        if (input.matches("\\d+")) {
            return getCharacterById(Long.parseLong(input));
        } else {
            return getCharacterByName(input);
        }
    }

    /**
     * Retrieves a character by its index position from the API.
     *
     * @param index The 1-based index of the character in the list
     * @return The character object if found, null otherwise
     * @throws PersistanceException if there's an error fetching the character
     */
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
