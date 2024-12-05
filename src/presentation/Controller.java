package presentation;

import business.CombatManager;

public class Controller {

    private final UI ui;

    private final CombatManager businessLayer;

    public Controller(UI ui, CombatManager combatManager) {
        this.ui = ui;
        this.businessLayer = combatManager;
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
                    break;
            }

        }
    }
}
