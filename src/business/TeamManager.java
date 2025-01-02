package business;

import business.entities.Member;
import business.entities.Team;
import persistance.TeamDAO;
import persistance.exceptions.PersistanceException;

import java.util.ArrayList;
import java.util.List;

public class TeamManager {

    private final TeamDAO teamDAO;
    private List<Team> teams;

    // Constructor with dependency injection
    public TeamManager(TeamDAO teamDAO) {
        this.teamDAO = teamDAO;
        this.teams = new ArrayList<>();
    }

    // Validate persistence source
    public void validatePersistenceSource() throws PersistanceException {
        if (!teamDAO.isFileOk()) {
            throw new PersistanceException("The teams.json file can't be accessed.");
        }
    }

    // Load teams from DAO
    public void loadTeams() throws PersistanceException {
        this.teams = teamDAO.loadTeams(); // Fetch teams from JSON file
    }

    // Save teams to DAO
    public void saveTeams() throws PersistanceException {
        teamDAO.saveTeams(teams); // Save the current list of teams to JSON
    }

    // Add a new team
    public void addTeam(Team team) throws PersistanceException {
        if (checkName(team.getName())) {
            System.out.println("Team with this name already exists!");
            return;
        }
        teams.add(team);
        saveTeams(); // Save updated team list
        System.out.println("Team added successfully.");
    }

    // Remove a team by name
    public void removeTeam(String teamName) throws PersistanceException {
        teams.removeIf(team -> team.getName().equalsIgnoreCase(teamName));
        saveTeams(); // Save updated team list
        System.out.println("Team removed successfully.");
    }

    // Check if a team name already exists
    private boolean checkName(String name) {
        return teams.stream().anyMatch(team -> team.getName().equalsIgnoreCase(name));
    }

    // Display information about a specific team
    public void displayTeamInfo(String teamName) {
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(teamName)) {
                System.out.println("Team: " + team.getName());
                System.out.println("Members:");
                team.getMembers().forEach(member -> System.out.println("\t- " + member.getName()));
                return;
            }
        }
        System.out.println("Team not found.");
    }

    // Show characters in a specific team
    public void showCharactersInTeams(String teamName) {
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(teamName)) {
                System.out.println("Team Members:");
                team.getMembers().forEach(member -> System.out.println("\t" + member.getName()));
                return;
            }
        }
        System.out.println("Team not found.");
    }
}
