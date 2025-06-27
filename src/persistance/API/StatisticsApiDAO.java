package persistance.API;

import business.entities.Statistics;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import edu.salle.url.api.ApiHelper;
import edu.salle.url.api.exception.ApiException;
import persistance.StatisticsDAO;
import persistance.exceptions.PersistanceException;

import java.util.*;

/**
 * API-based implementation of StatisticsDAO for managing statistics data.
 * Uses REST API calls to fetch and save statistics information from external sources.
 */
public class StatisticsApiDAO implements StatisticsDAO {
    private static final String BASE_URL = "https://balandrau.salle.url.edu/dpoo/S1-Project-13/stats";
    private final Gson gson = new Gson();

    /**
     * Loads statistics from the API.
     *
     * @return An ArrayList of Statistics objects
     * @throws PersistanceException if there's an error fetching statistics from the API
     */
    @Override
    public ArrayList<Statistics> loadStatistics() throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();
            String json = apiHelper.getFromUrl(BASE_URL);

            if (json == null || json.trim().isEmpty()) {
                return new ArrayList<>();
            }

            // Parse the root JSON array (which contains multiple snapshots)
            com.google.gson.JsonArray rootArray = gson.fromJson(json, com.google.gson.JsonArray.class);

            if (rootArray.size() == 0) {
                return new ArrayList<>();
            }

            // Get the LAST snapshot (the latest one)
            JsonElement lastSnapshot = rootArray.get(rootArray.size() - 1);
            ArrayList<Statistics> result = new ArrayList<>();

            if (lastSnapshot.isJsonArray()) {
                for (JsonElement element : lastSnapshot.getAsJsonArray()) {
                    if (element.isJsonObject()) {
                        Statistics stat = gson.fromJson(element, Statistics.class);
                        result.add(stat);
                    }
                }
            }

            return result;

        } catch (ApiException e) {
            throw new PersistanceException("Error fetching statistics from API", e);
        } catch (com.google.gson.JsonSyntaxException e) {
            throw new PersistanceException("Malformed JSON while loading statistics", e);
        }
    }

    
   
    /**
     * Validates if the API is available.
     *
     * @throws PersistanceException if the API is not reachable
     */
    public static void validateUsage() throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();
            String check = apiHelper.getFromUrl(BASE_URL);
        } catch (ApiException e){
            throw new PersistanceException(e.getMessage());
        }
    }

    /**
     * Saves statistics to the API.
     *
     * @param statistics The list of statistics to save
     * @throws PersistanceException if there's an error saving statistics to the API
     */
    @Override
    public void saveStatistics(List<Statistics> statistics) throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();

            // Step 1: Delete the only snapshot (index 0), if it exists
            try {
                apiHelper.deleteFromUrl(BASE_URL + "/0");
            } catch (ApiException e) {
                // You may ignore this if it's a 404 (nothing to delete)
            }

            // Step 2: Post the new statistics snapshot
            String jsonBody = gson.toJson(statistics);
            apiHelper.postToUrl(BASE_URL, jsonBody);
        } catch (ApiException e) {
            throw new PersistanceException("Error saving statistics to API: " + e.getMessage(), e);
        }
    }

}
