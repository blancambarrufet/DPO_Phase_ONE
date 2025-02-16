package persistance;

import business.entities.Statistics;
import java.util.ArrayList;
import java.util.List;

public interface StatisticsDAO {
    ArrayList<Statistics> loadStatistics(); // Load statistics from JSON
    void saveStatistics(List<Statistics> statistics); // Save statistics to JSON
    boolean validateFile();
}
