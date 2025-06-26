package persistance.API;

import business.entities.Statistics;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import edu.salle.url.api.ApiHelper;
import edu.salle.url.api.exception.ApiException;
import persistance.StatisticsDAO;
import persistance.exceptions.PersistanceException;

import java.net.http.HttpClient;
import java.util.*;

public class StatisticsApiDAO implements StatisticsDAO {
    private static final String BASE_URL = "https://balandrau.salle.url.edu/dpoo/S1-Project-13/stats";
    private static final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    @Override
    public ArrayList<Statistics> loadStatistics() throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();
            String json = apiHelper.getFromUrl(BASE_URL);

            if (json == null || json.trim().isEmpty()) {
                System.err.println("Warning: Empty response from statistics API");
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
            } else {
                System.err.println("Warning: Last snapshot is not a JSON array");
            }

            return result;

        } catch (ApiException e) {
            throw new PersistanceException("Error fetching statistics from API", e);
        } catch (com.google.gson.JsonSyntaxException e) {
            throw new PersistanceException("Malformed JSON while loading statistics", e);
        }
    }

    /**
     * Updates existing statistics or adds new ones. Always keeps the latest data.
     */
    private void updateOrAddStatistics(ArrayList<Statistics> allStats, Statistics newStat) {
        // Find existing statistics for this team
        for (int i = 0; i < allStats.size(); i++) {
            if (allStats.get(i).getName().equals(newStat.getName())) {
                // Replace with newer statistics
                allStats.set(i, newStat);
                return;
            }
        }
        // If not found, add new statistics
        allStats.add(newStat);
    }

    private boolean containsStatWithName(ArrayList<Statistics> stats, String name) {
        return stats.stream().anyMatch(s -> s.getName().equals(name));
    }

    /**
     * Validates if the API is available.
     */
    public static void validateUsage() throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();
            String check = apiHelper.getFromUrl(BASE_URL);
        } catch (ApiException e){
            throw new PersistanceException(e.getMessage());
        }
    }

    @Override
    public void saveStatistics(List<Statistics> statistics) throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();

            // Step 1: Delete the only snapshot (index 0), if it exists
            try {
                apiHelper.deleteFromUrl(BASE_URL + "/0");
            } catch (ApiException e) {
                System.err.println("Warning: Could not delete existing statistics snapshot: " + e.getMessage());
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
