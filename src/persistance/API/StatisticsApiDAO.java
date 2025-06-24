package persistance.API;

import business.entities.Statistics;
import com.google.gson.Gson;
import edu.salle.url.api.ApiHelper;
import edu.salle.url.api.exception.ApiException;
import persistance.StatisticsDAO;
import persistance.exceptions.PersistanceException;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

public class StatisticsApiDAO implements StatisticsDAO {
    private static final String BASE_URL = "https://balandrau.salle.url.edu/dpoo/S1-Project-13/stats";
    private static final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    @Override
    public ArrayList<Statistics> loadStatistics() throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();
            String json = apiHelper.getFromUrl(BASE_URL);

            // Check if JSON is empty or null
            if (json == null || json.trim().isEmpty()) {
                System.err.println("Warning: Empty response from statistics API");
                return new ArrayList<>();
            }

            // Custom parsing to handle nested array structure
            // Since API appends all saves, we need to get the LATEST occurrence of each team
            ArrayList<Statistics> allStats = new ArrayList<>();
            try {
                // First try to parse as a regular array of Statistics
                com.google.gson.JsonArray jsonArray = gson.fromJson(json, com.google.gson.JsonArray.class);
                
                for (com.google.gson.JsonElement element : jsonArray) {
                    if (element.isJsonObject()) {
                        // Direct Statistics object
                        Statistics stat = gson.fromJson(element, Statistics.class);
                        updateOrAddStatistics(allStats, stat);
                    } else if (element.isJsonArray()) {
                        // Nested array of Statistics
                        com.google.gson.JsonArray nestedArray = element.getAsJsonArray();
                        for (com.google.gson.JsonElement nestedElement : nestedArray) {
                            if (nestedElement.isJsonObject()) {
                                Statistics stat = gson.fromJson(nestedElement, Statistics.class);
                                updateOrAddStatistics(allStats, stat);
                            }
                        }
                    }
                }
                return allStats;
            } catch (com.google.gson.JsonSyntaxException e) {
                System.err.println("Warning: Malformed statistics JSON from API: " + e.getMessage());
                return new ArrayList<>();
            }

        } catch (ApiException e) {
            throw new PersistanceException("Error fetching statistics from API", e);
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
            String jsonBody = gson.toJson(statistics);
            String response = apiHelper.postToUrl(BASE_URL, jsonBody);
        } catch (ApiException e) {
            throw new PersistanceException("Error saving statistics to API: " + e.getMessage(), e);
        }
    }



}
