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

    /**
     * Validates the existence of json files
     *
     * @return true if the file exist, false otherwise
     */
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

    /**
     * Creation of the team, based in the user input of the name, team members by id of name and strategy.
     * If the characterManager, TeamManager or statisticsManager fails due to a PersistenceException
     * an error will be displayed
     */
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
            displayMessage("Error creating team: " + e.getMessage());
        }
    }



    /**
     * Display to the user and manages the team management menu
     */
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


    /**
     * Listing all the teams in the dataset and then the user can view the detail of a specific team with
     * their stats in the dataset
     * If teamManager or statisticManager fails due to the PersistenceException it will display an error message
     */
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
            displayMessage("Error retrieving teams: " + e.getMessage());
        }
    }

    /**
     * Deletes a team of by the name prompt by the user and a requesting a confirmation to safely remove a team
     * It will also delete the team stat information in the statistics file.
     * If the teamManager or statisticsManager fails due to a persistenceException it will display an error message
     */
    // Delete a Team
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


    /**
     * List all characters on the dataset, then allowing them to view the details of each character selected.
     * If the characterManager fails due to a PersistenceException, and error message will be displayed
     *
     */
    //List Characters
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


    /**
     * Listing all the items of the data set called by the itemManager, also the user can select the item
     * to view the information detail
     * If the itemManager fails due to a persistenceException it will display an error message
     */
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
            displayMessage("Error retrieving items: " + e.getMessage());
        }
    }

    //*************************************************
    //************* Functions for Combat **************
    //*************************************************

    /**
     * Simulates a combat match between two teams.
     * The method:
     * - Loads available teams from the persistence layer.
     * - Ensures at least two teams exist before starting combat.
     * - Prompts the user to select two teams.
     * - Initializes the teams and starts combat.
     * Handles persistence exceptions that may occur during team retrieval.
     */
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
