package persistance;

import business.entities.Member;
import business.entities.Team;
import business.entities.TeamPrint;

import java.util.ArrayList;
import java.util.List;

public interface TeamDAO {
    ArrayList<Team> loadTeams();
    void saveTeams(String team);
    boolean isFileOk();
    Team getTeamByName(String name);
    List<String> getTeamsNamesWithCharacter(long id);
    TeamPrint convertToTeamPrint(Team team);
    Team findTeamByName(String name);

    Member getRandomAvailableDefender(String teamName);
}
