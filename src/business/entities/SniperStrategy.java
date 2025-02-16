package business.entities;

public class SniperStrategy extends Strategy {
    @Override
    protected String chooseAction(Character character) {
        return "Attack Most Damaged Enemy";
    }
}
