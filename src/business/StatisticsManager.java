package business;

import business.entities.Statistics;
import persistance.API.StatisticsApiDAO;
import persistance.StatisticsDAO;
import persistance.exceptions.PersistanceException;
import persistance.json.StatisticsJsonDAO;

import java.util.List;

/**
 * Manages the statistics of teams, including validation of the persistence, combat results, and creation or delete.
 */
public class StatisticsManager {

    private StatisticsDAO statisticsDAO;

    /**
     * Constructor that initializes the statistics manager with the appropriate DAO.
     * Attempts to use API persistence first, falls back to JSON if API is unavailable.
     */
    public StatisticsManager() {
        try {
            StatisticsApiDAO.validateUsage();
            this.statisticsDAO = new StatisticsApiDAO();
        } catch (PersistanceException e) {
            this.statisticsDAO = new StatisticsJsonDAO();
        }
    }

    /**
     * Validates the persistence layer by attempting to load statistics.
     *
     * @return true if persistence is working, false otherwise
     */
    public boolean validatePersistance() {
        try {
            // Try to load statistics to validate connectivity
            List<Statistics> stats = statisticsDAO.loadStatistics();
            return stats != null; // Allow empty list, as statistics might not exist yet
        } catch (PersistanceException e) {
            return false;
        }
    }

    /**
     * Records the result of a combat between two teams.
     *
     * @param Team1 The name of the first team
     * @param Team2 The name of the second team
     * @param koTeam1 Number of KOs made by team 1
     * @param koTeam2 Number of KOs made by team 2
     * @param winner The name of the winning team
     * @throws PersistanceException if there's an error saving the statistics
     */
    public void recordCombatResult(String Team1, String Team2, int koTeam1, int koTeam2, String winner) throws PersistanceException {
        List<Statistics> statistics = statisticsDAO.loadStatistics();
        
        // Find or create statistics for Team1
        Statistics stat1 = statistics.stream()
                .filter(stat -> stat.getName().equalsIgnoreCase(Team1))
                .findFirst()
                .orElse(null);
        
        if (stat1 == null) {
            stat1 = new Statistics(Team1);
            statistics.add(stat1);
        }
        
        // Find or create statistics for Team2
        Statistics stat2 = statistics.stream()
                .filter(stat -> stat.getName().equalsIgnoreCase(Team2))
                .findFirst()
                .orElse(null);
        
        if (stat2 == null) {
            stat2 = new Statistics(Team2);
            statistics.add(stat2);
        }
        
        // Update Team1 statistics
        stat1.incrementGamesPlayed();
        if (winner.equalsIgnoreCase(Team1)) {
            stat1.incrementGamesWon();
        }
        stat1.incrementKOMade(koTeam2);
        stat1.incrementKOReceived(koTeam1);
        
        // Update Team2 statistics
        stat2.incrementGamesPlayed();
        if (winner.equalsIgnoreCase(Team2)) {
            stat2.incrementGamesWon();
        }
        stat2.incrementKOMade(koTeam1);
        stat2.incrementKOReceived(koTeam2);
        
        statisticsDAO.saveStatistics(statistics);
    }

    /**
     * Retrieves statistics for a team by name.
     *
     * @param name The name of the team
     * @return Statistics object for the team, or null if not found
     * @throws PersistanceException if there's an error loading the statistics
     */
    public Statistics getStaticByName(String name) throws PersistanceException {
        List<Statistics> statistics = statisticsDAO.loadStatistics();
        
        Statistics result = statistics.stream().filter(stat -> stat.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        
        return result;
    }

    /**
     * Creates or removes statistics for a team.
     *
     * @param name The name of the team
     * @param add true to add new statistics, false to remove existing ones
     * @throws PersistanceException if there's an error saving the statistics
     */
    public void createNewStats(String name, boolean add) throws PersistanceException {
        List<Statistics> statistics = statisticsDAO.loadStatistics();

        statistics.removeIf(stat -> stat.getName().equalsIgnoreCase(name));

        if (add) {
            statistics.add(new Statistics(name));
        }

        statisticsDAO.saveStatistics(statistics);
    }
}
