package persistance;

import business.entities.Team;
import business.entities.TeamPrint;

import java.util.ArrayList;
import java.util.List;

public interface TeamDAO {
    ArrayList<Team> loadTeams();
    void saveTeams(List<TeamPrint> team);
    boolean isFileOk();
}
