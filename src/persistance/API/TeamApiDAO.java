package persistance.API;

import business.CombatStrategy;
import business.StrategyFactory;
import business.entities.Character;
import business.entities.Member;
import business.entities.MemberPrint;
import business.entities.Team;
import business.entities.TeamPrint;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.salle.url.api.ApiHelper;
import edu.salle.url.api.exception.ApiException;
import edu.salle.url.api.exception.status.IncorrectRequestException;
import persistance.TeamDAO;
import persistance.exceptions.PersistanceException;

import java.util.ArrayList;
import java.util.List;

/**
 * API-based implementation of TeamDAO for managing team data.
 * Uses REST API calls to fetch, save, and manage team information from external sources.
 */
public class TeamApiDAO implements TeamDAO {
    private final Gson gson = new Gson();

    private static final String BASE_URL = "https://balandrau.salle.url.edu/dpoo/S1-Project-13/teams";

    /**
     * Validates that the API is accessible and working.
     *
     * @throws PersistanceException if the API is not reachable
     */
    public static void validateUsage() throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();
            String check = apiHelper.getFromUrl(BASE_URL);
        } catch (ApiException e){
            throw new PersistanceException(e.getMessage());
        }
    }

    /**
     * Loads all teams from the API.
     *
     * @return An ArrayList of Team objects
     * @throws PersistanceException if there's an error fetching teams from the API
     */
    @Override
    public ArrayList<Team> loadTeams() throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();
            String json = apiHelper.getFromUrl(BASE_URL);

            TeamPrint[] teamPrints = gson.fromJson(json, TeamPrint[].class);
            CharacterApiDAO characterApiDAO = new CharacterApiDAO();
            ArrayList<Team> finalTeams = new ArrayList<>();

            for (TeamPrint teamPrint : teamPrints) {
                List<Member> members = new ArrayList<>();
                for (MemberPrint m : teamPrint.getMembers()) {
                    Character character = characterApiDAO.getCharacterById(m.getId());
                    CombatStrategy strategy = StrategyFactory.createStrategyByName(m.getStrategy());
                    members.add(new Member(m.getId(), character, strategy));
                }
                Team t = new Team(teamPrint.getName());
                t.setMembers(members);
                finalTeams.add(t);
            }

            return finalTeams;

        } catch (ApiException e) {
            throw new PersistanceException("Error fetching teams from API", e);
        }
    }

    /**
     * Saves a new team to the API.
     *
     * @param newTeam The team to save
     * @throws PersistanceException if there's an error saving the team to the API
     */
    @Override
    public void saveNewTeams(Team newTeam) throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();
            String jsonBody = gson.toJson(convertToTeamPrint(newTeam));
            String response = apiHelper.postToUrl(BASE_URL, jsonBody);

        } catch (ApiException e) {
            throw new PersistanceException("Error saving team to API", e);
        }
    }

    /**
     * Retrieves a team by its name from the API.
     *
     * @param name The name of the team to retrieve
     * @return The Team object if found, null otherwise
     * @throws PersistanceException if there's an error fetching the team from the API
     */
    @Override
    public Team getTeamByName(String name) throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();

            String json = apiHelper.getFromUrl(BASE_URL + "?name=" + name);
            TeamPrint teamPrint;
            // Parse JSON response
            if (json.trim().startsWith("[")) {
                List<TeamPrint> teamPrints = gson.fromJson(json, new TypeToken<List<TeamPrint>>() {}.getType());
                if (teamPrints.isEmpty()) return null;
                teamPrint = teamPrints.getFirst();
            } else {
                teamPrint = gson.fromJson(json, TeamPrint.class);
            }

            // Convert to Team object with characters and strategies
            CharacterApiDAO characterApiDAO = new CharacterApiDAO();
            List<Member> finalMembers = new ArrayList<>();

            for (MemberPrint memberPrint : teamPrint.getMembers()) {
                Character character = characterApiDAO.getCharacterById(memberPrint.getId());
                CombatStrategy strategy = StrategyFactory.createStrategyByName(memberPrint.getStrategy());
                finalMembers.add(new Member(memberPrint.getId(), character, strategy));
            }

            Team team = new Team(teamPrint.getName());
            team.setMembers(finalMembers);
            return team;

        } catch (IncorrectRequestException e) {
            if (e.getStatusCode() == 404) {
                return null;
            }
            throw new PersistanceException("Failed to get team: " + e.getMessage(), e);

        } catch (ApiException e) {
            throw new PersistanceException("Error fetching team from API", e);
        }
    }

    /**
     * Retrieves a list of team names that contain a specific character.
     *
     * @param characterId The ID of the character
     * @return A list of team names containing the specified character
     * @throws PersistanceException if there's an error fetching teams from the API
     */
    @Override
    public List<String> getTeamsNamesWithCharacter(long characterId) throws PersistanceException {
        List<String> teamNames = new ArrayList<>();
        List<Team> teams = loadTeams();

        for (Team team : teams) {
            for (Member member : team.getMembers()) {
                if (member.getCharacterId() == characterId) {
                    teamNames.add(team.getName());
                    break;
                }
            }
        }
        return teamNames;
    }

    /**
     * Converts a Team object to a TeamPrint object for API serialization.
     *
     * @param team The team to convert
     * @return The TeamPrint object
     */
    @Override
    public TeamPrint convertToTeamPrint(Team team) {
        List<MemberPrint> memberPrints = new ArrayList<>();
        for (Member member : team.getMembers()) {
            memberPrints.add(new MemberPrint(member.getCharacterId(), member.getStrategyName()));
        }
        return new TeamPrint(team.getName(), memberPrints);
    }

    /**
     * Deletes a team from the API.
     *
     * @param teamName The name of the team to delete
     * @throws PersistanceException if there's an error deleting the team from the API
     */
    @Override
    public void deleteTeam(String teamName) throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();

            String url = BASE_URL + "?name=" + teamName;

            apiHelper.deleteFromUrl(url);

        } catch (ApiException e) {
            throw new PersistanceException("Error deleting team from API", e);
        }
    }

    /**
     * Checks if a team with the specified name exists.
     *
     * @param teamName The name of the team to check
     * @return true if the team exists, false otherwise
     * @throws PersistanceException if there's an error checking the team existence
     */
    @Override
    public boolean exists(String teamName) throws PersistanceException {
        return getTeamByName(teamName) != null;
    }

    /**
     * Matches character information with teams loaded from the API.
     *
     * @return An ArrayList of Team objects with full character information
     * @throws PersistanceException if there's an error fetching teams or characters from the API
     */
    private ArrayList<Team> matchCharacterTeam() throws PersistanceException {
        // Load the teams from the API
        ArrayList<Team> teams = loadTeams();
        if (teams == null) {
            return new ArrayList<>();
        }
        CharacterApiDAO characterApiDAO = new CharacterApiDAO();

        // For each team, update its members with full Character information
        for (Team team : teams) {
            List<Member> finalMembers = new ArrayList<>();

            for (Member member : team.getMembers()) {
                // If no character is linked, skip this member
                if (member.getCharacterId() == 0) {
                    continue;
                }
                // Retrieve the character via the API
                Character character = characterApiDAO.getCharacterById(member.getCharacterId());
                if (character == null) {
                    continue;
                }

                CombatStrategy strategy = StrategyFactory.createStrategyByName(member.getStrategyName());

                Member newMember = new Member(member.getCharacterId(), character, strategy);
                finalMembers.add(newMember);
            }

            team.setMembers(finalMembers);
        }
        return teams;
    }

    /**
     * Finds a team by its index position in the list.
     *
     * @param index The index (0-based) of the team in the list
     * @return The Team object if found
     * @throws PersistanceException if there's an error fetching teams from the API
     */
    @Override
    public Team findTeamByIndex(int index) throws PersistanceException {
        List<Team> teams = matchCharacterTeam();
        return teams.get(index);
    }

    /**
     * Loads the names of all teams from the API.
     *
     * @return A list of team names
     * @throws PersistanceException if there's an error fetching teams from the API
     */
    @Override
    public List<String> loadTeamNames() throws PersistanceException {
        List<String> teamNames = new ArrayList<>();
        List<Team> teams = loadTeams();
        for (Team team : teams) {
            teamNames.add(team.getName());
        }
        return teamNames;
    }
}
