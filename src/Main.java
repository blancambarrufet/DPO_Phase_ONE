import business.*;
import presentation.Controller;
import presentation.UI;

public class Main {
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
