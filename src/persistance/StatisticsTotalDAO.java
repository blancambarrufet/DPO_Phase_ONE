package persistance;

import business.entities.Item;
import business.entities.Statistics;
import persistance.json.exceptions.PersistanceException;

import java.util.List;
import java.util.ArrayList;

import persistance.api.ApiConnectionChecker;
import persistance.api.ApiPersistence;
import persistance.api.exception.ApiException;
import persistance.json.JsonPersistence;

public class StatisticsTotalDAO implements StatisticsDAO {

    // The persistence manager is used to manage statistics persistence, either through an API or a JSON file.
    private final PersistenceManager<Statistics> persistenceManager;

    // Instance of JsonPersistence for local file validation.
    JsonPersistence<Item> jsonPersistence;


    /**
     * Validates if the JSON file is correctly formatted and available.
     *
     * @return true if the file is valid, false otherwise.
     */
    public boolean validateFile() {
        return jsonPersistence.validateFile();
    }

    /**
     * Constructor that initializes the persistence method (API or JSON).
     * If the API is available, it uses ApiPersistence; otherwise, it falls back to JsonPersistence.
     * If neither is available, it throws a PersistanceException.
     *
     * @throws ApiException if an error occurs when connecting to the API.
     */
    public StatisticsTotalDAO() throws ApiException {
        boolean useApi = ApiConnectionChecker.isApiAvailable();
        this.persistenceManager = useApi
                ? new ApiPersistence<>("https://balandrau.salle.url.edu/dpoo/{id}/stats", Statistics[].class)
                : new JsonPersistence<>("data/stats.json", Statistics[].class);

        // Validate JSON if API is not available
        if (!useApi && !((JsonPersistence<Statistics>) persistenceManager).validateFile()) {
            throw new PersistanceException("Error: JSON file for statistics is missing or corrupted.");
        }
    }


    /**
     * Loads all statistics from the persistence manager.
     *
     * @return a list of all stored statistics.
     * @throws PersistanceException if an error occurs while loading statistics.
     */
    @Override
    public ArrayList<Statistics> loadStatistics() throws PersistanceException {
        return new ArrayList<>(persistenceManager.loadAll());
    }

    /**
     * Saves the provided list of statistics to the persistence manager.
     *
     * @param statistics the list of statistics to be saved.
     * @throws PersistanceException if an error occurs while saving the statistics data.
     */
    @Override
    public void saveStatistics(List<Statistics> statistics) throws PersistanceException {
        try {
            persistenceManager.save(statistics);
        } catch (PersistanceException e) {
            throw new PersistanceException("Failed to save statistics data.", e);
        }
    }
}
