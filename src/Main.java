import business.CombatManager;
import presentation.Controller;
import presentation.UI;

public class Main {
    public static void main(String[] args) {
        CombatManager combatManager = new CombatManager();
        UI ui = new UI();

        Controller controller = new Controller(ui, combatManager);
        controller.runMain();
    }
}
