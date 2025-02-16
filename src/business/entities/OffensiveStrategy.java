package business.entities;

public class OffensiveStrategy extends Strategy {
    @Override
    protected String chooseAction(Character character) {
        return "Attack";
    }
}
