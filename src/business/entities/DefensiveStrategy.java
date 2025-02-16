package business.entities;

public class DefensiveStrategy extends Strategy {
    @Override
    protected String chooseAction(Character character) {
        return "Defend";
    }
}
