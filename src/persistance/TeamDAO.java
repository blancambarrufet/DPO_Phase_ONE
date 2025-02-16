package persistance;

import business.entities.Team;
import business.entities.TeamPrint;
import persistance.api.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

public interface TeamDAO {
    ArrayList<Team> loadTeams();
    void saveNewTeams(Team team);
    boolean validateFile();
    Team getTeamByName(String name);
    List<String> getTeamsNamesWithCharacter(long id);
    TeamPrint convertToTeamPrint(Team team);
    Team findTeamByName(String name);
    void deleteTeam(String team);
    boolean exists(String teamName) throws ApiException;
    Team findTeamByIndex(int index) throws ApiException;
    List<String> loadTeamNames() throws ApiException;

}
