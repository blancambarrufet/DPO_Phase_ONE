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

        ui.displayPersistanceManagement();


        while (true) {
            switch (ui.printMainMenu()) {
                case LIST_CHARACTERS:
                    name = ui.requestCharacterInfo();

                    break;
                case MANAGE_TEAMS:

                    break;
                case LIST_ITEMS:
                    
                    break;
                case SIMULATE_COMBAT:
                    break;
                case EXIT:
                    System.out.println("We hope to see you again!");
                    System.exit(0);
            }

        }
    }
}
