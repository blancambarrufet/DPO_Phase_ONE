package business;

import business.entities.*;
import persistance.TeamDAO;
import persistance.exceptions.PersistanceException;
import persistance.json.TeamJsonDAO;

import java.util.List;

/**
 * Manages team operations in the game.
 * This class serves as an intermediary between the business logic and the data access layer.
 * It interacts with the persistence layer to retrieve and validate team data.
 */
public class TeamManager {

    private final TeamDAO teamDAO;
    private ItemManager itemManager;

    /**
     * Constructs a TeamManager with the specified ItemManager and with a JSON-based DAO.
     *
     * @param itemManager The ItemManager responsible for managing item assignments.
     * @throws PersistanceException If an error occurs initializing the DAO.
     */
    public TeamManager( ItemManager itemManager) throws PersistanceException {
        this.teamDAO = new TeamJsonDAO();
        this.itemManager = itemManager;
    }

    /**
     * Validates the existence of persistent team data and is correctly formatted.
     *
     * @return true if persistence is valid, false otherwise.
     */
    public boolean validatePersistence() {
        try {
            teamDAO.loadTeams();
            return true;
        } catch (PersistanceException e) {
            return false;
        }
    }

    /**
     * Deletes a team by the name
     *
     * @param teamName The name of the team to delete.
     * @return 1 if deletion is successful, 0 otherwise.
     * @throws PersistanceException If an error occurs during the deleting of the team.
     */
    public int deleteTeam(String teamName) throws PersistanceException {
        boolean removed =  teamDAO.exists(teamName);

        if (removed) {
            teamDAO.deleteTeam(teamName);
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Checks if a team with the specified name exists in the json file
     *
     * @param teamName The name of the team to check.
     * @return true if the team exists, false otherwise.
     * @throws PersistanceException If an error occurs during the check in the DAO
     */
    public boolean teamExists(String teamName) throws PersistanceException {
        return teamDAO.getTeamByName(teamName) != null;
    }


    /**
     * Adds a new team to the json file (persistence layer)
     *
     * @param newTeam The new team to add
     * @throws PersistanceException If an error occurs during the add in the DAO
     */
    public void addTeam(Team newTeam) throws PersistanceException {
        teamDAO.saveNewTeams(newTeam);
    }

    /**
     * Get the names of teams that include a character with the specified id.
     *
     * @param characterId The id of the character.
     * @return A list of team names that include the character.
     * @throws PersistanceException If an error occurs during retrieval in the DAO.
     */
    public List<String> getTeamsNamesWithCharacter(long characterId) throws PersistanceException {
        return teamDAO.getTeamsNamesWithCharacter(characterId);
    }

    /**
     * Initializes a team by equipping the weapon and armor to its members and resetting their damage.
     *
     * @param team The team to initialize.
     * @throws PersistanceException If an error occurs during initialization by retrieval of the items in the DAO.
     */
    public void initializeTeam(Team team) throws PersistanceException {
        for (Member member : team.getMembers()) {
            itemManager.equipItemsMember(member);
            member.resetDamage();
        }
    }

    /**
     * Get and returns a list of team names from the DAO
     *
     * @return A list of team names.
     */
    public List<String> loadTeamNames() {
        return teamDAO.loadTeamNames();
    }

    /**
     * Finds a team by its index in the team list from the DAO
     *
     * @param selectedOption The index of the team to retrieve.
     * @return The team object, or null if not found.
     */
    public Team findTeamByIndex(int selectedOption) {
        return teamDAO.findTeamByIndex(selectedOption - 1);
    }

    /**
     * Checks if a team is defeated meaning the members are KO
     *
     * @param team The team to check.
     * @return true if all members are KO, false otherwise.
     */
    public boolean isTeamDefeated(Team team) {
        for (Member member : team.getMembers()) {
            if (!member.isKO()) {
                return false;
            }
        }
        return true;
    }


}
