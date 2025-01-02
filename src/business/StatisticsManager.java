package business;

import business.entities.Statistics;
import persistance.StatisticsDAO;
import persistance.exceptions.PersistanceException;

import java.util.List;

public class StatisticsManager {

    private final StatisticsDAO statisticsDAO;

    public StatisticsManager(StatisticsDAO statisticsDAO) {
        this.statisticsDAO = statisticsDAO;
    }

    // Validate persistence source
    public void validatePersistenceSource() throws PersistanceException {
        if (!statisticsDAO.isFileOk()) {
            throw new PersistanceException("The stats.json file can't be accessed.");
        }
    }

    // Retrieve all statistics
    public List<Statistics> getAllStatistics() throws PersistanceException {
        return statisticsDAO.loadStatistics();
    }

    // Save statistics
    public void saveStatistics(List<Statistics> stats) throws PersistanceException {
        statisticsDAO.saveStatistics(stats);
    }

    // Display statistics
    public void displayStatistics() throws PersistanceException {
        List<Statistics> stats = getAllStatistics();
        for (Statistics stat : stats) {
            System.out.println("Match: " + stat.getMatchId());
            System.out.println("Winner: " + stat.getWinner());
            System.out.println("Duration: " + stat.getDuration() + " mins");
            System.out.println("---------------------");
        }
    }
}
