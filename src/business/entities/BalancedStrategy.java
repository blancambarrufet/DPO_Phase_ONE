package business.entities;

public class BalancedStrategy extends Strategy {
    @Override
    protected String chooseAction(Character character) {
        if (character.getWeight() > 50) {
            return "Defend";
        }
        return "Attack";
    }
}
