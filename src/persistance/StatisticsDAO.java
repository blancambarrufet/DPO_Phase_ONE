package persistance;

import business.entities.Statistics;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface for managing game statistics persistence.
 * Defines methods for loading and saving statistics data.
 */
public interface StatisticsDAO {

    /**
     * Loads game statistics from the persistence source.
     *
     * @return {@code ArrayList<Statistics>} A list of all stored game statistics.
     */
    ArrayList<Statistics> loadStatistics();

    /**
     * Saves updated game statistics to the persistence source.
     *
     * @param statistics The list of statistics to be saved.
     */
    void saveStatistics(List<Statistics> statistics);
}
