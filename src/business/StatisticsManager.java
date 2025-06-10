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

    public StatisticsManager() {
        try {
            StatisticsApiDAO.validateUsage();
            this.statisticsDAO = new StatisticsApiDAO();
        } catch (PersistanceException e) {
            System.err.println("API unavailable, switching to JSON persistence.");
            this.statisticsDAO = new StatisticsJsonDAO();
        }
    }



    public boolean validatePersistance() {
        try {
            // Try to load statistics to validate connectivity
            List<Statistics> stats = statisticsDAO.loadStatistics();
            return stats != null; // Allow empty list, as statistics might not exist yet
        } catch (PersistanceException e) {
            return false;
        }
    }

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
        statisticsDAO.saveStatistics(statistics);
    }

    public Statistics getStaticByName(String name) throws PersistanceException {
        List<Statistics> statistics = statisticsDAO.loadStatistics();
        return statistics.stream().filter(stat -> stat.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

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
