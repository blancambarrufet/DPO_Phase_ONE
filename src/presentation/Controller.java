package presentation;

import business.*;

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
        String name;

        if (!ui.validatePersistence()) {
            System.exit(0);
        }

        ui.displayPersistanceManagement();


        while (true) {
            switch (ui.printMainMenu()) {
                case LIST_CHARACTERS:
                    characterManager.printCharacters();
                    break;
                case MANAGE_TEAMS:
                    handleTeamManagement();
                    break;
                case LIST_ITEMS:
                    itemManager.printItems();
                    break;
                case SIMULATE_COMBAT:
                    combatManager.simulateCombat();
                    break;
                case EXIT:
                    System.out.println("We hope to see you again!");
                    System.exit(0);
            }

        }
    }

    private void handleTeamManagement() {
        while (true) {
            switch (ui.printTeamMenu()) {
                case CREATE_TEAM:
                    teamManager.createTeam(ui.requestTeamInfo());
                    break;
                case LIST_TEAM:
                    teamManager.displayTeams();
                    break;
                case DELETE_TEAM:
                    teamManager.deleteTeam(ui.requestTeamInfo());
                    break;
                case BACK:
                    return; // Go back to the main menu
            }
        }
    }

}
