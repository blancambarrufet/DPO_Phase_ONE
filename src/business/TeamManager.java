package business;

import business.entities.Character;
import business.entities.Team;
import persistance.TeamDAO;
import persistance.exceptions.PersistanceException;
import persistance.json.TeamJsonDAO;

import java.util.ArrayList;
import java.util.List;

public class TeamManager {

    private final TeamDAO teamDAO;
    private List<Team> teams;

    public TeamManager() throws PersistanceException {
        this.teamDAO = new TeamJsonDAO(); // Default path
        loadTeams(); // Load teams when initialized
    }

    // Constructor with dependency injection
    public TeamManager(TeamDAO teamDAO) throws PersistanceException {
        this.teamDAO = teamDAO;
        loadTeams(); // Load teams when initialized
    }

    // Get all teams
    public List<Team> getTeams() {
        return teams;
    }

    // Create a new team
    public void createTeam(Team newTeam) throws PersistanceException {
        // Check if team name is unique
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(newTeam.getName())) {
                System.out.println("Team name already exists!");
                return;
            }
        }

        // Add team and save to file
        teams.add(newTeam);
        saveTeams();
        System.out.println("Team " + newTeam.getName() + " created successfully.");
    }

    // Delete a team by name
    public void deleteTeam(String teamName) throws PersistanceException {
        boolean removed = teams.removeIf(team -> team.getName().equalsIgnoreCase(teamName));

        if (removed) {
            saveTeams();
            System.out.println("Team " + teamName + " deleted successfully.");
        } else {
            System.out.println("Team not found.");
        }
    }

    // Display all teams
    public void displayTeams() {
        if (teams.isEmpty()) {
            System.out.println("No teams available.");
        } else {
            for (Team team : teams) {
                System.out.println("Team: " + team.getName());
                System.out.println("Members: ");
                team.getMembers().forEach(member ->
                        System.out.println("\tCharacter ID: " + member.getCharacterId() +
                                ", Strategy: " + member.getStrategy()));
            }
        }
    }

    // Check if a team exists by name
    public boolean teamExists(String teamName) {
        return teams.stream().anyMatch(team -> team.getName().equalsIgnoreCase(teamName));
    }

    // Save all teams to the persistence source
    private void saveTeams() throws PersistanceException {
        teamDAO.saveTeams(teams);
    }

    private void loadTeams() throws PersistanceException {
        teams = teamDAO.loadTeams();
        if (teams == null || teams.isEmpty()) {
            System.out.println("DEBUG: No teams loaded from JSON file.");
        } else {
            System.out.println("DEBUG: Teams loaded -> " + teams.size());
            for (Team team : teams) {
                System.out.println("DEBUG: Team - " + team.getName() + ", Members -> " + team.getMembers().size());
            }
        }
        if (teams == null) { // If loading fails, initialize an empty list
            teams = new ArrayList<>();
        }
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


    /*
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

     */
}
