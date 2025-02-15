package persistance.json;

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

public class TeamJsonDAO implements TeamDAO {

    private static final String PATH = "data/teams.json";
    private final Gson gson;
    CharacterJsonDAO characterJsonDAO;

    public TeamJsonDAO() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.characterJsonDAO = new CharacterJsonDAO();
        initializeFile();
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
    public boolean isFileOk() {
        Path filePath = Path.of(PATH);
        return Files.exists(filePath) && Files.isReadable(filePath);
    }

    @Override
    public Team getTeamByName(String name) {
        try (JsonReader reader = new JsonReader(new FileReader(PATH))) {
            Team[] teamsArray = gson.fromJson(reader, Team[].class);
            for (Team team : teamsArray) {
                if (team.getName().equalsIgnoreCase(name)) {
                    return team;
                }
            }
            return null;
        } catch (IOException e) {
            throw new PersistanceException("Couldn't read teams file: " + PATH, e);
        }
    }

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
            Team[] teamsArray = gson.fromJson(reader, Team[].class);
            return new ArrayList<>(Arrays.asList(teamsArray)); // Convert array to ArrayList

        } catch (JsonSyntaxException | IOException e) {
            throw new PersistanceException("Couldn't read teams file: " + PATH, e);
        }
    }


    private ArrayList<Team> matchCharacterTeam() {
        ArrayList<Team> teams = loadTeams();

        if (teams == null) return new ArrayList<>(); // Ensure it's not null

        for (Team team : teams) {
            for (Member member : team.getMembers()) {
                if (member.getCharacterId() == 0) continue;

                if (characterJsonDAO == null) {
                    characterJsonDAO = new CharacterJsonDAO(); // Initialize if null
                }

                Character character = characterJsonDAO.getCharacterById(member.getCharacterId());
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

    public Team findTeamByName(String name) {
        try (JsonReader reader = new JsonReader(new FileReader(PATH))) {
            Team[] teamsArray = gson.fromJson(reader, Team[].class);

            for (Team team : teamsArray) {
                if (team.getName().equalsIgnoreCase(name)) {
                    return team;
                }
            }
        } catch (IOException e) {
            throw new PersistanceException("Couldn't read teams file: " + PATH, e);
        }

        return null;
    }

    @Override
    public Member getRandomAvailableDefender(String teamName) {
        try (JsonReader reader = new JsonReader(new FileReader(PATH))) {
            Team[] teamsArray = gson.fromJson(reader, Team[].class);
            if (teamsArray == null) return null;

            for (Team team : teamsArray) {
                if (team.getName().equalsIgnoreCase(teamName)) {
                    List<Member> availableDefenders = new ArrayList<>();
                    for (Member member : team.getMembers()) {
                        if (!member.isKO()) {
                            availableDefenders.add(member);
                        }
                    }

                    return availableDefenders.isEmpty() ? null : availableDefenders.get(new Random().nextInt(availableDefenders.size()));
                }
            }
        } catch (IOException | JsonSyntaxException e) {
            throw new PersistanceException("Error loading teams from " + PATH + ": " + e.getMessage(), e);
        }

        return null; // Team not found
    }

    private List<TeamPrint> loadTeamsPrint() {
        try (JsonReader reader = new JsonReader(new FileReader(PATH))) {
            TeamPrint[] teamsArray = gson.fromJson(reader, TeamPrint[].class);
            return teamsArray != null ? new ArrayList<>(Arrays.asList(teamsArray)) : new ArrayList<>();
        } catch (IOException | JsonSyntaxException e) {
            return new ArrayList<>(); // Return empty list if the file is empty or malformed
        }
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


        // Write updated list back to file
        try (FileWriter writer = new FileWriter(PATH)) {
            gson.toJson(teams, writer);
        } catch (IOException e) {
            throw new PersistanceException("Failed to write updated team list", e);
        }
    }

    public boolean exists(String teamName) {
        List<Team> teams = matchCharacterTeam();

        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(teamName)) {
                return true; // Found the team, return true immediately
            }
        }

        return false; // No match found, return false
    }


    public List<String> loadTeamNames() {
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


    public Team findTeamByIndex(int index){
        List<Team> teams = matchCharacterTeam();
        return teams.get(index );
    }




}
