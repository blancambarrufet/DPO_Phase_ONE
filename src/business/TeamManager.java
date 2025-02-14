package business;

import business.entities.*;
import persistance.TeamDAO;
import persistance.exceptions.PersistanceException;
import persistance.json.TeamJsonDAO;

import java.util.List;

public class TeamManager {

    private final TeamDAO teamDAO;
    private CharacterManager characterManager;
    private ItemManager itemManager;

    public TeamManager( ItemManager itemManager) throws PersistanceException {
        this.teamDAO = new TeamJsonDAO();
        this.itemManager = itemManager;
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
        boolean removed =  teamDAO.exists(teamName);

        if (removed) {
            teamDAO.deleteTeam(teamName);
            return 1;
        } else {
            return 0;
        }
    }

    // Check if a team exists by name
    public boolean teamExists(String teamName) throws PersistanceException {
        return teamDAO.getTeamByName(teamName) != null;
    }




    // Add a new team
    public void addTeam(Team newTeam) throws PersistanceException {
        teamDAO.saveNewTeams(newTeam);
    }



    public List<String> getTeamsNamesWithCharacter(long characterId) throws PersistanceException {
        return teamDAO.getTeamsNamesWithCharacter(characterId);
    }


    public Member getRandomAvailableDefender(String teamName) throws PersistanceException {
        return teamDAO.getRandomAvailableDefender(teamName);

    }

    public void initializeTeam(Team team) throws PersistanceException {
        for (Member member : team.getMembers()) {
            itemManager.equipItemsMember(member);
            member.resetDamage();
        }
    }

    public List<String> loadTeamNames() {
        return teamDAO.loadTeamNames();
    }

    public Team findTeamByIndex(int selectedOption) {
        return teamDAO.findTeamByIndex(selectedOption);
    }

    // Check if a Team is Defeated
    public boolean isTeamDefeated(Team team) {
        for (Member member : team.getMembers()) {
            if (!member.isKO()) {
                return false;
            }
        }
        return true;
    }


}
