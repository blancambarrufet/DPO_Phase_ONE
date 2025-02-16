package persistance;

import business.entities.*;
import business.entities.Character;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import persistance.json.exceptions.PersistanceException;

import persistance.json.exceptions.PersistanceException;
import persistance.api.ApiConnectionChecker;
import persistance.api.ApiPersistence;
import persistance.api.exception.ApiException;
import persistance.json.JsonPersistence;



import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class TeamTotalDAO implements TeamDAO {


    // The persistence manager is used to manage item persistence, either through an API or a JSON file.
    private final PersistenceManager<Team> persistenceManager;

    // Instance of JsonPersistence for local file validation.
    JsonPersistence<Team> jsonPersistence;

    private static final String PATH = "data/teams.json";
    CharacterTotalDAO characterTotalDAO;

    public TeamTotalDAO() throws ApiException {
        boolean useApi = ApiConnectionChecker.isApiAvailable();
        this.persistenceManager = useApi
                ? new ApiPersistence<>("https://balandrau.salle.url.edu/dpoo/{id}/teams", Team[].class)
                : new JsonPersistence<>("data/teams.json", Team[].class);

        // Validate JSON if API is not available
        if (!useApi && !((JsonPersistence<Team>) persistenceManager).validateFile()) {
            throw new PersistanceException("Error: JSON file for teams is missing or corrupted.");
        }
    }

    // Initialize file with empty structure if missing
    private void initializeFile() {
        Path filePath = Path.of(PATH);
        if (!Files.exists(filePath)) {
            try (FileWriter writer = new FileWriter(PATH)) {
                gson.toJson(new ArrayList<>(), writer); // Empty JSON array
            } catch (IOException e) {
                throw new RuntimeException("Error initializing teams.json file.", e);
            }
        }
    }

    @Override
    public boolean validateFile() {
        return jsonPersistence.validateFile();
    }

    @Override
    public ArrayList<Team> loadTeams() throws PersistanceException {
        return new ArrayList<>(persistenceManager.loadAll());
    }


    @Override
    public List<TeamPrint> loadTeamsPrint() throws PersistanceException {
        List<Team> teams = loadTeams();
        List<TeamPrint> teamPrints = new ArrayList<>();
        for (Team team : teams) {
            teamPrints.add(convertToTeamPrint(team));
        }
        return teamPrints;
    }



    private ArrayList<Team> matchCharacterTeam() throws ApiException {
        ArrayList<Team> teams = loadTeams();

        if (teams == null) return new ArrayList<>(); // Ensure it's not null

        for (Team team : teams) {
            for (Member member : team.getMembers()) {
                if (member.getCharacterId() == 0) continue;

                if (characterTotalDAO == null) {
                    characterTotalDAO = new CharacterTotalDAO(); // Initialize if null
                }

                Character character = characterTotalDAO.getCharacterById(member.getCharacterId());
                if (character != null) {
                    member.setCharacter(character); // Assign character properly
                } else {
                    System.out.println("WARNING: Character with ID " + member.getCharacterId() + " not found.");
                }
            }
        }
        return teams;
    }


    @Override
    public Team getTeamByName(String name) {
        ArrayList<Team> teams = loadTeams();
        for (Team team : teams) {
            if (team.getName().equals(name)) return team;
        }
        return null;
    }


    @Override
    public List<String> getTeamsNamesWithCharacter(long characterId) throws PersistanceException {
        List<String> teamNames = new ArrayList<>();
        List<Team> teamsArray = loadTeams();

        for (Team team : teamsArray) {
            for (Member member : team.getMembers()) {
                if (member.getCharacterId() == characterId) {
                    teamNames.add(team.getName());
                    break;
                }
            }
        }

        return teamNames;
    }

    public Team findTeamByName(String name) {
        List<Team> teams = loadTeams();
        for (Team team : teams) {
            if (team.getName().equals(name)) return team;
        }
        return null;
    }


    public TeamPrint convertToTeamPrint(Team team) {
        List<MemberPrint> memberPrints = new ArrayList<>();

        for (Member member : team.getMembers()) {
            memberPrints.add(new MemberPrint(member.getCharacterId(), member.getStrategy()));
        }

        return new TeamPrint(team.getName(), memberPrints);
    }


    public void deleteTeam(String teamName) {

        List<TeamPrint> teams = loadTeamsPrint();

        // Remove the team
        boolean removed = teams.removeIf(team -> team.getName().equalsIgnoreCase(teamName));

        if (!removed) {
            throw new PersistanceException("Team not found: " + teamName);
        }


        //SAVE TODO IMPLEMENT
        }


    public boolean exists(String teamName) throws ApiException {
        List<Team> teams = matchCharacterTeam();

        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(teamName)) {
                return true; // Found the team, return true immediately
            }
        }

        return false; // No match found, return false
    }


    public List<String> loadTeamNames() throws ApiException {
        List<Team> teams = matchCharacterTeam();

        // Ensure teams is never null
        if (teams == null) {
            return new ArrayList<>(); // Return an empty list instead of initializing
        }

        List<String> teamNames = new ArrayList<>();
        for (Team team : teams) {
            if (team.getName() != null) { // Avoid NullPointerException
                teamNames.add(team.getName());
            }
        }

        return teamNames;
    }


    public Team findTeamByIndex(int index) throws ApiException {
        List<Team> teams = matchCharacterTeam();
        return teams.get(index );
    }


    @Override
    public void saveNewTeams(TeamPrint newTeam) throws PersistanceException {
        try {
            // Load existing teams
            List<TeamPrint> teams = loadTeamsPrint();

            // Check if the team already exists
            if (teams.stream().anyMatch(team -> team.getName().equalsIgnoreCase(newTeam.getName()))) {
                throw new PersistanceException("Team with name '" + newTeam.getName() + "' already exists.");
            }

            // Add the new team
            teams.add(newTeam);

            // Save back to persistence
            //persistenceManager.save(teams);
        } catch (PersistanceException e) {
            throw new PersistanceException("Failed to save the new team: " + newTeam.getName(), e);
        }
    }



}
