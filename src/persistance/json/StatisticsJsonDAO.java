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


public class StatisticsJsonDAO implements StatisticsDAO {

    private static final String PATH = "data/stats.json";
    private final Gson gson;

    public StatisticsJsonDAO() {
        this.gson = new Gson();
    }

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

    @Override
    public void saveStatistics(List<Statistics> statistics) {
        try (FileWriter writer = new FileWriter(PATH)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(statistics, writer);
        } catch (IOException e) {
            throw new PersistanceException("Couldn't write teams file: " + PATH, e);
        }
    }

}
