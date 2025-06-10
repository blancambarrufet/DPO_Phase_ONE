package persistance;

import business.entities.Team;
import business.entities.TeamPrint;
import persistance.exceptions.PersistanceException;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for managing team data persistence.
 * Defines methods for loading, saving, retrieving, and deleting team information.
 */
public interface TeamDAO {

    /**
     * Loads all teams from the persistence source.
     *
     * @return {@code ArrayList<Team>} A list of all stored teams.
     * @throws PersistanceException If an error occurs during loading.
     */
    ArrayList<Team> loadTeams() throws PersistanceException;

    /**
     * Saves a new team to the persistence source.
     *
     * @param team The team to be saved.
     * @throws PersistanceException If an error occurs during saving.
     */
    void saveNewTeams(Team team) throws PersistanceException;

    /**
     * Retrieves a team by its name.
     *
     * @param name The name of the team.
     * @return Team The corresponding team object if found, otherwise null.
     * @throws PersistanceException If an error occurs during retrieval.
     */
    Team getTeamByName(String name) throws PersistanceException;

    /**
     * Retrieves a list of team names that contain a specific character.
     *
     * @param id The ID of the character.
     * @return {@code List<String>} A list of team names containing the specified character.
     * @throws PersistanceException If an error occurs during retrieval.
     */
    List<String> getTeamsNamesWithCharacter(long id) throws PersistanceException;

    /**
     * Converts a team object into a printable format.
     *
     * @param team The team to convert.
     * @return TeamPrint The converted team object in a display-friendly format.
     */
    TeamPrint convertToTeamPrint(Team team);

    /**
     * Deletes a team from the persistence source.
     *
     * @param team The name of the team to be deleted.
     * @throws PersistanceException If an error occurs during deletion.
     */
    void deleteTeam(String team) throws PersistanceException;

    /**
     * Checks if a team with the specified name exists in the system.
     *
     * @param teamName The name of the team to search for.
     * @return boolean True if the team exists, otherwise false.
     * @throws PersistanceException If an error occurs during the check.
     */
    boolean exists(String teamName) throws PersistanceException;

    /**
     * Retrieves a team by its index position in the list.
     *
     * @param index The index (0-based) of the team in the list.
     * @return Team The corresponding team object if found.
     * @throws PersistanceException If an error occurs during retrieval.
     */
    Team findTeamByIndex(int index) throws PersistanceException;

    /**
     * Loads the names of all teams in the system.
     *
     * @return {@code List<String>} A list of all available team names.
     * @throws PersistanceException If an error occurs during loading.
     */
    List<String> loadTeamNames() throws PersistanceException;
}
