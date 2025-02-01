package business;

import business.entities.Character;
import business.entities.Member;
import business.entities.Team;
import persistance.TeamDAO;
import persistance.exceptions.PersistanceException;
import persistance.json.TeamJsonDAO;

import java.util.ArrayList;
import java.util.List;

public class TeamManager {

    private final TeamDAO teamDAO;
    private List<Team> teams;
    private CharacterManager characterManager;

    public TeamManager(CharacterManager characterManager) throws PersistanceException {
        this.teamDAO = new TeamJsonDAO();
        this.characterManager = characterManager; // Assign CharacterManager
        loadTeams();
    }

    public TeamManager(TeamDAO teamDAO, CharacterManager characterManager) throws PersistanceException {
        this.teamDAO = teamDAO;
        this.characterManager = characterManager;
        loadTeams();
    }

    // Get all teams
    public List<Team> getTeams() {
        for (Team team : teams) {
            List<Member> updatedMembers = new ArrayList<>();

            for (Member member : team.getMembers()) {
                Character character = characterManager.getCharacterByID(member.getCharacterId());

                if (character != null) {
                    updatedMembers.add(new Member(character, member.getStrategy()));
                } else {
                    System.out.println("(ERROR) Character with ID " + member.getCharacterId() + " not found.");
                }
            }

            team.setMembers(updatedMembers);
        }


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
                List<Member> updateMembers = new ArrayList<>();

                for (Member member : team.getMembers()) {
                    Character character = characterManager.getCharacterByID(member.getCharacterId());
                    if (character != null) {
                        updateMembers.add(new Member(character, member.getStrategy()));
                    }
                    else {
                        System.out.println("(ERROR) Character with ID " + member.getCharacterId() + " not found in CharacterManager.");
                    }
                }
                team.setMembers(updateMembers);
            }
        }
        if (teams == null) { // If loading fails, initialize an empty list
            teams = new ArrayList<>();
        }


    }


    // Add a new team
    public void addTeam(Team newTeam) throws PersistanceException {
        System.out.println("DEBUG: Attempting to create team: " + newTeam.getName());

        // Check if team name is unique
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(newTeam.getName())) {
                System.out.println("DEBUG: Team name already exists!");
                return;
            }
        }


        // Add team and save to file
        teams.add(newTeam);
        System.out.println("DEBUG: Team successfully added to memory, saving to JSON...");

        saveTeams();
        System.out.println("DEBUG: Team " + newTeam.getName() + " created successfully.");
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

}
