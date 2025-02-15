package presentation;

import business.*;
import business.entities.*;
import business.entities.Character;
import persistance.exceptions.PersistanceException;

import java.util.List;

public class Controller {

    private final UI ui;

    private CombatManager combatManager;
    private ItemManager itemManager;
    private TeamManager teamManager;
    private StatisticsManager statisticsManager;
    private CharacterManager characterManager;

    public Controller(UI ui, CombatManager combatManager, ItemManager itemManager, TeamManager teamManager, CharacterManager characterManager, StatisticsManager statisticsManager) {
        this.ui = ui;
        this.characterManager = characterManager;
        this.itemManager = itemManager;
        this.teamManager = teamManager;
        this.combatManager = combatManager;
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
                        ui.displayMessage("We hope to see you again!");
                        System.exit(0);
                        break;
                }
            }
        } catch (Exception e) {
            ui.displayMessage("An unexpected error occurred: " + e.getMessage());
        }
    }

    private boolean validatePersistence() {
        try {
            // Validate critical files
            boolean charactersOk = characterManager.validatePersistenceSource(); // Characters.json
            boolean itemsOk = itemManager.validatePersistenceSource();          // Items.json
            boolean teamsOk = teamManager.validatePersistence();
            boolean statsOk = statisticsManager.validatePersistance();

            // Check and return based on UI validation
            return ui.validatePersistence(charactersOk, itemsOk, teamsOk, statsOk);

        } catch (Exception e) {
            return ui.validatePersistence(false, false, false, false); // Graceful shutdown
        }
    }


    //List Characters
    public void listCharacters() {
        try {
            List<String> characterNames = characterManager.getCharacterNames();

            int selectedOption = ui.displayCharactersList(characterNames);


            if (selectedOption == 0) {
                return; // Go back to the main menu
            }

            Character selectedCharacter = characterManager.findCharacterByIndex(selectedOption);

            if (selectedCharacter != null) {
                List<String> teamsOfCharacter = teamManager.getTeamsNamesWithCharacter(selectedCharacter.getId());

                // Display character details via the UI
                ui.displayCharacterDetails(selectedCharacter, teamsOfCharacter);
            }
            else {
                ui.displayMessage("Character not found!");
            }

        } catch (PersistanceException e) {
            ui.displayMessage("Error retrieving characters: " + e.getMessage());
        }
    }

    // Create a New Team
    private void createTeam() {
        try {
            String teamName = ui.requestTeamInfo();

            if (teamManager.teamExists(teamName)) {
                ui.errorCreateTeam(teamName);
                return;
            }

            Team newTeam = new Team(teamName);

            for (int i = 1; i <= 4; i++) {
                String characterInput = ui.requestCharacterName(i);

                Character character = characterManager.findCharacter(characterInput);
                if (character == null) {
                    ui.errorCreateTeam(characterInput);
                    return;
                }

                String strategy = ui.requestStrategy(i);

                Member member = new Member(character.getId(), character, strategy);
                newTeam.addMember(member);
            }

            teamManager.addTeam(newTeam);


            //create team statistics
            statisticsManager.createNewStats(teamName, true);
        } catch (PersistanceException e) {
            ui.displayMessage("Error creating team: " + e.getMessage());
        }
    }

    // List All Teams
    private void listTeams() {
        try {
            List<String> teams =  teamManager.loadTeamNames();

            int selectedOption = ui.displayTeamOptionList(teams);

            if (selectedOption == 0) {
                return; // Go back to the main menu
            }

            Team selectedTeam = teamManager.findTeamByIndex(selectedOption);

            ui.displayTeamDetails(selectedTeam);

            Statistics statistics = statisticsManager.getStaticByName(selectedTeam.getName());

            ui.printStatistics(statistics);
        } catch (PersistanceException e) {
            ui.displayMessage("Error retrieving teams: " + e.getMessage());
        }
    }

    // Delete a Team
    private void deleteTeam() {
        try {
            String teamName = ui.requestTeamInfo();
            boolean sure = ui.sure(teamName);

            int done=0;
            if (sure) {
                done = teamManager.deleteTeam(teamName);
                statisticsManager.createNewStats(teamName, false);
                if (done == 0) sure = false;
            }
            ui.confirmationMessage(teamName, sure);

        } catch (PersistanceException e) {
            ui.displayMessage("Error deleting team: " + e.getMessage());
        }

    }


    // List All Items
    private void listItems() {
        try {
            List<String> items = itemManager.getItemNames();
            int selectedItemOption = ui.displayItemsList(items);

            if (selectedItemOption == 0) {
                return;
            }

            String selectedItemName = items.get(selectedItemOption - 1);
            Item selectedItem = itemManager.getItemByName(selectedItemName);
            ui.displayItemDetails(selectedItem);
        } catch (PersistanceException e) {
            ui.displayMessage("Error retrieving items: " + e.getMessage());
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

    // Simulate Combat
    private void simulateCombat() {
        displayMessage("\nStarting simulation...");

        try {
            List<String> availableTeams = teamManager.loadTeamNames();
            ui.displayTeamsAvailable(availableTeams);

            if (availableTeams.size() < 2) {
                displayMessage("(ERROR) Not enough teams to start a combat.");
                return;
            }

            // Select teams using indexes
            int teamIndex1 = ui.requestTeamForCombat(1, availableTeams.size()) ;
            int teamIndex2 = ui.requestTeamForCombat(2, availableTeams.size()) ;

            Team team1 = teamManager.findTeamByIndex(teamIndex1);
            Team team2 = teamManager.findTeamByIndex(teamIndex2);

            displayMessage("\nInitializing teams...\n");

            combatManager.combatStart(team1, team2);

        } catch (PersistanceException e) {
            displayMessage("Error during combat setup: " + e.getMessage());
        }
    }

    public void displayExecutionTurn(String attacker, double damageAttack, String weapon, double damageReceived, String defender) {
        ui.displayExecutionTurn(attacker, damageAttack, weapon, damageReceived, defender);
    }

    public void displayItemDurabilityBreak(String memberName, String itemName) {
        ui.displayItemDurabilityBreak(memberName, itemName);
    }


    public void displayTeamStats(Team team, int teamNumber) {
        ui.displayTeamStats(team, teamNumber);
    }

    public void displayMessage(String message) {
        ui.displayMessage(message);
    }

    public void displayTeamInitialization(Team team, int teamNumber) {
        ui.displayTeamInitialization(team,teamNumber);
    }

    public void displayRoundMessage(int round) {
        ui.displayRoundMessage(round);
    }

    public void displayKOMember(String memberName) {
        ui.displayKOMember(memberName);
    }

    public void displayCombatResult(Team teamWinner, Team team1, Team team2) {
        ui.displayCombatResult(teamWinner, team1, team2);
    }

    public void displayEndRoundMessage() {
        ui.displayEndRoundMessage();
    }


}
