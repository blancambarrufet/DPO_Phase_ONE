package presentation;

import business.*;
import business.entities.*;
import business.entities.Character;
import persistance.exceptions.PersistanceException;

import java.util.List;

/**
 * The controller class manages the user interface and delegating the logic process to the business layer
 * It operates as the control class for user inputs directing the flow of information according to the user.
 */
public class Controller {

    private final UI ui;
    private CombatManager combatManager;
    private ItemManager itemManager;
    private TeamManager teamManager;
    private StatisticsManager statisticsManager;
    private CharacterManager characterManager;


    /**
     * Constructor of the Controller class
     * @param ui The user interface component
     * @param combatManager The manager for combat operations
     * @param itemManager The manager for item operations
     * @param teamManager The manager for team operations
     * @param characterManager the manager for character operations
     * @param statisticsManager the manager for statistics operations
     */
    public Controller(UI ui, CombatManager combatManager, ItemManager itemManager, TeamManager teamManager, CharacterManager characterManager, StatisticsManager statisticsManager) {
        this.ui = ui;
        this.characterManager = characterManager;
        this.itemManager = itemManager;
        this.teamManager = teamManager;
        this.combatManager = combatManager;
        this.statisticsManager = statisticsManager;
    }

    //*************************************************
    //************ General functionalities ************
    //*************************************************

    /**
     * Runs the main application loop, handles the MainMenu.
     */
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
                        displayMessage("We hope to see you again!");
                        System.exit(0);
                        break;
                }
            }
        } catch (Exception e) {
            displayMessage("An unexpected error occurred: " + e.getMessage());
        }
    }


    private boolean validatePersistence() {
        boolean charactersOk = false;
        boolean itemsOk = false;
        boolean teamsOk = false;
        boolean statsOk = false;

        try {
            charactersOk = characterManager.validatePersistenceSource();
            System.out.println("DEBUG: charactersOk is : " + charactersOk);
        } catch (Exception e) {
            System.out.println("DEBUG: charactersOk EXCEPTION: " + e.getMessage());
        }

        try {
            itemsOk = itemManager.validatePersistenceSource();
            System.out.println("DEBUG: itemsOk is : " + itemsOk);
        } catch (Exception e) {
            System.out.println("DEBUG: itemsOk EXCEPTION: " + e.getMessage());
        }

        try {
            teamsOk = teamManager.validatePersistence();
            System.out.println("DEBUG: teamsOk is : " + teamsOk);
        } catch (Exception e) {
            System.out.println("DEBUG: teamsOk EXCEPTION: " + e.getMessage());
        }

        try {
            statsOk = statisticsManager.validatePersistance();
            System.out.println("DEBUG: statsOk is : " + statsOk);
        } catch (Exception e) {
            System.out.println("DEBUG: statsOk EXCEPTION: " + e.getMessage());
        }

        ui.displayValidatePersistence(charactersOk, itemsOk, teamsOk, statsOk);
        return charactersOk && itemsOk;
    }


    /**
     * Displays a message to the user interface.
     * This method delegates the message display to the UI component.
     *
     * @param message The message to be displayed.
     */
    public void displayMessage(String message) {
        ui.displayMessage(message);
    }

    //*************************************************
    //************** Functions for Team  **************
    //*************************************************

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

                String strategyName = ui.requestStrategy(i);
                CombatStrategy strategy = StrategyFactory.createStrategyByName(strategyName);

                Member member = new Member(character.getId(), character, strategy);
                newTeam.addMember(member);
            }

            teamManager.addTeam(newTeam);


            //create team statistics
            statisticsManager.createNewStats(teamName, true);
        } catch (PersistanceException e) {
            displayMessage("Error creating team: " + e.getMessage());
        }
    }


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
            displayMessage("Error retrieving teams: " + e.getMessage());
        }
    }

    private void deleteTeam() {
        try {
            String teamName = ui.requestTeamInfo();
            boolean sure = ui.sure(teamName);

            int done;
            if (sure) {
                done = teamManager.deleteTeam(teamName);
                statisticsManager.createNewStats(teamName, false);
                if (done == 0) sure = false;
            }
            ui.confirmationMessage(teamName, sure);

        } catch (PersistanceException e) {
            displayMessage("Error deleting team: " + e.getMessage());
        }
    }


    //*************************************************
    //*********** Functions for Characters ************
    //*************************************************


    private void listCharacters() {
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
                displayMessage("Character not found!");
            }

        } catch (PersistanceException e) {
            displayMessage("Error retrieving characters: " + e.getMessage());
        }
    }

    //*************************************************
    //************* Functions for Items ***************
    //*************************************************


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
            displayMessage("Error retrieving items: " + e.getMessage());
        }
    }

    //*************************************************
    //************* Functions for Combat **************
    //*************************************************


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

    /**
     * Displays the execution of a combat turn, including damage dealt and received.
     *
     * @param attacker The name of the attacking character.
     * @param damageAttack The amount of damage dealt by the attacker.
     * @param weapon The weapon used in the attack.
     * @param damageReceived The amount of damage received by the defender.
     * @param defender The name of the defending character.
     */
    public void displayExecutionTurn(String attacker, double damageAttack, String weapon, double damageReceived, String defender) {
        ui.displayExecutionTurn(attacker, damageAttack, weapon, damageReceived, defender);
    }

    /**
     * Displays a message when an item breaks due to durability depletion.
     *
     * @param memberName The name of the character whose item broke.
     * @param itemName The name of the broken item.
     */
    public void displayItemDurabilityBreak(String memberName, String itemName) {
        ui.displayItemDurabilityBreak(memberName, itemName);
    }

    /**
     * Displays the current statistics of a team during combat.
     *
     * @param team The team whose stats are displayed.
     * @param teamNumber The number assigned to the team (e.g., Team #1, Team #2).
     */
    public void displayTeamStats(Team team, int teamNumber) {
        ui.displayTeamStats(team, teamNumber);
    }

    /**
     * Displays the initialization details of a team, including its assigned weapons and armor.
     *
     * @param team The team being initialized.
     * @param teamNumber The number assigned to the team (e.g., Team #1, Team #2).
     */
    public void displayTeamInitialization(Team team, int teamNumber) {
        ui.displayTeamInitialization(team,teamNumber);
    }

    /**
     * Displays a message indicating the start of a new combat round.
     *
     * @param round The current round number.
     */
    public void displayRoundMessage(int round) {
        ui.displayRoundMessage(round);
    }

    /**
     * Displays a message when a character is knocked out.
     *
     * @param memberName The name of the character who was knocked out.
     */
    public void displayKOMember(String memberName) {
        ui.displayKOMember(memberName);
    }

    /**
     * Displays the combat result, showing the winning team and remaining character stats.
     *
     * @param teamWinner The winning team (null if it's a tie).
     * @param team1 The first team that participated in the combat.
     * @param team2 The second team that participated in the combat.
     */
    public void displayCombatResult(Team teamWinner, Team team1, Team team2) {
        ui.displayCombatResult(teamWinner, team1, team2);
    }

    /**
     * Displays a message indicating the end of a combat round.
     */
    public void displayEndRoundMessage() {
        ui.displayEndRoundMessage();
    }


}
