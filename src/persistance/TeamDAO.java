package persistance;

import business.entities.Member;
import business.entities.Team;
import business.entities.TeamPrint;

import java.util.ArrayList;
import java.util.List;

public interface TeamDAO {
    ArrayList<Team> loadTeams();
    void saveNewTeams(Team team);
    Team getTeamByName(String name);
    List<String> getTeamsNamesWithCharacter(long id);
    TeamPrint convertToTeamPrint(Team team);
    void deleteTeam(String team);
    boolean exists(String teamName);
    Team findTeamByIndex(int index);
    List<String> loadTeamNames();
}
