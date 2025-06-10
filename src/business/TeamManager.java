package business;

import business.entities.*;
import persistance.API.TeamApiDAO;
import persistance.TeamDAO;
import persistance.exceptions.PersistanceException;
import persistance.json.TeamJsonDAO;

import java.util.List;

/**
 * Manages team operations in the game.
 */
public class TeamManager {

    private TeamDAO teamDAO;
    private ItemManager itemManager;

    /**
     * Constructs a TeamManager with the specified ItemManager and validates API availability.
     *
     * @param itemManager The ItemManager responsible for managing item assignments.
     */
    public TeamManager(ItemManager itemManager) {
        try {
            TeamApiDAO.validateUsage();
            this.teamDAO = new TeamApiDAO();
        } catch (PersistanceException e) {
            System.err.println("API unavailable, switching to JSON persistence.");
            this.teamDAO = new TeamJsonDAO();
        }
        this.itemManager = itemManager;
    }

    /**
     * Validates the existence of persistent team data.
     *
     * @return true if persistence is valid, false otherwise.
     */
    public boolean validatePersistence() {
        try {
            // Try to load team names to validate connectivity
            List<String> teamNames = teamDAO.loadTeamNames();
            return teamNames != null; // Allow empty list, as teams might not exist yet
        } catch (PersistanceException e) {
            return false;
        }
    }

    /**
     * Deletes a team by its name.
     *
     * @param teamName The name of the team to delete.
     * @return 1 if deletion is successful, 0 otherwise.
     * @throws PersistanceException If an error occurs during deletion.
     */
    public int deleteTeam(String teamName) throws PersistanceException {
        if (teamDAO.exists(teamName)) {
            teamDAO.deleteTeam(teamName);
            return 1;
        }
        return 0;
    }

    /**
     * Checks if a team with the specified name exists.
     *
     * @param teamName The name of the team to check.
     * @return true if the team exists, false otherwise.
     * @throws PersistanceException If an error occurs during the check.
     */
    public boolean teamExists(String teamName) throws PersistanceException {
        return teamDAO.getTeamByName(teamName) != null;
    }

    /**
     * Adds a new team to the system.
     *
     * @param newTeam The new team to add.
     * @throws PersistanceException If an error occurs during addition.
     */
    public void addTeam(Team newTeam) throws PersistanceException {
        teamDAO.saveNewTeams(newTeam);
    }

    /**
     * Retrieves the names of teams that include a character with the specified ID.
     *
     * @param characterId The ID of the character.
     * @return A list of team names that include the character.
     * @throws PersistanceException If an error occurs during retrieval.
     */
    public List<String> getTeamsNamesWithCharacter(long characterId) throws PersistanceException {
        return teamDAO.getTeamsNamesWithCharacter(characterId);
    }

    /**
     * Initializes a team by equipping weapons and armor to its members and resetting their damage.
     *
     * @param team The team to initialize.
     * @throws PersistanceException If an error occurs during initialization.
     */
    public void initializeTeam(Team team) throws PersistanceException {
        for (Member member : team.getMembers()) {
            itemManager.equipItemsMember(member);
            member.resetDamage();
        }
    }

    /**
     * Retrieves a list of team names from the DAO.
     *
     * @return A list of team names.
     * @throws PersistanceException If an error occurs during loading.
     */
    public List<String> loadTeamNames() throws PersistanceException {
        return teamDAO.loadTeamNames();
    }

    /**
     * Finds a team by its index in the team list.
     *
     * @param selectedOption The index of the team to retrieve.
     * @return The team object, or null if not found.
     * @throws PersistanceException If an error occurs during retrieval.
     */
    public Team findTeamByIndex(int selectedOption) throws PersistanceException {
        System.out.println("searching Team");
        return teamDAO.findTeamByIndex(selectedOption - 1);
    }

    /**
     * Checks if a team is defeated (all members are KO).
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
