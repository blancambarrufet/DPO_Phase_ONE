package persistance.json;

import business.CombatStrategy;
import business.StrategyFactory;
import business.entities.Character;
import business.entities.Member;
import business.entities.MemberPrint;
import business.entities.Team;
import business.entities.TeamPrint;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import persistance.TeamDAO;
import persistance.exceptions.PersistanceException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Implementation of TeamDAO for managing team data using JSON files.
 * This class provides functionality for loading, saving, deleting, and searching teams in the system.
 */
public class TeamJsonDAO implements TeamDAO {

    private static final String PATH = "data/teams.json";
    private final Gson gson;
    private CharacterJsonDAO characterJsonDAO;

    /**
     * Constructor for TeamJsonDAO.
     * Initializes the Gson instance for JSON processing and ensures the teams file exists.
     */
    public TeamJsonDAO() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.characterJsonDAO = new CharacterJsonDAO();
        initializeFile();
    }

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

    /**
     * Loads all teams from the JSON file.
     * If the file does not exist, it initializes an empty teams.json file.
     *
     * @return {@code List<String>} A list of all stored teams.
     * @throws PersistanceException If an error occurs while reading or initializing the file.
     */
    @Override
    public ArrayList<Team> loadTeams() throws PersistanceException {
        Path filePath = Path.of(PATH);

        //Create an empty file if missing
        if (!Files.exists(filePath)) {
            try (FileWriter writer = new FileWriter(PATH)) {
                gson.toJson(new ArrayList<>(), writer);
            } catch (IOException e) {
                throw new PersistanceException("Error initializing teams.json file.", e);
            }
        }

        //loading existing teams
        try (JsonReader reader = new JsonReader(new FileReader(PATH))) {
            TeamPrint[] teamPrints = gson.fromJson(reader, TeamPrint[].class);
            ArrayList<Team> fullTeams = new ArrayList<>();

            for (TeamPrint teamPrint : teamPrints) {
                List<Member> members = new ArrayList<>();
                for (MemberPrint m : teamPrint.getMembers()) {
                    Character c = characterJsonDAO.getCharacterById(m.getId());
                    CombatStrategy strategy = StrategyFactory.createStrategyByName(m.getStrategy());
                    members.add(new Member(m.getId(), c, strategy));
                }
                Team t = new Team(teamPrint.getName());
                t.setMembers(members);
                fullTeams.add(t);
            }

            return fullTeams; // Convert array to ArrayList

        } catch (JsonSyntaxException | IOException e) {
            throw new PersistanceException("Couldn't read teams file: " + PATH, e);
        }
    }


    private ArrayList<Team> matchCharacterTeam() throws PersistanceException {
        ArrayList<Team> teams = loadTeams();

        if (teams == null) return new ArrayList<>(); // Ensure it's not null

        for (Team team : teams) {
            List<Member> finalMembers = new ArrayList<>();

            for (Member member : team.getMembers()) {
                if (member.getCharacterId() == 0) continue;

                if (characterJsonDAO == null) {
                    characterJsonDAO = new CharacterJsonDAO(); // Initialize if null
                }

                Character character = characterJsonDAO.getCharacterById(member.getCharacterId());
                if (character == null) continue;

                CombatStrategy strategy = StrategyFactory.createStrategyByName(member.getStrategyName());

                //Create a new Member with the strategy
                Member finalMember = new Member(member.getCharacterId(), character, strategy);
                finalMembers.add(finalMember);
            }
            team.setMembers(finalMembers);
        }
        return teams;
    }


    private List<TeamPrint> loadTeamsPrint() {
            try (JsonReader reader = new JsonReader(new FileReader(PATH))) {
                TeamPrint[] teamsArray = gson.fromJson(reader, TeamPrint[].class);
                return teamsArray != null ? new ArrayList<>(Arrays.asList(teamsArray)) : new ArrayList<>();
            } catch (IOException | JsonSyntaxException e) {
                return new ArrayList<>(); // Return empty list if the file is empty or malformed
            }
    }

    /**
     * Converts a team object into a printable format.
     *
     * @param team The team to convert.
     * @return TeamPrint The converted team object.
     */
    public TeamPrint convertToTeamPrint(Team team) {
        List<MemberPrint> memberPrints = new ArrayList<>();

        for (Member member : team.getMembers()) {
            memberPrints.add(new MemberPrint(member.getCharacterId(), member.getStrategyName()));
        }

        return new TeamPrint(team.getName(), memberPrints);
    }

    /**
     * Saves a new team to the JSON file.
     *
     * @param newTeamName The team to be saved.
     * @throws PersistanceException If an error occurs while writing to the file.
     */
    @Override
    public void saveNewTeams(Team newTeamName) throws PersistanceException {
        try {
            // Load existing teams
            List<TeamPrint> teams = loadTeamsPrint();



            // Convert to TeamPrint
            TeamPrint teamPrint = convertToTeamPrint(newTeamName);

            // Add the new team print if not already in the list
            if (!teams.contains(teamPrint)) {
                teams.add(teamPrint);
            }

            // Save back to the file
            try (FileWriter writer = new FileWriter(PATH)) {
                gson.toJson(teams, writer);
            }

        } catch (IOException e) {
            throw new PersistanceException("Couldn't write teams file: " + PATH, e);
        }
    }

    /**
     * Deletes a team from the JSON file.
     *
     * @param teamName The name of the team to be deleted.
     * @throws PersistanceException If the team is not found or an error occurs while writing to the file.
     */
    @Override
    public void deleteTeam(String teamName) throws PersistanceException {

        List<TeamPrint> teams = loadTeamsPrint();

        // Remove the team
        boolean removed = teams.removeIf(team -> team.getName().equalsIgnoreCase(teamName));

        if (!removed) {
            throw new PersistanceException("Team not found: " + teamName);
        }


        // Write updated list back to file
        try (FileWriter writer = new FileWriter(PATH)) {
            gson.toJson(teams, writer);
        } catch (IOException e) {
            throw new PersistanceException("Failed to write updated team list", e);
        }
    }

    /**
     * Retrieves a team by its name.
     *
     * @param name The name of the team.
     * @return Team The corresponding team object if found, otherwise null.
     * @throws PersistanceException If the file cannot be read.
     */
    @Override
    public Team getTeamByName(String name) throws PersistanceException {
        try (JsonReader reader = new JsonReader(new FileReader(PATH))) {
            Team[] teamsArray = gson.fromJson(reader, Team[].class);
            for (Team team : teamsArray) {
                if (team.getName().equalsIgnoreCase(name)) {
                    List<Member> finalMembers = new ArrayList<>();
                    for (Member member : team.getMembers()) {
                        Character character = characterJsonDAO.getCharacterById(member.getCharacterId());
                        CombatStrategy strategy = StrategyFactory.createStrategyByName(member.getStrategyName());
                        finalMembers.add(new Member(member.getCharacterId(), character, strategy));
                    }
                    team.setMembers(finalMembers);
                    return team;
                }
            }
            return null;
        } catch (IOException | JsonSyntaxException e) {
            throw new PersistanceException("Couldn't read teams file: " + PATH, e);
        }
    }

    /**
     * Retrieves a list of team names that contain a specific character.
     *
     * @param characterId The ID of the character.
     * @return {@code List<String>} A list of team names containing the specified character.
     * @throws PersistanceException If an error occurs while reading the file.
     */
    @Override
    public List<String> getTeamsNamesWithCharacter(long characterId) throws PersistanceException {
        List<String> teamNames = new ArrayList<>();

        try (JsonReader reader = new JsonReader(new FileReader(PATH))) {
            Team[] teamsArray = gson.fromJson(reader, Team[].class);
            if (teamsArray == null) return teamNames;

            for (Team team : teamsArray) {
                for (Member member : team.getMembers()) {
                    if (member.getCharacterId() == characterId) {
                        teamNames.add(team.getName());
                        break;
                    }
                }
            }
        } catch (IOException | JsonSyntaxException e) {
            throw new PersistanceException("Error loading teams from " + PATH + ": " + e.getMessage(), e);
        }

        return teamNames;
    }

    /**
     * Checks if a team with the specified name exists in the system.
     *
     * @param teamName The name of the team to search for.
     * @return boolean True if the team exists, otherwise false.
     * @throws PersistanceException If an error occurs during the check.
     */
    @Override
    public boolean exists(String teamName) throws PersistanceException {
        List<Team> teams = matchCharacterTeam();

        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(teamName)) {
                return true; // Found the team, return true immediately
            }
        }

        return false; // No match found, return false
    }

    /**
     * Loads the names of all teams in the system.
     *
     * @return {@code List<String>} A list of all available team names.
     * @throws PersistanceException If an error occurs during loading.
     */
    @Override
    public List<String> loadTeamNames() throws PersistanceException {
        List<Team> teams = matchCharacterTeam();

        List<String> teamNames = new ArrayList<>();
        for (Team team : teams) {
            if (team.getName() != null) {
                teamNames.add(team.getName());
            }
        }

        return teamNames;
    }

    /**
     * Retrieves a team by its index position in the list.
     *
     * @param index The index (0-based) of the team in the list.
     * @return Team The corresponding team object.
     * @throws PersistanceException If an error occurs during retrieval.
     */
    @Override
    public Team findTeamByIndex(int index) throws PersistanceException {
        List<Team> teams = matchCharacterTeam();
        return teams.get(index);
    }



}
