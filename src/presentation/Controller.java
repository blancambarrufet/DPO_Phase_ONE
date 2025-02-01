package presentation;

import business.*;
import business.entities.*;
import business.entities.Character;
import persistance.exceptions.PersistanceException;

import java.util.ArrayList;
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


    private boolean validatePersistence() {
        try {
            // Validate critical files
            boolean charactersOk = characterManager.validatePersistenceSource(); // Characters.json
            boolean itemsOk = itemManager.validatePersistenceSource();          // Items.json

            // Initialize optional files (Teams & Stats)
            teamManager.getTeams();                        // Teams
            statisticsManager.displayStatistics();         // Stats

            // Check and return based on UI validation
            return ui.validatePersistence(charactersOk, itemsOk);
        } catch (Exception e) {
            e.printStackTrace(); // Log error for debugging
            return ui.validatePersistence(false, false); // Graceful shutdown
        }
    }



    public void listCharacters() {
        // Fetch characters and teams from the business layer
        ArrayList<Character> characters = new ArrayList<>(characterManager.getAllCharacters());
        ArrayList<Team> teams = new ArrayList<>(teamManager.getTeams());

        //Ask UI to display the characters and get the selected option
        int selectedOption = ui.displayCharactersList(characters);

        if (selectedOption == 0) {
            return; // Go back to the main menu
        }

        // Fetch selected character and associated teams
        Character selectedCharacter = characters.get(selectedOption - 1);
        List<String> associatedTeams = findTeamsForCharacter(selectedCharacter.getId(), teams);

        // Display character details via the UI
        ui.displayCharacterDetails(selectedCharacter, associatedTeams);
    }

    private List<String> findTeamsForCharacter(long characterId, ArrayList<Team> teams) {
        List<String> associatedTeams = new ArrayList<>();
        for (Team team : teams) {
            if (team.hasMember(characterId)) { // Business logic in the Team class
                associatedTeams.add(team.getName());
            }
        }
        return associatedTeams;
    }

    // List All Items
    private void listItems() {
        try {
            ArrayList<Item> items = new ArrayList<>(itemManager.getAllItems());
            int selectedItemOption = ui.displayItemsList(items);

            if (selectedItemOption == 0) {
                return;
            }

            Item selectedItem = items.get(selectedItemOption - 1);

            ui.displayItemDetails(selectedItem);
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
                ui.errorCreateTeam(teamName);
                return;
            }


            String Character1 = ui.requestCharacterName(1);
            String strategy1 = ui.requestStrategy(1);

            String Character2 = ui.requestCharacterName(2);
            String strategy2 = ui.requestStrategy(2);

            String Character3 = ui.requestCharacterName(3);
            String strategy3 = ui.requestStrategy(3);

            String Character4 = ui.requestCharacterName(4);
            String strategy4 = ui.requestStrategy(4);

            Team newTeam = new Team(teamName);

            Character character1 =  characterManager.findCharacter(Character1);
            if (character1 == null) {
                ui.errorCreateTeam(Character1);
                return;
            }
            Member member1 = new Member(character1.getId(), strategy1);

            Character character2 =  characterManager.findCharacter(Character2);
            if (character2 == null) {
                ui.errorCreateTeam(Character2);
                return;
            }
            Member member2 =new Member(character2.getId(), strategy2);

            Character character3 =  characterManager.findCharacter(Character3);
            if (character3 == null) {
                ui.errorCreateTeam(Character3);
                return;
            }
            Member member3 = new Member(character3.getId(), strategy3);

            Character character4 =  characterManager.findCharacter(Character4);
            if (character4 == null) {
                ui.errorCreateTeam(Character4);
                return;
            }
            Member member4 = new Member(character4.getId(), strategy4);

            newTeam.addMember(member1);
            newTeam.addMember(member2);
            newTeam.addMember(member3);
            newTeam.addMember(member4);

            teamManager.addTeam(newTeam);
        } catch (PersistanceException e) {
            System.out.println("Error creating team: " + e.getMessage());
        }
    }
    // List All Teams
    private void listTeams() {
        List<Team> teams =  teamManager.getTeams();
        ui.displayTeamList(teams);
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
            System.out.println("\nStarting simulation...");

            //Convert List<Team> to ArrayList<Team>
            ArrayList<Team> availableTeams = new ArrayList<>(teamManager.getTeams());

            ui.displayTeamList(availableTeams);

            //checking the number of teams available
            if (availableTeams.size() < 2) {
                System.out.println("(ERROR) Not enough teams to start a combat.");
                return;
            }

            int teamIndex1 = ui.requestCombatTeam(1, availableTeams.size()) - 1;
            int teamIndex2 = ui.requestCombatTeam(2, availableTeams.size()) - 1;

            Team team1 = availableTeams.get(teamIndex1);
            Team team2 = availableTeams.get(teamIndex2);

            System.out.println("\nInitializing teams...");

            List<Member> team1Members = team1.getMembers();
            List<Member> team2Members = team2.getMembers();

            List<Weapon> availableWeapons = itemManager.getAllWeapons();
            List<Armor> availableArmor = itemManager.getAllArmor();

            combatManager.initializeTeams(team1Members, team2Members, availableWeapons, availableArmor);

            ui.displayTeamDetails(team1,1, team1Members);
            ui.displayTeamDetails(team2,2, team2Members);


            System.out.println("\nCombat ready!");
            System.out.println("<Press any key to continue...>");
            ui.scanner.nextLine(); // Esto se puede hacer?

        } catch (Exception e) {
            System.out.println("Error during combat simulation: " + e.getMessage());
        }
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
