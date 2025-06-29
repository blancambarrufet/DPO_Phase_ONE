package presentation;

import business.*;
import business.entities.*;
import business.entities.Character;

import java.util.ArrayList;
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
     * Validates all persistence sources and displays the results.
     *
     * @return true if all persistence sources are valid, false otherwise
     */
    private boolean validatePersistence() {
        boolean charactersOk = false;
        boolean itemsOk = false;
        boolean teamsOk = false;
        boolean statsOk = false;

        try {
            charactersOk = characterManager.validatePersistenceSource();

        } catch (Exception e) {
            displayMessage("charactersOk EXCEPTION: " + e.getMessage());
        }

        try {
            itemsOk = itemManager.validatePersistenceSource();
        } catch (Exception e) {
            displayMessage("itemsOk EXCEPTION: " + e.getMessage());
        }

        try {
            teamsOk = teamManager.validatePersistence();
        } catch (Exception e) {
            displayMessage("teamsOk EXCEPTION: " + e.getMessage());
        }

        try {
            statsOk = statisticsManager.validatePersistance();
        } catch (Exception e) {
            displayMessage("statsOk EXCEPTION: " + e.getMessage());
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

    /**
     * Creates a new team with user input for team name, characters, and strategies.
     */
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
            displayMessage("Created new team " + teamName + " successfully!");
        } catch (Exception e) {
            displayMessage("Error creating team: " + e.getMessage());
        }
    }


    /**
     * Manages team operations through the team menu.
     */
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
     * Lists all teams and allows user to view team details.
     */
    private void listTeams() {
        try {
            List<String> teams =  teamManager.loadTeamNames();

            int selectedOption = ui.displayTeamOptionList(teams);

            if (selectedOption == 0) {
                return; // Go back to the main menu
            }

            Team selectedTeam = teamManager.findTeamByIndex(selectedOption);

            List<String> teamDetails = formatTeamDetails(selectedTeam);
            ui.displayTeamDetails(teamDetails);

            Statistics statistics = statisticsManager.getStaticByName(selectedTeam.getName());

            ui.printStatistics(statistics);
        } catch (Exception e) {
            displayMessage("Error retrieving teams: " + e.getMessage());
        }
    }

    /**
     * Formats team details into a list of display strings.
     *
     * @param team The team to format
     * @return A list of formatted strings for display
     */
    private List<String> formatTeamDetails(Team team) {
        List<String> lines = new ArrayList<>();
        lines.add("\n\tTeam name: " + team.getName() + "\n");

        int i = 1;
        int width = 30;

        for (Member member : team.getMembers()) {
            String paddedName = String.format("%-" + width + "s", member.getName());
            lines.add("\tCharacter #" + i + ": " + paddedName + "(" + member.getStrategyName().toUpperCase() + ")");
            i++;
        }

        return lines;
    }


    /**
     * Deletes a team after user confirmation.
     */
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

        } catch (Exception e) {
            displayMessage("Error deleting team: " + e.getMessage());
        }
    }


    //*************************************************
    //*********** Functions for Characters ************
    //*************************************************


    /**
     * Lists all characters and allows user to view character details.
     */
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

        } catch (Exception e) {
            displayMessage("Error retrieving characters: " + e.getMessage());
        }
    }

    //*************************************************
    //************* Functions for Items ***************
    //*************************************************


    /**
     * Lists all items and allows user to view item details.
     */
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
        } catch (Exception e) {
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

        } catch (Exception e) {
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
        List<String> lines = formatTeamStats(team, teamNumber);
        ui.displayTeamStats(lines);
    }

    /**
     * Converts a team's combat data into formatted display lines.
     *
     * @param team The team that it will display and to be formatted to lines
     * @param teamNumber Number assigned to the team
     */
    private List<String> formatTeamStats(Team team, int teamNumber) {
        List<String> lines = new ArrayList<>();
        lines.add("Team #" + teamNumber + " - " + team.getName());

        for (Member member : team.getMembers())  {
            String status = member.isKO() ? "KO" : Math.round(member.getDamageTaken() * 100) + " %";
            String weapon = member.getWeaponName();  // Handles nulls inside Member class if needed
            String armor = member.getArmorName();
            lines.add("\t- " + member.getName() + " (" + status + ") " + weapon + " - " + armor);
        }

        return lines;
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
