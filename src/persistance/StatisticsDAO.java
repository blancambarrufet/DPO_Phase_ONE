package persistance;

import business.entities.Statistics;
import java.util.List;

public interface StatisticsDAO {
    List<Statistics> loadStatistics(); // Load statistics from JSON
    void saveStatistics(List<Statistics> statistics); // Save statistics to JSON
    boolean isFileOk();
}
