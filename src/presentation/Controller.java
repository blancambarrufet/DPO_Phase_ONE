package presentation;

import business.*;
import business.entities.Character;
import business.entities.Team;
import persistance.exceptions.PersistanceException;

import java.util.ArrayList;

public class Controller {

    private final UI ui;

    private CombatManager combatManager;
    private ItemManager itemManager;
    private TeamManager teamManager;
    private StatisticsManager statisticsManager;
    private CharacterManager characterManager;

    public Controller(UI ui, CombatManager combatManager, ItemManager itemManager, TeamManager teamManager, CharacterManager characterManager, StatisticsManager statisticsManager) {
        this.ui = ui;
        this.combatManager = combatManager;
        this.itemManager = itemManager;
        this.teamManager = teamManager;
        this.characterManager = characterManager;
        this.statisticsManager = statisticsManager;
    }

    public void runMain() {
        try {
            // Validate persistence
            boolean persistenceValid = validatePersistence();
            if (!persistenceValid) {
                return; // Exit if persistence check fails
            }

            // Main Menu Loop
            while (true) {
                switch (ui.printMainMenu()) {
                    case LIST_CHARACTERS:
                        listCharacters();
                        break;
                    case MANAGE_TEAMS:
                        manageTeams();
                        break;
                    case LIST_ITEMS:
                        listItems();
                        break;
                    case SIMULATE_COMBAT:
                        simulateCombat();
                        break;
                    case EXIT:
                        System.out.println("We hope to see you again!");
                        System.exit(0);
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }


    // Validate Persistence Files
    private boolean validatePersistence() {
        try {
            characterManager.validatePersistenceSource(); // Check characters.json
            statisticsManager.displayStatistics(); // Verify stats loading
            return ui.validatePersistence(true); // Display success message
        } catch (PersistanceException e) {
            return ui.validatePersistence(false); // Display error message
        }
    }

    // List All Characters
    private void listCharacters() {
        try {
            ArrayList<Character> characters = new ArrayList<>(characterManager.getAllCharacters());
            ui.displayCharacters(characters);
        } catch (PersistanceException e) {
            System.out.println("Error retrieving characters: " + e.getMessage());
        }
    }

    // List All Items
    private void listItems() {
        try {
            ArrayList<business.entities.Item> items = new ArrayList<>(itemManager.getAllItems());
            ui.displayItems(items);
        } catch (PersistanceException e) {
            System.out.println("Error retrieving items: " + e.getMessage());
        }
    }

    // Manage Teams Menu
    private void manageTeams() {
        while (true) {
            switch (ui.printTeamMenu()) {
                case CREATE_TEAM:
                    createTeam();
                    break;
                case LIST_TEAM:
                    listTeams();
                    break;
                case DELETE_TEAM:
                    deleteTeam();
                    break;
                case BACK:
                    return; // Go back to main menu
            }
        }
    }

    // Create a New Team
    private void createTeam() {
        try {
            String teamName = ui.requestTeamInfo();

            if (teamManager.teamExists(teamName)) {
                System.out.println("We are sorry " + teamName + " is taken.");
                return;
            }

            Team newTeam = new Team(teamName);
            teamManager.createTeam(newTeam);
        } catch (PersistanceException e) {
            System.out.println("Error creating team: " + e.getMessage());
        }
    }

    // List All Teams
    private void listTeams() {
        teamManager.displayTeams();
    }

    // Delete a Team
    private void deleteTeam() {
        try {
            String teamName = ui.requestTeamInfo();
            teamManager.deleteTeam(teamName);
        } catch (PersistanceException e) {
            System.out.println("Error deleting team: " + e.getMessage());
        }
    }

    // Simulate Combat
    private void simulateCombat() {
        try {
            // Request team names
            String teamOneName = ui.requestCombatTeam();
            String teamTwoName = ui.requestCombatTeam();

            // Check if teams exist
            if (!teamManager.teamExists(teamOneName) || !teamManager.teamExists(teamTwoName)) {
                System.out.println("One or both teams do not exist!");
                return;
            }

            // Retrieve teams
            Team teamOne = getTeamByName(teamOneName);
            Team teamTwo = getTeamByName(teamTwoName);

            // Simulate combat
            combatManager.executeCombat(teamOne, teamTwo);

            // Update statistics
            updateStatistics(teamOne, teamTwo);
        } catch (Exception e) {
            System.out.println("Error during combat simulation: " + e.getMessage());
        }
    }

    // Get Team by Name (Helper Method)
    private Team getTeamByName(String teamName) {
        for (Team team : teamManager.getTeams()) {
            if (team.getName().equalsIgnoreCase(teamName)) {
                return team;
            }
        }
        return null;
    }

    // Update Statistics After Combat
    private void updateStatistics(Team teamOne, Team teamTwo) throws PersistanceException {
        if (combatManager.isTeamDefeated(teamOne) && combatManager.isTeamDefeated(teamTwo)) {
            // Tie - no winner
            statisticsManager.recordCombatResult("TIE", "TIE");
        } else if (combatManager.isTeamDefeated(teamOne)) {
            // Team Two wins
            statisticsManager.recordCombatResult(teamTwo.getName(), teamOne.getName());
        } else {
            // Team One wins
            statisticsManager.recordCombatResult(teamOne.getName(), teamTwo.getName());
        }
    }

}
