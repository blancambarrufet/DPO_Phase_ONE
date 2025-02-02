package persistance;

import business.entities.Team;

import java.util.ArrayList;
import java.util.List;

public interface TeamDAO {
    ArrayList<Team> loadTeams();
    void saveTeams(List<Team> team);
    boolean isFileOk();
}
