package business;

import business.entities.Statistics;
import persistance.StatisticsDAO;
import persistance.exceptions.PersistanceException;
import persistance.json.StatisticsJsonDAO;

import java.util.List;

public class StatisticsManager {

    private final StatisticsDAO statisticsDAO;

    public StatisticsManager() throws PersistanceException {
        this.statisticsDAO = new StatisticsJsonDAO();
    }

    public boolean validatePersistance() {
        try {
            statisticsDAO.loadStatistics();
            return true;
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

       statisticsDAO.saveStatistics(statistics);// Save changes
    }

    public Statistics getStaticByName(String name) throws PersistanceException {
        List<Statistics> statistics = statisticsDAO.loadStatistics();

        for (Statistics stat : statistics) {
            if (stat.getName().equalsIgnoreCase(name)) {
                return stat;
            }
        }
        return null;
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
