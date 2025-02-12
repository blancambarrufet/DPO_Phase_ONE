package business;

import business.entities.Statistics;
import persistance.StatisticsDAO;
import persistance.exceptions.PersistanceException;
import persistance.json.StatisticsJsonDAO;

import java.util.List;
import java.util.ArrayList;

public class StatisticsManager {

    private final StatisticsDAO statisticsDAO;
    private ArrayList<Statistics> statistics;

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

    public List<Statistics> getStatistics() {
        return statistics;
    }




    public void recordCombatResult(String Team1, String Team2, int koTeam1, int koTeam2, String winner) throws PersistanceException {
        loadStatistics();


        for (Statistics stat : statistics) {

            if (stat.getName().equalsIgnoreCase(Team1)) {
                stat.incrementGamesPlayed();
                if (winner.equalsIgnoreCase(Team1)) stat.incrementGamesWon();
                stat.incrementKOMade(koTeam2);
                stat.incrementKOReceived(koTeam2);
            }
            if (stat.getName().equalsIgnoreCase(Team2)) {
                stat.incrementGamesPlayed();
                if (winner.equalsIgnoreCase(Team2)) stat.incrementGamesWon();
                stat.incrementKOMade(koTeam1);
                stat.incrementKOReceived(koTeam2);
            }

        }

        saveStatistics(); // Save changes
    }

    // Display statistics
    public void displayStatistics() {
        for (Statistics stat : statistics) {
            System.out.println(stat.getSummary());
        }
    }

    public Statistics getStaticByName(String name) {
        for (Statistics stat : statistics) {
            if (stat.getName().equalsIgnoreCase(name)) {
                return stat;
            }
        }
        return null;
    }

    public void createNewStats(String name, boolean add) {
        if (add) statistics.add(new Statistics(name));
        else statistics.removeIf(stat -> stat.getName().equalsIgnoreCase(name));
        saveStatistics();
    }


}
