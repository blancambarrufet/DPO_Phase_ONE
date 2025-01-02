package persistance.json;

import business.entities.Team;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import persistance.TeamDAO;
import persistance.exceptions.PersistanceException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class TeamJsonDAO implements TeamDAO {

    private static final String PATH = "data/teams.json";
    private final Gson gson;

    public TeamJsonDAO() {
        this.gson = new Gson();
    }

    // Check if file exists and is readable
    public boolean isFileOk() {
        Path filePath = Path.of(PATH);
        return Files.exists(filePath) && Files.isReadable(filePath);
    }

    @Override
    public List<Team> loadTeams() throws PersistanceException {
        try {
            JsonReader reader = new JsonReader(new FileReader(PATH));
            Team[] teamsArray = gson.fromJson(reader, Team[].class);
            return Arrays.asList(teamsArray);
        } catch (IOException e) {
            throw new PersistanceException("Couldn't read teams file: " + PATH, e);
        }
    }

    @Override
    public void saveTeams(List<Team> teams) throws PersistanceException {
        try (FileWriter writer = new FileWriter(PATH)) {
            gson.toJson(teams, writer);
        } catch (IOException e) {
            throw new PersistanceException("Couldn't write teams file: " + PATH, e);
        }
    }
}
