package business;

import business.entities.*;
import business.entities.Character;
import persistance.TeamDAO;
import persistance.exceptions.PersistanceException;
import persistance.json.TeamJsonDAO;

import java.util.ArrayList;
import java.util.List;

public class TeamManager {

    private final TeamDAO teamDAO;
    private CharacterManager characterManager;

    public TeamManager(CharacterManager characterManager) throws PersistanceException {
        this.teamDAO = new TeamJsonDAO();
        this.characterManager = characterManager; // Assign CharacterManager
    }

    public boolean validatePersistence() {
        try {
            teamDAO.loadTeams();
            return true;
        } catch (PersistanceException e) {
            return false;
        }
    }

    // Delete a team by name
    public int deleteTeam(String teamName) throws PersistanceException {
        List<Team> teams = teamDAO.loadTeams();
        boolean removed = false;

        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getName().equalsIgnoreCase(teamName)) {
                teams.remove(i);
                removed = true;
                break;
            }
        }

        if (removed) {
            saveTeams(teams);
            return 1;
        } else {
            return 0;
        }
    }

    // Check if a team exists by name
    public boolean teamExists(String teamName) throws PersistanceException {
        return teamDAO.getTeamByName(teamName) != null;
    }

    // Save all teams to the persistence source
    private void saveTeams(List<Team> teams) throws PersistanceException {

        List<TeamPrint> teamsPrint = new ArrayList<>();

        for (Team team : teams) {
            teamsPrint.add(convertToTeamPrint(team));
        }

        teamDAO.saveTeams(teamsPrint);
    }

    //get All teams with the character referenced in each team
    public List<Team> loadTeams() throws PersistanceException {
        List<Team> teams = teamDAO.loadTeams(); // Load teams from JSON

        if (teams == null || teams.isEmpty()) {
            System.out.println("DEBUG: No teams loaded from JSON file.");
            return null;
        }

        System.out.println("DEBUG: Teams loaded -> " + teams.size());

        // Ensure each Member has its corresponding Character
        for (Team team : teams) {
            for (Member member : team.getMembers()) {
                if (member.getCharacterId() == 0) {
                    continue;
                }

                Character character = characterManager.findCharacter(String.valueOf(member.getCharacterId()));

                if (character != null) member.setCharacter(character);
            }
        }

        return teams;
    }

    // Add a new team
    public void addTeam(String newTeam) throws PersistanceException {
        teamDAO.saveTeams(newTeam);
    }

    private TeamPrint convertToTeamPrint(Team team) {
        List<MemberPrint> memberPrints = new ArrayList<>();

        for (Member member : team.getMembers()) {
            memberPrints.add(new MemberPrint(member.getCharacterId(), member.getStrategy()));
        }

        return new TeamPrint(team.getName(), memberPrints);
    }

    public List<String> getTeamsNamesWithCharacter(long characterId) throws PersistanceException {
        return teamDAO.getTeamsNamesWithCharacter(characterId);
    }


    public Member getRandomAvailableDefender(String teamName) throws PersistanceException {
        return teamDAO.getRandomAvailableDefender(teamName);

    }
}
