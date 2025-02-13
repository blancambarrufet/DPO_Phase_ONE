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

            //Missing the creation of the file team and statistics


            // Check and return based on UI validation
            return ui.validatePersistence(charactersOk, itemsOk);

        } catch (Exception e) {
            return ui.validatePersistence(false, false); // Graceful shutdown
        }
    }


    //List Characters
    public void listCharacters() {
        try {
            // Fetch characters and teams from the business layer
            List<Character> characters = characterManager.getAllCharacters();
            List<Team> teams = teamManager.loadTeams();

            //Ask UI to display the characters and get the selected option
            int selectedOption = ui.displayCharactersList(characters);

            if (selectedOption == 0) {
                return; // Go back to the main menu
            }

            // Fetch selected character and associated teams
            Character selectedCharacter = characters.get(selectedOption - 1);

            // Display character details via the UI
            ui.displayCharacterDetails(selectedCharacter, teams);
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
                String strategy = ui.requestStrategy(i);

                Character character = characterManager.findCharacter(characterInput);
                if (character == null) {
                    ui.errorCreateTeam(characterInput);
                    return;
                }

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
            List<Team> teams =  teamManager.loadTeams();
            int selectedOption = ui.displayTeamOptionList(teams);

            if (selectedOption == 0) {
                return; // Go back to the main menu
            }

            Team selectedTeam = teams.get(selectedOption - 1);
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
            List<Item> items = itemManager.getAllItems();
            int selectedItemOption = ui.displayItemsList(items);

            if (selectedItemOption == 0) {
                return;
            }

            Item selectedItem = items.get(selectedItemOption - 1);
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
        combatManager.combatSimulation();
    }

    public void displayTeamsAvailable(List<Team> teams) {
        ui.displayTeamList(teams);
    }

    public void displayExecutionTurn(String attacker, double damageAttack, String weapon, double damageReceived, String defender) {
        ui.displayExecutionTurn(attacker, damageAttack, weapon, damageReceived, defender);
    }

    public void displayItemDurabilityBreak(String memberName, String itemName) {
        ui.displayItemDurabilityBreak(memberName, itemName);
    }


    public void displayTeamStats(Team team, int teamNumber, List<Member> members) {
        ui.displayTeamStats(team, teamNumber, members);
    }

    public void displayMessage(String message) {
        ui.displayMessage(message);
    }

    public int requestTeamForCombat(int teamNumber, int maxTeams) {
        return ui.requestTeamForCombat(teamNumber, maxTeams);
    }

    public void displayTeamInitialization(Team team, int teamNumber, List<Member> members) {
        ui.displayTeamInitialization(team,teamNumber, members);
    }

    public void requestInput() {
        ui.scanner.nextLine();
    }

    public void displayRoundMessage(int round) {
        ui.displayRoundMessage(round);
    }

    public void displayKOMember(String memberName) {
        ui.displayKOMember(memberName);
    }

    public void displayCombatResult(Team teamWinner, Team team1, List<Member> team1Members, Team team2, List<Member> team2Members) {
        ui.displayCombatResult(teamWinner, team1, team1Members, team2, team2Members);
    }


}
