package persistance.json;

import business.entities.Team;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import persistance.TeamDAO;
import persistance.exceptions.PersistanceException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamJsonDAO implements TeamDAO {

    private static final String PATH = "data/teams.json";
    private final Gson gson;

    public TeamJsonDAO() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        initializeFile();
    }

    // Initialize file with empty structure if missing
    private void initializeFile() {
        Path filePath = Path.of(PATH);
        if (!Files.exists(filePath)) {
            try (FileWriter writer = new FileWriter(PATH)) {
                gson.toJson(List.of(), writer); // Empty JSON array
            } catch (IOException e) {
                throw new RuntimeException("Error initializing teams.json file.", e);
            }
        }
    }

    @Override
    public boolean isFileOk() {
        Path filePath = Path.of(PATH);
        return Files.exists(filePath) && Files.isReadable(filePath);
    }

    @Override
    public ArrayList<Team> loadTeams() throws PersistanceException {
        try (JsonReader reader = new JsonReader(new FileReader(PATH))) {
            Team[] teamsArray = gson.fromJson(reader, Team[].class);

            // Ensure the list is initialized correctly
            if (teamsArray == null) {
                return new ArrayList<>(); // Return an empty list if file is empty or improperly formatted
            }

            return new ArrayList<>(Arrays.asList(teamsArray)); // Convert array to ArrayList
        } catch (JsonSyntaxException | IOException e) {
            throw new PersistanceException("Couldn't read teams file: " + PATH, e);
        }
    }


    @Override
    public void saveTeams(List<Team> teams) throws PersistanceException {
        try (FileWriter writer = new FileWriter(PATH)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(teams, writer);
        } catch (IOException e) {
            throw new PersistanceException("Couldn't write teams file: " + PATH, e);
        }
    }
}
