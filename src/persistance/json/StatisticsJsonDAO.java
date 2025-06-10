package persistance.json;

import business.entities.Statistics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import persistance.StatisticsDAO;
import persistance.exceptions.PersistanceException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Implementation of StatisticsDAO for managing game statistics using JSON files.
 * This class handles reading from and writing to the stats.json file.
 */
public class StatisticsJsonDAO implements StatisticsDAO {

    private static final String PATH = "data/stats.json";
    private final Gson gson;

    /**
     * Constructor for StatisticsJsonDAO.
     * Initializes a Gson instance for JSON processing.
     */
    public StatisticsJsonDAO() {
        this.gson = new Gson();
    }

    /**
     * Loads game statistics from the JSON file.
     * If the file does not exist, it initializes a new empty stats.json file.
     *
     * @return {@code ArrayList<Statistics>} A list of all stored statistics.
     * @throws PersistanceException If an error occurs while reading or initializing the file.
     */
    @Override
    public ArrayList<Statistics> loadStatistics() throws PersistanceException {
        Path filePath = Path.of(PATH);

        if (!Files.exists(filePath)) {
            try (FileWriter writer = new FileWriter(PATH)) {
                gson.toJson(new ArrayList<Statistics>(), writer);
            } catch (IOException e) {
                throw new PersistanceException("Error initializing stats.json file.", e);
            }
        }

        //loading existing statistics
        try (JsonReader reader = new JsonReader(new FileReader(PATH))) {
            Statistics[] statsArray = gson.fromJson(reader, Statistics[].class);

            return new ArrayList<>(Arrays.asList(statsArray)); // Convert array to ArrayList
        } catch (JsonSyntaxException | IOException e) {
            throw new PersistanceException("Couldn't read teams file: " + PATH, e);
        }
    }


    /**
     * Saves the updated game statistics to the JSON file.
     *
     * @param statistics The list of statistics to be saved.
     * @throws PersistanceException If an error occurs while writing to the file.
     */
    @Override
    public void saveStatistics(List<Statistics> statistics) throws PersistanceException {
        try (FileWriter writer = new FileWriter(PATH)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(statistics, writer);
        } catch (IOException e) {
            throw new PersistanceException("Couldn't write teams file: " + PATH, e);
        }
    }

}
