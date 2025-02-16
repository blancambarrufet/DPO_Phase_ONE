import business.*;
import presentation.Controller;
import presentation.UI;

/**
 * Main class that runs the start of the application
 * It initializes the program and starts the user interaction
 */
public class Main {

    /**
     * The main method that initializes the managers, UI and Controller and start the main
     *
     * @param args Commandline arguments passed to the program
     */
    public static void main(String[] args) {
        CharacterManager characterManager = new CharacterManager();
        ItemManager itemManager = new ItemManager();
        TeamManager teamManager = new TeamManager(itemManager);
        StatisticsManager statisticsManager = new StatisticsManager();
        CombatManager combatManager = new CombatManager(itemManager,teamManager, statisticsManager);

        UI ui = new UI();

        Controller controller = new Controller(ui, combatManager, itemManager, teamManager, characterManager, statisticsManager);
        combatManager.setController(controller);
        controller.runMain();
    }
}
