package business;

import business.entities.Statistics;
import persistance.StatisticsDAO;
import persistance.exceptions.PersistanceException;
import persistance.json.StatisticsJsonDAO;

import java.util.List;

public class StatisticsManager {

    private final StatisticsDAO statisticsDAO;
    private List<Statistics> statistics;

    public StatisticsManager() throws PersistanceException {
        this.statisticsDAO = new StatisticsJsonDAO();
        loadStatistics();
    }

    public StatisticsManager(StatisticsDAO statisticsDAO) throws PersistanceException {
        this.statisticsDAO = statisticsDAO;
        loadStatistics();
    }

    private void loadStatistics() throws PersistanceException {
        statistics = statisticsDAO.loadStatistics();
    }

    // Save statistics to persistence
    private void saveStatistics() throws PersistanceException {
        statisticsDAO.saveStatistics(statistics);
    }

    // Record combat result
    public void recordCombatResult(String winner, String loser) throws PersistanceException {
        for (Statistics stat : statistics) {
            if (stat.getName().equalsIgnoreCase(winner)) {
                stat.incrementGamesWon();
            }
            if (stat.getName().equalsIgnoreCase(loser)) {
                //stat.incrementGamesLost();
            }
            stat.incrementGamesPlayed();
        }
        saveStatistics(); // Save changes
    }

    // Display statistics
    public void displayStatistics() {
        for (Statistics stat : statistics) {
            System.out.println(stat.getSummary());
        }
    }

    // Add a new team to statistics
    public void addTeam(String teamName) throws PersistanceException {
        if (statistics.stream().noneMatch(stat -> stat.getName().equalsIgnoreCase(teamName))) {
            statistics.add(new Statistics(teamName));
            saveStatistics();
        }
    }

    // Remove a team from statistics
    public void removeTeam(String teamName) throws PersistanceException {
        statistics.removeIf(stat -> stat.getName().equalsIgnoreCase(teamName));
        saveStatistics();
    }
}
