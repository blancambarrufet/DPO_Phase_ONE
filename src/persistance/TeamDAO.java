package persistance;

import business.entities.Team;
import business.entities.TeamPrint;

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
     */
    ArrayList<Team> loadTeams();

    /**
     * Saves a new team to the persistence source.
     *
     * @param team The team to be saved.
     */
    void saveNewTeams(Team team);

    /**
     * Retrieves a team by its name.
     *
     * @param name The name of the team.
     * @return Team The corresponding team object if found, otherwise null.
     */
    Team getTeamByName(String name);

    /**
     * Retrieves a list of team names that contain a specific character.
     *
     * @param id The ID of the character.
     * @return {@code List<String>} A list of team names containing the specified character.
     */
    List<String> getTeamsNamesWithCharacter(long id);

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
     */
    void deleteTeam(String team);

    /**
     * Checks if a team with the specified name exists in the system.
     *
     * @param teamName The name of the team to search for.
     * @return boolean True if the team exists, otherwise false.
     */
    boolean exists(String teamName);

    /**
     * Retrieves a team by its index position in the list.
     *
     * @param index The index (0-based) of the team in the list.
     * @return Team The corresponding team object if found.
     */
    Team findTeamByIndex(int index);

    /**
     * Loads the names of all teams in the system.
     *
     * @return {@code List<String>} A list of all available team names.
     */
    List<String> loadTeamNames();
}
