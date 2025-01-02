package persistance;

import business.entities.Team;
import java.util.List;

public interface TeamDAO {
    List<Team> loadTeams();
    void saveTeams(List<Team> team);
    boolean isFileOk();
}
