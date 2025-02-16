package business;

import business.entities.Statistics;
import persistance.StatisticsDAO;
import persistance.exceptions.PersistanceException;
import persistance.json.StatisticsJsonDAO;

import java.util.List;

/**
 * Manages the statistics of teams, including validation of the persistence, combat results, and creation or delete.
 * This class serves as an intermediary between the business logic and the data access layer.
 */
public class StatisticsManager {

    private final StatisticsDAO statisticsDAO;

    /**
     * Constructor of a StatisticsManager with a default StatisticsJsonDAO
     *
     * @throws PersistanceException If an error occurs during DAO initialization.
     */
    public StatisticsManager() throws PersistanceException {
        this.statisticsDAO = new StatisticsJsonDAO();
    }

    /**
     * Check the persistence of the statistics json file. It checks if the statisticsDao fails due to a
     * PersistenceException.
     *
     * @return true if the statistics file is valid; false otherwise.
     */
    public boolean validatePersistance() {
        try {
            statisticsDAO.loadStatistics();
            return true;
        } catch (PersistanceException e) {
            return false;
        }
    }

    /**
     * Save the result of a combat between two teams in the statistics json file. It checks the name
     * of the teams that played.
     *
     * @param Team1 The name of the first team.
     * @param Team2 The name of the second team.
     * @param koTeam1 The number of KO's made by team 1.
     * @param koTeam2 The number of KO's made by team 2.
     * @param winner The name of the winning team, or an empty string if it's a draw.
     * @throws PersistanceException If there is an error accessing or saving the statistics.
     */
    public void recordCombatResult(String Team1, String Team2, int koTeam1, int koTeam2, String winner) throws PersistanceException {
        List<Statistics> statistics = statisticsDAO.loadStatistics();

        for (Statistics stat : statistics) {
            if (stat.getName().equalsIgnoreCase(Team1)) {
                stat.incrementGamesPlayed();
                if (winner.equalsIgnoreCase(Team1)) {
                    stat.incrementGamesWon();
                }
                stat.incrementKOMade(koTeam2);
                stat.incrementKOReceived(koTeam1);
            }
            if (stat.getName().equalsIgnoreCase(Team2)) {
                stat.incrementGamesPlayed();
                if (winner.equalsIgnoreCase(Team2)) {
                    stat.incrementGamesWon();
                }
                stat.incrementKOMade(koTeam1);
                stat.incrementKOReceived(koTeam2);
            }

        }

       statisticsDAO.saveStatistics(statistics);// Save changes
    }

    /**
     * Get the statistics for a team by its name.
     *
     * @param name The name of the team.
     * @return The Statistics instance for the team, or null if not found.
     * @throws PersistanceException If there is an error accessing the statistics.
     */
    public Statistics getStaticByName(String name) throws PersistanceException {
        List<Statistics> statistics = statisticsDAO.loadStatistics();

        for (Statistics stat : statistics) {
            if (stat.getName().equalsIgnoreCase(name)) {
                return stat;
            }
        }
        return null;
    }

    /**
     * Creates new statistics for a team or removes them if requested by a boolean.
     * True if it wants to add or false if it wants to be removed
     *
     * @param name The name of the team.
     * @param add If true, adds the new statistics; if false, removes them.
     * @throws PersistanceException If there is an error accessing or saving the statistics.
     */
    public void createNewStats(String name, boolean add) throws PersistanceException {
        List<Statistics> statistics = statisticsDAO.loadStatistics();

        if (add) {
            statistics.add(new Statistics(name));
        } else {
            statistics.removeIf(stat -> stat.getName().equalsIgnoreCase(name));
        }

        statisticsDAO.saveStatistics(statistics);
    }



}
