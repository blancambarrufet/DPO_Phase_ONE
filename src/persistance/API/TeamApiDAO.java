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

public class TeamApiDAO implements TeamDAO {
    private final Gson gson = new Gson();


    private static final String BASE_URL = "https://balandrau.salle.url.edu/dpoo/S1-Project-13/teams";

    public static void validateUsage() throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();
            String check = apiHelper.getFromUrl(BASE_URL);
        } catch (ApiException e){
            throw new PersistanceException(e.getMessage());
        }
    }


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


    @Override
    public Team getTeamByName(String name) throws PersistanceException {
        try {
            ApiHelper apiHelper = new ApiHelper();

            String json = apiHelper.getFromUrl(BASE_URL + "?name=" + name);
            Team team;

            // Handle both array and single object responses
            if (json.trim().startsWith("[")) {
                // API returned an array
                List<Team> teams = gson.fromJson(json, new TypeToken<List<Team>>() {}.getType());
                if (teams.isEmpty()) return null;
                team = teams.get(0);
            } else {
                // API returned a single object
                team = gson.fromJson(json, Team.class);
            }

            CharacterApiDAO characterApiDAO = new CharacterApiDAO();
            List<Member> finalMembers = new ArrayList<>();

            for(Member member : team.getMembers()) {
                Character character = characterApiDAO.getCharacterById(member.getCharacterId());
                CombatStrategy strategy = StrategyFactory.createStrategyByName(member.getStrategyName());
                finalMembers.add(new Member(member.getCharacterId(), character, strategy));
            }

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

    @Override
    public TeamPrint convertToTeamPrint(Team team) {
        List<MemberPrint> memberPrints = new ArrayList<>();
        for (Member member : team.getMembers()) {
            memberPrints.add(new MemberPrint(member.getCharacterId(), member.getStrategyName()));
        }
        return new TeamPrint(team.getName(), memberPrints);
    }

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

    @Override
    public boolean exists(String teamName) throws PersistanceException {
        return getTeamByName(teamName) != null;
    }

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


    @Override
    public Team findTeamByIndex(int index) throws PersistanceException {
        List<Team> teams = matchCharacterTeam();
        return teams.get(index);
    }

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
