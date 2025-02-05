import business.*;
import presentation.Controller;
import presentation.UI;

public class Main {
    public static void main(String[] args) {
        CharacterManager characterManager = new CharacterManager();
        ItemManager itemManager = new ItemManager();
        TeamManager teamManager = new TeamManager(characterManager);
        CombatManager combatManager = new CombatManager(characterManager,itemManager);
        StatisticsManager statisticsManager = new StatisticsManager();

        UI ui = new UI();

        Controller controller = new Controller(ui, combatManager, itemManager, teamManager, characterManager, statisticsManager);
        controller.runMain();
    }
}
